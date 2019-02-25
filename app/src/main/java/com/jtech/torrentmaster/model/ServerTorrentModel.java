package com.jtech.torrentmaster.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务器种子资源对象
 */
public class ServerTorrentModel extends BaseModel {
    @SerializedName("InfoHash")
    private String infoHash = "";
    @SerializedName("Name")
    private String name = "";
    @SerializedName("Loaded")
    private boolean loaded = false;
    @SerializedName("Downloaded")
    private long downloaded = 0;
    @SerializedName("Size")
    private long size = 0;
    @SerializedName("Started")
    private boolean started = false;
    @SerializedName("Dropped")
    private boolean dropped = false;
    @SerializedName("Percent")
    private double percent = 0;
    @SerializedName("DownloadRate")
    private double downloadRate = 0;
    @SerializedName("Files")
    private List<FileModel> files = new ArrayList<>();

    public String getInfoHash() {
        return infoHash;
    }

    public ServerTorrentModel setInfoHash(String infoHash) {
        this.infoHash = infoHash;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServerTorrentModel setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public ServerTorrentModel setLoaded(boolean loaded) {
        this.loaded = loaded;
        return this;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public ServerTorrentModel setDownloaded(long downloaded) {
        this.downloaded = downloaded;
        return this;
    }

    public long getSize() {
        return size;
    }

    public ServerTorrentModel setSize(long size) {
        this.size = size;
        return this;
    }

    public boolean isStarted() {
        return started;
    }

    public ServerTorrentModel setStarted(boolean started) {
        this.started = started;
        return this;
    }

    public boolean isDropped() {
        return dropped;
    }

    public ServerTorrentModel setDropped(boolean dropped) {
        this.dropped = dropped;
        return this;
    }

    public double getPercent() {
        return percent;
    }

    public ServerTorrentModel setPercent(double percent) {
        this.percent = percent;
        return this;
    }

    public double getDownloadRate() {
        return downloadRate;
    }

    public ServerTorrentModel setDownloadRate(double downloadRate) {
        this.downloadRate = downloadRate;
        return this;
    }

    public List<FileModel> getFiles() {
        return files;
    }

    public ServerTorrentModel setFiles(List<FileModel> files) {
        this.files = files;
        return this;
    }

    /**
     * 种子内文件对象
     */
    public static class FileModel extends BaseModel {
        @SerializedName("Path")
        private String path = "";
        @SerializedName("Size")
        private long size = 0;
        @SerializedName("Chunks")
        private int chunks = 0;
        @SerializedName("Completed")
        private int completed = 0;
        @SerializedName("Started")
        private boolean started = false;
        @SerializedName("Percent")
        private double percent = 0;

        public String getPath() {
            return path;
        }

        public FileModel setPath(String path) {
            this.path = path;
            return this;
        }

        public long getSize() {
            return size;
        }

        public FileModel setSize(long size) {
            this.size = size;
            return this;
        }

        public int getChunks() {
            return chunks;
        }

        public FileModel setChunks(int chunks) {
            this.chunks = chunks;
            return this;
        }

        public int getCompleted() {
            return completed;
        }

        public FileModel setCompleted(int completed) {
            this.completed = completed;
            return this;
        }

        public boolean isStarted() {
            return started;
        }

        public FileModel setStarted(boolean started) {
            this.started = started;
            return this;
        }

        public double getPercent() {
            return percent;
        }

        public FileModel setPercent(double percent) {
            this.percent = percent;
            return this;
        }
    }
}