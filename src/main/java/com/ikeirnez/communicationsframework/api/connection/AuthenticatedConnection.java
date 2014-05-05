package com.ikeirnez.communicationsframework.api.connection;

/**
 * Represents a connection which is authenticated.
 *
 * @author iKeirNez
 */
public interface AuthenticatedConnection extends Connection {

  /**
   * Gets the key used to authenticate the connection
   *
   * @return The key used to authenticate the connection
   */
  public char[] getKey( );

  /**
   * Gets the authentication status
   *
   * @return The authentication status
   */
  public boolean isAuthenticated( );

}
