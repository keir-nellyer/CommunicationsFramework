package com.iKeirNez.packetapi.implementation.connection;

import com.iKeirNez.packetapi.api.connection.ClientConnection;
import com.iKeirNez.packetapi.implementation.handlers.StandardInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class IClientConnection extends IConnection implements ClientConnection {

    private EventLoopGroup group = new NioEventLoopGroup();
    public Bootstrap bootstrap = new Bootstrap();

    protected IClientConnection(IConnectionManager connectionManager, String serverAddress, int port) throws Exception {
        super(connectionManager, serverAddress, port);

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new StandardInitializer(this))
                .remoteAddress(serverAddress, port);

        init();
    }

    public void init(){
        bootstrap.connect().addListener((ChannelFuture f) -> {
            if (!f.isSuccess()){
                if (f.cause() instanceof ConnectException){
                    f.channel().eventLoop().schedule((Runnable) bootstrap::connect, 5, TimeUnit.SECONDS);
                } else {
                    throw new Exception("Error whilst connecting to " + getAddress() + ":" + getPort(), f.cause());
                }
            }
        });
    }

    @Override
    public void close() throws InterruptedException {
        group.shutdownGracefully().sync();
        super.close();
    }

}
