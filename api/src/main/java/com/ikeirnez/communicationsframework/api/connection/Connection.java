package com.ikeirnez.communicationsframework.api.connection;

import com.ikeirnez.communicationsframework.api.config.connection.ConnectionConfig;
import com.ikeirnez.communicationsframework.api.packets.Packet;

import java.io.Closeable;

/**
 * Represents a connection.
 *
 * @author iKeirNez
 */
public interface Connection extends Closeable {

    /**
     * Gets the configuration for this connection.
     *
     * @return the configuration for this connection
     */
    public ConnectionConfig getConnectionConfig();

    /**
     * Gets the type of connection this is.
     *
     * @return the type of connection this is
     */
    public ConnectionType getConnectionType();

    /**
     * Gets the connected state of this connection
     *
     * @return The connected state
     */
    public boolean isConnected();

    /**
     * Sends a packet via this connection to the other side
     *
     * @param packet              The packet instance to send
     * @param queueIfNotConnected If this connection is not currently connected (lost connection or initialising connection) defaults to true
     */
    public void sendPacket(Packet packet, boolean queueIfNotConnected);

    /**
     * Sends a packet via this connection to the other side
     *
     * @param packet The packet instance to send
     */
    public void sendPacket(Packet packet);

}
