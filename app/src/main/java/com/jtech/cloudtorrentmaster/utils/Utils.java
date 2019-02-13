package com.jtech.cloudtorrentmaster.utils;

import androidx.annotation.NonNull;

/**
 * 常用工具类
 */
public class Utils {
    /**
     * 拼接磁力链
     *
     * @param infoHash 哈希值
     * @param name
     * @param trackers
     * @return
     */
    public static String jointMagnet(@NonNull String infoHash, @NonNull String name, @NonNull String... trackers) {
        StringBuilder trackerInfo = new StringBuilder();
        for (String tracker : trackers) {
            trackerInfo.append("&tr=")
                    .append(tracker);
        }
        return String.format("magnet:?xt=urn:btih:%s&dn=%s%s", infoHash,
                name.replaceAll("\\s+", "+"), trackerInfo.toString());
    }
}