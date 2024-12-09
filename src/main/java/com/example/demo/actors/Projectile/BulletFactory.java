package com.example.demo.actors.Projectile;

import com.example.demo.interfaces.ObjectFactory;

/**
 * A factory class for creating and resetting projectiles of different types.
 * Implements the {@link ObjectFactory} interface for {@link Projectile} objects.
 * Provides a standardized way to instantiate and reuse projectile objects
 * for various types such as "user", "enemy", "boss", and "bossTwo".
 */
public class BulletFactory implements ObjectFactory<Projectile> {

    /**
     * The type of projectile to be created. Supported types include:
     * <ul>
     *     <li>"user" - A {@link UserProjectile} created at (0, 0).</li>
     *     <li>"enemy" - An {@link EnemyProjectile} created at (0, 0).</li>
     *     <li>"boss" - A {@link BossProjectile} created at (0, y).</li>
     *     <li>"bossTwo" - A {@link BossTwoProjectile} created at (0, y).</li>
     * </ul>
     */
    private final String type;

    /**
     * Constructs a {@code BulletFactory} with the specified type.
     *
     * @param type the type of projectile this factory will produce.
     *             Must be one of "user", "enemy", "boss", or "bossTwo".
     */
    public BulletFactory(String type) {
        this.type = type;
    }

    /**
     * Creates a new instance of a projectile based on the factory's type.
     *
     * @return a new {@link Projectile} instance corresponding to the specified type.
     *         Returns {@code null} if the type is not recognized.
     */
    @Override
    public Projectile create() {
        return switch (type) {
            case "user" -> new UserProjectile(0, 0);
            case "enemy" -> new EnemyProjectile(0, 0);
            case "boss" -> new BossProjectile(0);
            case "bossTwo" -> new BossTwoProjectile(0);
            default -> null;
        };
    }

    /**
     * Resets the given projectile to its default state, making it reusable.
     * Delegates the reset operation to the {@link Projectile#reset()} method.
     *
     * @param projectile the {@link Projectile} instance to reset.
     *                   Must not be {@code null}.
     */
    @Override
    public void reset(Projectile projectile) {
        projectile.reset();
    }
}
