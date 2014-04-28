package com.iKeirNez.packetapi.examples.standard.server;

import com.iKeirNez.packetapi.api.connection.Connection;
import com.iKeirNez.packetapi.api.packets.PacketHandler;
import com.iKeirNez.packetapi.api.packets.PacketListener;
import com.iKeirNez.packetapi.examples.PacketTest;

/**
 * A listener class to demonstrate the usage of Standard Connections
 * Created by iKeirNez on 28/04/2014.
 */
public class StandardServerExampleListener implements PacketListener {

    @PacketHandler
    public void onTestPacket(Connection connection, PacketTest packet){ // this will be run when we receive the packet from the client, also notice how the first parameter is optional
        System.out.println("Received packet, data: " + packet.getRandomString() + "\nSending reply"); // print the data
        connection.sendPacket(new PacketTest("Hey Client! I got your message, here's a reply!")); // reply
    }

}
