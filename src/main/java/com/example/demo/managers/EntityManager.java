package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.DestructionType;
import com.example.demo.actors.planes.FighterPlane;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EntityManager {
    private final List<ActiveActorDestructible> friendlyUnits;
    private final List<ActiveActorDestructible> enemyUnits;
    private final List<ActiveActorDestructible> userProjectiles;
    private final List<ActiveActorDestructible> enemyProjectiles;
    private final Map<ActiveActorDestructible, Rectangle> actorHitboxes;
    private final Group root;
    private final List<Consumer<ActiveActorDestructible>> enemyDestroyedListeners;

    public EntityManager(Group root) {
        this.root = root;
        this.friendlyUnits = new ArrayList<>();
        this.enemyUnits = new ArrayList<>();
        this.userProjectiles = new ArrayList<>();
        this.enemyProjectiles = new ArrayList<>();
        this.actorHitboxes = new HashMap<>();
        this.enemyDestroyedListeners = new ArrayList<>();
    }

    public void addFriendlyUnit(ActiveActorDestructible unit) {
        friendlyUnits.add(unit);
    }

    public void addEnemyUnit(ActiveActorDestructible enemy) {
        enemyUnits.add(enemy);
        root.getChildren().add(enemy);
    }

    public void addEnemyDestroyedListener(Consumer<ActiveActorDestructible> listener) {
        enemyDestroyedListeners.add(listener);
    }

    public void addEnemyProjectile(ActiveActorDestructible projectile) {
        if (projectile != null) {
            enemyProjectiles.add(projectile);
            root.getChildren().add(projectile);
        }
    }

    public void updateActors() {
        updateActorList(friendlyUnits);
        updateActorList(enemyUnits);
        updateActorList(userProjectiles);
        updateActorList(enemyProjectiles);
    }

    private void updateActorList(List<ActiveActorDestructible> actors) {
        actors.forEach(actor -> {
            actor.updateActor();
            updateHitbox(actor);
        });
    }

    private void updateHitbox(ActiveActorDestructible actor) {
        Rectangle oldHitbox = actorHitboxes.get(actor);
        if (oldHitbox != null) {
            root.getChildren().remove(oldHitbox);
        }
        Rectangle hitbox = actor.getHitboxRectangle();
        actorHitboxes.put(actor, hitbox);
        root.getChildren().add(hitbox);
    }

    public void removeDestroyedActors() {
        removeDestroyedFromList(friendlyUnits);
        removeDestroyedFromList(enemyUnits);
        removeDestroyedFromList(userProjectiles);
        removeDestroyedFromList(enemyProjectiles);
    }

    private void removeDestroyedFromList(List<ActiveActorDestructible> actors) {
        List<ActiveActorDestructible> destroyedActors = actors.stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());

        destroyedActors.forEach(actor -> {
            root.getChildren().remove(actor);
            root.getChildren().remove(actorHitboxes.remove(actor));

            // Only notify if it's an enemy fighter plane destroyed by projectile
            if (enemyUnits.contains(actor) &&
                    actor instanceof FighterPlane &&
                    actor.getDestructionType() == DestructionType.PROJECTILE_KILL) {
                notifyEnemyDestroyed(actor);
            }
        });

        actors.removeAll(destroyedActors);
    }

    private void notifyEnemyDestroyed(ActiveActorDestructible enemy) {
        enemyDestroyedListeners.forEach(listener -> listener.accept(enemy));
    }

    public List<ActiveActorDestructible> getFriendlyUnits() {
        return friendlyUnits;
    }

    public List<ActiveActorDestructible> getEnemyUnits() {
        return enemyUnits;
    }

    public List<ActiveActorDestructible> getUserProjectiles() {
        return userProjectiles;
    }

    public List<ActiveActorDestructible> getEnemyProjectiles() {
        return enemyProjectiles;
    }

    public int getCurrentNumberOfEnemies() {
        return enemyUnits.size();
    }
}