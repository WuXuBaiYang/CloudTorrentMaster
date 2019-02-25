package com.jtech.torrentmaster.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jtech.torrentmaster.R;
import com.jtech.torrentmaster.model.ServerConfigModel;
import com.jtech.torrentmaster.model.ServerDownloadsModel;
import com.jtech.torrentmaster.model.ServerSearchModel;
import com.jtech.torrentmaster.model.ServerStatsModel;
import com.jtech.torrentmaster.model.ServerTorrentModel;
import com.jtech.torrentmaster.model.ServerUserModel;
import com.jtech.torrentmaster.model.event.BaseEvent;
import com.jtech.torrentmaster.model.event.ConnectServerEvent;
import com.jtech.torrentmaster.model.event.ServerConfigEvent;
import com.jtech.torrentmaster.model.event.ServerConnectEvent;
import com.jtech.torrentmaster.model.event.ServerDownloadsEvent;
import com.jtech.torrentmaster.model.event.ServerSearchEvent;
import com.jtech.torrentmaster.model.event.ServerStatsEvent;
import com.jtech.torrentmaster.model.event.ServerTorrentsEvent;
import com.jtech.torrentmaster.model.event.ServerUsersEvent;
import com.jtechlib2.util.Bus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 长连接服务
 */
public class ServerConnectService extends Service {
    private ServerConnectSocketTask socketTask;
    private JsonObject originalJson;

    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册到当前类
        Bus.getOn(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 服务器链接状态操作
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectEvent(ConnectServerEvent event) {
        if (event.isConnected()) {//发起连接
            startConnect(event.getIpAddress(), event.getPort());
        } else {//关闭连接
            stopConnect();
        }
    }

    /**
     * 发起连接
     *
     * @param host
     * @param port
     */
    private void startConnect(@NonNull String host, int port) {
        //停止现有链接
        stopConnect();
        //实例化socket任务
        this.socketTask = new ServerConnectSocketTask()
                .setAutoReconnect(true);
        this.socketTask.execute(String.format(
                getString(R.string.server_connect_sync), host, port));
    }

    /**
     * 停止连接
     */
    private void stopConnect() {
        if (null != socketTask) {
            this.socketTask.setAutoReconnect(false)
                    .closeConnect();
            this.socketTask = null;
        }
    }

    /**
     * 初始化原始消息
     *
     * @param originalJson
     */
    private void initOriginalJson(@NonNull JsonObject originalJson) {
        this.originalJson = originalJson;
        //分发初始数据的所有模块
        distributeEvent(originalJson,
                "Config", "SearchProviders", "Downloads", "Torrents", "Users", "Stats");
    }

    /**
     * 更新原始数据
     *
     * @param originalJson 原始json数据
     * @param updateJson   json更新字段
     */
    private void updateOriginalJson(@NonNull JsonObject originalJson, @NonNull JsonObject updateJson) {
        if (!updateJson.get("delta").getAsBoolean()) return;
        DocumentContext document = JsonPath.parse(originalJson.toString());
        String targetModelName = "";
        for (JsonElement element : updateJson.getAsJsonArray("body")) {
            JsonObject option = element.getAsJsonObject();
            String op = option.get("op").getAsString();
            String[] paths = option.get("path").getAsString().split("/");
            String targetKey = paths[paths.length - 1];
            targetModelName = paths[1];
            if ("replace".equals(op) || "add".equals(op)) {
                document.put(parsePaths(true, paths),
                        targetKey, option.get("value"));
            } else if ("remove".equals(op)) {
                document.delete(parsePaths(false, paths));
            }
        }
        //将修改过的json替换至原始json中
        this.originalJson = new Gson()
                .toJsonTree(document.read("$"), new TypeToken<LinkedHashMap>() {
                }.getType()).getAsJsonObject();
        //分发消息
        distributeEvent(this.originalJson, targetModelName);
    }

    /**
     * 解析路径信息并拼接为可查询路径
     *
     * @param stayParent
     * @param paths
     * @return
     */
    private String parsePaths(boolean stayParent, String... paths) {
        StringBuilder pathBuilder = new StringBuilder().append("$.body");
        for (int j = 1; j < paths.length - (stayParent ? 1 : 0); j++) {
            //如果上一个节点是以下参数则当前节点为集合选择
            String lastPath = paths[j - 1];
            if ("Files".equals(lastPath) || "Children".equals(lastPath)) {
                pathBuilder.append(".[")
                        .append(paths[j]).append("]");
            } else {
                pathBuilder.append(".").append(paths[j]);
            }
        }
        return pathBuilder.toString();
    }

    /**
     * 分发消息
     *
     * @param originalJson 原始json数据
     * @param modelsName   需要分发的模块名称(需要与真实key对应)
     */
    private void distributeEvent(@NonNull JsonObject originalJson, String... modelsName) {
        JsonObject jsonBody = originalJson.getAsJsonObject("body");
        for (String modelName : modelsName) {
            JsonElement modelJson = jsonBody.get(modelName);
            BaseEvent event = null;
            if ("Config".equals(modelName)) {//服务器配置信息
                Bus.get().postSticky(new ServerConfigEvent(
                        new Gson().fromJson(modelJson, ServerConfigModel.class)));
            } else if ("SearchProviders".equals(modelName)) {//搜索站点
                Bus.get().postSticky(
                        new ServerSearchEvent(handleSearchProvidersModel(modelJson)));
            } else if ("Downloads".equals(modelName)) {//下载信息
                event = new ServerDownloadsEvent(new Gson()
                        .fromJson(modelJson, ServerDownloadsModel.class));
            } else if ("Torrents".equals(modelName)) {//种子资源信息
                event = new ServerTorrentsEvent(handleTorrentsModel(modelJson));
            } else if ("Users".equals(modelName)) {//用户信息
                event = new ServerUsersEvent(handleUsersModel(modelJson));
            } else if ("Stats".equals(modelName)) {//服务器状态
                event = new ServerStatsEvent(new Gson()
                        .fromJson(modelJson, ServerStatsModel.class));
            }
            if (null != event) Bus.get().post(event);
        }
    }

    /**
     * 处理资源搜索站点对象
     *
     * @param modelJson
     * @return
     */
    private List<ServerSearchModel> handleSearchProvidersModel(@NonNull JsonElement modelJson) {
        List<ServerSearchModel> models = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : modelJson.getAsJsonObject().entrySet()) {
            models.add(new ServerSearchModel()
                    .setName(entry.getValue().getAsJsonObject().get("name").getAsString())
                    .setKeyword(entry.getKey()));
        }
        return models;
    }

