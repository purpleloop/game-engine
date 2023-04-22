package io.github.purpleloop.gameengine.core.config;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

/** This class stores an keyboard mapping for actions. */
public class KeyBoardActionMap {

    /** Logger of the class. */
    private static final Log LOG = LogFactory.getLog(KeyBoardActionMap.class);

    /**
     * Represents the neutral state of the keyboard, when no key is pressed or
     * locked.
     */
    private static final int KEY_CODE_NONE = KeyEvent.VK_UNDEFINED;

    /** The keyboard mapping from key event value to action name. */
    private Map<Integer, String> keyMap;

    /** Creates a new mapping. */
    public KeyBoardActionMap() {
        keyMap = new HashMap<>();
    }

    /**
     * Add an action mapping for a given key event name.
     * 
     * @param key the key event name
     * @param action the action name
     */
    public void addKeyMap(String key, String action) {

        try {
            // Retrieve the KeyEvent by it's constant name in the KeyEvent class
            Class<KeyEvent> keyEventClass = KeyEvent.class;
            Field keyField = keyEventClass.getDeclaredField(key);
            int keyEvent = (int) keyField.get(keyEventClass);
            keyMap.put(keyEvent, action);

        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
                | SecurityException e) {
            LOG.error("Unable to retrieve a KeyEvent class constant named " + key + ".", e);
        }

    }

    /**
     * @param keyEvent the keyEvent code
     * @return the name of the corresponding action (or null if it does not
     *         exist).
     */
    public String getActionForKey(int keyEvent) {
        return keyMap.get(keyEvent);
    }

    /** @return the idle action name, when no key is pressed */
    public String getActionForNoKeypressed() {
        return keyMap.get(KEY_CODE_NONE);
    }

    /** @return the mapping */
    public Map<Integer, String> getKeyMap() {
        return keyMap;
    }

    /** @return the mapping entry set */
    public Set<Entry<Integer, String>> entrySet() {
        return keyMap.entrySet();
    }

    /**
     * Fills the keyboard mapping from an XML descriptor.
     * 
     * @param elements the mapping elements
     */
    public void fillFromElement(List<Element> elements) {
        for (Element elementKeyMap : elements) {
            String key = elementKeyMap.getAttribute("key");
            String action = elementKeyMap.getAttribute("action");
            addKeyMap(key, action);
        }
    }
}
