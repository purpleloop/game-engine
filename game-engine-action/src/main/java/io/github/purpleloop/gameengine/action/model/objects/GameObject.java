package io.github.purpleloop.gameengine.action.model.objects;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.github.purpleloop.commons.direction.Direction;
import io.github.purpleloop.gameengine.action.model.interfaces.IEnvironmentObjet;

/**
 * A game object is the base class for everything placed in a game environment.
 * An object has a unique id and a name. It has an orientation and a location.
 * It can have various properties addressed by name.
 */
public class GameObject implements IEnvironmentObjet {

	/** A default appearance. */
	private static final IAppearance DEFAULT_APPAREANCE = new IAppearance() {

		/** Default appearance name. */
		private static final String DEFAULT_NAME = "default-appearance";

		@Override
		public String getName() {
			return DEFAULT_NAME;
		}
		
	};

	/** Next unique identifier for game objects. */
	private static int nextId;

	/** Object name. */
	private String name;

	/** Unique identifier. */
	private int id;

	/** Vertical location. */
	protected int xLoc;

	/** Horizontal location. */
	protected int yLoc;

	/** Direction (heading). */
	protected Direction orientation = Direction.NONE;

	/** A reusable random generator for agents. */
	protected Random random = new Random();

	/** Extra properties. */
	private Map<String, Object> properties;

	/** Constructor for game objects. */
	public GameObject() {

		name = "dummy";
		id = nextId++;
		properties = new HashMap<>();
	}

	@Override
	public String toString() {

		return new StringBuilder().append("object name=").append(name).append(" location=").append(descibeLocation())
				.append(", ori=").append(orientation).toString();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean hasName(String name) {
		return this.getName().equals(name);
	}

	@Override
	public Direction getOrientation() {
		return orientation;
	}

	/** @param orientation the orientation to set */
	public void setOrientation(Direction orientation) {
		this.orientation = orientation;
	}

	@Override
	public int getXLoc() {
		return xLoc;
	}

	@Override
	public int getYLoc() {
		return yLoc;
	}

	/**
	 * @param x horizontal location
	 * @param y vertical location
	 */
	public void setLoc(int x, int y) {
		xLoc = x;
		yLoc = y;
	}

	@Override
	public void evolve() {
		// A default object does not evolve in any way.
	}

	@Override
	public int getAnimationSequence() {
		return 0;
	}

	@Override
	public Rectangle getCollisionRectangle() {
		return null;
	}

	@Override
	public final int getId() {
		return id;
	}

	@Override
	public boolean collides(IEnvironmentObjet other) {
		return false;
	}

	@Override
	public final void setProperty(String key, Object value) {
		properties.put(key, value);
	}

	@Override
	public final Object getProperty(String key) {
		return properties.get(key);
	}

	@Override
	public boolean getBooleanProperty(String key) {

		Object property = getProperty(key);

		if (property == null) {
			return false;
		}

		return ((Boolean) property).booleanValue();
	}

	public String descibeLocation() {
		return "(" + xLoc + "," + yLoc + ")";
	}

	@Override
	public IAppearance getAppearance() {
		return DEFAULT_APPAREANCE;
	}

}
