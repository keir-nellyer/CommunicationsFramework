package com.iKeirNez.packetapi.test;

import com.iKeirNez.packetapi.api.connection.ConnectionManager;
import com.iKeirNez.packetapi.api.HookType;
import com.iKeirNez.packetapi.api.connection.Connection;
import com.iKeirNez.packetapi.api.packets.PacketHandler;
import com.iKeirNez.packetapi.api.packets.PacketListener;
import com.iKeirNez.packetapi.implementation.connection.IConnectionManager;

/**
 * Created by iKeirNez on 18/04/2014.
 */
public class TestServer implements PacketListener {

    public static Connection clientConnection = null;

    public static void main(String[] args){
        try {
            ConnectionManager connectionManager = new IConnectionManager();
            connectionManager.registerListener(new TestServer());
            connectionManager.addHook(HookType.CONNECTED, (c) -> {
                System.out.println("Server connected");
                c.sendPacket(new TestPacket(555));
            });

            clientConnection = connectionManager.newServerConnection("localhost", 25565);
            connectionManager.addHook(HookType.LOST_CONNECTION, (s) -> System.out.println("Lost connection hook"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PacketHandler
    public void onPacket(TestPacket packet){
        System.out.println("[Server] TestPacket receieved, data: " + packet.getNumber());
    }

}
