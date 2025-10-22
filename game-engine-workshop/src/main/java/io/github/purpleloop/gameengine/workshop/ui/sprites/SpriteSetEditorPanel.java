package io.github.purpleloop.gameengine.workshop.ui.sprites;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.Optional;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.sprites.model.IndexedSpriteSet;
import io.github.purpleloop.commons.swing.sprites.model.SpriteGridIndex;
import io.github.purpleloop.commons.swing.sprites.model.SpriteModel;
import io.github.purpleloop.gameengine.workshop.ui.StatusObserver;
import io.github.purpleloop.gameengine.workshop.ui.context.WorkshopContext;
import io.github.purpleloop.gameengine.workshop.ui.context.WorkshopPanel;
import io.github.purpleloop.gameengine.workshop.ui.sprites.SpriteSourcePanel.IndexSelectionListener;

/**
 * A panel for editing a sprite set. It is composed of an image source panel and
 * of an animation panel.
 */
public class SpriteSetEditorPanel extends WorkshopPanel implements IndexSelectionListener {

    /** Serialization tag. */
    private static final long serialVersionUID = -8549388832219503324L;

    /** Logger of the class. */
    private static final Log LOG = LogFactory.getLog(SpriteSetEditorPanel.class);

    /** Command to start playing the animation. */
    private static final String CMD_PLAY = "CMD_PLAY";

    /** Command to stop paying the animation. */
    private static final String CMD_STOP = "CMD_STOP";

