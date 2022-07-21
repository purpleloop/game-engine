package io.github.purpleloop.gameengine.workshop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Preferences. */
public class Preferences {

    /** Property key for the recent file. */
    private static final String RECENT_FILE = "recentFile";

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(Preferences.class);

    /** The preference properties. */
    private Properties preferenceProperties;

    /** The preferences properties file. */
    private File propertiesFile;

    /** Constructor of preferences. */
    public Preferences() {

        Path path = Paths.get(System.getProperty("user.home") + System.getProperty("file.separator")
                + ".game-engine-workshop");

        propertiesFile = path.toFile();

        preferenceProperties = new Properties();
        load();

    }

    /** Loads the preferences. */
    public void load() {

        if (propertiesFile.canRead()) {

            LOG.info("Loading preferences from " + propertiesFile.getAbsolutePath());
            try (FileInputStream fis = new FileInputStream(propertiesFile);) {

                preferenceProperties.load(fis);

            } catch (IOException e) {
                LOG.error("Error loading properties file " + propertiesFile.getAbsolutePath(), e);
            }
        }

    }

    /** Saves the preferences. */
    public void save() {

        LOG.info("Saving preferences to " + propertiesFile.getAbsolutePath());

        try (FileOutputStream fos = new FileOutputStream(propertiesFile);) {

            preferenceProperties.store(fos, "Game Engine Workshop Preferences");

        } catch (IOException e) {
            LOG.error("Error saving properties file " + propertiesFile.getAbsolutePath(), e);
        }

    }

    /**
     * Memorize a recently used path.
     * 
     * @param absolutePath path to memorize
     */
    public void setRecentlyUsed(String absolutePath) {
        preferenceProperties.setProperty(RECENT_FILE, absolutePath);
    }

    /**
     * @return the recently used path
     */
    public String getRecentlyUsed() {
        return (String) preferenceProperties.get(RECENT_FILE);
    }

}
