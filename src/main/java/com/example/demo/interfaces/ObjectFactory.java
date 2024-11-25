package com.example.demo.interfaces;

public interface ObjectFactory<T> {
    T create();
    void reset(T obj);
}
