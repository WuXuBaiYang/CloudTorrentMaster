package com.jtech.cloudtorrentmaster.model;

/**
 * 服务器用户对象
 */
public class ServerUserModel extends BaseModel {
    private String id = "";
    private String ipAddress = "";

    public String getId() {
        return id;
    }

    public ServerUserModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public ServerUserModel setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }
}