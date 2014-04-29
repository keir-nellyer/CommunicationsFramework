package com.iKeirNez.CommunicationsFramework.examples.standard.client;

import com.iKeirNez.CommunicationsFramework.api.packets.PacketHandler;
import com.iKeirNez.CommunicationsFramework.api.packets.PacketListener;
import com.iKeirNez.CommunicationsFramework.examples.PacketTest;

/**
 * A listener class to demonstrate the usage of Standard Connections
 * Created by iKeirNez on 28/04/2014.
 */
public class StandardClientExampleListener implements PacketListener {

    @PacketHandler
    public void onPacketTest(PacketTest packet){ // this will be run when we receive a reply from the server
        System.out.println("Received packet, data: " + packet.getRandomString()); // print the data
    }

}
