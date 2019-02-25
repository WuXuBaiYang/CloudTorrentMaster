package com.jtech.torrentmaster.model;

import com.google.gson.annotations.SerializedName;

/**
 * 服务器配置信息
 */
public class ServerConfigModel extends BaseModel {
    @SerializedName("AutoStart")
    private boolean autoStart = false;
    @SerializedName("DisableEncryption")
    private boolean disableEncryption = false;
    @SerializedName("DownloadDirectory")
    private String downloadDirectory = "";
    @SerializedName("EnableUpload")
    private boolean enableUpload = false;
    @SerializedName("EnableSeeding")
    private boolean enableSeeding = false;
    @SerializedName("IncomingPort")
    private int incomingPort = 0;

    public boolean isAutoStart() {
        return autoStart;
    }

    public ServerConfigModel setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
        return this;
    }

    public boolean isDisableEncryption() {
        return disableEncryption;
    }

    public ServerConfigModel setDisableEncryption(boolean disableEncryption) {
        this.disableEncryption = disableEncryption;
        return this;
    }

    public String getDownloadDirectory() {
        return downloadDirectory;
    }

    public ServerConfigModel setDownloadDirectory(String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
        return this;
    }

    public boolean isEnableUpload() {
        return enableUpload;
    }

    public ServerConfigModel setEnableUpload(boolean enableUpload) {
        this.enableUpload = enableUpload;
        return this;
    }

    public boolean isEnableSeeding() {
        return enableSeeding;
    }

    public ServerConfigModel setEnableSeeding(boolean enableSeeding) {
        this.enableSeeding = enableSeeding;
        return this;
    }

    public int getIncomingPort() {
        return incomingPort;
    }

    public ServerConfigModel setIncomingPort(int incomingPort) {
        this.incomingPort = incomingPort;
        return this;
    }
}