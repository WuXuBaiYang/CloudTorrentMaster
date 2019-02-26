package com.jtech.torrentmaster.manager;

import androidx.annotation.NonNull;

/**
 * 文件类型管理
 */
public class FileTypeManager {
    //文件类型
    public final static int IMAGE = 0x002, VIDEO = 0x003,
            AUDIO = 0x004, UNKNOWN = 0x005;

    /**
     * 根据文件名获取文件类型
     *
     * @param fileName
     * @return
     */
    public static int getFileType(@NonNull String fileName) {
        //获取文件词缀
        String suffix = fileName.contains(".") ?
                fileName.substring(fileName.lastIndexOf(".")) : "";
        switch (suffix) {
            case "png"://图片类型
            case "gif":
            case "jpeg":
            case "jpg":
                return IMAGE;
            case "mp4"://视频类型
                return VIDEO;
            case "mp3"://音频类型
                return AUDIO;
            default://默认返回未知文件类型
                return UNKNOWN;
        }
    }
}