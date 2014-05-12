package com.ikeirnez.communicationsframework.api.connection;

import com.ikeirnez.communicationsframework.api.Callback;
import com.ikeirnez.communicationsframework.api.HookType;
import com.ikeirnez.communicationsframework.api.packets.PacketListener;

import java.io.Closeable;
import java.util.List;

/**
 * Manages a group of connections.
 *
 * @author iKeirNez
 */
public interface ConnectionManager extends Closeable {

    /**
     * Gets the connections associated with this manager
     *
     * @return The connections associated with this manager
     */
    public List<Connection> getConnections();

    /**
     * Returns a new instance of a client connection
     *
     * @param serverddress The address of the server we should connect to
     * @param port         The port we should use to connect
     * @return The Client Connection
     */
    public ClientConnection newClientConnection(String serverddress, int port);

    /**
     * Returns a new instance of an authenticated client connection
     *
     * @param serverddress The address of the server we should connect to
     * @param port         The port we should use to connect
     * @param key          The key to authenticate with, this should be the same as the one on the server-side
     * @return The Authenticated Client Connection
     */
    public AuthenticatedClientConnection newAuthenticatedClientConnection(String serverddress, int port, char[] key);

    /**
     * Returns a new instance of a server connection
     *
     * @param clientddress The address of the client which we will accept connections from (null or empty string to allow any address)
     * @param port         The port we should listen on
     * @return The Server Connection
     */
    public ServerConnection newServerConnection(String clientddress, int port);

    /**
     * Returns a new instance of an authenticated server connection
     *
     * @param clientddress The address of the client which we will accept connections from (null or empty string to allow any address)
     * @param port         The port we should listen on
     * @param key          The key to authenticate with, this will be matched against the clients version
     * @return The Authenticated Server Connection
     */
    public AuthenticatedServerConnection newAuthenticatedServerConnection(String clientddress, int port, char[] key);

    /**
     * Registers an instance as being a listener, any packets received from linked Connection will be passed to applicable methods
     *
     * Methods must have the {@link com.ikeirnez.communicationsframework.api.packets.PacketHandler} annotation
     * Methods have an optional first parameter {@link com.ikeirnez.communicationsframework.api.connection.Connection} and a required second parameter implementing {@link com.ikeirnez.communicationsframework.api.packets.Packet}
     *
     * @param packetListener The instance we should register and pass packets to
     */
    public void registerListener(PacketListener packetListener);

    /**
     * Checks if a listener is registered
     *
     * @param packetListener The listener to check for being registered
     * @return Register status
     */
    public boolean isListenerRegistered(PacketListener packetListener);

    /**
     * Un-registers a listener instance
     *
     * @param packetListener The listener instance to un-register
     */
    public void unregisterListener(PacketListener packetListener);

    /**
     * Consumer which will be called when something happens
     *
     * @param hookType The type of hook to listen for
     * @param callable The {@link com.ikeirnez.communicationsframework.api.Callback} object to be called
     */
    public void addHook(HookType hookType, Callback<Connection> callable);

}
