package com.example.demo.utils;

public interface ObjectFactory<T> {
    T create();
    void reset(T obj);
}
