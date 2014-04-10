package com.iKeirNez.packetapi.test;

import com.iKeirNez.packetapi.api.Connection;
import com.iKeirNez.packetapi.api.ConnectionManager;
import com.iKeirNez.packetapi.api.HookType;
import com.iKeirNez.packetapi.implementations.IClientConnection;
import com.iKeirNez.packetapi.implementations.IConnectionManager;
import com.iKeirNez.packetapi.api.packets.PacketHandler;
import com.iKeirNez.packetapi.api.packets.PacketListener;

import java.io.IOException;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public class Test implements PacketListener {

    public static Connection serverConnection, clientConnection;

    public static void main(String[] args){
        try {
            ConnectionManager connectionManager = new IConnectionManager();
            connectionManager.registerListener(new Test());
            serverConnection = connectionManager.newServerConnection("localhost", 25565);

            connectionManager.addHook(HookType.CONNECTED, (c) -> {
                if (c instanceof IClientConnection){
                    System.out.println("Client connected");
                    serverConnection.sendPacket(new TestPacket(555));
                }
            });

            clientConnection = connectionManager.newClientConnection("localhost", 25565);

            connectionManager.addHook(HookType.LOST_CONNECTION, (s) -> System.out.println("Lost connection hook"));

            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                clientConnection.close();
                serverConnection.close();
                System.out.println("Closed");
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PacketHandler
    public void onPacket(TestPacket packet){
        System.out.println("TestPacket receieved, data: " + packet.getNumber());
    }

}
