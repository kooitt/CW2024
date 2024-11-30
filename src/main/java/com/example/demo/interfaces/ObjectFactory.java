// ObjectFactory.java
package com.example.demo.interfaces;

/**
 * Generic factory interface for creating and resetting objects.
 *
 * @param <T> the type of objects.
 */
public interface ObjectFactory<T> {
    T create();
    void reset(T obj);
}
