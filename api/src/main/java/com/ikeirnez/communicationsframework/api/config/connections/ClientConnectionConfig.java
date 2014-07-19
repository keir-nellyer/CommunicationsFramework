package com.ikeirnez.communicationsframework.api.config.connections;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by iKeirNez on 19/07/2014.
 */
public class ClientConnectionConfig extends ConnectionConfig {

    private final InetSocketAddress hostAddress;

    public ClientConnectionConfig(String hostName, int hostPort){
        this(new InetSocketAddress(hostName, hostPort));
    }

    public ClientConnectionConfig(InetAddress hostName, int hostPort){
        this(new InetSocketAddress(hostName, hostPort));
    }

    public ClientConnectionConfig(InetSocketAddress hostAddress){
        super(hostAddress.getPort());
        this.hostAddress = hostAddress;
    }

    private ClientConnectionConfig(Builder builder){
        super(builder);
        this.hostAddress = builder.hostAddress;
    }

    public InetSocketAddress getHostAddress() {
        return hostAddress;
    }

    public static class Builder extends ConnectionConfig.Builder {

        private final InetSocketAddress hostAddress;

        private Builder(InetSocketAddress hostAddress){
            super(hostAddress.getPort());
            this.hostAddress = hostAddress;
        }

        @Override
        public ConnectionConfig build(){
            return new ClientConnectionConfig(this);
        }

    }

}
