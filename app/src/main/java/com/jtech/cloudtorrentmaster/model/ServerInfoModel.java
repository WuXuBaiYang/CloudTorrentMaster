package com.jtech.cloudtorrentmaster.model;

/**
 * 服务器信息
 */
public class ServerInfoModel extends BaseModel {
    private String label;
    private String ipAddress;
    private int port;
    private String iconPath;
    private int iconResId;

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

    public String getIconPath() {
        return iconPath;
    }

    public ServerInfoModel setIconPath(String iconPath) {
        this.iconPath = iconPath;
        return this;
    }

    public int getIconResId() {
        return iconResId;
    }

    public ServerInfoModel setIconResId(int iconResId) {
        this.iconResId = iconResId;
        return this;
    }
}