    /**
     * 处理种子资源对象
     *
     * @param modelJson
     * @return
     */
    private List<ServerTorrentModel> handleTorrentsModel(@NonNull JsonElement modelJson) {
        List<ServerTorrentModel> models = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : modelJson.getAsJsonObject().entrySet()) {
            models.add(new Gson()
                    .fromJson(entry.getValue(), ServerTorrentModel.class));
        }
        return models;
    }

    /**
     * 处理用户对象
     *
     * @param modelJson
     * @return
     */
    private List<ServerUserModel> handleUsersModel(@NonNull JsonElement modelJson) {
        List<ServerUserModel> models = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : modelJson.getAsJsonObject().entrySet()) {
            models.add(new ServerUserModel()
                    .setId(entry.getKey())
                    .setIpAddress(entry.getValue().getAsString()));
        }
        return models;
    }

    /**
     * 服务器长连接
     */
    @SuppressLint("StaticFieldLeak")
    private class ServerConnectSocketTask extends AsyncTask<String, String, Boolean> {
        private final static String TYPE_SERVER_MESSAGE = "serverMessage";
        private final static String TYPE_UPDATE_MESSAGE = "updateMessage";
        //心跳包检测频率，默认15秒
        private long heartBeatDelay = 15 * 1000;
        private WebSocketClient webSocketClient;
        private boolean autoReconnect = false;

        @SuppressLint("HandlerLeak")
        private Handler heartBeatHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //如果对象不为空并且正在连接，则继续发送心跳包
                if (null != webSocketClient && (!webSocketClient.isClosed()
                        || !webSocketClient.isClosing())) {
                    webSocketClient.send("");
                    sendHeartBeat();
                }
            }
        };

        /**
         * 发送心跳包
         */
        private void sendHeartBeat() {
            heartBeatHandler.sendEmptyMessageDelayed(0, heartBeatDelay);
        }

        /**
         * 设置是否自动重连
         *
         * @param autoReconnect
         * @return
         */
        ServerConnectSocketTask setAutoReconnect(boolean autoReconnect) {
            this.autoReconnect = autoReconnect;
            return this;
        }

        /**
         * 关闭链接
         */
        void closeConnect() {
            if (null != webSocketClient) {
                this.webSocketClient.close();
                this.webSocketClient = null;
            }
        }

        /**
         * 发送链接状态消息
         *
         * @param connectStats
         * @param reason
         */
        void sendConnectStatsEvent(int connectStats, String reason) {
            Bus.get().post(new ServerConnectEvent(connectStats)
                    .setReason(reason));
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String requestUrl = strings[0];
            if (TextUtils.isEmpty(requestUrl)) return false;
            try {
                //初始化webSocket
                this.webSocketClient = new WebSocketClient(new URI(requestUrl)) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        sendConnectStatsEvent(1, null);
                    }

                    @Override
                    public void onMessage(String message) {
                        onProgressUpdate(message, message.startsWith("{\"id\"")
                                ? TYPE_SERVER_MESSAGE : TYPE_UPDATE_MESSAGE);
                    }

                    @Override
                    @SuppressLint("DefaultLocale")
                    public void onClose(int code, String reason, boolean remote) {
                        sendConnectStatsEvent(-1, null);
                        //自动重连
                        if (autoReconnect && null != webSocketClient) {
                            webSocketClient.reconnect();
                            sendConnectStatsEvent(0, null);
                        }
                    }

                    @Override
                    public void onError(Exception ex) {
                    }
                };
                //开始连接
                this.webSocketClient.connect();
                //发送连接中状态
                sendConnectStatsEvent(0, null);
                //发送心跳包
                sendHeartBeat();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            JsonObject messageJson = new JsonParser()
                    .parse(values[0]).getAsJsonObject();
            switch (values[1]) {
                case TYPE_SERVER_MESSAGE://原始数据消息
                    initOriginalJson(messageJson);
                    break;
                case TYPE_UPDATE_MESSAGE://更新数据消息
                    updateOriginalJson(originalJson, messageJson);
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //反注册
        Bus.getOff(this);
    }
}