package com.jtech.torrentmaster.manager;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;

/**
 * 图标管理
 */
public class IconsManager {
    //声明国家图标资源表和文件类型资源表
    private LinkedHashMap<String, Uri> iconsLogoMap, iconsFileTypeMap;
    private static IconsManager manager;

    /**
     * 主构造，初始化资源
     *
     * @param context
     */
    private IconsManager(@NonNull Context context) {
        this.iconsFileTypeMap = new LinkedHashMap<>();
        this.iconsLogoMap = new LinkedHashMap<>();
        try {
            //读取asset中的资源
            AssetManager assetManager = context.getAssets();
            //加载国家国旗资源
            loadAssetsFileList(assetManager, "icons/logo", iconsLogoMap);
            //加载文件类型资源
            loadAssetsFileList(assetManager, "icons/file_type", iconsFileTypeMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载资源文件
     *
     * @param assetManager
     * @param filePath
     * @param linkedHashMap
     * @throws IOException
     */
    private void loadAssetsFileList(@NonNull AssetManager assetManager, @NonNull String filePath,
                                    @NonNull LinkedHashMap<String, Uri> linkedHashMap) throws IOException {
        for (String fileName :
                Objects.requireNonNull(assetManager.list(filePath))) {
            String key = fileName.substring(0, fileName.indexOf("."));
            linkedHashMap.put(key, Uri.parse(
                    String.format("file:///android_asset/%s/%s", filePath, fileName)));
        }
    }

    /**
     * 获取单利
     *
     * @return
     */
    public static IconsManager get(@NonNull Context context) {
        if (null == manager) {
            manager = new IconsManager(context);
        }
        return manager;
    }

    /**
     * 获取国家国旗集合
     *
     * @return
     */
    public List<Map.Entry<String, Uri>> getLogoIcons() {
        if (null == iconsLogoMap) return new ArrayList<>();
        return new ArrayList<>(iconsLogoMap.entrySet());
    }

    /**
     * 获取文件夹类型icon
     *
     * @return
     */
    public Uri getFolderTypeIcon() {
        return getFileTypeIcon("folder");
    }

    /**
     * 获取文件类型图标通过文件文明(词缀)
     *
     * @param fileName
     * @return
     */
    public Uri getFileTypeIconByFileName(@NonNull String fileName) {
        //获取文件词缀
        String suffix = fileName.contains(".") ?
                fileName.substring(fileName.lastIndexOf(".")) : "";
        switch (suffix) {
            case "png"://图片类型
                // TODO: 2019/2/25 判断更多类型
                return getFileTypeIcon("image");
            default://默认返回未知文件类型
                return getFileTypeIcon("unknown");
        }
    }

    /**
     * 获取文件类型图标
     *
     * @param key
     * @return
     */
    public Uri getFileTypeIcon(@NonNull String key) {
        if (null == iconsFileTypeMap) return Uri.EMPTY;
        return iconsFileTypeMap.get(key);
    }
}