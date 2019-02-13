package com.jtech.cloudtorrentmaster.manager;

import android.Manifest;
import android.app.Activity;

import com.jtechlib2.listener.PermissionListener;
import com.jtechlib2.manager.BasePermissionManager;

/**
 * 权限管理
 */
public class PermissionManager extends BasePermissionManager {
    //必备权限
    private final static String[] mustPermission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 检查必备权限
     *
     * @param activity
     * @param listener
     */
    public static void checkMust(Activity activity, PermissionListener listener) {
        checkMultiple(activity, mustPermission, listener);
    }
}