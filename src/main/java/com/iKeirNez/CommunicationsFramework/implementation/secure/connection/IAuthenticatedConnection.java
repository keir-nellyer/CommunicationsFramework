package com.iKeirNez.CommunicationsFramework.implementation.secure.connection;

import com.iKeirNez.CommunicationsFramework.api.connection.AuthenticatedConnection;
import com.iKeirNez.CommunicationsFramework.implementation.IConnectionManager;
import com.iKeirNez.CommunicationsFramework.implementation.standard.connection.IConnection;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public class IAuthenticatedConnection extends IConnection implements AuthenticatedConnection {

    @Getter public final char[] key;
    public AtomicBoolean authenticated = new AtomicBoolean(false);

    protected IAuthenticatedConnection(IConnectionManager connectionManager, String hostName, int port, char[] key) {
        super(connectionManager, hostName, port);
        this.key = key;
    }

    public boolean isAuthenticated(){
        return authenticated.get();
    }

    @Override
    public boolean isConnected(){ // always return false if we're not authenticated
        return isAuthenticated() && super.isConnected();
    }

}
