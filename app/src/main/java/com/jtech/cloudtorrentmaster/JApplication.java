package com.jtech.cloudtorrentmaster;

import android.content.Intent;

import com.jtech.cloudtorrentmaster.service.KeepAliveService;
import com.jtechlib2.BaseApplication;

/**
 * application
 */
public class JApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //启动服务器消息推送服务
        startService(new Intent(getApplicationContext(), KeepAliveService.class));
    }
}