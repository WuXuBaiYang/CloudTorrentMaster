package com.jtech.cloudtorrentmaster.model;

/**
 * 服务器信息
 */
public class ServerInfoModel extends BaseModel {
    private String label;
    private String ipAddress;
    private int port;
    private String iconUri;

    public String getLabel() {
        return label;
    }

    public ServerInfoModel setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public ServerInfoModel setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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