package io.github.purpleloop.gameengine.action.model.level;

/** A level manager. */
public interface ILevelManager {

	/**
	 * @return get the level for the requested index.
	 * @param currentLevelIndex the level index
	 */
	IGameLevel getLevel(int currentLevelIndex);

	/** @return the number of levels in this level manager */
	int getSize();

}
