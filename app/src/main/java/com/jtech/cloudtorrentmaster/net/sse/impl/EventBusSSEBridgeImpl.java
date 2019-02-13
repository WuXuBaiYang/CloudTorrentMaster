package com.jtech.cloudtorrentmaster.net.sse.impl;

import com.jtech.cloudtorrentmaster.net.sse.EventBusSSEBridge;

import java.util.function.Function;

import io.vertx.core.http.HttpServerRequest;

public class EventBusSSEBridgeImpl extends SSEHandlerImpl implements EventBusSSEBridge {

    private Function<HttpServerRequest, String> mapper = HttpServerRequest::path;

    public EventBusSSEBridgeImpl() {
        super();
        connectHandler(sseConnection -> {
            sseConnection.forward(mapper.apply(sseConnection.request()));
            closeHandler(v -> sseConnection.close());
        });
    }

    @Override
    public EventBusSSEBridge mapping(Function<HttpServerRequest, String> mapper) {
        this.mapper = mapper;
        return this;
    }

}
