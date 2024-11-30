package com.example.demo.utils;

import com.example.demo.interfaces.ObjectFactory;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Generic object pool for reusing objects.
 *
 * @param <T> type of objects in the pool.
 */
public class ObjectPool<T> {
    private Deque<T> available = new ArrayDeque<>();
    private ObjectFactory<T> factory;

    /**
     * Constructs an ObjectPool with the specified factory.
     *
     * @param factory the factory to create and reset objects.
     */
    public ObjectPool(ObjectFactory<T> factory) {
        this.factory = factory;
    }

    /**
     * Acquires an object from the pool or creates a new one.
     *
     * @return an object of type T.
     */
    public T acquire() {
        return available.isEmpty() ? factory.create() : available.pop();
    }

    /**
     * Releases an object back to the pool.
     *
     * @param obj the object to release.
     */
    public void release(T obj) {
        factory.reset(obj);
        available.push(obj);
    }
}
