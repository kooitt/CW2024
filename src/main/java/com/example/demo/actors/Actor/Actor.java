package com.example.demo.actors.Actor;

import com.example.demo.components.CollisionComponent;
import com.example.demo.components.HealthComponent;
import com.example.demo.components.MovementComponent;
import com.example.demo.interfaces.Destructible;
import com.example.demo.levels.LevelParent;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * The {@code Actor} class serves as the base class for all in-game actors. It provides core functionality for
 * rendering, movement, health management, and collision handling. Subclasses should define specific behavior
 * by implementing the abstract {@code updateActor} method.
 */
public abstract class Actor extends Group implements Destructible {

    /** Path to the directory containing actor images. */
    public static final String IMAGE_LOCATION = "/com/example/demo/images/";

    /** The visual representation of the actor as an {@code ImageView}. */
    public ImageView imageView;

    /** Component responsible for handling the movement of the actor. */
    protected MovementComponent movementComponent;

    /** Component responsible for handling collisions involving the actor. */
    private CollisionComponent collisionComponent;

    /** Component responsible for managing the health of the actor. */
    private HealthComponent healthComponent;

    /** Flag indicating whether the actor is destroyed. */
    protected boolean isDestroyed;

    /**
     * Constructs an {@code Actor} with specified initial properties.
     *
     * @param imageName   the name of the image file used to represent the actor
     * @param imageHeight the height of the image
     * @param initialXPos the initial X-coordinate of the actor
     * @param initialYPos the initial Y-coordinate of the actor
     * @param maxHealth   the maximum health of the actor
     */
    public Actor(String imageName, int imageHeight, double initialXPos, double initialYPos, int maxHealth) {
        imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_LOCATION + imageName)).toExternalForm()));
        imageView.setFitHeight(imageHeight);
        imageView.setPreserveRatio(true);
        setLayoutX(initialXPos);
        setLayoutY(initialYPos);
        getChildren().add(imageView);

        movementComponent = new MovementComponent(0, 0);
        healthComponent = new HealthComponent(this, maxHealth);
        setHealthComponent(healthComponent);

        double hitboxWidth = imageView.getFitWidth();
        double hitboxHeight = imageView.getFitHeight();
        collisionComponent = new CollisionComponent(this, hitboxWidth, hitboxHeight, 0, 0);
        setCollisionComponent(collisionComponent);

        isDestroyed = false;
    }

    /**
     * Updates the actor's position based on its movement component.
     */
    public void updatePosition() {
        movementComponent.update(this);
    }

    /**
     * Updates the actor's behavior and state. This method must be implemented by subclasses.
     *
     * @param deltaTime the time elapsed since the last update, in seconds
     * @param level     the current level in which the actor exists
     */
    public abstract void updateActor(double deltaTime, LevelParent level);

    /**
     * Retrieves the movement component of the actor.
     *
     * @return the movement component
     */
    public MovementComponent getMovementComponent() {
        return movementComponent;
    }

    /**
     * Sets the movement component for the actor.
     *
     * @param movementComponent the movement component to set
     */
    public void setMovementComponent(MovementComponent movementComponent) {
        this.movementComponent = movementComponent;
    }

    /**
     * Updates the image displayed by the actor.
     *
     * @param imageName the name of the new image file
     */
    protected void setImageViewImage(String imageName) {
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_LOCATION + imageName)).toExternalForm()));
    }

    /**
     * Destroys the actor, making it invisible and setting its destroyed flag.
     */
    @Override
    public void destroy() {
        isDestroyed = true;
        setVisible(false);
    }

    /**
     * Checks whether the actor is destroyed.
     *
     * @return {@code true} if the actor is destroyed, {@code false} otherwise
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Retrieves the {@code ImageView} used for rendering the actor.
     *
     * @return the image view
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Sets the health component for the actor.
     *
     * @param healthComponent the health component to set
     */
    public void setHealthComponent(HealthComponent healthComponent) {
        this.healthComponent = healthComponent;
    }

    /**
     * Retrieves the health component of the actor.
     *
     * @return the health component
     */
    public HealthComponent getHealthComponent() {
        return healthComponent;
    }

    /**
     * Reduces the actor's health by a specified amount of damage.
     *
     * @param damage the amount of damage to apply
     */
    @Override
    public void takeDamage(int damage) {
        if (healthComponent != null) {
            healthComponent.takeDamage(damage);
        }
    }

    /**
     * Retrieves the actor's current health.
     *
     * @return the current health, or 0 if no health component is set
     */
    public int getCurrentHealth() {
        return healthComponent != null ? healthComponent.getCurrentHealth() : 0;
    }

    /**
     * Retrieves the actor's maximum health.
     *
     * @return the maximum health, or 0 if no health component is set
     */
    public int getMaxHealth() {
        return healthComponent != null ? healthComponent.getMaxHealth() : 0;
    }

    /**
     * Retrieves the collision component of the actor.
     *
     * @return the collision component
     */
    public CollisionComponent getCollisionComponent() {
        return collisionComponent;
    }

    /**
     * Sets the collision component for the actor.
     *
     * @param collisionComponent the collision component to set
     */
    public void setCollisionComponent(CollisionComponent collisionComponent) {
        this.collisionComponent = collisionComponent;
    }

    /**
     * Computes the X-coordinate for a projectile fired from the actor.
     *
     * @param offset the offset from the actor's current X-coordinate
     * @return the X-coordinate of the projectile
     */
    public double getProjectileXPosition(double offset) {
        return getLayoutX() + getTranslateX() + offset;
    }

    /**
     * Computes the Y-coordinate for a projectile fired from the actor.
     *
     * @param offset the offset from the actor's current Y-coordinate
     * @return the Y-coordinate of the projectile
     */
    public double getProjectileYPosition(double offset) {
        return getLayoutY() + getTranslateY() + offset;
    }
}
