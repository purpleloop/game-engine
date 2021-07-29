package io.github.purpleloop.gameengine.core.sound.interfaces;

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
}
