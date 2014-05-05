package com.ikeirnez.communicationsframework.api;

/**
 * Enum containing types of hooks you may listen for.
 *
 * @author iKeirNez
 */
public enum HookType {

  /**
   * Called when the connection is first made
   */
  CONNECTED,

  /**
   * Called when the connection to the other end is lost/disconnected
   */
  LOST_CONNECTION,

  /**
   * Called when we regain connectivity to the other side
   */
  RECONNECTED,

  /**
   * Only applicable for {@link com.ikeirnez.communicationsframework.api.connection.AuthenticatedConnection}'s, called when authentication with the other side fails
   */
  AUTHENTICATION_FAILED

}
