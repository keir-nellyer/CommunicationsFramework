package com.iKeirNez.packetapi.connections;

import com.iKeirNez.packetapi.HookType;
import com.iKeirNez.packetapi.threads.IncomingThread;
import com.iKeirNez.packetapi.threads.OutgoingThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * Created by iKeirNez on 04/04/2014.
 */
public class ServerConnection extends Connection {

    private ServerSocket serverSocket;
    private InetSocketAddress inetAddress;

    public long lastHeartbeat = System.currentTimeMillis();
    public boolean offline = false;

    protected ServerConnection(ConnectionManager connectionManager, String clientAddress, int port) throws IOException {
        super(connectionManager, clientAddress, port);
        this.inetAddress = new InetSocketAddress(clientAddress, port);

        serverSocket = new ServerSocket(port);

        new Thread(() -> {
            while (socket == null && true){ // todo
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            incomingThread = new IncomingThread("[Server] Incoming: " + clientAddress + ":" + port, connectionManager, this);
            outgoingThread = new OutgoingThread("[Server] Outgoing: " + clientAddress + ":" + port, this);

            incomingThread.start();
            outgoingThread.start();

            connected = true;
            connectionManager.callHook(this, HookType.CONNECTED);
        }).start();
    }

    @Override
    public void close(){
        super.close();

        try {
            serverSocket.close();
        } catch (IOException e) {}
    }

}
