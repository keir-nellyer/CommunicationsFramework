package com.iKeirNez.CommunicationsFramework.api;

/**
 * Created by iKeirNez on 06/04/2014.
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
     * Only applicable for {@link com.iKeirNez.CommunicationsFramework.api.connection.AuthenticatedConnection}'s, called when authentication with the other side fails
     */
    AUTHENTICATION_FAILED

}
