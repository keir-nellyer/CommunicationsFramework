package com.ikeirnez.communicationsframework.api.config;

import org.apache.commons.lang3.Validate;

/**
 * Created by iKeirNez on 30/07/2014.
 */
public class ConnectionManagerConfig {

    private ClassLoader classLoader;

    /**
     * Creates a new instance.
     *
     * @param classLoader The ClassLoader used for decoding packets into objects.
     */
    public ConnectionManagerConfig(ClassLoader classLoader){
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static class Builder {
        private ClassLoader classLoader;

        public Builder withClassLoader(ClassLoader classLoader){
            this.classLoader = classLoader;
            return this;
        }

        public Builder withConnectionDefaults(){ // todo

            return this;
        }

        public ConnectionManagerConfig build(){
            Validate.notNull(classLoader, "ClassLoader cannot be null.7");
            ConnectionManagerConfig connectionManagerConfig = new ConnectionManagerConfig(classLoader);
            // todo defaults
            return connectionManagerConfig;
        }
    }
}
