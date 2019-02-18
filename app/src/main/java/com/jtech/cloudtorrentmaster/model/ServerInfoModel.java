package com.jtech.cloudtorrentmaster.model;

import android.annotation.SuppressLint;

/**
 * 服务器信息
 */
public class ServerInfoModel extends BaseModel {
    private String label;
    private String ip;
    private int port;
    private String iconUri;

    /**
     * 获取完整的ip地址+端口
     *
     * @return
     */
    @SuppressLint("DefaultLocale")
    public String getIpAddress() {
        return String.format("%s:%d", ip, port);
    }

    public String getLabel() {
        return label;
    }

    public ServerInfoModel setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public ServerInfoModel setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ServerInfoModel setPort(int port) {
        this.port = port;
        return this;
    }

    public String getIconUri() {
        return iconUri;
    }

    public ServerInfoModel setIconUri(String iconUri) {
        this.iconUri = iconUri;
        return this;
    }
}