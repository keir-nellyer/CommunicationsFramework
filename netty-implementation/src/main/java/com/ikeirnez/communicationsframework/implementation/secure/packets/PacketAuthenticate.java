package com.ikeirnez.communicationsframework.implementation.secure.packets;

import com.ikeirnez.communicationsframework.api.packets.Packet;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class PacketAuthenticate implements Packet {

    private static final long serialVersionUID = 8253624944786768643L;
    private final char[] key;

    public PacketAuthenticate(char[] key) {
        super();
        this.key = key;
    }

    public char[] getKey() {
        return key;
    }
}
