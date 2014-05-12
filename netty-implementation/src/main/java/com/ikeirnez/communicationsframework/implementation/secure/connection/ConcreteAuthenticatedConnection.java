package com.ikeirnez.communicationsframework.implementation.secure.connection;

import com.ikeirnez.communicationsframework.api.connection.AuthenticatedConnection;
import com.ikeirnez.communicationsframework.implementation.ConcreteConnectionManager;
import com.ikeirnez.communicationsframework.implementation.standard.connection.ConcreteConnection;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class ConcreteAuthenticatedConnection extends ConcreteConnection implements AuthenticatedConnection {

    public final char[] key;
    public AtomicBoolean authenticated = new AtomicBoolean(false);

    protected ConcreteAuthenticatedConnection(ConcreteConnectionManager connectionManager, String hostName, int port, char[] key) {
        super(connectionManager, hostName, port);
        this.key = key;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated.get();
    }

    @Override
    public boolean isConnected() { // always return false if we're not authenticated
        return isAuthenticated() && super.isConnected();
    }

    @Override
    public char[] getKey() {
        return key;
    }
}
