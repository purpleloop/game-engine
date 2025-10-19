package io.github.purpleloop.gameengine.action.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.core.config.GameConfig;

/** Launching parameters. */
public class LaunchingParameters {

    /** Class logger. */
    public static final Log LOG = LogFactory.getLog(LaunchingParameters.class);

    /** Are there errors in the parameters ? */
    private boolean errors = false;

    /** Is full screen requested ? */
    private boolean fullScreen;

    /** The configuration file name to load. */
    private String configFileName;

    /**
     * Analyzes the parameters given in a array of strings.
     * 
     * @param args parameters
     */
    public void analyseParameters(String[] args) {

        fullScreen = false;

        configFileName = null;

        boolean unexpectedParameter = false;

        for (String arg : args) {
            if (arg.startsWith("-c")) {
                configFileName = arg.substring(2);

                LOG.debug("Configuration file " + configFileName);

            } else if (arg.startsWith("-fullscreen")) {

                LOG.debug("Full screen");

                fullScreen = true;

            } else {
                unexpectedParameter = true;
            }
        }

        if (configFileName == null) {
            configFileName = GameConfig.DEFAULT_CONFIG_FILE_NAME;
        }

        if (unexpectedParameter) {
            errors = true;
            showUsage();
        }

    }

    /** @return are there parameters errors ? */
    public boolean hasErrors() {
        return errors;
    }

    /**
     * Display help on parameters on standard error stream.
     */
    private static void showUsage() {
        System.err.println("There was a problem with the launching paremeters.");
        System.err.println("-cconfig.xml");
        System.err.println("-fullscreen");
    }

    /** @return is full screen requested ? */
    public boolean isFullScreen() {
        return fullScreen;
    }

    /** @return the configuration file name */
    public String getConfigFileName() {
        return configFileName;
    }

}
