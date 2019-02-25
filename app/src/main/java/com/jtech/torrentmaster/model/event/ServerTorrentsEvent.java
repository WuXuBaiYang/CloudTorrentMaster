package com.jtech.torrentmaster.model.event;

import com.jtech.torrentmaster.model.ServerTorrentModel;

import java.util.List;

/**
 * 服务器种子事件
 */
public class ServerTorrentsEvent extends BaseEvent {
    private List<ServerTorrentModel> models;

    public ServerTorrentsEvent(List<ServerTorrentModel> models) {
        this.models = models;
    }

    public List<ServerTorrentModel> getModels() {
        return models;
    }

    public ServerTorrentsEvent setModels(List<ServerTorrentModel> models) {
        this.models = models;
        return this;
    }
}