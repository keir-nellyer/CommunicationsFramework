package com.ikeirnez.communicationsframework.implementation.standard.connection;

import com.ikeirnez.communicationsframework.api.config.connection.ClientConnectionConfig;
import com.ikeirnez.communicationsframework.api.connection.ClientConnection;
import com.ikeirnez.communicationsframework.api.connection.ConnectionType;
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
import java.util.logging.Logger;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class ConcreteClientConnection extends ConcreteConnection implements ClientConnection {

    public final Bootstrap bootstrap = new Bootstrap();
    private final EventLoopGroup group = new NioEventLoopGroup();

    private final ClientConnectionConfig connectionConfig;

    public ConcreteClientConnection(ConcreteConnectionManager connectionManager, ClientConnectionConfig connectionConfig) {
        super(connectionManager, Logger.getLogger("Client Connection (" + connectionConfig.getServerAddress().toString() + ")"));

        this.connectionConfig = connectionConfig;

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new StandardInitializer(this))
                .remoteAddress(connectionConfig.getServerAddress());
    }

    @Override
    public ClientConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.CLIENT;
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
                        throw new Exception("Error whilst connecting to " + connectionConfig.getServerAddress(), f.cause());
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
