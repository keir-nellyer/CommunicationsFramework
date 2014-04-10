package com.iKeirNez.networking.connections;

import com.iKeirNez.networking.HookType;
import com.iKeirNez.networking.threads.IncomingThread;
import com.iKeirNez.networking.threads.OutgoingThread;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class ClientConnection extends Connection {

    protected ClientConnection(ConnectionManager connectionManager, String serverAddress, int port) throws IOException {
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
