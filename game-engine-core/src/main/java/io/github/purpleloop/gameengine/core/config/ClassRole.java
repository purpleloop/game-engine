package io.github.purpleloop.gameengine.core.config;

/** The class roles in the game engine configuration. */
public enum ClassRole {

    /** Role of a class implementing the view in the user interface. */
    VIEW,

    /** Role of a class implementing the game in the model. */
    GAME,

    // --- Action games -----------------------------------------------
    
    /** Role of a class implementing an environment in the model. */
    ENVIRONMENT,
    
    /** Role of a class implementing a game session in the model. */
    SESSION,
    
    /** Role of a class implementing a level in the model. */
    LEVEL,
    
    /** Role of a class implementing a level provider. */
    LEVEL_PROVIDER,
    
    /** Role of a class implementing a controller in the model. */
    CONTROLLER,

    // --- Board games ------------------------------------------------
    
    /** Role of a class implementing the board in the model. */
    BOARD,

    // --- Network ------------------------------------------------
    
    /** Role of a class implementing the message factory. */
    MESSAGE_FACTORY;
	
}
