package com.example.demo.interfaces;

/**
 * A generic interface for creating and resetting objects, typically used in object pooling mechanisms.
 * This interface defines two key methods:
 * - {@link #create()} for creating a new instance of the object.
 * - {@link #reset(Object)} for resetting the state of an existing object, making it reusable.
 *
 * @param <T> The type of the objects that this factory will create and reset.
 */
public interface ObjectFactory<T> {

    /**
     * Creates a new instance of the object.
     * This method is called when a new object is needed in the object pool.
     *
     * @return A new instance of type {@code T}.
     */
    T create();

    /**
     * Resets the state of an object, preparing it for reuse.
     * This method is typically called before returning the object to the pool
     * to ensure it is in a consistent and reusable state.
     *
     * @param obj The object to reset. The implementation should ensure all
     *            fields and states of the object are reverted to their default values.
     */
    void reset(T obj);
}
