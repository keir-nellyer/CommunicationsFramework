package com.iKeirNez.packetapi.test;

import com.iKeirNez.packetapi.api.connection.Connection;
import com.iKeirNez.packetapi.api.connection.ConnectionManager;
import com.iKeirNez.packetapi.api.HookType;
import com.iKeirNez.packetapi.implementation.connection.IConnectionManager;
import com.iKeirNez.packetapi.api.packets.PacketHandler;
import com.iKeirNez.packetapi.api.packets.PacketListener;

import java.net.ConnectException;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public class TestClient implements PacketListener {

    public static Connection serverConnection = null;

    public static void main(String[] args){
        try {
            ConnectionManager connectionManager = new IConnectionManager();
            connectionManager.registerListener(new TestClient());
            connectionManager.addHook(HookType.CONNECTED, (c) -> {
                System.out.println("Client connected");
                c.sendPacket(new TestPacket(6969));
            });


            while (serverConnection == null){
                try {
                    serverConnection = connectionManager.newClientConnection("localhost", 25565);
                } catch (ConnectException e){}
            }

            connectionManager.addHook(HookType.LOST_CONNECTION, (s) -> System.out.println("Lost connection hook"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PacketHandler
    public void onPacket(TestPacket packet){
        System.out.println("[Client] TestPacket receieved, data: " + packet.getNumber());
    }

}
