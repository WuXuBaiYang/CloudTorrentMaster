package com.jtech.cloudtorrentmaster.net.sse;

import com.jtech.cloudtorrentmaster.net.sse.impl.SSEHandlerImpl;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

@VertxGen
public interface SSEHandler extends Handler<RoutingContext> {

	static SSEHandler create() {
		return new SSEHandlerImpl();
	}

	@Fluent
	public SSEHandler connectHandler(Handler<SSEConnection> connection);

	@Fluent
	public SSEHandler closeHandler(Handler<SSEConnection> connection);
}
