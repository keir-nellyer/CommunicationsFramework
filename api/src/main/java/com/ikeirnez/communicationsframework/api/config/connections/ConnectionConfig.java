package com.ikeirnez.communicationsframework.api.config.connections;

import com.ikeirnez.communicationsframework.api.config.authentication.AuthenticationConfig;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by iKeirNez on 17/07/2014.
 */
public class ConnectionConfig extends DefaultConnectionConfig {

    private final int port;

    public ConnectionConfig(int port){
        this.port = port;
    }

    public ConnectionConfig(Builder builder){
        super(builder);
        this.port = builder.port;
    }

    public static class Builder extends DefaultConnectionConfig.Builder {

        protected final int port;

        public Builder(int port){
            this.port = port;
        }

        @Override
        public ConnectionConfig build(){
            return new ConnectionConfig(this);
        }

    }
}
