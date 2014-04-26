package com.iKeirNez.packetapi.implementation.handlers;

import com.iKeirNez.packetapi.api.HookType;
import com.iKeirNez.packetapi.api.packets.Packet;
import com.iKeirNez.packetapi.implementation.connection.IConnection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by iKeirNez on 18/04/2014.
 */
public class ConnectionHandler extends ChannelInboundHandlerAdapter implements Closeable {

    private final IConnection connection;
    private ChannelHandlerContext ctx;

    public ConnectionHandler(IConnection connection){
        this.connection = connection;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;

        connection.connectQueue.forEach(connection::sendPacket);
        connection.connectQueue.clear();

        if (connection.firstConnect){
            connection.getConnectionManager().callHook(connection, HookType.CONNECTED);
            connection.firstConnect = false;
        } else {
            connection.logger.info("Successfully reconnected");
            connection.getConnectionManager().callHook(connection, HookType.RECONNECTED);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
        connection.getConnectionManager().callListeners(connection, (Packet) object);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        connection.logger.log(Level.SEVERE, "Unexpected exception from downstream, disconnecting...", cause);
        ctx.close();
    }

    public void send(Packet packet){
        ctx.writeAndFlush(packet).addListener(future -> {
            if (!future.isSuccess()){
                throw new Exception("Unexpected exception whilst sending packet", future.cause());
            }
        });
    }

    public boolean connected(){
        return ctx != null && ctx.channel().isOpen();
    }

    @Override
    public void close() throws IOException {
        ctx.disconnect().syncUninterruptibly();
    }

}
