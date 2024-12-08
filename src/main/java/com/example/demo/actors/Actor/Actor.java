package com.example.demo.actors.Actor;

import com.example.demo.components.CollisionComponent;
import com.example.demo.components.HealthComponent;
import com.example.demo.components.MovementComponent;
import com.example.demo.interfaces.Destructible;
import com.example.demo.levels.LevelParent;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Actor extends Group implements Destructible {

    public static final String IMAGE_LOCATION = "/com/example/demo/images/";

    protected ImageView imageView;
    protected MovementComponent movementComponent;
    private CollisionComponent collisionComponent;
    private HealthComponent healthComponent;
    protected boolean isDestroyed;

    public Actor(String imageName, int imageHeight, double initialXPos, double initialYPos, int maxHealth) {
        imageView = new ImageView(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
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

    public void updatePosition() {
        movementComponent.update(this);
    }

    public abstract void updateActor(double deltaTime, LevelParent level);

    public MovementComponent getMovementComponent() {
        return movementComponent;
    }

    public void setMovementComponent(MovementComponent movementComponent) {
        this.movementComponent = movementComponent;
    }

    protected void setImageViewImage(String imageName) {
        imageView.setImage(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
    }

    @Override
    public void destroy() {
        isDestroyed = true;
        setVisible(false);
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setHealthComponent(HealthComponent healthComponent) {
        this.healthComponent = healthComponent;
    }

    public HealthComponent getHealthComponent() {
        return healthComponent;
    }

    @Override
    public void takeDamage(int damage) {
        if (healthComponent != null) {
            healthComponent.takeDamage(damage);
        }
    }

    public int getCurrentHealth() {
        return healthComponent != null ? healthComponent.getCurrentHealth() : 0;
    }

    public int getMaxHealth() {
        return healthComponent != null ? healthComponent.getMaxHealth() : 0;
    }

    public CollisionComponent getCollisionComponent() {
        return collisionComponent;
    }

    public void setCollisionComponent(CollisionComponent collisionComponent) {
        this.collisionComponent = collisionComponent;
    }

    public double getProjectileXPosition(double offset) {
        return getLayoutX() + getTranslateX() + offset;
    }

    public double getProjectileYPosition(double offset) {
        return getLayoutY() + getTranslateY() + offset;
    }
}