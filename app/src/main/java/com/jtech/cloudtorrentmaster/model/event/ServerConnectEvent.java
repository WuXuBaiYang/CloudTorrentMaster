package com.jtech.cloudtorrentmaster.model.event;

/**
 * 服务器链接状态
 */
public class ServerConnectEvent extends BaseEvent {
    private boolean connected;
    private String reason;
    private int code;

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

    public String getReason() {
        return reason;
    }

    public ServerConnectEvent setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public int getCode() {
        return code;
    }

    public ServerConnectEvent setCode(int code) {
        this.code = code;
        return this;
    }
}