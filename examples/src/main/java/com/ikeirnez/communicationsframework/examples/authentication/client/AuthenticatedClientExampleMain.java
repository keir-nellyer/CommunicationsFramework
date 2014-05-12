package com.ikeirnez.communicationsframework.examples.authentication.client;

import com.ikeirnez.communicationsframework.api.Callback;
import com.ikeirnez.communicationsframework.api.HookType;
import com.ikeirnez.communicationsframework.api.connection.AuthenticatedClientConnection;
import com.ikeirnez.communicationsframework.api.connection.Connection;
import com.ikeirnez.communicationsframework.api.connection.ConnectionManager;
import com.ikeirnez.communicationsframework.api.connection.ConnectionManagerFactory;
import com.ikeirnez.communicationsframework.examples.PacketTest;
import com.ikeirnez.communicationsframework.examples.authentication.Common;

/**
 * A dummy client to demonstrate authentication usage
 * Created by iKeirNez on 27/04/2014.
 */
public class AuthenticatedClientExampleMain {

    public ConnectionManager connectionManager = ConnectionManagerFactory.getNewConnectionManager(getClass().getClassLoader()); // create a ConnectionManager to manage our connections
    public AuthenticatedClientConnection connection;
    public AuthenticatedClientExampleMain(String host, int port, char[] key) {
        // its a good idea to register hooks and listeners before attempting the connection
        // this is due to read and writes being handled asynchronously and therefore we might
        // not register everything in time

        connectionManager.addHook(HookType.CONNECTED, new Callback<Connection>() { // add hook for when we are connected
            @Override
            public void call(Connection connection) {
                System.out.println("Woo! looks like we're authenticated & connected");
            }
        });

        connectionManager.addHook(HookType.AUTHENTICATION_FAILED, new Callback<Connection>() { // add hook for if authentication fails
            @Override
            public void call(Connection connection) {
                System.out.println("Hmm, something went wrong, probably mismatching keys");
            }
        });

        connectionManager.registerListener(new AuthenticationClientExampleListener()); // register listener for reply

        connection = connectionManager.newAuthenticatedClientConnection(host, port, key); // create the connection instance and populate it with our data
        connection.connect(); // attempt connecting asynchronously
        connection.sendPacket(new PacketTest("Hey, this message was sent from the client")); // send a packet to the server
    }

    public static void main(String[] args) {
        new AuthenticatedClientExampleMain("localhost", 25566, Common.KEY); // Common.KEY would be replaced with your actual saved key in char[] form, probably loaded from file, try changing this to something incorrect and see it doesn't authenticate
    }

}
