package com.jtech.torrentmaster.model.event;

import com.jtech.torrentmaster.model.ServerUserModel;

import java.util.List;

/**
 * 服务器用户事件
 */
public class ServerUsersEvent extends BaseEvent {
    private List<ServerUserModel> models;

    public ServerUsersEvent(List<ServerUserModel> models) {
        this.models = models;
    }

    public List<ServerUserModel> getModels() {
        return models;
    }

    public ServerUsersEvent setModels(List<ServerUserModel> models) {
        this.models = models;
        return this;
    }
}