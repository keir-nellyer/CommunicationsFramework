package com.ikeirnez.communicationsframework.implementation.standard.connection;

import com.ikeirnez.communicationsframework.api.connection.Connection;
import com.ikeirnez.communicationsframework.api.packets.Packet;
import com.ikeirnez.communicationsframework.implementation.ConcreteConnectionManager;
import com.ikeirnez.communicationsframework.implementation.handlers.PacketHandler;
import io.netty.channel.Channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public abstract class ConcreteConnection implements Connection {

    public final Logger logger;
    public final List<Packet> connectQueue = Collections.synchronizedList(new ArrayList<Packet>()); // packets to be sent when connection is (re)gained
    protected final ConcreteConnectionManager connectionManager;
    public PacketHandler packetHandler = null;
    public AtomicBoolean authenticated = new AtomicBoolean(false);
    public AtomicBoolean firstConnect = new AtomicBoolean(true);
    public AtomicBoolean closing = new AtomicBoolean(false), expectingDisconnect = new AtomicBoolean(false);

    public ConcreteConnection(ConcreteConnectionManager connectionManager, Logger logger) {
        this.connectionManager = connectionManager;
        this.logger = logger;

        connectionManager.connections.add(this);
    }

    @Override
    public boolean isConnected() {
        return (packetHandler != null) && packetHandler.connected();
    }

    @Override
    public void sendPacket(Packet packet) {
        sendPacket(packet, true);
    }

    @Override
    public void sendPacket(Packet packet, boolean queueIfNotConnected) {
        if (!isConnected()) {
            if (queueIfNotConnected) {
                connectQueue.add(packet);
            }

            return;
        }

        packetHandler.send(packet);
    }

    @Override
    public void close() throws IOException {
        close(true);
    }

    public void close(boolean print) throws IOException {
        if (print) {
            logger.info("Closing...");
        }

        closing.set(true);
        connectionManager.connections.remove(this);

        if (packetHandler != null) {
            packetHandler.close();
        }
    }

    public ConcreteConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public final class Unsafe {
        public Channel getChannel(){
            return packetHandler.ctx == null ? null : packetHandler.ctx.channel();
        }
    }
}