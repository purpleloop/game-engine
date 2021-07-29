package io.github.purpleloop.gameengine.core.util;

/** Exception for game engine problems. */
public class EngineException extends Exception {

    /** Serialization tag. */
    private static final long serialVersionUID = -4335573554405756576L;

    /**
     * Creates a new game engine exception with a given message.
     * 
     * @param message the message describing the encountered problem
     */
    public EngineException(String message) {
        super(message);
    }

    /**
     * Creates a new game engine exception with a given message.
     * 
     * @param message the message describing the encountered problem
     * @param cause cause of the exception
     */
    public EngineException(String message, Throwable cause) {
        super(message, cause);
    }

}
