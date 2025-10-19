package io.github.purpleloop.gameengine.action.model.level;

import java.util.Collections;
import java.util.List;

import io.github.purpleloop.gameengine.core.config.GameConfig;
import io.github.purpleloop.gameengine.core.config.IDataFileProvider;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** Models a single level game. */
public class SingleLevelManager implements ILevelManager {

    /** The default level. */
    private IGameLevel defaultLevel = new IGameLevel() {

        @Override
        public List<LevelLink> getLinks() {
            return Collections.emptyList();
        }

        @Override
        public String getId() {
            return "default";
        }
    };

    @Override
    public String getStartLevelId() {
        return defaultLevel.getId();
    }

    /**
     * Constructor of the game level set.
     * 
     * @param config the game configuration
     * @param dataFileProvider the data provider
     */
    public SingleLevelManager(GameConfig config, IDataFileProvider dataFileProvider) {    
    }

    @Override
    public IGameLevel getLevel(String levelId) throws EngineException {
        return defaultLevel;
    }

    @Override
    public int getSize() {
        return 1;
    }

}
