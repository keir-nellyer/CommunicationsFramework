package com.iKeirNez.packetapi.implementation.handlers;

import com.iKeirNez.packetapi.implementation.connection.IConnection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by iKeirNez on 19/04/2014.
 */
public class ReconnectHandler extends SimpleChannelInboundHandler<Object> {

    private IConnection connection;

    public ReconnectHandler(IConnection connection){
        this.connection = connection;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // discard
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        connection.logger.warning("Lost connection, attempting reconnect...");
        connection.init();
    }
}
