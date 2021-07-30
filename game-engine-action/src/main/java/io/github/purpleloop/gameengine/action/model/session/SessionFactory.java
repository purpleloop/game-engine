package io.github.purpleloop.gameengine.action.model.session;

import io.github.purpleloop.commons.lang.ReflexivityTools;
import io.github.purpleloop.gameengine.action.model.interfaces.IGameEngine;
import io.github.purpleloop.gameengine.action.model.interfaces.ISession;
import io.github.purpleloop.gameengine.core.config.ClassRole;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** The session game factory. */
public final class SessionFactory {

    /** Private constructor. */
    private SessionFactory() {
    }

    /**
     * Creates a game session.
     * 
     * @param gameEngine the game engine where the session runs
     * @return session the created session
     * @throws EngineException in case of problem during session creation
     */
    public static ISession createSession(IGameEngine gameEngine) throws EngineException {
        Class<?>[] paramClasses = new Class<?>[1];
        paramClasses[0] = IGameEngine.class;

        Object[] paramValues = new Object[1];
        paramValues[0] = gameEngine;

        String sessionClassName = gameEngine.getConfig().getClassName(ClassRole.SESSION);

        ISession session = ReflexivityTools.createInstance(sessionClassName, paramClasses,
                paramValues);

        return session;
    }

}
