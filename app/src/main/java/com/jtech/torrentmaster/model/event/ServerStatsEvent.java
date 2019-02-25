package com.jtech.torrentmaster.model.event;

import com.jtech.torrentmaster.model.ServerStatsModel;

/**
 * 服务器状态事件
 */
public class ServerStatsEvent extends BaseEvent {
    private ServerStatsModel model;

    public ServerStatsEvent(ServerStatsModel model) {
        this.model = model;
    }

    public ServerStatsModel getModel() {
        return model;
    }

    public ServerStatsEvent setModel(ServerStatsModel model) {
        this.model = model;
        return this;
    }
}