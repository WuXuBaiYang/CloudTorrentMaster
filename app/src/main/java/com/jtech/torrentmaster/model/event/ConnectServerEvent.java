package com.jtech.torrentmaster.model.event;

import androidx.annotation.NonNull;

/**
 * 连接到服务器事件
 */
public class ConnectServerEvent extends BaseEvent {
    private String ipAddress = "";
    private boolean isConnected;
    private int port = 0;

    public ConnectServerEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public ConnectServerEvent(@NonNull String ipAddress, @NonNull int port) {
        this.ipAddress = ipAddress;
        this.isConnected = true;
        this.port = port;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public ConnectServerEvent setConnected(boolean connected) {
        isConnected = connected;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public ConnectServerEvent setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ConnectServerEvent setPort(int port) {
        this.port = port;
        return this;
    }
}