package io.github.purpleloop.gameengine.action.model.dialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.xml.XMLTools;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** A model of dialog. */
public class Dialog {

    /** The dialog id. */
    private String id;

    /** The dialog nodes. */
    private Map<String, DialogNode> dialogNodes;

    /**
     * Constructor of the dialog.
     * 
     * @param id the dialog id
     */
    public Dialog(String id) {
        this.id = id;
        this.dialogNodes = new HashMap<>();
    }

    /**
     * Constructor of the dialog.
     * 
     * @param dialogElement the XML element source
     * 
     * @throws PurpleException in case of problem
     */
    public Dialog(Element dialogElement) throws PurpleException {

        this.id = dialogElement.getAttribute("id");

        this.dialogNodes = new HashMap<>();
        for (Element dialogNodeElement : XMLTools.getChildElements(dialogElement, "dialog-node")) {
            addNode(new DialogNode(dialogNodeElement));
        }
    }

    /** @return the dialog id */
    private String getId() {
        return this.id;
    }

    /**
     * Add a node to the dialog.
     * 
     * @param dialogNode the node to add
     */
    private void addNode(DialogNode dialogNode) {
        dialogNodes.put(dialogNode.getId(), dialogNode);
    }

    /**
     * Retrieve a dialog node by it's index.
     * 
     * @param index the index to fetch
     * @return the dialog node
     */
    public DialogNode getNode(String index) {
        return dialogNodes.get(index);
    }

    /**
     * Loads a map of dialogues from an XML file.
     * 
     * @param dialogSetFileName the dialog file name
     * @return a dialog map
     * @throws EngineException in case of problems
     */
    public static Map<String, Dialog> loadFromXml(String dialogSetFileName) throws EngineException {

        Map<String, Dialog> dialogs = new HashMap<>();

        try {
            Document document = XMLTools.getDocument(new File(dialogSetFileName));

            Element dialogSetElement = document.getDocumentElement();

            for (Element dialogElement : XMLTools.getChildElements(dialogSetElement, "dialog")) {

                Dialog dialog = new Dialog(dialogElement);
                dialogs.put(dialog.getId(), dialog);
            }

            return dialogs;

        } catch (PurpleException e) {
            throw new EngineException("Error while loading dialogs", e);
        }
    }

}
