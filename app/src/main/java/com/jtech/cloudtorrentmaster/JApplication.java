package com.jtech.cloudtorrentmaster;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.jtech.cloudtorrentmaster.service.ServerConnectService;
import com.jtechlib2.BaseApplication;

/**
 * application
 */
public class JApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //绑定消息推送服务
        bindService(new Intent(this, ServerConnectService.class),
                serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 服务
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    public void onTerminate() {
        super.onTerminate();
        //取消绑定推送服务
        unbindService(serviceConnection);
    }
}