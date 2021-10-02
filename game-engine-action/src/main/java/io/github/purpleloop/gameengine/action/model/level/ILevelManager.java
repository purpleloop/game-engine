package io.github.purpleloop.gameengine.action.model.level;

/** A level manager. */
public interface ILevelManager {

	/** No level. */
	String NO_LEVEL = "NO_LEVEL";
	
	/**
	 * @return get the level for the requested index.
	 * @param currentLevelIndex the level index
	 */
	IGameLevel getLevel(String currentLevelIndex);
	
	/**
	 * @return get the next level for the requested index.
	 * @param currentLevelIndex the level index
	 */	
	IGameLevel getNextLevel(String currentLevelIndex);
	
	/** @return the number of levels in this level manager */
	int getSize();

}
