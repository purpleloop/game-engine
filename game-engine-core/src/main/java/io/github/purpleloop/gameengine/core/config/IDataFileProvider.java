package io.github.purpleloop.gameengine.core.config;

import java.awt.Image;
import java.io.InputStream;

import io.github.purpleloop.gameengine.core.util.EngineException;

/** Data file provider. */
public interface IDataFileProvider {

    /**
     * Provides an input stream for a file given by it's name.
     * 
     * @param fileName the file name
     * @return the input stream for the file
     * @throws EngineException in case of problems
     */
    InputStream getInputStream(String fileName) throws EngineException;

    /**
     * Obtain the image for a given image name.
     * 
     * @param imageName the image name
     * @return the requested image
     * @throws EngineException in case of problems
     */
    Image getImage(String imageName) throws EngineException;

}
