package io.github.purpleloop.gameengine.workshop.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.exception.PurpleException;
import io.github.purpleloop.commons.swing.SwingUtils;
import io.github.purpleloop.commons.swing.sprites.model.SpriteModel;
import io.github.purpleloop.gameengine.workshop.Preferences;
import io.github.purpleloop.gameengine.workshop.ui.config.JConfigPanel;
import io.github.purpleloop.gameengine.workshop.ui.sprites.SpriteSetEditorPanel;

/** Main frame of the game engine workshop. */
public class WorkshopUi extends JFrame implements StatusObserver {

    /** Class logger. */
    private static final Log LOG = LogFactory.getLog(WorkshopUi.class);

    /** Serialization tag. */
    private static final long serialVersionUID = 729956185901387883L;

    /** Action to creates a new sprite sheet. */
    private Action newSpriteSheetAction = new AbstractAction("New sprite sheet for image") {

        /** Serial tag. */
        private static final long serialVersionUID = -5802251822349890082L;

        @Override
        public void actionPerformed(ActionEvent e) {
            newSpriteSheet();
        }
    };

    /** Action to open an existing sprite sheet. */
    private Action openSpriteSheetAction = new AbstractAction("Open sprite sheet") {

        /** Serial tag. */
        private static final long serialVersionUID = 8706455940617931114L;

        @Override
        public void actionPerformed(ActionEvent e) {
            openSpriteSheet();
        }
    };

    /** Action for saving the current sprite sheet. */
    private Action saveSpriteSheetAction = new AbstractAction("Save sprite sheet") {

        /** Serial tag. */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            saveSpriteSheet();
        }

    };

    /** Statuses. */
    private Map<String, String> statuses = new HashMap<>();

    /** The status label. */
    private JLabel status;

    /** File chooser for images. */
    private JFileChooser fcImages;

    /** File chooser for the sprite sheet to edit. */
    private JFileChooser fcSpriteSheet;

    /** The sprite sheet editor panel. */
    private SpriteSetEditorPanel spriteSetEditorPanel;

    /** The user preference. */
    private Preferences preferences;

    /** A filter for XML sprite descriptors. */
    private FileFilter xmlFilter = new FileFilter() {

        @Override
        public String getDescription() {
            return "XML sprite descriptors";
        }

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getAbsolutePath().endsWith(".xml");
        }
    };

    /** A filter for sprite images. */
    private FileFilter imageFilter = new FileFilter() {

        @Override
        public String getDescription() {
            return "Sprite source images";
        }

        @Override
        public boolean accept(File f) {
            String[] supportedSuffixes = ImageIO.getReaderFileSuffixes();

            boolean supported = false;

            String absolutePath = f.getAbsolutePath();
            for (String supportedSuffix : supportedSuffixes) {
                supported |= absolutePath.endsWith(supportedSuffix);
            }

            return f.isDirectory() || supported;
        }
    };

    /** Constructor of the workshop frame. */
    public WorkshopUi() {
        super("Game Engine Workshop");

        LOG.debug("Creates the main frame.");

        preferences = new Preferences();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu menuSpriteSheet = new JMenu("Sprite Sheet");
        SwingUtils.addMenuItem(newSpriteSheetAction, menuSpriteSheet);
        SwingUtils.addMenuItem(openSpriteSheetAction, menuSpriteSheet);
        SwingUtils.addMenuItem(saveSpriteSheetAction, menuSpriteSheet);
        menubar.add(menuSpriteSheet);

        JTabbedPane tabPane = new JTabbedPane();

        spriteSetEditorPanel = new SpriteSetEditorPanel(this);
        tabPane.add(spriteSetEditorPanel, "SpriteSet editor");
        tabPane.add(new JConfigPanel(), "Configuration editor");
        mainPanel.add(tabPane, BorderLayout.CENTER);

        status = new JLabel();
        mainPanel.add(status, BorderLayout.SOUTH);

        pack();

        spriteSetEditorPanel.getSourcePanel().requestFocusInWindow();

        fcSpriteSheet = new JFileChooser(new File("."));
        fcSpriteSheet.setDialogTitle("Select the sprite sheet descriptor");
        fcSpriteSheet.setFileFilter(xmlFilter);

        fcImages = new JFileChooser(new File("."));
        fcImages.setDialogTitle("Select the source image");
        fcImages.setFileFilter(imageFilter);

        String recentlyUsed = preferences.getRecentlyUsed();
        if (StringUtils.isNotBlank(recentlyUsed)) {

            File recentDir = Paths.get(recentlyUsed).getParent().toFile().getAbsoluteFile();

            LOG.info("Preset path to " + recentDir.getAbsolutePath());
            fcSpriteSheet.setCurrentDirectory(recentDir);
            fcImages.setCurrentDirectory(recentDir);

        }

    }

    /** Creates a new sprite sheet (from a source image). */
    protected void newSpriteSheet() {

        int dialogResult = fcImages.showOpenDialog(this);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {

            File selectedFile = fcImages.getSelectedFile();
            createSpriteModelWithFile(selectedFile);
        }
    }

    /** Open an existing sprite sheet. */
    protected void openSpriteSheet() {

        int dialogResult = fcSpriteSheet.showOpenDialog(this);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {

            File selectedFile = fcSpriteSheet.getSelectedFile();
            createSpriteModelWithFile(selectedFile);
        }
    }

    /**
     * Creates a new sprite model using the given file as source image.
     * 
     * @param file source image file
     */
    private void createSpriteModelWithFile(File file) {

        preferences.setRecentlyUsed(file.getAbsolutePath());

        LOG.debug("Creating a sprite model with source image file " + file);
        try {
            SpriteModel spriteModel = new SpriteModel(file.getAbsolutePath());
            spriteSetEditorPanel.setSpriteModel(spriteModel);

            repaint();
        } catch (PurpleException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Save the current sprite model. */
    protected void saveSpriteSheet() {

        int dialogResult = fcSpriteSheet.showSaveDialog(this);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {

            File selectedFile = fcSpriteSheet.getSelectedFile();
            LOG.debug("Save spritesheet to file " + selectedFile);

            try {

                spriteSetEditorPanel.getSpriteModel().saveToFile(selectedFile);
            } catch (PurpleException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        }

    }

    /**
     * Game engine workshop entry point.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        LOG.info("Starting the game engine workshop.");

        WorkshopUi mainFrame = new WorkshopUi();

        mainFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                super.windowClosing(e);

                mainFrame.savePreferences();

                LOG.info("Terminate application");
                System.exit(0);
            }

        });

        mainFrame.setVisible(true);
    }

    protected void savePreferences() {
        preferences.save();
    }

    @Override
    public void setStatus(String categorie, String valeur) {

        statuses.put(categorie, valeur);

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> ent : statuses.entrySet()) {

            stringBuilder.append(ent.getKey());
            stringBuilder.append(" ");
            stringBuilder.append(ent.getValue());
        }

        status.setText(stringBuilder.toString());
    }

}
