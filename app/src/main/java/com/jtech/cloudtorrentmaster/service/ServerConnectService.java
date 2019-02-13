package com.jtech.cloudtorrentmaster.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jtech.cloudtorrentmaster.model.ServerConfigModel;
import com.jtech.cloudtorrentmaster.model.ServerDownloadsModel;
import com.jtech.cloudtorrentmaster.model.ServerSearchModel;
import com.jtech.cloudtorrentmaster.model.ServerStatsModel;
import com.jtech.cloudtorrentmaster.model.ServerTorrentModel;
import com.jtech.cloudtorrentmaster.model.ServerUserModel;
import com.jtech.cloudtorrentmaster.model.event.BaseEvent;
import com.jtech.cloudtorrentmaster.model.event.ConnectServerEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerConfigEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerConnectEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerDownloadsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerSearchEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerStatsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerTorrentsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerUsersEvent;
import com.jtech.cloudtorrentmaster.net.sse.EventSource;
import com.jtechlib2.util.Bus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.file.FileSystemOptions;
import io.vertx.core.http.HttpClientOptions;

/**
 * 长连接服务
 */
public class ServerConnectService extends Service {
    private String originalJson = "";
    private EventSource eventSource;

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
        //初始化配置参数
        HttpClientOptions httpClientOptions = new HttpClientOptions();
        httpClientOptions.setDefaultHost(host);
        httpClientOptions.setDefaultPort(port);
        VertxOptions vertxOptions = new VertxOptions();
        FileSystemOptions fileSystemOptions = new FileSystemOptions();
        vertxOptions.setFileSystemOptions(fileSystemOptions.setClassPathResolvingEnabled(false));
        this.eventSource = EventSource.create(Vertx.vertx(vertxOptions), httpClientOptions);
        new ServerConnectTask().execute(eventSource);
    }

    /**
     * 停止连接
     */
    private void stopConnect() {
        if (null != eventSource) {
            eventSource.close();
            eventSource = null;
        }
    }

    /**
     * 初始化原始消息
     *
     * @param originalJson
     */
    private void initOriginalJson(@NonNull String originalJson) {
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
    private void updateOriginalJson(@NonNull String originalJson, @NonNull JsonObject updateJson) {
        if (!updateJson.get("delta").getAsBoolean()) return;
        DocumentContext document = JsonPath.parse(originalJson);
        String targetModelName = "";
        for (JsonElement element : updateJson.getAsJsonArray("body")) {
            JsonObject option = element.getAsJsonObject();
            String op = option.get("op").getAsString();
            String[] paths = option.get("path").getAsString().split("/");
            String targetKey = paths[paths.length - 1];
            targetModelName = paths[1];
            String jsonPath = parsePaths(paths);
            if ("replace".equals(op) || "add".equals(op)) {
                document.put(jsonPath, targetKey, option.get("value"));
            } else if ("remove".equals(op)) {
                document.delete(jsonPath);
            }
        }
        //将修改过的json替换至原始json中
        this.originalJson = document.jsonString();
        //分发消息
        distributeEvent(this.originalJson, targetModelName);
    }

    /**
     * 解析路径信息并拼接为可查询路径
     *
     * @param paths
     * @return
     */
    private String parsePaths(String... paths) {
        StringBuilder pathBuilder = new StringBuilder().append("$.body");
        for (int j = 1; j < paths.length - 1; j++) {
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
    private void distributeEvent(@NonNull String originalJson, String... modelsName) {
        JsonObject jsonBody = new JsonParser().parse(originalJson)
                .getAsJsonObject().getAsJsonObject("body");
        for (String modelName : modelsName) {
            JsonElement modelJson = jsonBody.get(modelName);
            BaseEvent event = null;
            if ("Config".equals(modelName)) {//服务器配置信息
                event = new ServerConfigEvent(new Gson()
                        .fromJson(modelJson, ServerConfigModel.class));
            } else if ("SearchProviders".equals(modelName)) {//搜索站点
                event = new ServerSearchEvent(handleSearchProvidersModel(modelJson));
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
     * 服务器链接任务
     */
    @SuppressLint("StaticFieldLeak")
    private class ServerConnectTask extends AsyncTask<EventSource, String, Boolean> {
        @Override
        protected Boolean doInBackground(EventSource... eventSources) {
            EventSource eventSource = eventSources[0];
            //发起连接
            eventSource.connect("/sync", null, event ->
                    onProgressUpdate("{\"connected\":" + event.succeeded() + "}"));
            //回调消息
            eventSource.onMessage(this::onProgressUpdate);
            //服务器关闭监听
            eventSource.onClose(event ->
                    onProgressUpdate("{\"connected\":false}"));
            return false;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            JsonObject jsonObject = new JsonParser().parse(values[0]).getAsJsonObject();
            if (jsonObject.has("id")) {//原始数据消息
                initOriginalJson(values[0]);
            } else if (jsonObject.has("delta")) {//更新数据消息
                updateOriginalJson(originalJson, jsonObject);
            } else if (jsonObject.has("connected")) {//链接状态消息
                Bus.get().post(new ServerConnectEvent(
                        jsonObject.get("connected").getAsBoolean()));
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