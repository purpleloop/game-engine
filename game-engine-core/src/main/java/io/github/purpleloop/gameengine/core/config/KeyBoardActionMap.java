package io.github.purpleloop.gameengine.core.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Element;

/** This class stores an keyboard mapping for actions. */
public class KeyBoardActionMap {

    /**
     * Represents the neutral state of the keyboard, when no key is pressed or
     * locked.
     */
    private static final String KEY_CODE_NONE = "none";

    /** The keyboard mapping from key to action name. */
    private Map<String, String> keyMap;

    /** Creates a new mapping. */
    public KeyBoardActionMap() {
        keyMap = new HashMap<>();
    }

    /**
     * Add an action mapping for a given key.
     * 
     * @param key the key
     * @param action the action name
     */
    public void addKeyMap(String key, String action) {
        keyMap.put(key, action);
    }

    /**
     * @param keyText the name of the key
     * @return the name of the corresponding action (or null if it does not
     *         exist).
     */
    public String getActionForKey(String keyText) {
        return keyMap.get(keyText);
    }

    /** @return the idle action name, when no key is pressed */
    public String getActionForNoKeypressed() {
        return keyMap.get(KEY_CODE_NONE);
    }

    /** @return the mapping */
    public Map<String, String> getKeyMap() {
        return keyMap;
    }

    /** @return the mapping entry set */
    public Set<Entry<String, String>> entrySet() {
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
