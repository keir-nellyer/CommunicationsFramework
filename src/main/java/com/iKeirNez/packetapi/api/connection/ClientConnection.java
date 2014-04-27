package com.iKeirNez.packetapi.api.connection;

/**
 * Created by iKeirNez on 10/04/2014.
 */
public interface ClientConnection extends Connection {

    /**
     * Initiates a connection to the specified host name & port
     * Will continually attempt to connect until success
     */
    public void connect();

}
