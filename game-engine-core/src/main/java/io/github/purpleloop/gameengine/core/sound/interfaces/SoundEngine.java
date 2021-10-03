package io.github.purpleloop.gameengine.core.sound.interfaces;

import java.io.File;

import io.github.purpleloop.gameengine.core.util.EngineException;

/** A basic sound engine interface. */
public interface SoundEngine {

    /**
     * Play a sound, given by it's name.
     * 
     * @param soundName name of the sound
     */
    void playSound(String soundName);

    /**
     * Plays a sound in loop, given by it's name.
     * 
     * @param soundName name of the sound
     */
    void loopSound(String soundName);

    /**
     * Play a background sound.
     * 
     * @param bgSoundFile the sound file
     * @throws EngineException in case of problem
     */
    void playBackgroundSound(File bgSoundFile) throws EngineException;

    /** Stop playing a background sound (if needed). */
    void stopBackgroundSound();
}
