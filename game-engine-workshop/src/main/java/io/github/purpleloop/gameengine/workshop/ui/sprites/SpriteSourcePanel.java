package io.github.purpleloop.gameengine.workshop.ui.sprites;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.sprites.model.IndexedSpriteSet;
import io.github.purpleloop.commons.swing.sprites.model.SpriteGridIndex;
import io.github.purpleloop.commons.swing.sprites.model.SpriteModel;
import io.github.purpleloop.gameengine.workshop.ui.StatusObserver;

/**
 * A panel used to display the source image containing sprites. This panel is
 * use to calibrate the indexed sprite set (grid / serial).
 */
public class SpriteSourcePanel extends JPanel implements MouseMotionListener, MouseListener {

    /** Panel width in pixels. */
    public static final int WIDTH = 1200;

    /** Panel height in pixels. */
    public static final int HEIGHT = 600;

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(SpriteSourcePanel.class);

    /** Serial tag. */
    private static final long serialVersionUID = 7806953187113632886L;

    /** Selection listener. */
    public interface IndexSelectionListener {

        /** @param indexSprite the index of the selected sprite */
        void setSelectedSpriteIndex(int indexSprite);
    }

    /** The selection listener. */
    private IndexSelectionListener indexSelectionListener;

    /** The sprite model. */
    private SpriteModel spriteModel;

    /** Rectangle. */
    private Rectangle2D selectedElementRectangle;

    /** The editor panel. */
    private SpriteSetEditorPanel spriteSetEditorPanel;

    /** Mouse location. */
    private Point mouseLoc;

    /** The status observer. */
    private StatusObserver statusObserver;

    /** Selection rectangle. */
    private Rectangle selectionRectangle;

    /** Cached source image. */
    private Image cachedImage;

