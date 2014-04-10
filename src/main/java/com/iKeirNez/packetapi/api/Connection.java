package com.iKeirNez.packetapi.api;

import com.iKeirNez.packetapi.api.packets.Packet;

import java.net.Socket;

/**
 * Created by iKeirNez on 10/04/2014.
 */
public interface Connection {

    /**
     * Gets the associated socket
     * @return The associated socket
     */
    public Socket getSocket();

    /**
     * Gets the connected state of this connection
     * @return The connected state
     */
    public boolean isConnected();

    /**
     * Gets the address that this Connection will connect to (may be null in some ServerConnection instances)
     * @return The address that this Connection will connect to
     */
    public String getAddress();

    /**
     * Gets the port this Connection will use
     * @return The port
     */
    public int getPort();

    /**
     * Sends a packet via this connection to the other side
     * @param packet The packet instance to send
     */
    public void sendPacket(Packet packet);

    /**
     * Gracefully closes all threads and sockets associated with this connection
     */
    public void close();

}
