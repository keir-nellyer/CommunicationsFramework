package com.ikeirnez.communicationsframework.implementation.standard.connection;

import com.ikeirnez.communicationsframework.api.connection.ServerConnection;
import com.ikeirnez.communicationsframework.implementation.ConcreteConnectionManager;
import com.ikeirnez.communicationsframework.implementation.standard.handlers.StandardInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class ConcreteServerConnection extends ConcreteConnection implements ServerConnection {

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final ServerBootstrap serverBootstrap = new ServerBootstrap();

    public ConcreteServerConnection(ConcreteConnectionManager connectionManager, String clientHostname, int port) {
        super(connectionManager, clientHostname, port);

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new StandardInitializer(this))
                .localAddress("", getSocketAddress().getPort());
    }

    public void bind() {
        serverBootstrap.bind().addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (!f.isSuccess()) {
                    throw new Exception("Error whilst binding port for: " + getSocketAddress(), f.cause());
                }
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
