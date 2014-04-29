package com.iKeirNez.CommunicationsFramework.implementation.secure.connection;

import com.iKeirNez.CommunicationsFramework.api.connection.AuthenticatedServerConnection;
import com.iKeirNez.CommunicationsFramework.implementation.IConnectionManager;
import com.iKeirNez.CommunicationsFramework.implementation.secure.handlers.AuthenticatedServerHandler;
import com.iKeirNez.CommunicationsFramework.implementation.standard.handlers.StandardInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class IAuthenticatedServerConnection extends IAuthenticatedConnection implements AuthenticatedServerConnection {

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final ServerBootstrap serverBootstrap = new ServerBootstrap();

    public IAuthenticatedServerConnection(IConnectionManager connectionManager, String hostName, int port, char[] key) {
        super(connectionManager, hostName, port, key);

        IAuthenticatedServerConnection instance = this;

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline = ch.pipeline();

                        StandardInitializer.addObjectHandlers(instance, channelPipeline);
                        ch.pipeline().addLast(new AuthenticatedServerHandler(instance));
                        StandardInitializer.addOthers(instance, channelPipeline);
                    }
                })
                .localAddress("", getSocketAddress().getPort());
    }

    public void bind(){
        serverBootstrap.bind().addListener((ChannelFuture f) -> {
            if (!f.isSuccess()){
                throw new Exception("Error whilst binding port for: " + getSocketAddress(), f.cause());
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
