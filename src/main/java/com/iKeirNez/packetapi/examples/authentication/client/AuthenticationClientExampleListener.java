package com.iKeirNez.packetapi.examples.authentication.client;

import com.iKeirNez.packetapi.api.packets.PacketHandler;
import com.iKeirNez.packetapi.api.packets.PacketListener;
import com.iKeirNez.packetapi.examples.PacketTest;

/**
 * A listener class to demonstrate the usage of Authenticated Connections
 * Created by iKeirNez on 27/04/2014.
 */
public class AuthenticationClientExampleListener implements PacketListener {

    @PacketHandler
    public void onPacketTest(PacketTest packet){ // this will be run when we receive a reply from the server
        System.out.println("Received packet, data: " + packet.getRandomString()); // print the data
    }

}
