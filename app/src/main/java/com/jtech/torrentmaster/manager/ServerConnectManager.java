package com.jtech.torrentmaster.manager;

import com.jtech.torrentmaster.model.event.ConnectServerEvent;
import com.jtechlib2.util.Bus;

/**
 * 长连接服务管理
 */
public class ServerConnectManager {
    /**
     * 连接到服务器
     *
     * @param ipAddress
     * @param port
     */
    public static void connectServer(String ipAddress, int port) {
        Bus.get().post(new ConnectServerEvent(ipAddress, port));
    }

    /**
     * 取消链接到服务器
     */
    public static void disconnectServer() {
        Bus.get().post(new ConnectServerEvent(false));
    }
}