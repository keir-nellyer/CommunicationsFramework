package com.ikeirnez.communicationsframework.api.config.filter;

import com.ikeirnez.communicationsframework.api.connection.ServerConnection;

import java.net.InetAddress;

/**
 * This is a very simple implementation of {@link ConnectionFilter}, it allows all connections
 */
public class AllowAllConnectionFilter implements ConnectionFilter {
    @Override
    public boolean shouldAccept(ServerConnection serverConnection, InetAddress incomingConnectionAddress) {
        return true;
    }
}
