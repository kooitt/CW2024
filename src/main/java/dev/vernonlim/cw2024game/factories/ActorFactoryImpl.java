package dev.vernonlim.cw2024game.factories;

import dev.vernonlim.cw2024game.assets.AssetLoader;
import dev.vernonlim.cw2024game.elements.ProjectileListener;
import dev.vernonlim.cw2024game.elements.actors.*;
import dev.vernonlim.cw2024game.elements.strategies.*;
import dev.vernonlim.cw2024game.factories.interfaces.ActorFactory;
import dev.vernonlim.cw2024game.factories.interfaces.ElementFactory;
import dev.vernonlim.cw2024game.factories.interfaces.ProjectileFactory;
import dev.vernonlim.cw2024game.managers.InputManager;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.layout.Pane;

public class ActorFactoryImpl extends FactoryParent implements ActorFactory {
    protected InputManager inputManager;
    protected ProjectileListener projectileListener;
    protected ProjectileFactory projectileFactory;
    protected ElementFactory elementFactory;

    public ActorFactoryImpl(Pane root, AssetLoader loader, InputManager inputManager, ProjectileFactory projectileFactory, ProjectileListener projectileListener, ElementFactory elementFactory) {
        super(root, loader);

        this.inputManager = inputManager;
        this.projectileFactory = projectileFactory;
        this.projectileListener = projectileListener;
        this.elementFactory = elementFactory;
    }

    @Override
    public UserPlane createUserPlane(UserPlaneCode userPlaneCode) {
        return switch (userPlaneCode) {
            case REGULAR_PLANE -> createRegularPlane();
            case GREEN_PLANE -> createGreenPlane();
        };
    }

    @Override
    public FighterPlane createEnemy(EnemyCode enemyCode, double initialXPos, double initialYPos) {
        return switch (enemyCode) {
            case ENEMY_PLANE -> createEnemyPlane(initialXPos, initialYPos);
            case BOSS -> createBoss();
            case ENEMY_BLUE -> createEnemyBlue(initialXPos, initialYPos);
            default -> createEnemyPlane(initialXPos, initialYPos);
        };
    }

    private UserPlane createRegularPlane() {
        int initialHealth = 5;

        ImageView imageView = makeView("userplane");
        imageView.setFitHeight(40);

        AudioClip fireSound = loader.loadSound("gunshot");
        AudioClip deathSound = loader.loadSound("pichuun");

        PlaneStrategy planeStrategy = new RegularPlaneStrategy(inputManager);

        return new RegularPlane(planeStrategy, projectileFactory, root, projectileListener, inputManager, imageView, fireSound, deathSound, initialHealth, 24.0f, 7.0f);
    }

    private UserPlane createGreenPlane() {
        int initialHealth = 3;

        ImageView imageView = makeView("userplane2");
        imageView.setFitHeight(40);

        AudioClip fireSound = loader.loadSound("laser");
        AudioClip deathSound = loader.loadSound("pichuun");

        PlaneStrategy planeStrategy = new GreenPlaneStrategy(inputManager);

        return new GreenPlane(planeStrategy, projectileFactory, root, projectileListener, inputManager, imageView, fireSound, deathSound, initialHealth, 30.0f, 7.0f);
    }

    private FighterPlane createEnemyPlane(double initialXPos, double initialYPos) {
        ImageView imageView = makeView("enemyplane");
        imageView.setFitHeight(54);

        AudioClip fireSound = loader.loadSound("missile");
        AudioClip deathSound = loader.loadSound("explosion");

        PlaneStrategy planeStrategy = new EnemyPlaneStrategy();

        EnemyPlane plane = new EnemyPlane(planeStrategy, projectileFactory, root, projectileListener, imageView, fireSound, deathSound);
        plane.setPosition(initialXPos, initialYPos);

        return plane;
    }

    private FighterPlane createEnemyBlue(double initialXPos, double initialYPos) {
        ImageView imageView = makeView("enemyblue");
        imageView.setFitHeight(54);

        AudioClip fireSound = loader.loadSound("laser");
        AudioClip deathSound = loader.loadSound("explosion");

        PlaneStrategy planeStrategy = new EnemyPlaneStrategy();

        EnemyPlane plane = new EnemyPlane(planeStrategy, projectileFactory, root, projectileListener, imageView, fireSound, deathSound);
        plane.setPosition(initialXPos, initialYPos);

        return plane;
    }

    private FighterPlane createBoss() {
        ImageView imageView = makeView("bossplane");
        imageView.setFitHeight(56);

        AudioClip fireSound = loader.loadSound("fireball");
        AudioClip deathSound = loader.loadSound("explosion");
        AudioClip shieldSound = loader.loadSound("metalhit");
        AudioClip damageSound = loader.loadSound("click");
        damageSound.setVolume(1.0);

        BossStrategy bossStrategy = new BossStrategyImpl();

        return new Boss(bossStrategy, elementFactory, projectileFactory, root, projectileListener, imageView, fireSound, deathSound, shieldSound, damageSound);
    }
}
