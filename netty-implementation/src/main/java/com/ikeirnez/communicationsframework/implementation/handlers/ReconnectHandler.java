package com.ikeirnez.communicationsframework.implementation.handlers;

import com.ikeirnez.communicationsframework.api.HookType;
import com.ikeirnez.communicationsframework.api.connection.ClientConnection;
import com.ikeirnez.communicationsframework.implementation.standard.connection.ConcreteConnection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by iKeirNez on 19/04/2014.
 */
public class ReconnectHandler extends SimpleChannelInboundHandler<Object> {

    private final ConcreteConnection connection;

    public ReconnectHandler(ConcreteConnection connection) {
        this.connection = connection;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // discard
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (!connection.closing.get()) {
            if (connection instanceof ClientConnection) {
                connection.logger.warning("Lost connection, attempting reconnect...");
                ctx.channel().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        ((ClientConnection) connection).connect();
                    }
                }, 5, TimeUnit.SECONDS);
            } else {
                connection.logger.warning("Lost connection, listening for reconnect...");
            }

            connection.getConnectionManager().callHook(connection, HookType.LOST_CONNECTION);
        }

        connection.closing.set(false);
    }
}
