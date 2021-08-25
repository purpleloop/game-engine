package io.github.purpleloop.gameengine.workshop.sprites;

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

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A panel used to display the source image containing sprites.
 * This panel is use to calibrate the indexed sprite set (grid / serial).
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
    private Rectangle2D rectangle;

    /** Mouse location. */
    private Point mouseLoc;

    /** The status observer. */
    private StatusObserver statusObserver;

    /** Selection rectangle. */
    private Rectangle selectionRectangle;

    /** Cached source image. */
    private Image cachedImage;

    /** The grid index. */
    private SpriteGridIndex spriteGridIndex = new SpriteGridIndex();

    /** The keyboard controller used to adjust the grid. */
    private KeyListener gridKeyboardController = new KeyAdapter() {

        @Override
        public void keyTyped(KeyEvent evt) {

            LOG.debug(evt.getKeyCode());

            int keyCode = evt.getKeyCode();

            switch (keyCode) {
            case KeyEvent.VK_UP:
                spriteGridIndex.translate(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                spriteGridIndex.translate(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                spriteGridIndex.translate(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                spriteGridIndex.translate(1, 0);
                break;

            default:

            }

            repaint();

        }

    };

    /**
     * Constructor of the panel.
     * 
     * @param statusObserver the status observer
     */
    public SpriteSourcePanel(StatusObserver statusObserver) {
        super();

        this.statusObserver = statusObserver;

        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(gridKeyboardController);

        requestFocus();

        updatePrefSize(WIDTH, HEIGHT);

        spriteGridIndex.setGrid(3, 3, new Point(10, 10), 50, 50, 5, 5);

    }

    @Override
    public void paint(Graphics graphics) {

        Graphics2D graphics2d = (Graphics2D) graphics;

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        if (spriteModel == null) {
            graphics.drawString("You can open a sprite model descriptor.", 50, 50);

        } else {

            graphics.drawImage(spriteModel.getImage(), 0, 0, this);
            if (rectangle != null) {
                graphics2d.setColor(Color.RED);
                graphics2d.draw(rectangle);

            }
            if (selectionRectangle != null) {
                graphics2d.setColor(Color.WHITE);
                graphics2d.draw(selectionRectangle);

            }
        }

        if (spriteGridIndex != null) {

            graphics2d.setColor(Color.GREEN);
            Rectangle2D rc = new Rectangle2D.Double(0, 0, 1, 1);
            for (int c = 0; c < spriteGridIndex.getSpritesCount(); c++) {

                rc.setRect(spriteGridIndex.getX(c), spriteGridIndex.getY(c), spriteGridIndex.getWidth(c), spriteGridIndex.getHeigth(c));

                graphics2d.draw(rc);
            }

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

            IndexedSpriteSet spriteIndex = spriteModel.getIndex();

            int indexSprite = spriteIndex.getIndexFor(e.getPoint());
            LOG.debug(" => " + indexSprite);
            indexSelectionListener.setSelectedSpriteIndex(indexSprite);

            rectangle = new Rectangle2D.Double(spriteIndex.getX(indexSprite), spriteIndex.getY(indexSprite), spriteIndex.getWidth(indexSprite), spriteIndex.getHeigth(indexSprite));

            LOG.debug("Rectangle " + rectangle);

            repaint();
        }

        selectionRectangle = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point dest = e.getPoint();

        selectionRectangle = new Rectangle(mouseLoc.x, mouseLoc.y, dest.x - mouseLoc.x, dest.y - mouseLoc.y);

        if (statusObserver != null) {
            statusObserver.setStatus("Rectangle", "(" + selectionRectangle.width + "," + selectionRectangle.height + ")");
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
        }

        reset();

    }

    /** Reset the panel. */
    private void reset() {
        rectangle = null;
        selectionRectangle = null;
    }

    /** Update the panel size.
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
