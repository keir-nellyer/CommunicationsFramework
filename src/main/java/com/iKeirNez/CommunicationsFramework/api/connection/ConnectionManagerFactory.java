package com.iKeirNez.CommunicationsFramework.api.connection;

import com.iKeirNez.CommunicationsFramework.implementation.IConnectionManager;

/**
 * This class had to be created as Java 7 and below don't allow static methods in interfaces
 * Created by iKeirNez on 01/05/2014.
 */
public class ConnectionManagerFactory {

    /**
     * Returns a new instance of this class
     * @param classLoader The class loader (can be gotten with getClass#getClassLoader)
     * @return The new instance
     */
    public static ConnectionManager getNewConnectionManager(ClassLoader classLoader){
        return new IConnectionManager(classLoader); // this should be our only ever reference to the internals from the API
    }

}
