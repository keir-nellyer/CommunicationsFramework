package com.ikeirnez.communicationsframework.api.config;

import com.ikeirnez.communicationsframework.api.config.connections.DefaultConnectionConfig;

/**
 * Created by iKeirNez on 15/07/2014.
 */
public class ConnectionManagerConfig {

    private DefaultConnectionConfig defaultConnectionConfig;

    public ConnectionManagerConfig(){}

    protected ConnectionManagerConfig(Builder builder){
        setDefaultConnectionConfig(defaultConnectionConfig);
    }

    public DefaultConnectionConfig getDefaultConnectionConfig() {
        return defaultConnectionConfig;
    }

    public void setDefaultConnectionConfig(DefaultConnectionConfig defaultConnectionConfig) {
        this.defaultConnectionConfig = defaultConnectionConfig;
    }

    public static class Builder {
        private DefaultConnectionConfig defaultConnectionConfig;

        protected Builder(){}

        public Builder withDefaultConnectionConfig(DefaultConnectionConfig defaultConnectionConfig){
            this.defaultConnectionConfig = defaultConnectionConfig;
            return this;
        }

        public ConnectionManagerConfig build(){
            return new ConnectionManagerConfig(this);
        }
    }
}
