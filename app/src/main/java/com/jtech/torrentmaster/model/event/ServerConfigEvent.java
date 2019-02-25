package com.jtech.torrentmaster.model.event;

import com.jtech.torrentmaster.model.ServerConfigModel;

/**
 * 服务器配置事件
 */
public class ServerConfigEvent extends BaseEvent {
    private ServerConfigModel model;

    public ServerConfigEvent(ServerConfigModel model) {
        this.model = model;
    }

    public ServerConfigModel getModel() {
        return model;
    }

    public ServerConfigEvent setModel(ServerConfigModel model) {
        this.model = model;
        return this;
    }
}