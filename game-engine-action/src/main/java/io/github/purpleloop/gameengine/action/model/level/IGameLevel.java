package io.github.purpleloop.gameengine.action.model.level;

import java.util.List;

/**
 * Defines a game level.
 * 
 * A level is typically a 2d map that is used to build a game environment.
 */
public interface IGameLevel {

    /** @return the level id */
    String getId();
    
    /** @return links associated to this level */
    List<LevelLink> getLinks();

}
