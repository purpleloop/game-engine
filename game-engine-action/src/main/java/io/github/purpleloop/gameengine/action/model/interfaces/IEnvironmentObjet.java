package io.github.purpleloop.gameengine.action.model.interfaces;

import java.awt.Rectangle;

import io.github.purpleloop.commons.direction.Direction;
import io.github.purpleloop.gameengine.action.model.objects.IAppearance;

/** Represents a localized environment object. */
public interface IEnvironmentObjet {

	/** @return unique name of the object */
	String getName();

	/** @param name Unique name of the object */
	void setName(String name);

	/**
	 * @param name name to test
	 * @return true if the object has the given name
	 */
	boolean hasName(String name);

	/** @return Orientation of the object */
	Direction getOrientation();

	/** @return Abscissa of the object */
	int getXLoc();

	/** @return Ordinate of the object */
	int getYLoc();

	/** Make the object evolve. */
	void evolve();

	/**
	 * @return returns the animation sequence number of the object.
	 * 
	 *         FIXME Animation sequence number : To improve as this should not be in
	 *         the model.
	 */
	int getAnimationSequence();

	/**
	 * @return Returns the collision rectangle : the portion of space occupied by
	 *         the agent.
	 */
	Rectangle getCollisionRectangle();

	/** @return the object identifier. */
	int getId();

	/**
	 * Indicates if this object collides with another one.
	 * 
	 * @param other other object
	 * @return true if this object collides with the one given as parameter
	 */
	boolean collides(IEnvironmentObjet other);

	/**
	 * Sets a property on the agent.
	 * 
	 * @param key   the key of the property
	 * @param value value to associate to the property
	 */
	void setProperty(String key, Object value);

	/**
	 * Gets the value of the property for the given key.
	 * 
	 * @param key the key of the property
	 * @return value to associate to the property, or null if it does not exist
	 */
	Object getProperty(String key);

	/**
	 * Returns a boolean property.
	 * 
	 * @param key the key of the property
	 * @return property value (defaults false if missing).
	 */
	boolean getBooleanProperty(String key);

	/** @return the object appearance */
	IAppearance getAppearance();

}
