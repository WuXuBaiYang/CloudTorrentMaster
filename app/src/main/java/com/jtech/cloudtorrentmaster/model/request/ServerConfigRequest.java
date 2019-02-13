package com.jtech.cloudtorrentmaster.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * 服务器配置参数请求
 */
public class ServerConfigRequest extends BaseRequest {
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

    public ServerConfigRequest setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
        return this;
    }

    public boolean isDisableEncryption() {
        return disableEncryption;
    }

    public ServerConfigRequest setDisableEncryption(boolean disableEncryption) {
        this.disableEncryption = disableEncryption;
        return this;
    }

    public String getDownloadDirectory() {
        return downloadDirectory;
    }

    public ServerConfigRequest setDownloadDirectory(String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
        return this;
    }

    public boolean isEnableUpload() {
        return enableUpload;
    }

    public ServerConfigRequest setEnableUpload(boolean enableUpload) {
        this.enableUpload = enableUpload;
        return this;
    }

    public boolean isEnableSeeding() {
        return enableSeeding;
    }

    public ServerConfigRequest setEnableSeeding(boolean enableSeeding) {
        this.enableSeeding = enableSeeding;
        return this;
    }

    public int getIncomingPort() {
        return incomingPort;
    }

    public ServerConfigRequest setIncomingPort(int incomingPort) {
        this.incomingPort = incomingPort;
        return this;
    }
}