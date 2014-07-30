package com.ikeirnez.communicationsframework.api.connection;

import com.ikeirnez.communicationsframework.api.config.connection.ClientConnectionConfig;

/**
 * Represents a client connection.
 *
 * @author iKeirNez
 */
public interface ClientConnection extends Connection {

    @Override
    public ClientConnectionConfig getConnectionConfig();

    /**
     * Initiates a connection to the specified host name and port
     * Will continually attempt to connect until success
     */
    public void connect();

}
