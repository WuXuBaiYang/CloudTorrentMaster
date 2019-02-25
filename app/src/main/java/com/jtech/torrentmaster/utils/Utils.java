package com.jtech.torrentmaster.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

import static android.content.Context.CLIPBOARD_SERVICE;

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
            if (TextUtils.isEmpty(tracker)) continue;
            trackerInfo.append("&tr=")
                    .append(tracker);
        }
        return String.format("magnet:?xt=urn:btih:%s&dn=%s%s", infoHash,
                name.replaceAll("\\s+", "+"), trackerInfo.toString());
    }

    /**
     * 判断是否为ip地址(0.0.0.0)
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIPAddress(String ipAddress) {
        String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
        return Pattern.compile(regex)
                .matcher(ipAddress)
                .matches();
    }

    /**
     * 从剪切板中找到所有的磁力链
     *
     * @param context
     * @return
     */
    public static List<String> findMagnetInClipBoard(Context context) {
        List<String> magnets = new ArrayList<>();
        ClipboardManager clipboardManager = (ClipboardManager)
                context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        for (int i = 0; i < Objects.requireNonNull(clipData).getItemCount(); i++) {
            String text = clipData.getItemAt(i).getText().toString();
            if (Utils.isMagnet(text)) magnets.add(text);
        }
        return magnets;
    }

    /**
     * 判断是否为磁力链
     *
     * @param magnet
     * @return
     */
    public static boolean isMagnet(String magnet) {
        String regex = "^(magnet:\\?xt=urn:btih:)[0-9a-fA-F]{40}.*$";
        return Pattern.compile(regex)
                .matcher(magnet)
                .matches();
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