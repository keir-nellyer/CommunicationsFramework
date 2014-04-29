package com.iKeirNez.CommunicationsFramework.examples.authentication.server;

import com.iKeirNez.CommunicationsFramework.api.HookType;
import com.iKeirNez.CommunicationsFramework.api.connection.AuthenticatedServerConnection;
import com.iKeirNez.CommunicationsFramework.api.connection.ConnectionManager;
import com.iKeirNez.CommunicationsFramework.examples.authentication.Common;

/**
 * A dummy server to demonstrate authentication usage
 * Created by iKeirNez on 27/04/2014.
 */
public class AuthenticatedServerExampleMain {

    public static void main(String[] args){
        new AuthenticatedServerExampleMain("localhost", 25566, Common.KEY);  // Common.KEY would be replaced with your actual saved key in char[] form, probably loaded from file
    }

    public ConnectionManager connectionManager = ConnectionManager.getNewInstance(getClass().getClassLoader()); // create a ConnectionManager to manage our connections
    public AuthenticatedServerConnection connection;

    public AuthenticatedServerExampleMain(String host, int port, char[] key){
        // its a good idea to register hooks and listeners before attempting the connection
        // this is due to read and writes being handled asynchronously and therefore we might
        // not register everything in time

        connectionManager.addHook(HookType.CONNECTED, c -> { // add hook for when we are connected
            System.out.println("Woo! looks like we're authenticated & connected");
        });

        connectionManager.addHook(HookType.AUTHENTICATION_FAILED, c -> { // add hook for if authentication fails
            System.out.println("Hmm, something went wrong, probably mismatching keys");
        });

        connectionManager.registerListener(new AuthenticatedServerExampleListener()); // register listener for incoming test packet

        connection = connectionManager.newAuthenticatedServerConnection(host, port, key); // create the connection instance and populate it with our data
        connection.bind(); // begin listening for incoming connections
    }

}
