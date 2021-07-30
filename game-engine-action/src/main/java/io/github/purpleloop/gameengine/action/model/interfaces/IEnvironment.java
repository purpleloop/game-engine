package io.github.purpleloop.gameengine.action.model.interfaces;

import java.util.List;

/** Models a game environment. */
public interface IEnvironment extends ISessionEnvironment {

	/** @return lists all objects of the environment (which may be consequent). */
	List<IEnvironmentObjet> getObjects();

}
