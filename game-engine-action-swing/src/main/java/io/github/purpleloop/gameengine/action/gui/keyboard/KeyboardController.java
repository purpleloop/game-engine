package io.github.purpleloop.gameengine.action.gui.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.util.MapList;
import io.github.purpleloop.commons.util.MapListImpl;
import io.github.purpleloop.gameengine.action.model.interfaces.IControllable;
import io.github.purpleloop.gameengine.action.model.interfaces.IController;
import io.github.purpleloop.gameengine.action.model.interfaces.IGameEngine;
import io.github.purpleloop.gameengine.core.config.KeyBoardActionMap;
import io.github.purpleloop.gameengine.core.util.EngineException;

/**
 * This class implements an keyboard controller.
 * 
 * It is used to manage keystrokes in game engine
 * <ul>
 * <li>as player input for the controllable agents of the action game</li>
 * <li>as common actions for the engine</li>
 * </ul>
 */
public class KeyboardController implements KeyListener, IController {

	/** Class logger. */
	public static final Log LOG = LogFactory.getLog(KeyboardController.class);

	/** FIXME : A dummy legacy constant - AFAIR was for single controller adaptation. */
	private static final String DUMMY_ALL_CONSTANT = "ALL";
	
	/** Game engine where to transmit the inputs. */
	private IGameEngine gameEngine;

	/** Set of controllable elements that are registered in this controller. */
	private MapList<String, IControllable> controlled;

	/** The action key registry for common actions of the game engine. */
	private KeyActionRegistry keyActionRegistry;

	/** The keyboard action map for the controlled elements of the game. */
	private KeyBoardActionMap keyMap;

	/**
	 * Keyboard controller constructor
	 * 
	 * @param gameEngine Game engine that will be connected for transmit key inputs
	 */
	public KeyboardController(IGameEngine gameEngine) {
		this.gameEngine = gameEngine;
		this.keyMap = gameEngine.getConfig().getKeyMap();
		this.controlled = new MapListImpl<>();
		this.keyActionRegistry = new KeyActionRegistry();
		registerKeyActions();
	}

	/** Registers the key bindings for common actions. */
	private void registerKeyActions() {

		LOG.debug("Registering common actions in the keyboard controller");

		// Common engine key bindings

		keyActionRegistry.register(KeyEvent.VK_ENTER, () -> {
			try {
				gameEngine.startGame();
			} catch (EngineException e) {
				LOG.error("Game could not be started.", e);
			}
		});

		keyActionRegistry.register(KeyEvent.VK_ESCAPE, () -> {
			try {
				gameEngine.stopGame();
			} catch (EngineException e) {
				LOG.error("Game could not be stopped.", e);
			}
		});

		keyActionRegistry.register(KeyEvent.VK_P, () -> gameEngine.pauseGame());
		keyActionRegistry.register(KeyEvent.VK_D, () -> gameEngine.dumpObjects());
		keyActionRegistry.register(KeyEvent.VK_S, () -> gameEngine.getSoundEngine().switchSounds());
		keyActionRegistry.register(KeyEvent.VK_I, () -> gameEngine.getView().switchDebugInfo()

		);
	}
	
	@Override
	public void keyPressed(KeyEvent keyEvent) {
		int keyCode = keyEvent.getKeyCode();

		KeyAction commonAction = this.keyActionRegistry.getKeyAction(keyCode);

		if (commonAction != null) {
			commonAction.onKeyAction();
		} else {
			String keyText = KeyEvent.getKeyText(keyCode);
			String action = keyMap.getActionForKey(keyText);
			if (action != null) {
				for (IControllable controllable : controlled.allValues()) {
					controllable.getActionStore().addAction(action);
				}

			} else {
				LOG.debug("Unmapped key pressed event : " + keyText);
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent evt) {
		// Nothing to be done here
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		int keyCode = evt.getKeyCode();

		if (!keyActionRegistry.isMappedKey(keyCode)) {

			String keyText = KeyEvent.getKeyText(keyCode);

			String action = keyMap.getActionForKey(keyText);
			if (action != null) {
				for (IControllable controllable : controlled.allValues()) {
					controllable.getActionStore().forgetAction(action);
				}

			} else {
				LOG.debug("Unmapped key released event : " + keyText);
			}
		}

	}

	@Override
	public void registerControlListener(IControllable controllable) {
		LOG.debug("Registering a controlable : " + controllable);
		controlled.putValue(DUMMY_ALL_CONSTANT, controllable);
	}

	@Override
	public void unRegisterControlListener(IControllable controllable) {
		LOG.debug("Unregistering controlable : " + controllable);
		controlled.removeAll(controllable);
	}

}
