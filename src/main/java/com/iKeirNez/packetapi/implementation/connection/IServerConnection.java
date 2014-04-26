package com.iKeirNez.packetapi.implementation.connection;

import com.iKeirNez.packetapi.api.connection.ServerConnection;
import com.iKeirNez.packetapi.implementation.handlers.StandardInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class IServerConnection extends IConnection implements ServerConnection {

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final ServerBootstrap serverBootstrap = new ServerBootstrap();

    protected IServerConnection(IConnectionManager connectionManager, String clientAddress, int port){
        super(connectionManager, clientAddress, port);

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new StandardInitializer(this))
                .localAddress(clientAddress, port);

        serverBootstrap.bind().addListener((ChannelFuture f) -> {
            if (!f.isSuccess()){
                throw new Exception("Error whilst connecting to " + getAddress() + ":" + getPort(), f.cause());
            }
        });
    }

    @Override
    public void close() throws IOException {
        super.close();
        bossGroup.shutdownGracefully().syncUninterruptibly();
        workerGroup.shutdownGracefully().syncUninterruptibly();
    }

}
