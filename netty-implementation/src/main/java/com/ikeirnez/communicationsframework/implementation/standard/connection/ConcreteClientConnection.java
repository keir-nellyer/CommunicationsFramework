package com.ikeirnez.communicationsframework.implementation.standard.connection;

import com.ikeirnez.communicationsframework.api.connection.ClientConnection;
import com.ikeirnez.communicationsframework.implementation.ConcreteConnectionManager;
import com.ikeirnez.communicationsframework.implementation.standard.handlers.StandardInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class ConcreteClientConnection extends ConcreteConnection implements ClientConnection {

    public final Bootstrap bootstrap = new Bootstrap();
    private final EventLoopGroup group = new NioEventLoopGroup();

    public ConcreteClientConnection(ConcreteConnectionManager connectionManager, String serverddress, int port) {
        super(connectionManager, serverddress, port);

        if (serverddress == null || serverddress.isEmpty()) {
            throw new UnsupportedOperationException("Server Address cannot be null or empty");
        }

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new StandardInitializer(this))
                .remoteAddress(getSocketAddress());
    }

    @Override
    public void connect() {
        bootstrap.connect().addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (!f.isSuccess()) {
                    if (f.cause() instanceof ConnectException) {
                        f.channel().eventLoop().schedule(new Runnable() {
                            @Override
                            public void run() {
                                connect();
                            }
                        }, 1, TimeUnit.SECONDS);
                    } else {
                        throw new Exception("Error whilst connecting to " + getSocketAddress(), f.cause());
                    }
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
