package io.github.purpleloop.gameengine.action.model.interfaces;

import java.util.Optional;

import io.github.purpleloop.gameengine.action.model.level.ILevelManager;
import io.github.purpleloop.gameengine.core.config.GameConfig;
import io.github.purpleloop.gameengine.core.interfaces.IRootGameEngine;
import io.github.purpleloop.gameengine.core.sound.interfaces.MutableSoundEngine;
import io.github.purpleloop.gameengine.core.util.EngineException;

/**
 * Describes a software engine where the game executes. This central interface
 * rules the exchanges of the game model with the external elements like the
 * user interface, the controller, the virtual machine and so on.
 */
public interface IGameEngine extends IRootGameEngine  {

	/**
	 * @return the game configuration
	 */
	GameConfig getConfig();

	/**
	 * Starts a new game session.
	 * 
	 * @throws EngineException in case of problems
	 */
	void startGame() throws EngineException;

	/**
	 * Ends a game session if it exists.
	 * 
	 * @throws EngineException in case of problems
	 */
	void stopGame() throws EngineException;

	/**
	 * @return true if a game is running in the engine, false elsewhere
	 */
	boolean hasRunningGame();

	/**
	 * Pauses the game.
	 * 
	 * @return true if the game is paused, false elsewhere
	 */
	boolean pauseGame();

	/**
	 * Plays a sound.
	 * 
	 * @param soundName the name of the sound to play as registered in the sound
	 *                  engine
	 */
	void playSound(String soundName);

	/**
	 * @return the sound engine
	 */
	MutableSoundEngine getSoundEngine();

	/** Dump all objects of the current environment in the logs. */
	void dumpObjects();

	/** @return the controller for the current player */
	IController getController();

	/** @return the current game session, if it exists */
	Optional<ISession> getSession();

	/** @return the view associated with the game engine. */
	IGameView getView();

	/** @return the level manager */
	ILevelManager getLevelManager();

	/** @return the optional dialogEngine */
    Optional<IDialogEngine> getDialogEngine();
}
