package com.ikeirnez.communicationsframework.api.connection;

import com.ikeirnez.communicationsframework.api.packets.Packet;

import java.io.Closeable;
import java.net.InetSocketAddress;

/**
 * Represents a connection.
 *
 * @author iKeirNez
 */
public interface Connection extends Closeable {

	/**
	 * Gets the connected state of this connection
	 *
	 * @return The connected state
	 */
	public boolean isConnected( );

	/**
	 * Gets the address that this Connection will connect to (hostname may be null in some ServerConnection cases)
	 *
	 * @return The address that this Connection will connect to/listen for
	 */
	public InetSocketAddress getSocketAddress( );

	/**
	 * Gets the hostname that this Connection will connect to (may be null in some ServerConnection instances)
	 *
	 * @return The address that this Connection will connect to
	 */
	public String getHostName( );

	/**
	 * Gets the port this Connection will use for the connection
	 *
	 * @return The port
	 */
	public int getPort( );

	/**
	 * Sends a packet via this connection to the other side
	 *
	 * @param packet The packet instance to send
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
