package com.jtech.torrentmaster.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务器下载信息对象
 */
public class ServerDownloadsModel extends BaseModel {
    @SerializedName("Name")
    private String name = "";
    @SerializedName("Size")
    private long size = 0;
    @SerializedName("Modified")
    private String modified = "";
    @SerializedName("Children")
    private List<ServerDownloadsModel> childes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public ServerDownloadsModel setName(String name) {
        this.name = name;
        return this;
    }

    public long getSize() {
        return size;
    }

    public ServerDownloadsModel setSize(long size) {
        this.size = size;
        return this;
    }

    public String getModified() {
        return modified;
    }

    public ServerDownloadsModel setModified(String modified) {
        this.modified = modified;
        return this;
    }

    public List<ServerDownloadsModel> getChildes() {
        return childes;
    }

    public ServerDownloadsModel setChildes(List<ServerDownloadsModel> childes) {
        this.childes = childes;
        return this;
    }
}