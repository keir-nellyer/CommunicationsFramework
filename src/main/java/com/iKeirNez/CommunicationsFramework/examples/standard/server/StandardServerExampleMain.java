package com.iKeirNez.CommunicationsFramework.examples.standard.server;

import com.iKeirNez.CommunicationsFramework.api.HookType;
import com.iKeirNez.CommunicationsFramework.api.connection.ConnectionManager;
import com.iKeirNez.CommunicationsFramework.api.connection.ServerConnection;

/**
 * A dummy server to demonstrate standard usage
 * Created by iKeirNez on 2804/2014.
 */
public class StandardServerExampleMain {

    public static void main(String[] args){
        new StandardServerExampleMain("localhost", 25566); // listen for connection from localhost:25566
    }

    public ConnectionManager connectionManager = ConnectionManager.getNewInstance(getClass().getClassLoader()); // create a ConnectionManager to manage our connections
    public ServerConnection connection;

    public StandardServerExampleMain(String host, int port){
        // its a good idea to register hooks and listeners before attempting the connection
        // this is due to read and writes being handled asynchronously and therefore we might
        // not register everything in time

        connectionManager.addHook(HookType.CONNECTED, c -> { // add hook for when we are connected
            System.out.println("Woo! looks like we're connected");
        });

        connectionManager.registerListener(new StandardServerExampleListener()); // register listener for incoming test packet

        connection = connectionManager.newServerConnection(host, port); // create the connection instance and populate it with our data
        connection.bind(); // begin listening for incoming connections
    }

}
