package io.github.purpleloop.gameengine.action.model.interfaces;

/**
 * A logical controller usable for the game engine.
 * 
 * The typical implementation example is the {@link KeyboardController}.
 */
public interface IController {

	/**
	 * Registers a controllable game element to be controlled by this controller.
	 * 
	 * @param controllableElement the controllable element
	 */
	void registerControlListener(IControllable controllableElement);

	/**
	 * Unregisters a controllable game element to be not anymore controlled by this
	 * controller.
	 * 
	 * @param controllableElement the controllable element
	 */
	void unRegisterControlListener(IControllable controllableElement);

}
