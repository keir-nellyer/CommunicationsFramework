package com.iKeirNez.networking.threads;

import com.iKeirNez.networking.connections.Connection;
import com.iKeirNez.networking.connections.ConnectionManager;
import com.iKeirNez.networking.packets.Packet;
import lombok.Getter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketTimeoutException;

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
