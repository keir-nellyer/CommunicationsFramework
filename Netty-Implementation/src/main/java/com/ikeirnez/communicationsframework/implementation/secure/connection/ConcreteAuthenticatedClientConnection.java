package com.ikeirnez.communicationsframework.implementation.secure.connection;

import com.ikeirnez.communicationsframework.api.connection.AuthenticatedClientConnection;
import com.ikeirnez.communicationsframework.implementation.ConcreteConnectionManager;
import com.ikeirnez.communicationsframework.implementation.secure.handlers.AuthenticatedClientHandler;
import com.ikeirnez.communicationsframework.implementation.standard.handlers.StandardInitializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class ConcreteAuthenticatedClientConnection extends ConcreteAuthenticatedConnection implements AuthenticatedClientConnection {

  private final EventLoopGroup group = new NioEventLoopGroup();
  public final Bootstrap bootstrap = new Bootstrap();

  public ConcreteAuthenticatedClientConnection(ConcreteConnectionManager connectionManager, String serverddress, int port, char[] key) {
    super(connectionManager, serverddress, port, key);

    if (serverddress == null || serverddress.isEmpty()) {
      throw new UnsupportedOperationException("Server Address cannot be null or empty");
    }

    final ConcreteAuthenticatedClientConnection instance = this;

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
  public void connect( ) {
    bootstrap.connect().addListener(new GenericFutureListener<ChannelFuture>() {
      @Override
      public void operationComplete(ChannelFuture f) throws Exception {
        if (!f.isSuccess()) {
          if (f.cause() instanceof ConnectException) {
            f.channel().eventLoop().schedule(new Runnable() {
              @Override
              public void run( ) {
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
  public void close( ) throws IOException {
    super.close();
    group.shutdownGracefully().syncUninterruptibly();
  }

}
