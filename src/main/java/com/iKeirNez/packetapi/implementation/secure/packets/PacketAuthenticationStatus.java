package com.iKeirNez.packetapi.implementation.secure.packets;

import com.iKeirNez.packetapi.api.packets.Packet;
import lombok.Data;

/**
 * Created by iKeirNez on 27/04/2014.
 */
@Data
public class PacketAuthenticationStatus implements Packet {

    private static final long serialVersionUID = 2164463256993419796L;

    private final boolean allowed;
}
