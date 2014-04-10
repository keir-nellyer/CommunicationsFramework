package com.iKeirNez.packetapi.connections;

import com.iKeirNez.packetapi.api.ClientConnection;
import com.iKeirNez.packetapi.api.HookType;
import com.iKeirNez.packetapi.threads.IncomingThread;
import com.iKeirNez.packetapi.threads.OutgoingThread;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class IClientConnection extends IConnection implements ClientConnection {

    protected IClientConnection(IConnectionManager connectionManager, String serverAddress, int port) throws IOException {
        super(connectionManager, serverAddress, port);

        socket = new Socket(serverAddress, port);

        incomingThread = new IncomingThread("[Client] Incoming: " + serverAddress + ":" + port, connectionManager, this);
        outgoingThread = new OutgoingThread("[Client] Outgoing: " + serverAddress + ":" + port, this);

        incomingThread.start();
        outgoingThread.start();

        connected = true;
        connectionManager.callHook(this, HookType.CONNECTED);
    }

}
