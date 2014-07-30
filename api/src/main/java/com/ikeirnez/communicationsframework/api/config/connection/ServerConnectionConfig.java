package com.ikeirnez.communicationsframework.api.config.connection;

import com.ikeirnez.communicationsframework.api.filter.ConnectionFilter;

/**
 * Created by iKeirNez on 30/07/2014.
 */
public class ServerConnectionConfig extends ConnectionConfig {

    private final int port;
    private ConnectionFilter connectionFilter;

    public ServerConnectionConfig(int port){
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public ConnectionFilter getConnectionFilter() {
        return connectionFilter;
    }

    public void setConnectionFilter(ConnectionFilter connectionFilter) {
        this.connectionFilter = connectionFilter;
    }

    // todo builder
}
