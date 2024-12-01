package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class ObservableHelper implements Observable{

    private final List<Observer> observers = new ArrayList<>();

    // add an observer
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // remove an observer
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    // notify all observers
    public void notifyObservers(String levelName) {
        for (Observer observer : observers) {
            observer.update(levelName);
        }
    }
}
