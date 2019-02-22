package com.jtech.cloudtorrentmaster.manager;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.jtech.cloudtorrentmaster.common.Constants;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtechlib2.cache.BaseCacheManager;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * 常用参数缓存管理
 */
public class ParamsCacheManager extends BaseCacheManager {
    //服务器信息列表
    private final static String KEY_SERVER_INFO_LIST = "keyServerInfoList";
    //剪切板中的磁力链记录
    private final static String KEY_CLIP_MAGNET = "keyClipMagnet";

    @SuppressLint("StaticFieldLeak")
    private static ParamsCacheManager manager;

    /**
     * 主构造
     *
     * @param context
     */
    private ParamsCacheManager(Context context) {
        super(context);
    }

    /**
     * 获取单利
     *
     * @param context
     * @return
     */
    public static ParamsCacheManager get(Context context) {
        if (null == manager) {
            manager = new ParamsCacheManager(context);
        }
        return manager;
    }

    /**
     * 存储服务器信息列表
     *
     * @param list
     */
    public void setServerInfoList(List<ServerInfoModel> list) {
        put(KEY_SERVER_INFO_LIST, list);
    }

    /**
     * 添加服务器信息
     *
     * @param model
     */
    public void addOrUpdateServerInfo(@NonNull ServerInfoModel model, int index) {
        List<ServerInfoModel> list = getServerInfoList();
        for (int i = 0; i < list.size(); i++) {
            if (model.getId().equals(list.get(i).getId())) {
                list.set(i, model);
                setServerInfoList(list);
                return;
            }
        }
        list.add(index, model);
        setServerInfoList(list);
    }

    /**
     * 获取服务器信息列表
     *
     * @return
     */
    public List<ServerInfoModel> getServerInfoList() {
        return getList(KEY_SERVER_INFO_LIST, new TypeToken<List<ServerInfoModel>>() {
        }.getType());
    }

    @Override
    public String getCacheName() {
        return Constants.KEY_CACHE_NAME;
    }

    /**
     * 存储剪切板磁力链记录
     *
     * @param magnet
     */
    public void setClipMagnet(@NonNull String magnet) {
        put(KEY_CLIP_MAGNET, magnet);
    }

    /**
     * 获取剪切板中的磁力链记录
     *
     * @return
     */
    public String getClipMagnet() {
        return getString(KEY_CLIP_MAGNET);
    }
}