package com.jtech.torrentmaster.model.event;

import com.jtech.torrentmaster.model.ServerSearchModel;

import java.util.List;

/**
 * 搜索站点消息
 */
public class ServerSearchEvent extends BaseEvent {
    private List<ServerSearchModel> models;

    public ServerSearchEvent(List<ServerSearchModel> models) {
        this.models = models;
    }

    public List<ServerSearchModel> getModels() {
        return models;
    }

    public ServerSearchEvent setModels(List<ServerSearchModel> models) {
        this.models = models;
        return this;
    }
}