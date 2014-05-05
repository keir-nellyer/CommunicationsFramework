package com.ikeirnez.communicationsframework.api.connection;

import com.ikeirnez.communicationsframework.implementation.IConnectionManager;

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
  @SuppressWarnings("deprecation")
  public static ConnectionManager getNewConnectionManager(ClassLoader classLoader) {
    return new IConnectionManager(classLoader); // this should be our only ever reference to the internals from the API
  }

}