    /** An empty tree model - for initialization. */
    private static final TreeModel EMPTY_MODEL = new TreeModel() {

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

    /** The selected sprite index, optional. */
    private Optional<IndexedSpriteSet> selectedSpriteIndex = Optional.empty();

    /** The sprite animation panel. */
    private SpriteAnimationPanel spriteAnimationPanel;

    /** The sprite model. */
    private SpriteModel spriteModel;

    /** The panel for the source image. */
    private SpriteSourcePanel spriteSourcePanel;

    /** View of the sprite model structure represented as a JTree. */
    private JTree spriteModelJTree;

    /** An editor for the source image. */
    private SpriteImageSourcePanel spriteImageSourcePanel;

    /** An editor for a sprite grid index. */
    private SpriteGridIndexPanel spriteGridPanel;

    /** The tree model adapter for the sprite model. */
    private TreeModelAdapter treeModelAdapter;

    /** Delete node action. */
    private Action deleteNodeAction = new AbstractAction("Delete") {

        /** Serial tag. */
        private static final long serialVersionUID = -7895800483021015063L;

        @Override
        public void actionPerformed(ActionEvent e) {

            TreePath path = spriteModelJTree.getSelectionPath();
            LOG.debug("Delete tree node at path " + path);

            treeModelAdapter.deleteObjectAtPath(path);

            refreshTree();
        }
    };

    /**
     * Creates a sprite set panel.
     * 
     * @param workshopContext the workshop context
     * 
     * @param statusObserver the status observer
     */
    public SpriteSetEditorPanel(WorkshopContext workshopContext, StatusObserver statusObserver) {

        super(workshopContext);

        setLayout(new BorderLayout());

        spriteSourcePanel = new SpriteSourcePanel(this, statusObserver);
        spriteSourcePanel.setSpriteIndexListener(this);

        JScrollPane scrollPane = new JScrollPane(spriteSourcePanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setBackground(Color.BLACK);
        add(scrollPane, BorderLayout.CENTER);

        JPanel editionPanel = new JPanel();
        editionPanel.setLayout(new BoxLayout(editionPanel, BoxLayout.Y_AXIS));
        add(editionPanel, BorderLayout.NORTH);

        JToolBar tbSpriteSheet = new JToolBar(JToolBar.HORIZONTAL);

        createToolButton("", CMD_DEFINE_RECTANGLE, "Define a rectangle", "Rect", tbSpriteSheet,
                e -> defineRectangle());
        createToolButton("", CMD_ADD_GRID_INDEX, "Add a grid index", "Grid", tbSpriteSheet,
                e -> defineGridIndex());
        createToolButton("", CMD_ADD_GRID_INDEX, "Register sprites", "Register", tbSpriteSheet,
                e -> registerSprites());

        editionPanel.add(tbSpriteSheet);

        spriteImageSourcePanel = new SpriteImageSourcePanel();

        spriteImageSourcePanel.setAssociatedSpriteSourcePanel(spriteSourcePanel);

        editionPanel.add(spriteImageSourcePanel);

        spriteGridPanel = new SpriteGridIndexPanel(this);
        editionPanel.add(spriteGridPanel);

        JPanel animationPanel = new JPanel();

        animationPanel.setLayout(new BorderLayout());

        spriteModelJTree = new JTree(EMPTY_MODEL);
        spriteModelJTree.setEditable(true);
        add(spriteModelJTree, BorderLayout.WEST);

        spriteModelJTree.addTreeSelectionListener(e -> handleTreeSelection(e.getPath()));

        JPopupMenu spriteModelPopupMenu = new JPopupMenu("Node");
        JMenuItem deleteNodeMenuItem = new JMenuItem(deleteNodeAction);
        spriteModelPopupMenu.add(deleteNodeMenuItem);
        spriteModelJTree.setComponentPopupMenu(spriteModelPopupMenu);

        spriteAnimationPanel = new SpriteAnimationPanel(350, 800);
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

    /** Refresh the sprite model tree. */
    protected void refreshTree() {

        // TODO for the moment, we recreate a new tree model, see how to improve
        treeModelAdapter = new TreeModelAdapter(spriteModel);
        spriteModelJTree.setModel(treeModelAdapter);
        repaint();
    }

    /** Register sprites. */
    private void registerSprites() {
        spriteModel.registerSprites();
    }

    /** Add a new grid index to the model. */
    private void defineGridIndex() {
        spriteModel.addIndex(new SpriteGridIndex("SpriteGridIndex" + spriteModel.getNextId()));

        spriteGridPanel.modelChanged();

        refreshTree();
    }

    /**
     * Get the current rectangular shape in the source panel and create a sprite
     * rectangle.
     */
    private void defineRectangle() {
        Rectangle2D rect = spriteSourcePanel.getCurrentRect();

        if (rect != null) {
            spriteModel.addRectangle(rect);

            refreshTree();
        }
    }

    /** @param spriteModel the sprite model */
    public void setSpriteModel(SpriteModel spriteModel) {

        this.spriteModel = spriteModel;

        spriteImageSourcePanel.setSpriteModel(spriteModel);
        spriteSourcePanel.setSpriteModel(spriteModel);
        spriteAnimationPanel.setSpriteModel(spriteModel);

        treeModelAdapter = new TreeModelAdapter(spriteModel);
        spriteModelJTree.setModel(treeModelAdapter);

        spriteGridPanel.modelChanged();

        store("spriteModel", spriteModel);
    }

    @Override
    public void setSelectedSpriteIndex(IndexedSpriteSet spriteIndex) {
        spriteAnimationPanel.setIndexToAnimate(spriteIndex);
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

        // Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(listener);

        // Look for the image.
        URL imageURL = null;
        if (!StringUtils.isBlank(imageName)) {

            String imgLocation = "images/" + imageName + ".gif";
            imageURL = this.getClass().getResource(imgLocation);

            if (imageURL != null) {
                button.setIcon(new ImageIcon(imageURL, altText));
            } else {
                LOG.error("Resource not found: " + imgLocation);
            }
        }

        if (imageURL == null) {
            button.setText(altText);
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

    /**
     * Handles a selection in the sprite model tree.
     * 
     * @param path the selected path
     */
    protected void handleTreeSelection(TreePath path) {
        LOG.debug("Selection made in the sprite model tree " + path);
        selectedSpriteIndex = treeModelAdapter.selectPath(path);

        if (selectedSpriteIndex.isPresent()) {
            spriteGridPanel.modelChanged();
        }

        repaint();
    }

    /** @return The selected sprite index, optional */
    public Optional<IndexedSpriteSet> getSelectedSpriteIndex() {
        return selectedSpriteIndex;
    }

}
