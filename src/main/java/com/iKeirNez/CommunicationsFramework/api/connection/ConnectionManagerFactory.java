package com.iKeirNez.CommunicationsFramework.api.connection;

import com.iKeirNez.CommunicationsFramework.implementation.IConnectionManager;

/**
 * Static class used for getting a new {@link com.iKeirNez.CommunicationsFramework.api.connection.ConnectionManager}
 *
 * @author iKeirNez
 */
public class ConnectionManagerFactory {

    /**
     * Returns a new instance of this class
     *
     * @param classLoader The class loader (can be gotten with getClass#getClassLoader)
     * @return The new instance
     */
    public static ConnectionManager getNewConnectionManager(ClassLoader classLoader){
        return new IConnectionManager(classLoader); // this should be our only ever reference to the internals from the API
    }

}
