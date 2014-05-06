package com.ikeirnez.communicationsframework.examples;

import com.ikeirnez.communicationsframework.api.packets.Packet;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class PacketTest implements Packet {

  private static final long serialVersionUID = -3355162086281993659L;

  private final String randomString;

  public PacketTest(String randomString) {
    this.randomString = randomString;
  }

  public String getRandomString( ) {
    return randomString;
  }
}
