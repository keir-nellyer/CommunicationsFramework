package com.iKeirNez.CommunicationsFramework.api.connection;

/**
 * Represents a server connection.
 *
 * @author iKeirNez
 */
public interface ServerConnection extends Connection {

    /**
     * Binds the specified port and begins listening for incoming connections
     */
    public void bind();

}
