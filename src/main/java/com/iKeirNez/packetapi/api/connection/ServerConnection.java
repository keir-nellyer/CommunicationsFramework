package com.iKeirNez.packetapi.api.connection;

/**
 * Created by iKeirNez on 10/04/2014.
 */
public interface ServerConnection extends Connection {

    /**
     * Binds the specified port and begins listening for incoming connections
     */
    public void bind();

}
