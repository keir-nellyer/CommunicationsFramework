package com.iKeirNez.packetapi.api;

import com.iKeirNez.packetapi.api.packets.PacketListener;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by iKeirNez on 10/04/2014.
 */
public interface ConnectionManager {

    /**
     * Gets the connections associated with this manager
     * @return The connections associated with this manager
     */
    public List<Connection> getConnections();

    /**
     * Returns a new instance of a client connection
     * @param serverAddress The address of the server we should connect to
     * @param port The port we should use to connect
     * @return The Client Connection
     * @throws java.io.IOException Thrown if anything goes wrong during construction
     */
    public ClientConnection newClientConnection(String serverAddress, int port) throws IOException;

    /**
     * Returns a new instance of a server connection
     * @param clientAddress The address of the client which we will accept connections from (null to allow any address)
     * @param port The port we should listen on
     * @return The Server Connection
     * @throws IOException Thrown if anything goes wrong during construction
     */
    public ServerConnection newServerConnection(String clientAddress, int port) throws IOException;

    /**
     * Registers an instance as being a listener, any packets received from linked Connection will be passed to applicable methods
     *
     * Methods must have the {@link com.iKeirNez.packetapi.api.packets.PacketHandler} annotation
     * Methods have an optional first parameter {@link com.iKeirNez.packetapi.connections.IConnection} and a required second parameter implementing {@link com.iKeirNez.packetapi.api.packets.Packet}
     *
     * @param packetListener The instance we should register and pass packets to
     */
    public void registerListener(PacketListener packetListener);

    /**
     * Checks if a listener is registered
     * @param packetListener The listener to check for being registered
     * @return Register status
     */
    public boolean isListenerRegistered(PacketListener packetListener);

    /**
     * Un-registers a listener instance
     * @param packetListener The listener instance to un-register
     */
    public void unregisterListener(PacketListener packetListener);

    /**
     * Consumer which will be called when something happens
     * @param hookType The type of hook to listen for
     * @param consumer The consumer to be called
     */
    public void addHook(HookType hookType, Consumer<Connection> consumer);

}
