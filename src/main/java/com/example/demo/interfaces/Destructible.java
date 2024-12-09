package com.example.demo.interfaces;

/**
 * Interface representing objects that can be destroyed and take damage.
 * This interface is designed for objects that need to respond to damage
 * and destruction logic, typically used in games or simulations.
 */
public interface Destructible {

	/**
	 * Applies damage to the object, reducing its health or durability.
	 * Implementations should define how the damage affects the object's state.
	 *
	 * @param damage the amount of damage to apply; must be a non-negative integer.
	 */
	void takeDamage(int damage);

	/**
	 * Destroys the object, marking it as no longer active or functional.
	 * Implementations should define what happens when the object is destroyed,
	 * such as removing it from the game world or triggering an explosion effect.
	 */
	void destroy();
}
