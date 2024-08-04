package io.github.purpleloop.gameengine.workshop.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.sprites.model.IndexedSpriteSet;
import io.github.purpleloop.commons.swing.sprites.model.SingleSprite;
import io.github.purpleloop.commons.swing.sprites.model.SpriteModel;

/** An adapter for the tree model of a sprite set. */
public class TreeModelAdapter implements TreeModel {

    /** Class logger. */
    public static final Log LOG = LogFactory.getLog(TreeModelAdapter.class);

    /** The root of the sprite model. */
    private static final String SPRITE_MODEL_NODE = "SpriteModel";

    /** Source image node. */
    private static final String IMAGE_NODE = "Image";

    /** The indexes node. */
    private static final String INDEXES_NODE = "Indexes";

    /** Single sprites represented by rectangles node. */
    private static final String SINGLE_SPRITES = "Rectangles";

    /** The sprite set model. */
    private SpriteModel spriteModel;

    /** Tree model listeners. */
    private List<TreeModelListener> listeners;

    /**
     * Constructor of the adapter.
     * 
     * @param spriteModel the associated sprite model
     */
    public TreeModelAdapter(SpriteModel spriteModel) {
        this.spriteModel = spriteModel;
        listeners = new ArrayList<>();
    }

    @Override
    public Object getRoot() {
        // The Sprite model node is the root of the tree
        return SPRITE_MODEL_NODE;
    }

    @Override
    public Object getChild(Object parent, int index) {

        LOG.debug("getChild " + parent + " at index " + index);

        if (parent == SPRITE_MODEL_NODE) {

            if (index == 0) {
                return IMAGE_NODE;

            } else if (index == 1) {
                return INDEXES_NODE;
            }

        }

        if (parent == INDEXES_NODE) {
            return spriteModel.getIndexes().get(index);

        }

        if (parent == SINGLE_SPRITES) {
            return spriteModel.getSingleSprites().get(index);
        }

        return null;

    }

    @Override
    public int getChildCount(Object parent) {

        int result = 0;

        if (parent == SPRITE_MODEL_NODE) {
            // The image source and the indexes
            result = 2;
        }

        if (parent == INDEXES_NODE) {
            result = spriteModel.getIndexes().size();
        }

        if (parent == SINGLE_SPRITES) {
            result = spriteModel.getSingleSprites().size();
        }

        if (parent instanceof IndexedSpriteSet) {
            result = 0;
        }

        if (parent instanceof SingleSprite) {
            result = 0;
        }

        LOG.debug("getChildCount for " + parent + " => " + result);
        return result;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node != SPRITE_MODEL_NODE && node != SINGLE_SPRITES && node != INDEXES_NODE;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        LOG.debug("Ignore value for path changed " + path + " " + newValue);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {

        LOG.debug("getIndexOfChild for parent " + parent + " and child " + child);
        if (parent == null || child == null) {
            return -1;
        }

        if (parent == SPRITE_MODEL_NODE) {
            if (child == INDEXES_NODE) {
                return 1;
            } else if (child == IMAGE_NODE) {
                return 0;
            }
            return -1;
        }

        if (parent == INDEXES_NODE) {
            return spriteModel.getIndexes().indexOf(child);
        }

        if (parent == SINGLE_SPRITES) {
            return spriteModel.getSingleSprites().indexOf(child);
        }

        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    /** On update. */
    public void update() {
        TreeModelEvent e = new TreeModelEvent(this, new Object[] { SPRITE_MODEL_NODE });

        for (TreeModelListener listener : listeners) {
            listener.treeStructureChanged(e);
        }

    }

    /**
     * Deletes a tree node given it's path.
     * 
     * @param path the tree path of the element to delete
     */
    public void deleteObjectAtPath(TreePath path) {

        Object[] pathElements = path.getPath();

        // Currently only deletion of an index is implemented
        if (pathElements.length != 3 || pathElements[0] != SPRITE_MODEL_NODE
                || pathElements[1] != INDEXES_NODE) {
            return;
        }

        IndexedSpriteSet nodeToRemove = (IndexedSpriteSet) pathElements[2];
        spriteModel.removeIndex(nodeToRemove);
    }

}
