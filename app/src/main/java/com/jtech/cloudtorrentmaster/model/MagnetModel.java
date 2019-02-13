package com.jtech.cloudtorrentmaster.model;

import com.google.gson.annotations.SerializedName;

/**
 * 磁力链对象
 */
public class MagnetModel extends BaseModel {
    @SerializedName("magnet")
    private String magnet = "";
    @SerializedName("infohash")
    private String infoHash = "";
    @SerializedName("tracker")
    private String tracker = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("path")
    private String path = "";
    @SerializedName("peers")
    private String peers = "";
    @SerializedName("seeds")
    private String seeds = "";
    @SerializedName("size")
    private String size = "";
    @SerializedName("url")
    private String url = "";

    public String getMagnet() {
        return magnet;
    }

    public MagnetModel setMagnet(String magnet) {
        this.magnet = magnet;
        return this;
    }

    public String getInfoHash() {
        return infoHash;
    }

    public MagnetModel setInfoHash(String infoHash) {
        this.infoHash = infoHash;
        return this;
    }

    public String getTracker() {
        return tracker;
    }

    public MagnetModel setTracker(String tracker) {
        this.tracker = tracker;
        return this;
    }

    public String getName() {
        return name;
    }

    public MagnetModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public MagnetModel setPath(String path) {
        this.path = path;
        return this;
    }

    public String getPeers() {
        return peers;
    }

    public MagnetModel setPeers(String peers) {
        this.peers = peers;
        return this;
    }

    public String getSeeds() {
        return seeds;
    }

    public MagnetModel setSeeds(String seeds) {
        this.seeds = seeds;
        return this;
    }

    public String getSize() {
        return size;
    }

    public MagnetModel setSize(String size) {
        this.size = size;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MagnetModel setUrl(String url) {
        this.url = url;
        return this;
    }
}