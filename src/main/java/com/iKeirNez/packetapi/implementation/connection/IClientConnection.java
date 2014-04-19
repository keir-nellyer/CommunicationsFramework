package com.iKeirNez.packetapi.implementation.connection;

import com.iKeirNez.packetapi.api.connection.ClientConnection;
import com.iKeirNez.packetapi.implementation.handlers.ConnectionHandler;
import com.iKeirNez.packetapi.implementation.handlers.ReconnectHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class IClientConnection extends IConnection implements ClientConnection {

    private EventLoopGroup group = new NioEventLoopGroup();
    public Bootstrap bootstrap = new Bootstrap();

    protected IClientConnection(IConnectionManager connectionManager, String serverAddress, int port) throws Exception {
        super(connectionManager, serverAddress, port);

        IClientConnection instance = this;

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel socketChannel) throws Exception {
                        connectionHandler = new ConnectionHandler(instance);

                        socketChannel.pipeline().addLast(
                                new ObjectEncoder(),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                connectionHandler,
                                new IdleStateHandler(5000, 0, 0),
                                new ReconnectHandler(instance));
                    }
                }).remoteAddress(serverAddress, port);

        init();
    }

    public void init(){
        bootstrap.connect().addListener((ChannelFuture f) -> {
            if (!f.isSuccess()){
                f.channel().eventLoop().schedule((Runnable) bootstrap::connect, 5, TimeUnit.SECONDS);
            }
        });
    }

    @Override
    public void close() throws InterruptedException {
        group.shutdownGracefully().sync();
        super.close();
    }

}
