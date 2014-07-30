package com.ikeirnez.communicationsframework.implementation.authentication.simple;

import com.ikeirnez.communicationsframework.api.HookType;
import com.ikeirnez.communicationsframework.api.authentication.SimpleConnectionAuthentication;
import com.ikeirnez.communicationsframework.implementation.authentication.PacketAuthenticationStatus;
import com.ikeirnez.communicationsframework.implementation.standard.connection.ConcreteServerConnection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class SimpleAuthServerHandler extends ChannelInboundHandlerAdapter {

    private ConcreteServerConnection connection;
    private SimpleConnectionAuthentication authentication;

    public SimpleAuthServerHandler(ConcreteServerConnection connection, SimpleConnectionAuthentication authentication) {
        this.connection = connection;
        this.authentication = authentication;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (connection.authenticated.get()) {
            ctx.fireChannelActive();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!connection.authenticated.get()) {
            if (msg instanceof PacketSimpleAuthenticate) {
                PacketSimpleAuthenticate packet = (PacketSimpleAuthenticate) msg;
                connection.authenticated.set(packet.getKey().equals(authentication.getKey()));
                ctx.writeAndFlush(new PacketAuthenticationStatus(connection.authenticated.get()));

                if (connection.authenticated.get()) {
                    ctx.fireChannelActive();
                } else {
                    connection.closing.set(true);
                    ctx.channel().disconnect();
                    connection.getConnectionManager().callHook(connection, HookType.AUTHENTICATION_FAILED);
                }
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // makes sure we re-authenticate when reconnecting
        connection.authenticated.set(false);
        ctx.fireChannelInactive();
    }

}
