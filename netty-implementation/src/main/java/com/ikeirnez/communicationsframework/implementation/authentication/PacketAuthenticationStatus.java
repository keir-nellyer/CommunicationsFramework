package com.ikeirnez.communicationsframework.implementation.authentication;

import com.ikeirnez.communicationsframework.api.packets.Packet;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class PacketAuthenticationStatus implements Packet {

    private static final long serialVersionUID = 2164463256993419796L;

    private final boolean allowed;

    public PacketAuthenticationStatus(boolean allowed) {
        this.allowed = allowed;
    }

    public boolean isAllowed() {
        return allowed;
    }
}
