package com.ikeirnez.communicationsframework.implementation.handlers;

import com.ikeirnez.communicationsframework.api.packets.Packet;
import com.ikeirnez.communicationsframework.api.HookType;
import com.ikeirnez.communicationsframework.implementation.standard.connection.ConcreteConnection;
import com.ikeirnez.communicationsframework.implementation.packets.PacketDisconnect;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by iKeirNez on 18/04/2014.
 */
public class PacketHandler extends ChannelInboundHandlerAdapter implements Closeable {

  private final ConcreteConnection connection;
  private ChannelHandlerContext ctx = null;

  public PacketHandler(ConcreteConnection connection) {
    this.connection = connection;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    if (connection.getHostName() != null && !connection.getHostName().isEmpty() && !connection.getSocketAddress().isUnresolved()
        && !((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress().equalsIgnoreCase(connection.getSocketAddress().getAddress().getHostAddress())) {
      connection.closing.set(true);
      ctx.channel().disconnect();
      return;
    }

    this.ctx = ctx;

    for (Packet packet : connection.connectQueue) {
      connection.sendPacket(packet);
    }

    connection.connectQueue.clear();

    if (connection.firstConnect.get()) {
      connection.getConnectionManager().callHook(connection, HookType.CONNECTED);
      connection.firstConnect.set(false);
    } else {
      connection.logger.info("Successfully reconnected");
      connection.getConnectionManager().callHook(connection, HookType.RECONNECTED);
    }
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
    Packet packet = (Packet) object;

    if (packet instanceof PacketDisconnect) {
      connection.expectingDisconnect.set(true);
    } else {
      connection.getConnectionManager().callListeners(connection, packet);
    }
  }

  public ChannelFuture send(Packet packet) {
    ChannelFuture channelFuture = ctx.writeAndFlush(packet);
    channelFuture.addListener(new GenericFutureListener<ChannelFuture>() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        if (!future.isSuccess() && !connection.closing.get()) {
          throw new Exception("Unexpected exception whilst sending packet", future.cause());
        }
      }
    });

    return channelFuture;
  }

  public boolean connected( ) {
    return ctx != null && ctx.channel().isOpen();
  }

  @Override
  public void close( ) throws IOException {
    if (ctx != null) {
      connection.closing.set(true);

      if (connection.isConnected()) {
        send(new PacketDisconnect()).syncUninterruptibly();
      }

      ctx.channel().disconnect().syncUninterruptibly();
    }
  }

}
