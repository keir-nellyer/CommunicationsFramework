package com.iKeirNez.packetapi.test;

import com.iKeirNez.packetapi.packets.Packet;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public class TestPacket implements Packet {

    private static final long serialVersionUID = -250778971482745068L;

    private int number;

    public TestPacket(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }

}
