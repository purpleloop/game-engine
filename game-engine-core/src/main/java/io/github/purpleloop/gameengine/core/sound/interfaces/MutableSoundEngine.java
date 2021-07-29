package io.github.purpleloop.gameengine.core.sound.interfaces;

/** A sound engine for games that can be muted. */
public interface MutableSoundEngine extends SoundEngine {

    /**
     * Enable/disables the sound.
     * 
     * @param mode true if sounds must be played, false elsewhere
     */
	void enableSounds(boolean mode);

	/**
     * Is sound enabled ?
     * 
     * @return true if sounds can be played, false elsewhere
     */
	boolean canPlaySounds();

    /** Enable or disables the sounds, bases on the current state of the sound engine. */
    void switchSounds();
}
