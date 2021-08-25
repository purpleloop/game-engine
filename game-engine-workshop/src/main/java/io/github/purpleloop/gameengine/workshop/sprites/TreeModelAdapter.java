package io.github.purpleloop.gameengine.workshop.sprites;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** An adapter for the tree model of a sprite set. */
public class TreeModelAdapter implements TreeModel {

	/** Class logger. */
	public static final Log LOG = LogFactory.getLog(TreeModelAdapter.class);

	/** The root of the sprite model. */
	private static final String ROOT = "SpriteModel";

	/** Source image. */
	private static final String IMAGE_NODE = "Image";

	/** Single sprites represented by rectangles. */
	private static final String RECTANGLES = "Rectangles";

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
		return ROOT;
	}

	@Override
	public Object getChild(Object parent, int index) {

		LOG.debug("getChild " + parent + " at index " + index);

		if (parent == ROOT) {

			if (index == 0) {
				return IMAGE_NODE;
			} else if (index == 1) {
				return RECTANGLES;
			}

		}

		if (parent == RECTANGLES) {
			return spriteModel.rectangles().get(index);
		}

		return null;

	}

	@Override
	public int getChildCount(Object parent) {

		int result = 0;

		if (parent == ROOT) {
			result = 2;
		}

		if (parent == RECTANGLES) {
			result = spriteModel.rectangles().size();
		}

		LOG.debug("getChildCount for " + parent + " => " + result);
		return result;
	}

	@Override
	public boolean isLeaf(Object node) {
		return node != ROOT && node != RECTANGLES;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {

		LOG.debug("getIndexOfChild for parent " + parent + " and child " + child);
		if (parent == null || child == null) {
			return -1;
		}

		if (parent == ROOT) {
			if (child == RECTANGLES) {
				return 1;
			} else if (child == IMAGE_NODE) {
				return 0;
			}
			return -1;
		}

		if (parent == RECTANGLES) {
			return spriteModel.rectangles().indexOf(child);
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

}
