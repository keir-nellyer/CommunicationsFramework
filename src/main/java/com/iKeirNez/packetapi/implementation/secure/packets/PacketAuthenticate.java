package com.iKeirNez.packetapi.implementation.secure.packets;

import com.iKeirNez.packetapi.api.packets.Packet;
import lombok.Data;

/**
 * Created by iKeirNez on 27/04/2014.
 */
@Data
public class PacketAuthenticate implements Packet {

    private static final long serialVersionUID = 8253624944786768643L;

    private final char[] key;
}
