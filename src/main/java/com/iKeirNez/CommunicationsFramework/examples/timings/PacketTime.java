package com.iKeirNez.CommunicationsFramework.examples.timings;

import com.iKeirNez.CommunicationsFramework.api.packets.Packet;
import lombok.Data;

/**
 * Basic packet which contains the time (in nanoseconds) at which the packet was sent & also a boolean which will be true if it is the last packet being sent
 */
@Data
public class PacketTime implements Packet {

    private static final long serialVersionUID = 573893027740646116L;

    private final long nanosSent;
    private final boolean last;
}
