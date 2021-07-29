package io.github.purpleloop.gameengine.core.config;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.swing.image.ImageUtils;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** Local file provider. */
public final class LocalDataFileProvider implements IDataFileProvider {

    /** The singleton instance. */
    private static LocalDataFileProvider instance;

    /** @return the singleton instance */
    public static synchronized LocalDataFileProvider getInstance() {
        if (instance == null) {
            instance = new LocalDataFileProvider();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws EngineException
     */
    public InputStream getInputStream(String filename) throws EngineException {
        File fich = new File(filename);

        if (!fich.canRead()) {
            throw new EngineException("Unable to access file '" + filename + "'.");
        }

        InputStream is;
        try {
            is = new FileInputStream(fich);
        } catch (FileNotFoundException e) {
            is = null;
            e.printStackTrace();
        }

        return is;
    }

    /** {@inheritDoc} */
    public Image getImage(String imageName) throws EngineException {
        try {
            return ImageUtils.loadImageFromFile(imageName);
        } catch (PurpleException e) {
            throw new EngineException("Failed to load image " + imageName);
        }
    }

}
