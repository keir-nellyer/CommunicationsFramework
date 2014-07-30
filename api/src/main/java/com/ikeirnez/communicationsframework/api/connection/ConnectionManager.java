package com.ikeirnez.communicationsframework.api.connection;

import com.ikeirnez.communicationsframework.api.Callback;
import com.ikeirnez.communicationsframework.api.HookType;
import com.ikeirnez.communicationsframework.api.config.connection.ClientConnectionConfig;
import com.ikeirnez.communicationsframework.api.config.connection.ServerConnectionConfig;
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
     * Creates a new client connection.
     *
     * @param clientConnectionConfig the configuration for the connection
     * @return the connection
     */
    public ClientConnection newClientConnection(ClientConnectionConfig clientConnectionConfig);

    /**
     * Creates a new server connection.
     *
     * @param serverConnectionConfig the configuration for the connection
     * @return the connection
     */
    public ServerConnection newServerConnection(ServerConnectionConfig serverConnectionConfig);

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
