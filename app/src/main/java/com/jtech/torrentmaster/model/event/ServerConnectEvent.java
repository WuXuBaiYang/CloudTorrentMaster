package com.jtech.torrentmaster.model.event;

/**
 * 服务器链接状态
 */
public class ServerConnectEvent extends BaseEvent {
    //链接状态 -1未连接 0正在连接 1已连接
    private int connectStats;
    private String reason = "";

    public ServerConnectEvent(int connectStats) {
        this.connectStats = connectStats;
    }

    public int getConnectStats() {
        return connectStats;
    }

    public ServerConnectEvent setConnectStats(int connectStats) {
        this.connectStats = connectStats;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public ServerConnectEvent setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public boolean isConnected() {
        return connectStats == 1;
    }

    public boolean isConnecting() {
        return connectStats == 0;
    }

    public boolean isClosed() {
        return connectStats == -1;
    }
}