package com.iKeirNez.packetapi.connections;

import com.iKeirNez.packetapi.api.Connection;
import com.iKeirNez.packetapi.api.packets.Packet;
import com.iKeirNez.packetapi.threads.IncomingThread;
import com.iKeirNez.packetapi.threads.OutgoingThread;
import lombok.Getter;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public class IConnection implements Connection {

    @Getter protected Socket socket = null;
    protected IncomingThread incomingThread = null;
    protected OutgoingThread outgoingThread = null;
    @Getter protected boolean connected = false;

    protected IConnectionManager connectionManager;
    @Getter private String address;
    @Getter private int port;

    protected IConnection(IConnectionManager connectionManager, String address, int port){
        this.connectionManager = connectionManager;
        this.address = address;
        this.port = port;
    }

    public void sendPacket(Packet packet){
        if (!isConnected()){
            throw new RuntimeException("Tried to send packet whilst not connected");
        }

        outgoingThread.addToQueue(packet);
    }

    public void close(){
        connectionManager.connections.remove(this);

        if (incomingThread != null){
            incomingThread.close();
        }

        if (outgoingThread != null){
            outgoingThread.awaitTermination();
            while (!outgoingThread.isClosed());
        }

        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {}
        }
    }

}
