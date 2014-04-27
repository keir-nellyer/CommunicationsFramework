package com.iKeirNez.packetapi.implementation.handlers;

import com.iKeirNez.packetapi.implementation.standard.connection.IConnection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Level;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class BasicHandler extends ChannelInboundHandlerAdapter {

    private IConnection connection;

    public BasicHandler(IConnection connection){
        this.connection = connection;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (!connection.expectingDisconnect.get()){ // disconnecting causes some mean looking errors, lets suppress them
            connection.logger.log(Level.SEVERE, "Unexpected exception from downstream, disconnecting...", cause);
            connection.expectingDisconnect.set(false);
        }

        ctx.close();
    }

}
