package io.github.purpleloop.gameengine.workshop.sprites;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.gameengine.workshop.sprites.SpriteSourcePanel.IndexSelectionListener;

/** Panel to work on a sprite set. */
public class SpriteSetPanel extends JPanel implements IndexSelectionListener, ActionListener {

	/** Serialization tag. */
	private static final long serialVersionUID = -8549388832219503324L;

	/** Logger of the class. */
	private static final Log LOG = LogFactory.getLog(SpriteSetPanel.class);

	/** Command to start playing the animation. */
	private static final String CMD_PLAY = "CMD_PLAY";

	/** Command to stop paying the animation. */
	private static final String CMD_STOP = "CMD_STOP";

	/** The tree model. */
	private static final TreeModel DUMMY_MODEL = new TreeModel() {

		@Override
		public void valueForPathChanged(TreePath path, Object newValue) {
		}

		@Override
		public void removeTreeModelListener(TreeModelListener l) {
		}

		@Override
		public boolean isLeaf(Object node) {
			return false;
		}

		@Override
		public Object getRoot() {
			return "empty";
		}

		@Override
		public int getIndexOfChild(Object parent, Object child) {
			return 0;
		}

		@Override
		public int getChildCount(Object parent) {
			return 0;
		}

		@Override
		public Object getChild(Object parent, int index) {
			return null;
		}

		@Override
		public void addTreeModelListener(TreeModelListener l) {
		}
	};

	/** Command to define a rectangle. */
	private static final String CMD_DEFINE_RECTANGLE = "CMD_DEFINE_RECTANGLE";

	/** The currently selected index. */
	private int selectedIndex;

	/** The sprite animation panel. */
	private SpriteAnimationPanel spriteAnimationPanel;

	/** The sprite model. */
	private SpriteModel spriteModel;

	/** The panel for the source image. */
	private SpriteSourcePanel spriteSourcePanel;

	/** View of the sprite model structure represented as a JTree. */
	private JTree tSpriteModel;

	/**
	 * Creates a sprite set panel.
	 * 
	 * @param statusObserver the status observer
	 */
	public SpriteSetPanel(StatusObserver statusObserver) {

		setLayout(new BorderLayout());

		spriteSourcePanel = new SpriteSourcePanel(statusObserver);
		spriteSourcePanel.setSpriteIndexListener(this);

		JToolBar tbSpriteSheet = new JToolBar(JToolBar.HORIZONTAL);

		createToolButton("", CMD_DEFINE_RECTANGLE, "Define a rectangle", "Rect", tbSpriteSheet);

		add(tbSpriteSheet, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(spriteSourcePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scrollPane.setBackground(Color.BLACK);
		add(scrollPane, BorderLayout.CENTER);

		JPanel animationPanel = new JPanel();

		animationPanel.setLayout(new BorderLayout());
		tSpriteModel = new JTree(DUMMY_MODEL);
		add(tSpriteModel, BorderLayout.WEST);

		spriteAnimationPanel = new SpriteAnimationPanel(200, 800);
		animationPanel.add(spriteAnimationPanel, BorderLayout.CENTER);

		JToolBar tbAnimation = new JToolBar(SwingConstants.HORIZONTAL);
		createToolButton("", CMD_PLAY, "Play animation", "Play", tbAnimation);
		createToolButton("", CMD_STOP, "Stop animation", "Stop", tbAnimation);
		animationPanel.add(tbAnimation, BorderLayout.NORTH);

		final SpinnerNumberModel spinModel = new SpinnerNumberModel(100, 10, 5000, 5);

		final JSpinner slAnimationDelay = new JSpinner(spinModel);
		slAnimationDelay
				.addChangeListener(event -> spriteAnimationPanel.setAnimationDelay(spinModel.getNumber().intValue()));
		tbAnimation.add(slAnimationDelay);

		add(animationPanel, BorderLayout.EAST);
	}

	/** @param spriteModel the sprite model */
	public void setSpriteModel(SpriteModel spriteModel) {

		this.spriteModel = spriteModel;

		spriteSourcePanel.setSpriteModel(spriteModel);
		spriteAnimationPanel.setSpriteModel(spriteModel);

		tSpriteModel.setModel(new TreeModelAdapter(spriteModel));

		repaint();
	}

	@Override
	public void setSelectedSpriteIndex(int indexSprite) {
		this.selectedIndex = indexSprite;
		spriteAnimationPanel.setIndexToAnimate(selectedIndex);
	}

	/**
	 * Creates a tool button on a tool bar.
	 * 
	 * @param imageName     the tool button image name
	 * @param actionCommand the action command
	 * @param toolTipText   the text for the tool tip
	 * @param altText       the alternative text
	 * @param toolBar       the owner tool bar
	 * @return the tool button
	 */
	protected JButton createToolButton(String imageName, String actionCommand, String toolTipText, String altText,
			JToolBar toolBar) {
		// Look for the image.
		String imgLocation = "images/" + imageName + ".gif";
		URL imageURL = this.getClass().getResource(imgLocation);

		// Create and initialize the button.
		JButton button = new JButton();
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		button.addActionListener(this);

		if (imageURL != null) {
			button.setIcon(new ImageIcon(imageURL, altText));
		} else {
			button.setText(altText);
			LOG.error("Resource not found: " + imgLocation);
		}

		toolBar.add(button);

		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(CMD_PLAY)) {

			spriteAnimationPanel.play();

		} else if (cmd.equals(CMD_STOP)) {

			spriteAnimationPanel.stop();

		} else if (cmd.equals(CMD_DEFINE_RECTANGLE)) {

			Rectangle2D rect = spriteSourcePanel.getCurrentRect();

			if (rect != null) {
				spriteModel.addRectangle(rect);

				// FIXME : Here we recreate the tree model adapter ... see if we can switch to a listener on the tree and update the model 
				tSpriteModel.setModel(new TreeModelAdapter(spriteModel));

			}
		}

	}

	/** @return the sprite source panel */
	public SpriteSourcePanel getSourcePanel() {
		return spriteSourcePanel;
	}

}
