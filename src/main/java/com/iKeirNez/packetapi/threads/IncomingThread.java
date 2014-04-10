package com.iKeirNez.packetapi.threads;

import com.iKeirNez.packetapi.connections.Connection;
import com.iKeirNez.packetapi.connections.ConnectionManager;
import com.iKeirNez.packetapi.packets.Packet;
import lombok.Getter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class IncomingThread extends Thread implements Runnable {

    @Getter private boolean running = true, closed = false;
    private ConnectionManager connectionManager;
    private Connection connection;

    public IncomingThread(String title, ConnectionManager connectionManager, Connection connection){
        super(title);
        this.connectionManager = connectionManager;
        this.connection = connection;
    }

    @Override
    public void run(){
        closed = false;

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(connection.getSocket().getInputStream()))){
            while (running){
                Packet packet = (Packet) objectInputStream.readObject();
                connectionManager.callListeners(connection, packet);
            }
        } catch (IOException e) {
            if (running){
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        closed = true;
    }

    public void close(){
        running = false;
    }

}
