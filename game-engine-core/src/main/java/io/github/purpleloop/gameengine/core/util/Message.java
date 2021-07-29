package io.github.purpleloop.gameengine.core.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Class for the management of user messages. */
public final class Message {

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(Message.class);

    /** Name of the message file. */
    private static final String MESSAGE_BASE_NAME = "messages";

    /** Ressource bundle containing messages. */
    private static ResourceBundle messages;

    /** Private constructor. */
    private Message() { }

    static {
        messages = ResourceBundle.getBundle(MESSAGE_BASE_NAME);
    }

    /** Builds a string containing the message for the key and the provided arguments. 
     * @param key key identifying the message template
     * @param args arguments to replace in the template
     * @return string containing the message
     */
    public static String getMessage(String key, Object... args) {

        try {

            String msg = messages.getString(key);

            if (msg == null) {
                LOG.error("Missing message for key " + key + ".");
                return "...";
            }

            for (int i = 0; i < args.length; i++) {
                msg = msg.replaceAll("%" + (i + 1), args[i].toString());
            }

            return msg;
        } catch (MissingResourceException e) {
            LOG.warn("Missing message resource for key " + key);
            return key;
        }

    }

}
