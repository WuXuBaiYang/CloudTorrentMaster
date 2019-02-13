package com.jtech.cloudtorrentmaster.model;

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
    private List<ChildModel> childes = new ArrayList<>();

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

    public List<ChildModel> getChildes() {
        return childes;
    }

    public ServerDownloadsModel setChildes(List<ChildModel> childes) {
        this.childes = childes;
        return this;
    }

    /**
     * 下载内容的子集
     */
    public static class ChildModel extends BaseModel {
        @SerializedName("Name")
        private String name = "";
        @SerializedName("Size")
        private int size = 0;
        @SerializedName("Modified")
        private String modified = "";
        @SerializedName("Children")
        private List<ChildModel> childes = new ArrayList<>();

        public String getName() {
            return name;
        }

        public ChildModel setName(String name) {
            this.name = name;
            return this;
        }

        public int getSize() {
            return size;
        }

        public ChildModel setSize(int size) {
            this.size = size;
            return this;
        }

        public String getModified() {
            return modified;
        }

        public ChildModel setModified(String modified) {
            this.modified = modified;
            return this;
        }

        public List<ChildModel> getChildes() {
            return childes;
        }

        public ChildModel setChildes(List<ChildModel> childes) {
            this.childes = childes;
            return this;
        }
    }
}