package io.github.purpleloop.gameengine.action.model.environment;

/** Content of an environment cell (to be used for an AbstractCellObjectEnvironment). */
public interface ICellContents {

	/** @return The character used to represent the contents in XML level files. */
	char getLevelChar();

}
