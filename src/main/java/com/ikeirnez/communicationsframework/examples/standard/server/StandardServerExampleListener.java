package com.ikeirnez.communicationsframework.examples.standard.server;

import com.ikeirnez.communicationsframework.api.connection.Connection;
import com.ikeirnez.communicationsframework.api.packets.PacketHandler;
import com.ikeirnez.communicationsframework.api.packets.PacketListener;
import com.ikeirnez.communicationsframework.examples.PacketTest;

/**
 * A listener class to demonstrate the usage of Standard Connections
 * Created by iKeirNez on 28/04/2014.
 */
public class StandardServerExampleListener implements PacketListener {

  @PacketHandler
  public void onTestPacket(Connection connection, PacketTest packet) { // this will be run when we receive the packet from the client, also notice how the first parameter is optional
    System.out.println("Received packet, data: " + packet.getRandomString() + "\nSending reply"); // print the data
    connection.sendPacket(new PacketTest("Hey Client! I got your message, here's a reply!")); // reply
  }

}
