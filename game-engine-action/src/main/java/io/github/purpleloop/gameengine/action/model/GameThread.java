package io.github.purpleloop.gameengine.action.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.lang.ThreadObserver;
import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.core.util.EngineException;

/**
 * This class manage the execution of a game on a thread. The game session is
 * regularly updated.
 */
public class GameThread extends Thread {

	/** Class logger. */
	private static Log log = LogFactory.getLog(GameThread.class);

	/** Delay between two activations of the session, in ms. */
	private int activationDelay;

	/** Observer of the game thread. */
	private ThreadObserver threadObserver;

	/** Is the game thread terminated ? */
	private boolean terminated;

	/** The game session. */
	private ISession session;

	/** Is the game paused ? */
	private boolean paused;

	/**
	 * Creates a game thread.
	 * 
	 * @param gameSession     The game session
	 * @param activationDelay delay between two session activations
	 */
	public GameThread(ISession gameSession, int activationDelay) {
		super("Creating the game thread");
		this.terminated = false;
		this.paused = false;
		this.activationDelay = activationDelay;
		this.session = gameSession;
	}

	/**
	 * @param obs thread observer
	 */
	public void setThreadObserver(ThreadObserver threadObserver) {
		this.threadObserver = threadObserver;
	}

	/** Terminates the game. */
	public synchronized void terminate() {
		terminated = true;
	}

	/**
	 * Switch the pause mode.
	 * 
	 * @return true if the game is paused, false otherwise
	 */
	public synchronized boolean pause() {
		paused = !paused;
		return paused;
	}

	@Override
	public void run() {

		try {
			while ((!terminated) && (!session.isEnded())) {

				if (!paused) {
					session.update();
				}

				try {
					sleep(activationDelay);
				} catch (InterruptedException e) {
					throw new EngineException("Thread game has been interrupted " + e);
				}
			}

		} catch (EngineException e) {
			log.error("An exception occured on the game engine thread :", e);
		} finally {

			log.info("The game thread is ending ...");
			if (threadObserver != null) {
				threadObserver.threadDeath(this);
			}
		}
	}

}
