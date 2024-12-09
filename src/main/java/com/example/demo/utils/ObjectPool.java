package com.example.demo.utils;

import com.example.demo.interfaces.ObjectFactory;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A generic object pool implementation that reuses objects to minimize resource allocation and enhance performance.
 *
 * @param <T> The type of objects managed by this pool.
 */
public class ObjectPool<T> {

    /**
     * A deque (double-ended queue) to store available objects for reuse.
     */
    private final Deque<T> available = new ArrayDeque<>();

    /**
     * The factory responsible for creating and resetting objects managed by the pool.
     */
    private final ObjectFactory<T> factory;

    /**
     * Constructs an {@code ObjectPool} with the specified factory.
     *
     * @param factory The factory used to create and reset objects for the pool.
     */
    public ObjectPool(ObjectFactory<T> factory) {
        this.factory = factory;
    }

    /**
     * Acquires an object from the pool. If the pool is empty, a new object is created using the factory.
     *
     * @return An object of type {@code T} from the pool, either reused or newly created.
     */
    public T acquire() {
        return available.isEmpty() ? factory.create() : available.pop();
    }

    /**
     * Releases an object back to the pool, resetting it to its initial state.
     * This ensures the object is ready for reuse in subsequent operations.
     *
     * @param obj The object to be returned to the pool.
     */
    public void release(T obj) {
        factory.reset(obj);
        available.push(obj);
    }
}
