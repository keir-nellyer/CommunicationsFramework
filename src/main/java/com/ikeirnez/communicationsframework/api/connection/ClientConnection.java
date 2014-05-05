package com.ikeirnez.communicationsframework.api.connection;

/**
 * Represents a client connection.
 *
 * @author iKeirNez
 */
public interface ClientConnection extends Connection {

	/**
	 * Initiates a connection to the specified host name and port
	 * Will continually attempt to connect until success
	 */
	public void connect( );

}
