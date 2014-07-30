package com.ikeirnez.communicationsframework.api.config.connection;

import com.ikeirnez.communicationsframework.api.authentication.ConnectionAuthentication;

import java.util.concurrent.ThreadFactory;

/**
 * Configuration options which can be used to create new connections.
 */
public class ConnectionConfig {

    private ConnectionAuthentication connectionAuthentication;
    private ThreadFactory threadFactory;

    public ConnectionConfig(){}

    public ConnectionAuthentication getAuthentication(){
        return connectionAuthentication;
    }

    public void setAuthentication(ConnectionAuthentication connectionAuthentication){
        this.connectionAuthentication = connectionAuthentication;
    }

    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    // todo builder
}
