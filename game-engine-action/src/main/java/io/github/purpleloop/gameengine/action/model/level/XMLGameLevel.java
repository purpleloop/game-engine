package io.github.purpleloop.gameengine.action.model.level;

import org.w3c.dom.Element;

/** A game level expressed in XML. */
public interface XMLGameLevel extends IGameLevel {

    /**
     * Loads a level from an XML element.
     * 
     * @param levelElement XML element to read
     * @throws Exception in case of problems
     */
    void loadFromXML(Element levelElement) throws Exception;
}
