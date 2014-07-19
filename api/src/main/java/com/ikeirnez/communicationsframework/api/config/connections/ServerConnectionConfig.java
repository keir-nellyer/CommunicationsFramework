package com.ikeirnez.communicationsframework.api.config.connections;

import com.ikeirnez.communicationsframework.api.config.filter.ConnectionFilter;

/**
 * Created by iKeirNez on 19/07/2014.
 */
public class ServerConnectionConfig extends ConnectionConfig {

    private ConnectionFilter connectionFilter;

    public ServerConnectionConfig(int port) {
        super(port);
    }

    public ConnectionFilter getConnectionFilter() {
        return connectionFilter;
    }

    public void setConnectionFilter(ConnectionFilter connectionFilter) {
        this.connectionFilter = connectionFilter;
    }

    public ServerConnectionConfig(Builder builder) {
        super(builder);
    }

    // todo some sort of builder
}
