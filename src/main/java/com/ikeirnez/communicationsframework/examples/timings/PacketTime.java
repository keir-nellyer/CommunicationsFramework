package com.ikeirnez.communicationsframework.examples.timings;

import com.ikeirnez.communicationsframework.api.packets.Packet;

/**
 * Basic packet which contains the time (in nanoseconds) at which the packet was sent & also a boolean which will be true if it is the last packet being sent
 */
public class PacketTime implements Packet {

  private static final long serialVersionUID = 573893027740646116L;

  private final long nanosSent;
  private final boolean last;

  public PacketTime(long nanosSent, boolean last) {
    this.nanosSent = nanosSent;
    this.last = last;
  }

  public long getNanosSent( ) {
    return nanosSent;
  }

  public boolean isLast( ) {
    return last;
  }
}
