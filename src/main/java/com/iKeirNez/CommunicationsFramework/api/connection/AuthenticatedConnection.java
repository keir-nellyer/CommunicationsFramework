package com.iKeirNez.CommunicationsFramework.api.connection;

/**
 * Created by iKeirNez on 27/04/2014.
 */
public interface AuthenticatedConnection extends Connection {

    /**
     * Gets the key used to authenticate the connection
     * @return The key used to authenticate the connection
     */
    public char[] getKey();

    /**
     * Gets the authentication status
     * @return The authentication status
     */
    public boolean isAuthenticated();

}
