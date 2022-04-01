package io.github.purpleloop.gameengine.action.model.dialog;

import java.util.Optional;

import org.w3c.dom.Element;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.xml.XMLTools;

/** Models a dialog choice. */
public class DialogChoice {

    /** The text describing the choice. */
    private String text;

    /** The outcome of the choice. */
    private String outcome;

    /**
     * Create a dialog choice.
     * 
     * @param text the text of the choice
     * @param outcome the outcome of the choice
     */
    public DialogChoice(String text, String outcome) {
        this.text = text;
        this.outcome = outcome;
    }

    /**
     * Create a dialog choice.
     * 
     * @param dialogChoiceElement he XML source element
     * @throws PurpleException in case of problem
     */
    public DialogChoice(Element dialogChoiceElement) throws PurpleException {

        Optional<Element> uniqueChildElement = XMLTools.getUniqueChildElement(dialogChoiceElement,
                "text");

        if (uniqueChildElement.isPresent()) {
            this.text = uniqueChildElement.get().getTextContent();
        }
        this.outcome = dialogChoiceElement.getAttribute("outcome");
    }

    /** @return the text of the choice */
    public String getText() {
        return text;
    }

    /** @return the outcome of the choice */
    public String getOutcome() {
        return this.outcome;
    }

}
