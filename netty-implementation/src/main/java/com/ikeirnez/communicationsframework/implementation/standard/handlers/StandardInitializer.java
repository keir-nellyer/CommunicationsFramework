package com.ikeirnez.communicationsframework.implementation.standard.handlers;

import com.ikeirnez.communicationsframework.api.authentication.ConnectionAuthentication;
import com.ikeirnez.communicationsframework.api.authentication.SimpleConnectionAuthentication;
import com.ikeirnez.communicationsframework.implementation.authentication.simple.SimpleAuthClientHandler;
import com.ikeirnez.communicationsframework.implementation.handlers.BasicHandler;
import com.ikeirnez.communicationsframework.implementation.handlers.PacketHandler;
import com.ikeirnez.communicationsframework.implementation.handlers.ReconnectHandler;
import com.ikeirnez.communicationsframework.implementation.authentication.simple.SimpleAuthServerHandler;
import com.ikeirnez.communicationsframework.implementation.standard.connection.ConcreteClientConnection;
import com.ikeirnez.communicationsframework.implementation.standard.connection.ConcreteConnection;
import com.ikeirnez.communicationsframework.implementation.standard.connection.ConcreteServerConnection;
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

        ConnectionAuthentication authentication = connection.getConnectionConfig().getAuthentication(); // todo below is all VERY ugly :(
        if (authentication != null){
            switch (connection.getConnectionType()){
                default: break;
                case SERVER:
                    if (authentication instanceof SimpleConnectionAuthentication){
                        channelPipeline.addLast(new SimpleAuthServerHandler((ConcreteServerConnection) connection, (SimpleConnectionAuthentication) authentication));
                    }

                    break;
                case CLIENT:
                    if (authentication instanceof SimpleConnectionAuthentication){
                        channelPipeline.addLast(new SimpleAuthClientHandler((ConcreteClientConnection) connection, (SimpleConnectionAuthentication) authentication));
                    }

                    break;
            }
        }

        addOthers(connection, channelPipeline);
    }
}
