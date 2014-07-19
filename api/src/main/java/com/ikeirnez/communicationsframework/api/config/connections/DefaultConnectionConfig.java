package com.ikeirnez.communicationsframework.api.config.connections;

import com.ikeirnez.communicationsframework.api.config.authentication.AuthenticationConfig;

import java.util.concurrent.ThreadFactory;

/**
 * A config that can be used as a template for creating connections.
 */
public class DefaultConnectionConfig {

    private AuthenticationConfig authenticationConfig;
    private ThreadFactory threadFactory;

    public DefaultConnectionConfig(){}

    protected DefaultConnectionConfig(Builder builder){
        setAuthenticationConfig(builder.authenticationConfig);
        setThreadFactory(builder.threadFactory);
    }

    public AuthenticationConfig getAuthenticationConfig() {
        return authenticationConfig;
    }

    public void setAuthenticationConfig(AuthenticationConfig authenticationConfig) {
        this.authenticationConfig = authenticationConfig;
    }

    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    public static class Builder {
        protected ThreadFactory threadFactory;
        protected AuthenticationConfig authenticationConfig;

        public Builder(){}

        public Builder withThreadFactory(ThreadFactory threadFactory){
            this.threadFactory = threadFactory;
            return this;
        }

        public Builder withAuthentication(AuthenticationConfig authenticationConfig){
            this.authenticationConfig = authenticationConfig;
            return this;
        }

        public DefaultConnectionConfig build(){
            return new DefaultConnectionConfig(this);
        }
    }

}
