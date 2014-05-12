package com.ikeirnez.communicationsframework.api;

/**
 * Small "hack" to get this working in versions lower than 1.8.
 * Inspired by Java 8s {@link java.util.function.Consumer}.
 *
 * @author iKeirNez
 */
public interface Callback<T> {

    /**
     * Accept an input value.
     *
     * @param t The input object.
     */
    public void call(T t);

}
