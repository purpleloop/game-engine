package io.github.purpleloop.gameengine.workshop.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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

import io.github.purpleloop.commons.swing.sprites.model.SpriteGridIndex;
import io.github.purpleloop.commons.swing.sprites.model.SpriteModel;
import io.github.purpleloop.gameengine.workshop.ui.SpriteSourcePanel.IndexSelectionListener;

/**
 * A panel for editing a sprite set. It is composed of an image source panel and
 * of an animation panel.
 */
public class SpriteSetEditorPanel extends JPanel implements IndexSelectionListener {

    /** Serialization tag. */
    private static final long serialVersionUID = -8549388832219503324L;

    /** Logger of the class. */
    private static final Log LOG = LogFactory.getLog(SpriteSetEditorPanel.class);

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

    /** Command to add a grid index. */
    private static final String CMD_ADD_GRID_INDEX = "CMD_ADD_GRID_INDEX";

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

    /** An editor for a sprite grid index. */
    private SpriteGridIndexPanel spriteGridPanel;

    /** The tree model adapter for the sprite model. */
    private TreeModelAdapter treeModelAdapter;

    /**
     * Creates a sprite set panel.
     * 
     * @param statusObserver the status observer
     */
    public SpriteSetEditorPanel(StatusObserver statusObserver) {

        setLayout(new BorderLayout());

        spriteSourcePanel = new SpriteSourcePanel(statusObserver);
        spriteSourcePanel.setSpriteIndexListener(this);

        spriteGridPanel = new SpriteGridIndexPanel(this);

        JToolBar tbSpriteSheet = new JToolBar(JToolBar.HORIZONTAL);

        createToolButton("", CMD_DEFINE_RECTANGLE, "Define a rectangle", "Rect", tbSpriteSheet,
                e -> defineRectangle());
        createToolButton("", CMD_ADD_GRID_INDEX, "Add a grid index", "Grid", tbSpriteSheet,
                e -> defineGridIndex());
        createToolButton("", CMD_ADD_GRID_INDEX, "Register sprites", "Register", tbSpriteSheet,
                e -> registerSprites());

        add(tbSpriteSheet, BorderLayout.NORTH);

        add(spriteGridPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(spriteSourcePanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
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
        createToolButton("", CMD_PLAY, "Play animation", "Play", tbAnimation,
                e -> spriteAnimationPanel.play());
        createToolButton("", CMD_STOP, "Stop animation", "Stop", tbAnimation,
                e -> spriteAnimationPanel.stop());
        animationPanel.add(tbAnimation, BorderLayout.NORTH);

        tbAnimation.add(new JLabel("Delay"));

        final SpinnerNumberModel delaySpinModel = new SpinnerNumberModel(100, 10, 5000, 5);

        final JSpinner slAnimationDelay = new JSpinner(delaySpinModel);
        slAnimationDelay.addChangeListener(event -> spriteAnimationPanel
                .setAnimationDelay(delaySpinModel.getNumber().intValue()));

        tbAnimation.add(slAnimationDelay);

        tbAnimation.add(new JLabel("frames count"));

        JSpinner framesCountSpinner = new JSpinner();
        tbAnimation.add(framesCountSpinner);
        framesCountSpinner.addChangeListener(
                event -> spriteAnimationPanel.setFrameCount((int) framesCountSpinner.getValue()));

        add(animationPanel, BorderLayout.EAST);
    }

    /** Register sprites. */
    private void registerSprites() {
        spriteModel.registerSprites();
    }

    /** Add a new grid index to the model. */
    private void defineGridIndex() {
        spriteModel.addIndex(new SpriteGridIndex(spriteModel.getNextId()));

        spriteGridPanel.setModel(spriteModel);

        // TODO works but maybe to improve to prevent the whole reconstruction of the
        // tree model
        treeModelAdapter = new TreeModelAdapter(spriteModel);
        tSpriteModel.setModel(treeModelAdapter);

        repaint();
    }

    /**
     * Get the current rectangular shape in the source panel and create a sprite
     * rectangle.
     */
    private void defineRectangle() {
        Rectangle2D rect = spriteSourcePanel.getCurrentRect();

        if (rect != null) {
            spriteModel.addRectangle(rect);

            // TODO works but maybe to improve to prevent the whole reconstruction of the
            // tree model
            tSpriteModel.setModel(new TreeModelAdapter(spriteModel));

        }
    }

    /** @param spriteModel the sprite model */
    public void setSpriteModel(SpriteModel spriteModel) {

        this.spriteModel = spriteModel;

        spriteSourcePanel.setSpriteModel(spriteModel);
        spriteAnimationPanel.setSpriteModel(spriteModel);

        treeModelAdapter = new TreeModelAdapter(spriteModel);
        tSpriteModel.setModel(treeModelAdapter);

        spriteGridPanel.setModel(spriteModel);

    }

    @Override
    public void setSelectedSpriteIndex(int indexSprite) {
        this.selectedIndex = indexSprite;
        spriteAnimationPanel.setIndexToAnimate(selectedIndex);
    }

    /**
     * Creates a tool button on a tool bar.
     * 
     * @param imageName the tool button image name
     * @param actionCommand the action command
     * @param toolTipText the text for the tool tip
     * @param altText the alternative text
     * @param toolBar the owner tool bar
     * @param listener the action listener for this button
     * @return the tool button
     */
    protected JButton createToolButton(String imageName, String actionCommand, String toolTipText,
            String altText, JToolBar toolBar, ActionListener listener) {

        // Look for the image.
        String imgLocation = "images/" + imageName + ".gif";
        URL imageURL = this.getClass().getResource(imgLocation);

        // Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(listener);

        if (imageURL != null) {
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {
            button.setText(altText);
            LOG.error("Resource not found: " + imgLocation);
        }

        toolBar.add(button);

        return button;
    }

    /** @return the sprite source panel */
    public SpriteSourcePanel getSourcePanel() {
        return spriteSourcePanel;
    }

    /** @return the sprite model */
    public SpriteModel getSpriteModel() {
        return spriteModel;
    }

}
