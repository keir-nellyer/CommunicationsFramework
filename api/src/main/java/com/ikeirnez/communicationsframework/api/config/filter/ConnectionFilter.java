package com.ikeirnez.communicationsframework.api.config.filter;

import com.ikeirnez.communicationsframework.api.connection.ServerConnection;

import java.net.InetAddress;

/**
 * Interface for allowing or disallowing incoming connections.
 */
@FunctionalInterface
public interface ConnectionFilter {

    /**
     * Called when a client attempts to connect to the server.
     *
     * @param serverConnection the server connection the client is attempting to connect to
     * @param incomingConnectionAddress the clients address
     * @return true if we should accept this connection, false otherwise
     */
    public boolean shouldAccept(ServerConnection serverConnection, InetAddress incomingConnectionAddress);

}
