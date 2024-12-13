package com.example.demo.actors.core;

import javafx.scene.Group;
import java.util.List;
import java.util.stream.Collectors;

public class ActorManager {

    private final Group root;

    public ActorManager(Group root) {
        this.root = root;
    }

    public void removeDestroyedActors(List<ActiveActorDestructible> actors) {
        List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
                .collect(Collectors.toList());
        root.getChildren().removeAll(destroyedActors);
        actors.removeAll(destroyedActors);
    }
}
