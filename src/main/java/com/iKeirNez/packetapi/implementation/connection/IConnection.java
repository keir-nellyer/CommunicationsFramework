package com.iKeirNez.packetapi.implementation.connection;

import com.iKeirNez.packetapi.api.connection.Connection;
import com.iKeirNez.packetapi.api.packets.Packet;
import com.iKeirNez.packetapi.implementation.handlers.ConnectionHandler;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public abstract class IConnection implements Connection {

    @Getter protected final IConnectionManager connectionManager;
    @Getter private final String address;
    @Getter private final int port;

    public ConnectionHandler connectionHandler = null;
    public final Logger logger;
    public boolean firstConnect = true;
    public final List<Packet> connectQueue = new ArrayList<>(); // packets to be sent when connection is (re)gained
    public boolean closing = false, expectingDisconnect = false;

    protected IConnection(IConnectionManager connectionManager, String address, int port){
        this.connectionManager = connectionManager;
        this.address = address;
        this.port = port;

        connectionManager.connections.add(this);
        logger = Logger.getLogger("Connection (" + address + ":" + port + ")");
    }

    public boolean isConnected(){
        return connectionHandler.connected();
    }

    public void sendPacket(Packet packet, boolean queueIfNotConnected){
        if (!isConnected()){
            if (queueIfNotConnected){
                connectQueue.add(packet);
            }

            return;
        }

        connectionHandler.send(packet);
    }

    @Override
    public void close() throws IOException {
        close(true);
    }

    public void close(boolean print) throws IOException {
        if (print){
            logger.info("Closing...");
        }

        closing = true;
        connectionManager.connections.remove(this);
        connectionHandler.close();
    }

}
