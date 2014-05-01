package com.iKeirNez.CommunicationsFramework.api;

/**
 * Pretty much the same as a Java 8 Consumer (except we can't use them in Java 6)
 * Created by iKeirNez on 01/05/2014.
 */
public interface Callback<T> {

    public void call(T t);

}