    /** The keyboard controller used to adjust the grid. */
    private KeyListener gridKeyboardController = new KeyAdapter() {

        @Override
        public void keyTyped(KeyEvent evt) {

            LOG.debug(evt.getKeyCode());
            int keyCode = evt.getKeyCode();

            List<IndexedSpriteSet> indexes = spriteModel.getIndexes();
            if (indexes.size() > 0) {

                // FIXME manage multiple index
                IndexedSpriteSet spriteIndex = indexes.get(0);

                switch (keyCode) {
                case KeyEvent.VK_UP:
                    spriteIndex.translate(0, -1);
                    break;
                case KeyEvent.VK_DOWN:
                    spriteIndex.translate(0, 1);
                    break;
                case KeyEvent.VK_LEFT:
                    spriteIndex.translate(-1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    spriteIndex.translate(1, 0);
                    break;

                default:

                }

                repaint();
            }
        }

    };

    /**
     * Constructor of the panel.
     * 
     * @param spriteSetEditorPanel the editor panel
     * @param statusObserver the status observer
     */
    public SpriteSourcePanel(SpriteSetEditorPanel spriteSetEditorPanel,
            StatusObserver statusObserver) {
        super();

        this.spriteSetEditorPanel=spriteSetEditorPanel;
        this.statusObserver = statusObserver;

        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(gridKeyboardController);

        requestFocus();

        updatePrefSize(WIDTH, HEIGHT);

    }

    @Override
    public void paint(Graphics graphics) {

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        if (spriteModel == null) {

            graphics.setColor(Color.LIGHT_GRAY);
            graphics.drawString(
                    "No sprite model is configured. You can create a new sprite model or open an existing one.",
                    50, 50);

        } else {
            paintSpriteModel((Graphics2D) graphics);
        }

    }

    /**
     * Paints the sprite model.
     * 
     * @param graphics the graphic where to paint
     */
    private void paintSpriteModel(Graphics2D graphics) {

        graphics.drawImage(spriteModel.getImage(), 0, 0, this);

        for (IndexedSpriteSet spriteIndex : spriteModel.getIndexes()) {

            Optional<IndexedSpriteSet> indexesSpriteSetOpt = spriteSetEditorPanel.getSelectedSpriteIndex();
            
            if (indexesSpriteSetOpt.isPresent() && spriteIndex == indexesSpriteSetOpt.get()
                    && spriteIndex instanceof SpriteGridIndex) {

                SpriteGridIndex spriteGridIndex = (SpriteGridIndex) spriteIndex;

                graphics.setColor(Color.GREEN);
                Rectangle2D rc = new Rectangle2D.Double(0, 0, 1, 1);
                for (int indexValue = 0; indexValue < spriteGridIndex
                        .getSpritesCount(); indexValue++) {

                    int x = spriteIndex.getX(indexValue);
                    int y = spriteIndex.getY(indexValue);
                    rc.setRect(x, y, spriteIndex.getWidth(indexValue),
                            spriteIndex.getHeight(indexValue));

                    // Draw the cell rectangle
                    graphics.draw(rc);

                    // Draw the index of the cell
                    graphics.drawString(Integer.toString(indexValue), x + 2, y + 11);
                }

            }

        }

        if (selectedElementRectangle != null) {
            graphics.setColor(Color.RED);
            graphics.draw(selectedElementRectangle);
        }

        if (selectionRectangle != null) {
            graphics.setColor(Color.PINK);
            graphics.draw(selectionRectangle);

        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

        mouseLoc = mouseEvent.getPoint();
        if (statusObserver != null) {
            statusObserver.setStatus("Location", "(" + mouseLoc.x + "," + mouseLoc.y + ")");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        LOG.debug("Mouse in " + e.getPoint());

        if (spriteModel != null) {

            for (IndexedSpriteSet spriteIndex : spriteModel.getIndexes()) {

                Optional<Integer> indexSpriteOptional = spriteIndex.getIndexFor(e.getPoint());

                if (indexSpriteOptional.isPresent()) {
                    int indexSprite = indexSpriteOptional.get();
                    LOG.debug("Selected index => " + indexSprite);
                    indexSelectionListener.setSelectedSpriteIndex(indexSprite);

                    selectedElementRectangle = new Rectangle2D.Double(spriteIndex.getX(indexSprite),
                            spriteIndex.getY(indexSprite), spriteIndex.getWidth(indexSprite),
                            spriteIndex.getHeight(indexSprite));

                    LOG.debug("Selected rectangle " + selectedElementRectangle);
                }

            }

            repaint();
        }

        selectionRectangle = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point dest = e.getPoint();

        selectionRectangle = new Rectangle(mouseLoc.x, mouseLoc.y, dest.x - mouseLoc.x,
                dest.y - mouseLoc.y);

        if (statusObserver != null) {
            statusObserver.setStatus("Rectangle",
                    "(" + selectionRectangle.width + "," + selectionRectangle.height + ")");
        }

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Unused
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Unused
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Unused
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Unused
    }

    /**
     * Set up the sprite index listener.
     * 
     * @param listener the index listener
     */
    public void setSpriteIndexListener(IndexSelectionListener listener) {
        this.indexSelectionListener = listener;

    }

    /** @param spriteModel the sprite index model */
    public void setSpriteModel(SpriteModel spriteModel) {
        this.spriteModel = spriteModel;

        if (spriteModel != null) {
            this.cachedImage = spriteModel.getImage();
            if (cachedImage != null) {
                updatePrefSize(cachedImage.getWidth(this), cachedImage.getHeight(this));
            }
        } else {
            this.cachedImage = null;
            updatePrefSize(200, 200);
        }

        reset();

    }

    /** Reset the panel. */
    private void reset() {
        selectedElementRectangle = null;
        selectionRectangle = null;
    }

    /**
     * Update the panel size.
     * 
     * @param newWidth width
     * @param newHeight height
     */
    private void updatePrefSize(int newWidth, int newHeight) {
        setPreferredSize(new Dimension(newWidth, newHeight));
    }

    /** @return the current rectangle. */
    public Rectangle2D getCurrentRect() {
        return selectionRectangle;
    }

}
