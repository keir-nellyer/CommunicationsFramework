package com.iKeirNez.packetapi.implementation.connection;

import com.iKeirNez.packetapi.api.connection.Connection;
import com.iKeirNez.packetapi.api.packets.Packet;
import com.iKeirNez.packetapi.implementation.handlers.ConnectionHandler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public abstract class IConnection implements Connection {

    @Getter protected IConnectionManager connectionManager;
    @Getter private String address;
    @Getter private int port;

    public ConnectionHandler connectionHandler = null;
    public Logger logger;
    public boolean firstConnect = true;
    public List<Packet> connectQueue = new ArrayList<>(); // packets to be sent when connection is (re)gained

    protected IConnection(IConnectionManager connectionManager, String address, int port){
        this.connectionManager = connectionManager;
        this.address = address;
        this.port = port;

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

    public abstract void handleReconnect();

    public void close() throws InterruptedException {
        connectionManager.connections.remove(this);
        connectionHandler.close();
    }

}
