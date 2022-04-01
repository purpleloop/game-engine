package io.github.purpleloop.gameengine.action.model.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Element;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.xml.XMLTools;

/** Models a dialog node. */
public class DialogNode {

    /** The identifier of the node. */
    private String id;

    /** The text of the node. */
    private String text;

    /** The possible choices from the node. */
    private List<DialogChoice> choices;

    /**
     * Constructor of a dialog node.
     * 
     * @param id identifier
     * @param text the text of the node
     */
    public DialogNode(String id, String text) {
        this.id = id;
        this.text = text;
        this.choices = new ArrayList<>();
    }

    /**
     * Constructor of a dialog node.
     * 
     * @param dialogNodeElement the XML source element
     * @throws PurpleException in case of problem
     */
    public DialogNode(Element dialogNodeElement) throws PurpleException {

        this.id = dialogNodeElement.getAttribute("id");
        this.choices = new ArrayList<>();

        Optional<Element> textElementOptional = XMLTools.getUniqueChildElement(dialogNodeElement,
                "text");
        if (textElementOptional.isPresent()) {
            this.text = textElementOptional.get().getTextContent();
        }
        
        for (Element dialogChoiceElement : XMLTools.getChildElements(dialogNodeElement,
                "dialog-choice")) {

            addChoice(new DialogChoice(dialogChoiceElement));

        }

    }

    /** @return the identifier of the node */
    public String getId() {
        return id;
    }

    /** @return the text of this node */
    public String getText() {
        return text;
    }

    /**
     * Add a possible choice from this node.
     * 
     * @param dialogChoice the choice to add
     */
    public void addChoice(DialogChoice dialogChoice) {
        choices.add(dialogChoice);
    }

    /** @return all possible choices from this node */
    public List<DialogChoice> getChoices() {
        return choices;
    }

}
