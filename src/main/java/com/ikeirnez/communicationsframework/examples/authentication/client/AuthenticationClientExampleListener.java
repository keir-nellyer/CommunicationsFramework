package com.ikeirnez.communicationsframework.examples.authentication.client;

import com.ikeirnez.communicationsframework.api.packets.PacketHandler;
import com.ikeirnez.communicationsframework.api.packets.PacketListener;
import com.ikeirnez.communicationsframework.examples.PacketTest;

/**
 * A listener class to demonstrate the usage of Authenticated Connections
 * Created by iKeirNez on 27/04/2014.
 */
public class AuthenticationClientExampleListener implements PacketListener {

  @PacketHandler
  public void onPacketTest(PacketTest packet) { // this will be run when we receive a reply from the server
    System.out.println("Received packet, data: " + packet.getRandomString()); // print the data
  }

}
