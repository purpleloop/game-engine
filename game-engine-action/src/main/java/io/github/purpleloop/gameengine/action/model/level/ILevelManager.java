package io.github.purpleloop.gameengine.action.model.level;

import io.github.purpleloop.gameengine.core.util.EngineException;

/** A level manager. */
public interface ILevelManager {

	/** No level. */
	String NO_LEVEL = "NO_LEVEL";
	
	/** @return the id of the first start level */
	String getStartLevelId();
	
	/**
	 * Provides a requested level.
	 * @param levelId the level id
	 * @return the level for the requested id.
	 * @throws EngineException in case of error on levels
     */	
	IGameLevel getLevel(String levelId) throws EngineException;
	
	/** @return the number of levels in this level manager */
	int getSize();

}
