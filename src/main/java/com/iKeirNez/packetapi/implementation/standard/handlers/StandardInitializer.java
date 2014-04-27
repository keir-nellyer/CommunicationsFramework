package com.iKeirNez.packetapi.implementation.standard.handlers;

import com.iKeirNez.packetapi.implementation.handlers.BasicHandler;
import com.iKeirNez.packetapi.implementation.handlers.PacketHandler;
import com.iKeirNez.packetapi.implementation.handlers.ReconnectHandler;
import com.iKeirNez.packetapi.implementation.standard.connection.IConnection;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by iKeirNez on 19/04/2014.
 */
public class StandardInitializer extends ChannelInitializer<SocketChannel> {

    private final IConnection connection;

    public StandardInitializer(IConnection connection){
        this.connection = connection;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();

        addObjectHandlers(connection, channelPipeline);
        addOthers(connection, channelPipeline);
    }

    public static void addObjectHandlers(IConnection connection, ChannelPipeline channelPipeline){
        channelPipeline.addLast(
                new ObjectEncoder(),
                new ObjectDecoder(ClassResolvers.weakCachingResolver(connection.getConnectionManager().classLoader)),
                new BasicHandler(connection));
    }

    public static void addOthers(IConnection connection, ChannelPipeline channelPipeline){
        connection.packetHandler = new PacketHandler(connection);

        channelPipeline.addLast(
                connection.packetHandler,
                new IdleStateHandler(5000, 0, 0),
                new ReconnectHandler(connection));
    }
}
