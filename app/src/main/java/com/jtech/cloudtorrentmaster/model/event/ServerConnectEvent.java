package com.jtech.cloudtorrentmaster.model.event;

/**
 * 服务器链接状态
 */
public class ServerConnectEvent extends BaseEvent {
    private boolean connected;

    public ServerConnectEvent(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public ServerConnectEvent setConnected(boolean connected) {
        this.connected = connected;
        return this;
    }
}