package com.iKeirNez.packetapi.implementation.secure.connection;

import com.iKeirNez.packetapi.api.connection.AuthenticatedClientConnection;
import com.iKeirNez.packetapi.implementation.IConnectionManager;
import com.iKeirNez.packetapi.implementation.secure.handlers.AuthenticatedClientHandler;
import com.iKeirNez.packetapi.implementation.standard.handlers.StandardInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class IAuthenticatedClientConnection extends IAuthenticatedConnection implements AuthenticatedClientConnection {

    private final EventLoopGroup group = new NioEventLoopGroup();
    public final Bootstrap bootstrap = new Bootstrap();

    public IAuthenticatedClientConnection(IConnectionManager connectionManager, String serverAddress, int port, char[] key) {
        super(connectionManager, serverAddress, port, key);

        if (serverAddress == null || serverAddress.isEmpty()){
            throw new UnsupportedOperationException("Server Address cannot be null or empty");
        }

        IAuthenticatedClientConnection instance = this;

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline = ch.pipeline();

                        StandardInitializer.addObjectHandlers(instance, channelPipeline);
                        ch.pipeline().addLast(new AuthenticatedClientHandler(instance));
                        StandardInitializer.addOthers(instance, channelPipeline);
                    }
                }).remoteAddress(getSocketAddress());
    }

    @Override
    public void connect(){
        bootstrap.connect().addListener((ChannelFuture f) -> {
            if (!f.isSuccess()){
                if (f.cause() instanceof ConnectException){
                    f.channel().eventLoop().schedule((Runnable) this::connect, 1, TimeUnit.SECONDS);
                } else {
                    throw new Exception("Error whilst connecting to " + getSocketAddress(), f.cause());
                }
            }
        });
    }

    @Override
    public void close() throws IOException {
        super.close();
        group.shutdownGracefully().syncUninterruptibly();
    }

}
