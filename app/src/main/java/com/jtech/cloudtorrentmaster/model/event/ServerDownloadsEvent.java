package com.jtech.cloudtorrentmaster.model.event;

import com.jtech.cloudtorrentmaster.model.ServerDownloadsModel;

/**
 * 服务器下载事件
 */
public class ServerDownloadsEvent extends BaseEvent {
    private ServerDownloadsModel model;

    public ServerDownloadsEvent(ServerDownloadsModel model) {
        this.model = model;
    }

    public ServerDownloadsModel getModel() {
        return model;
    }

    public ServerDownloadsEvent setModel(ServerDownloadsModel model) {
        this.model = model;
        return this;
    }
}