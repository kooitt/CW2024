package com.example.demo.utils;

import com.example.demo.interfaces.ObjectFactory;

import java.util.Stack;

public class ObjectPool<T> {
    private Stack<T> availableObjects = new Stack<>();
    private ObjectFactory<T> factory;

    public ObjectPool(ObjectFactory<T> factory) {
        this.factory = factory;
    }

    public T acquire() {
        if (availableObjects.isEmpty()) {
            return factory.create();
        } else {
            return availableObjects.pop();
        }
    }

    public void release(T obj) {
        factory.reset(obj);
        availableObjects.push(obj);
    }
}
