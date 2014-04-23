package com.iKeirNez.packetapi.implementation.handlers;

import com.iKeirNez.packetapi.implementation.connection.IConnection;
import io.netty.channel.ChannelInitializer;
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
        connection.connectionHandler = new ConnectionHandler(connection);

        socketChannel.pipeline().addLast(
                new ObjectEncoder(),
                new ObjectDecoder(ClassResolvers.weakCachingResolver(connection.getConnectionManager().classLoader)),
                connection.connectionHandler,
                new IdleStateHandler(5000, 0, 0),
                new ReconnectHandler(connection));
    }
}
