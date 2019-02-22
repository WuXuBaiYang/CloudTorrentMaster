package com.jtech.cloudtorrentmaster.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.jtechlib2.net.BaseApi;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 接口管理
 */
public class API extends BaseApi {
    private CloudTorrentApi cloudTorrentApi;
    private String baseUrl = "";
    private static API api;

    /**
     * 获取单利
     *
     * @return
     */
    public static API get() {
        if (null == api) {
            api = new API();
        }
        return api;
    }

    /**
     * 初始化离线服务器接口对象
     *
     * @param baseUrl
     * @return
     */
    public void initCloudTorrentApi(String baseUrl) {
        this.baseUrl = baseUrl;
        //实例化gson
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        OkHttpClient.Builder builder = new OkHttpClient()
                .newBuilder()
                .connectTimeout(25 * 1000, TimeUnit.MILLISECONDS);
        cloudTorrentApi = create(builder.build(),
                JGsonConverterFactory.create(gson), RxJava2CallAdapterFactory.create(), baseUrl, CloudTorrentApi.class);
    }

    /**
     * 种子离线服务接口
     *
     * @return
     */
    public CloudTorrentApi cloudTorrentApi() {
        if (null == cloudTorrentApi) {
            initCloudTorrentApi(baseUrl);
        }
        return cloudTorrentApi;
    }
}