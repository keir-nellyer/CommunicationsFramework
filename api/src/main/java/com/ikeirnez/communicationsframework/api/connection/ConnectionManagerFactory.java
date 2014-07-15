package com.ikeirnez.communicationsframework.api.connection;

import java.lang.reflect.InvocationTargetException;

/**
 * Static class used for getting a new {@link com.ikeirnez.communicationsframework.api.connection.ConnectionManager}
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
    public static ConnectionManager getNewNettyConnectionManager(ClassLoader classLoader) {
        try {
            return (ConnectionManager) Class.forName("com.ikeirnez.communicationsframework.implementation.ConcreteConnectionManager").getConstructor(ClassLoader.class).newInstance(classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ConcreteConnectionManager not found, make sure you've included the implementation in your class path", e.getCause());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

}
