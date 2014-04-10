package com.iKeirNez.networking.connections;

import com.iKeirNez.networking.packets.Packet;
import com.iKeirNez.networking.threads.IncomingThread;
import com.iKeirNez.networking.threads.OutgoingThread;
import lombok.Getter;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public class Connection {

    @Getter protected Socket socket = null;
    protected IncomingThread incomingThread = null;
    protected OutgoingThread outgoingThread = null;
    @Getter protected boolean connected = false;

    protected ConnectionManager connectionManager;
    @Getter private String address;
    @Getter private int port;

    protected Connection(ConnectionManager connectionManager, String address, int port){
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
