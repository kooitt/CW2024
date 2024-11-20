package com.example.demo.factory;

import com.example.demo.actors.ActiveActorDestructible;

public interface ActorFactory {
    ActiveActorDestructible createActor(double x, double y);
}