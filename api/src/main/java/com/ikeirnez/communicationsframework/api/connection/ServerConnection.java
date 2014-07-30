package com.ikeirnez.communicationsframework.api.connection;

import com.ikeirnez.communicationsframework.api.config.connection.ServerConnectionConfig;

import java.net.InetAddress;

/**
 * Represents a server connection.
 *
 * @author iKeirNez
 */
public interface ServerConnection extends Connection {

    @Override
    public ServerConnectionConfig getConnectionConfig();

    /**
     * Returns the {@link java.net.InetAddress} of the currently connected client.
     *
     * @return the address of the currently connected client
     */
    public InetAddress getConnectedClient();

    /**
     * Binds the specified port and begins listening for incoming connections
     */
    public void bind();

}
