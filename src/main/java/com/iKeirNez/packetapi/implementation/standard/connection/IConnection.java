package com.iKeirNez.packetapi.implementation.standard.connection;

import com.iKeirNez.packetapi.api.connection.Connection;
import com.iKeirNez.packetapi.api.packets.Packet;
import com.iKeirNez.packetapi.implementation.IConnectionManager;
import com.iKeirNez.packetapi.implementation.handlers.PacketHandler;
import lombok.Getter;

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
public abstract class IConnection implements Connection {

    @Getter protected final IConnectionManager connectionManager;
    @Getter private final InetSocketAddress socketAddress;
    @Getter private final String hostName;
    @Getter private final int port;

    public PacketHandler packetHandler = null;
    public final Logger logger;
    public AtomicBoolean firstConnect = new AtomicBoolean(true);
    public final List<Packet> connectQueue = Collections.synchronizedList(new ArrayList<>()); // packets to be sent when connection is (re)gained
    public AtomicBoolean closing = new AtomicBoolean(false), expectingDisconnect = new AtomicBoolean(false);

    public IConnection(IConnectionManager connectionManager, String hostName, int port){
        this.connectionManager = connectionManager;
        this.hostName = hostName;
        this.port = port;
        this.socketAddress = new InetSocketAddress(hostName, port);

        connectionManager.connections.add(this);
        logger = Logger.getLogger("Connection (" + hostName + ":" + port + ")");
    }

    public boolean isConnected(){
        return packetHandler != null && packetHandler.connected();
    }

    public void sendPacket(Packet packet, boolean queueIfNotConnected){
        if (!isConnected()){
            if (queueIfNotConnected){
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
        if (print){
            logger.info("Closing...");
        }

        closing.set(true);
        connectionManager.connections.remove(this);
        packetHandler.close();
    }

}
