package com.jtech.cloudtorrentmaster.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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


    /**
     * 显示软键盘
     *
     * @param context
     * @param view
     * @return
     */
    public static boolean showSoftInput(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param view
     * @return
     */
    public static boolean hideSoftInput(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}