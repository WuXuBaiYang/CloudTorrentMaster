package com.jtech.cloudtorrentmaster.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
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
import com.jtech.cloudtorrentmaster.model.event.ServerDownloadsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerSearchEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerStatsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerTorrentsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerUsersEvent;
import com.jtechlib2.util.Bus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 长连接服务
 */
public class KeepAliveService extends Service {
    private String originalJson = "";

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
        // TODO: 2019/2/13
        if (event.isConnected()) {

        } else {

        }
    }

    /**
     * 更新原始数据
     *
     * @param originalJson 原始json数据
     * @param updateJson   json更新字段
     */
    private void updateOriginalJson(@NonNull String originalJson, @NonNull JSONObject updateJson) throws JSONException {
        if (!updateJson.getBoolean("delta")) return;
        DocumentContext document = JsonPath.parse(originalJson);
        String targetModelName = "";
        JSONArray options = updateJson.getJSONArray("body");
        for (int i = 0; i < options.length(); i++) {
            JSONObject option = options.getJSONObject(i);
            String op = option.getString("op");
            String[] paths = option.getString("path").split("/");
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
     * @throws JSONException
     */
    private void distributeEvent(@NonNull String originalJson, String... modelsName) throws JSONException {
        JSONObject jsonBody = new JSONObject(originalJson).getJSONObject("body");
        for (String modelName : modelsName) {
            String modelJson = jsonBody.getString(modelName);
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
     * @throws JSONException
     */
    private List<ServerSearchModel> handleSearchProvidersModel(@NonNull String modelJson) throws JSONException {
        List<ServerSearchModel> models = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(modelJson);
        Iterator<String> iterable = jsonObject.keys();
        while (iterable.hasNext()) {
            String key = iterable.next();
            JSONObject value = jsonObject.getJSONObject(key);
            models.add(new ServerSearchModel()
                    .setName(value.optString("name", "unknown"))
                    .setKeyword(key));
        }
        return models;
    }

    /**
     * 处理种子资源对象
     *
     * @param modelJson
     * @return
     * @throws JSONException
     */
    private List<ServerTorrentModel> handleTorrentsModel(@NonNull String modelJson) throws JSONException {
        List<ServerTorrentModel> models = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(modelJson);
        Iterator<String> iterable = jsonObject.keys();
        while (iterable.hasNext()) {
            String objectJson = jsonObject.getString(iterable.next());
            models.add(new Gson()
                    .fromJson(objectJson, ServerTorrentModel.class));
        }
        return models;
    }

    /**
     * 处理用户对象
     *
     * @param modelName
     * @return
     * @throws JSONException
     */
    private List<ServerUserModel> handleUsersModel(@NonNull String modelName) throws JSONException {
        List<ServerUserModel> models = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(modelName);
        Iterator<String> iterable = jsonObject.keys();
        while (iterable.hasNext()) {
            String key = iterable.next();
            String value = jsonObject.getString(key);
            models.add(new ServerUserModel()
                    .setId(key)
                    .setIpAddress(value));
        }
        return models;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //反注册
        Bus.getOff(this);
    }
}