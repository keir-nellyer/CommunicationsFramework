package com.iKeirNez.packetapi.implementation.connection;

import com.iKeirNez.packetapi.api.connection.ServerConnection;
import com.iKeirNez.packetapi.implementation.handlers.StandardInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class IServerConnection extends IConnection implements ServerConnection {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    protected IServerConnection(IConnectionManager connectionManager, String clientAddress, int port) throws Exception {
        super(connectionManager, clientAddress, port);

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new StandardInitializer(this))
                .localAddress(clientAddress, port);

        init();
    }

    public void init(){
        serverBootstrap.bind().addListener((ChannelFuture f) -> {
            if (!f.isSuccess()){
                throw new Exception("Error whilst connecting to " + getAddress() + ":" + getPort(), f.cause());
            }
        });
    }

    @Override
    public void close() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
        super.close();
    }

}
