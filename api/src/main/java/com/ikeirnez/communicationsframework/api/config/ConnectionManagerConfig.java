package com.ikeirnez.communicationsframework.api.config;

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
            if (classLoader == null){
                throw new RuntimeException("ClassLoader cannot be null");
            }

            ConnectionManagerConfig connectionManagerConfig = new ConnectionManagerConfig(classLoader);
            // todo defaults
            return connectionManagerConfig;
        }
    }
}
