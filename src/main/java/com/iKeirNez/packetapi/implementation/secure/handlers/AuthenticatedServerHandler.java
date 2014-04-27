package com.iKeirNez.packetapi.implementation.secure.handlers;

import com.iKeirNez.packetapi.api.HookType;
import com.iKeirNez.packetapi.implementation.secure.connection.IAuthenticatedServerConnection;
import com.iKeirNez.packetapi.implementation.secure.packets.PacketAuthenticate;
import com.iKeirNez.packetapi.implementation.secure.packets.PacketAuthenticationStatus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class AuthenticatedServerHandler extends ChannelInboundHandlerAdapter {

    private IAuthenticatedServerConnection connection;

    public AuthenticatedServerHandler(IAuthenticatedServerConnection connection){
        this.connection = connection;
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
            if (msg instanceof PacketAuthenticate) {
                PacketAuthenticate packet = (PacketAuthenticate) msg;
                connection.authenticated.set(Arrays.equals(packet.getKey(), connection.key));
                ctx.writeAndFlush(new PacketAuthenticationStatus(connection.authenticated.get()));

                if (connection.authenticated.get()){
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

}
