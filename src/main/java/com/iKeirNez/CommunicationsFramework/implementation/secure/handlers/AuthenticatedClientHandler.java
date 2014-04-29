package com.iKeirNez.CommunicationsFramework.implementation.secure.handlers;

import com.iKeirNez.CommunicationsFramework.api.HookType;
import com.iKeirNez.CommunicationsFramework.implementation.secure.connection.IAuthenticatedClientConnection;
import com.iKeirNez.CommunicationsFramework.implementation.secure.packets.PacketAuthenticate;
import com.iKeirNez.CommunicationsFramework.implementation.secure.packets.PacketAuthenticationStatus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class AuthenticatedClientHandler extends ChannelInboundHandlerAdapter {

    private IAuthenticatedClientConnection connection;

    public AuthenticatedClientHandler(IAuthenticatedClientConnection connection){
        this.connection = connection;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (!connection.authenticated.get()){
            ctx.channel().writeAndFlush(new PacketAuthenticate(connection.key));
        } else {
            ctx.fireChannelActive();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        if (!connection.authenticated.get()) {
            if (msg instanceof PacketAuthenticationStatus) {
                PacketAuthenticationStatus packet = (PacketAuthenticationStatus) msg;

                if (packet.isAllowed()) {
                    connection.authenticated.set(true);
                    connection.logger.info("Authentication successful");
                    ctx.fireChannelActive();
                } else {
                    connection.logger.info("Authentication failed");
                    connection.expectingDisconnect.set(true);
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
