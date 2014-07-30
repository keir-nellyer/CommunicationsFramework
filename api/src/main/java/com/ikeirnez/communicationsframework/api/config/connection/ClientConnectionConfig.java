package com.ikeirnez.communicationsframework.api.config.connection;

import java.net.InetSocketAddress;

/**
 * Created by iKeirNez on 30/07/2014.
 */
public class ClientConnectionConfig extends ConnectionConfig {

    private InetSocketAddress serverAddress;

    public ClientConnectionConfig(InetSocketAddress serverAddress){
        this.serverAddress = serverAddress;
    }

    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(InetSocketAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    // todo builder
}
