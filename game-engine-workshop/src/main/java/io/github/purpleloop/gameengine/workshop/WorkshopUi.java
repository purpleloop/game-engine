package io.github.purpleloop.gameengine.workshop;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
import javax.swing.WindowConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.SwingUtils;
import io.github.purpleloop.gameengine.core.util.EngineException;
import io.github.purpleloop.gameengine.workshop.sprites.SpriteModel;
import io.github.purpleloop.gameengine.workshop.sprites.SpriteSetPanel;
import io.github.purpleloop.gameengine.workshop.sprites.StatusObserver;

/** Main frame of the game engine workshop. */
public class WorkshopUi extends JFrame implements StatusObserver {

	/** Class logger. */
	private static final Log LOG = LogFactory.getLog(WorkshopUi.class);

	/** Serialization tag. */
	private static final long serialVersionUID = 729956185901387883L;

	/** Action to creates a new sprite sheet. */
	private Action newSpriteSheetAction = new AbstractAction("New sprite sheet") {

		/** Serial tag. */
		private static final long serialVersionUID = -5802251822349890082L;

		@Override
		public void actionPerformed(ActionEvent e) {

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

	/** Statuses. */
	private Map<String, String> statuses = new HashMap<>();

	/** The status label. */
	private JLabel status;

	/** File chooser for the sprite sheet to edit. */
	private JFileChooser fcSpriteSheet;

	/** The sprite sheet editor panel. */
	private SpriteSetPanel spriteEditor;

	/** Constructor of the workshop frame. */
	public WorkshopUi() {
		super("Game Engine Workshop");

		LOG.debug("Creates the main frame.");

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		setContentPane(mainPanel);

		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);

		JMenu menuSpriteSheet = new JMenu("Sprite Sheet");
		SwingUtils.addMenuItem(newSpriteSheetAction, menuSpriteSheet);
		SwingUtils.addMenuItem(openSpriteSheetAction, menuSpriteSheet);
		menubar.add(menuSpriteSheet);

		JTabbedPane tabPane = new JTabbedPane();

		spriteEditor = new SpriteSetPanel(this);
		tabPane.add(spriteEditor, "SpriteSet editor");
		tabPane.add(new JConfigPanel(), "Configuration editor");
		mainPanel.add(tabPane, BorderLayout.CENTER);

		status = new JLabel();
		mainPanel.add(status, BorderLayout.SOUTH);

		fcSpriteSheet = new JFileChooser(new File("."));

		pack();

		spriteEditor.getSourcePanel().requestFocusInWindow();
	}

	/** Open an existing sprite sheet. */
	protected void openSpriteSheet() {

		int dialogResult = fcSpriteSheet.showOpenDialog(this);

		if (dialogResult == JFileChooser.APPROVE_OPTION) {

			File selectedFile = fcSpriteSheet.getSelectedFile();
			LOG.debug("Opening file " + selectedFile);

			SpriteModel spriteModel;
			try {
				spriteModel = new SpriteModel(selectedFile.getAbsolutePath());
				spriteEditor.setSpriteModel(spriteModel);
			} catch (EngineException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
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
