package com.ikeirnez.communicationsframework.implementation.authentication.simple;

import com.ikeirnez.communicationsframework.api.packets.Packet;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class PacketSimpleAuthenticate implements Packet {

    private static final long serialVersionUID = 8253624944786768643L;
    private final String key;

    public PacketSimpleAuthenticate(String key) {
        super();
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
