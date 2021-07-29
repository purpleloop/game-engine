package io.github.purpleloop.gameengine.core.sound.interfaces;

/** Resolver for named sound files. */
public interface ISoundFileResolver {

    /**
     * Get the name of the sound file associated to the given sound.
     * 
     * @param soundName name of the sound
     * @return name of the associated sound file
     */
    String getSoundFileName(String soundName);

}
