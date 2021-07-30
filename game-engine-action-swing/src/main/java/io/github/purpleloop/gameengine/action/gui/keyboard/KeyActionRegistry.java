package io.github.purpleloop.gameengine.action.gui.keyboard;

import java.util.HashMap;
import java.util.Map;

/** The action key registry.
 * 
 * This registry maintains bindings of actions for keystrokes.
 */
public class KeyActionRegistry {

    /** Common bindings between keyCodes and actionKey handlers. */
    private Map<Integer, KeyAction> commonKeyActions;

    /** Creates an action key registry. */
    public KeyActionRegistry() {
        this.commonKeyActions = new HashMap<Integer, KeyAction>();
    }

    /** Register a binding of an action for a given key.
     * @param keyCode the keyCode
     * @param keyAction the KeyAction handling the action to perform
     */
    public void register(int keyCode, KeyAction keyAction) {
        this.commonKeyActions.put(keyCode, keyAction);
    }

    /** @param keyCode the keyCode to search
     * @return the action key handler
     */
    public KeyAction getKeyAction(int keyCode) {
        return this.commonKeyActions.get(keyCode);
    }

    /** @param keyCode the keyCode to search
     * @return true if the keyCode is bound to an action key handler, false otherwise 
     */
    public boolean isMappedKey(int keyCode) {
        return this.commonKeyActions.containsKey(keyCode);
    }

}
