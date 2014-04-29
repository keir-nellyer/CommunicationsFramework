package com.iKeirNez.CommunicationsFramework.examples.standard.client;

import com.iKeirNez.CommunicationsFramework.api.HookType;
import com.iKeirNez.CommunicationsFramework.api.connection.ClientConnection;
import com.iKeirNez.CommunicationsFramework.api.connection.ConnectionManager;
import com.iKeirNez.CommunicationsFramework.examples.PacketTest;

/**
 * A dummy client to demonstrate standard usage
 * Created by iKeirNez on 28/04/2014.
 */
public class StandardClientExampleMain {

    public static void main(String[] args){
        new StandardClientExampleMain("localhost", 25566); // connect to localhost:25566
    }

    public ConnectionManager connectionManager = ConnectionManager.getNewInstance(getClass().getClassLoader()); // create a ConnectionManager to manage our connections
    public ClientConnection connection;

    public StandardClientExampleMain(String host, int port){
        // its a good idea to register hooks and listeners before attempting the connection
        // this is due to read and writes being handled asynchronously and therefore we might
        // not register everything in time

        connectionManager.addHook(HookType.CONNECTED, c -> { // add hook for when we are connected
            System.out.println("Woo! looks like we're connected");
        });

        connectionManager.registerListener(new StandardClientExampleListener()); // register listener for reply

        connection = connectionManager.newClientConnection(host, port); // create the connection instance and populate it with our data
        connection.connect(); // attempt connecting asynchronously
        connection.sendPacket(new PacketTest("Hey, this message was sent from the client")); // send a packet to the server
    }

}
