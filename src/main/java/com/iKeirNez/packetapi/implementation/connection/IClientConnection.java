package com.iKeirNez.packetapi.implementation.connection;

import com.iKeirNez.packetapi.api.connection.ClientConnection;
import com.iKeirNez.packetapi.implementation.handlers.StandardInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class IClientConnection extends IConnection implements ClientConnection {

    private final EventLoopGroup group = new NioEventLoopGroup();
    private final Bootstrap bootstrap = new Bootstrap();

    protected IClientConnection(IConnectionManager connectionManager, String serverAddress, int port){
        super(connectionManager, serverAddress, port);

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new StandardInitializer(this))
                .remoteAddress(serverAddress, port);

        connect();
    }

    public void connect(){
        bootstrap.connect().addListener((ChannelFuture f) -> {
            if (!f.isSuccess()){
                if (f.cause() instanceof ConnectException){
                    f.channel().eventLoop().schedule((Runnable) this::connect, 1, TimeUnit.SECONDS);
                } else {
                    throw new Exception("Error whilst connecting to " + getAddress() + ":" + getPort(), f.cause());
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
