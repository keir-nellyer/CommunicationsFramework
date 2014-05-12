package com.ikeirnez.communicationsframework.implementation.standard.handlers;

import com.ikeirnez.communicationsframework.implementation.handlers.BasicHandler;
import com.ikeirnez.communicationsframework.implementation.handlers.PacketHandler;
import com.ikeirnez.communicationsframework.implementation.handlers.ReconnectHandler;
import com.ikeirnez.communicationsframework.implementation.standard.connection.ConcreteConnection;
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

    // Cache to save on resources
    private static ObjectEncoder objectEncoder = new ObjectEncoder();

    private final ConcreteConnection connection;

    public StandardInitializer(ConcreteConnection connection) {
        this.connection = connection;
    }

    public static void addObjectHandlers(ConcreteConnection connection, ChannelPipeline channelPipeline) {
        channelPipeline.addLast(
                objectEncoder,
                new ObjectDecoder(ClassResolvers.weakCachingResolver(connection.getConnectionManager().classLoader)),
                new BasicHandler(connection));
    }

    public static void addOthers(ConcreteConnection connection, ChannelPipeline channelPipeline) {
        connection.packetHandler = new PacketHandler(connection);

        channelPipeline.addLast(
                connection.packetHandler,
                new IdleStateHandler(5000, 0, 0),
                new ReconnectHandler(connection));
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();

        addObjectHandlers(connection, channelPipeline);
        addOthers(connection, channelPipeline);
    }
}
