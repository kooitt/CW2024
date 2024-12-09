package com.example.demo.utils;

import com.example.demo.interfaces.ObjectFactory;

import java.util.ArrayDeque;
import java.util.Deque;

public class ObjectPool<T> {
    private final Deque<T> available = new ArrayDeque<>();
    private final ObjectFactory<T> factory;

    public ObjectPool(ObjectFactory<T> factory) {
        this.factory = factory;
    }

    public T acquire() {
        return available.isEmpty() ? factory.create() : available.pop();
    }

    public void release(T obj) {
        factory.reset(obj);
        available.push(obj);
    }
}
