package com.example.demo.factory;

import com.example.demo.actors.ActiveActorDestructible;

public abstract class AbstractActorFactory implements ActorFactory {
    protected final int defaultImageHeight;

    protected AbstractActorFactory(int defaultImageHeight) {
        this.defaultImageHeight = defaultImageHeight;
    }

    @Override
    public abstract ActiveActorDestructible createActor(double x, double y);

}