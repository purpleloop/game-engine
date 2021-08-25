package io.github.purpleloop.gameengine.workshop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.github.purpleloop.commons.swing.SwingUtils;
import io.github.purpleloop.gameengine.core.config.ClassRole;
import io.github.purpleloop.gameengine.core.config.GameConfig;
import io.github.purpleloop.gameengine.core.config.LocalDataFileProvider;
import io.github.purpleloop.gameengine.core.util.EngineException;

/** The panel used to edit the configuration. */
public class JConfigPanel extends JPanel implements ActionListener {

	/**
	 * A panel used to associate a Java class to a given role of the game engine.
	 */
	public class ClassRolePanel extends JPanel {

		/** Serial tag. */
		private static final long serialVersionUID = -2740346971561199452L;

		/** The text field for the class name. */
		private JTextField tfClassName;

		/**
		 * Create a class role panel.
		 * 
		 * @param title the title of the panel
		 * @param role  the associated class role
		 */
		public ClassRolePanel(String title, ClassRole role) {
			add(new JLabel(title));
			tfClassName = new JTextField(40);
			add(tfClassName);
		}

		/**
		 * @param className the class name
		 */
		public void setValue(String className) {
			tfClassName.setText(className);
		}

	}

	/** Class logger. */
	private static final Log LOG = LogFactory.getLog(JConfigPanel.class);

	/** Command to create an new game. */
	private static final String CMD_NEW_GAME = "CMD_NEW_GAME";

	/** Command to create open a game. */
	private static final String CMD_OPEN_GAME = "CMD_OPEN_GAME";

	/** Serial tag. */
	private static final long serialVersionUID = -4599042381744589721L;

	/** Class-role panels. */
	private HashMap<ClassRole, ClassRolePanel> classRolePanels;

	/** The game configuration. */
	private GameConfig gameConfig;

	/** Create the configuration panel. */
	public JConfigPanel() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		classRolePanels = new HashMap<>();

		for (ClassRole classRole : ClassRole.values()) {

			ClassRolePanel classRolePanel = new ClassRolePanel(classRole.name(), ClassRole.ENVIRONMENT);

			classRolePanels.put(classRole, classRolePanel);

			add(classRolePanel);
		}

		SwingUtils.createButton("New", CMD_NEW_GAME, this, this, true);
		SwingUtils.createButton("Open", CMD_OPEN_GAME, this, this, true);
		
	}

	/**
	 * Set the game configuration to edit.
	 * 
	 * @param gameConfig the game configuration
	 */
	public void setConfig(GameConfig gameConfig) {

		for (ClassRole classRole : ClassRole.values()) {

			ClassRolePanel classRolePanel = classRolePanels.get(classRole);
			String value = null;
			try {
				value = gameConfig.getClassName(classRole);
			} catch (EngineException e) {
				LOG.error("failed to get the value for class role " + classRole, e);
			}
			classRolePanel.setValue(value);
		}

	}

	/** Create a new configuration. */
	private void newConfig() {
		LOG.debug("Creating a new configuration");
		gameConfig = new GameConfig();
		setConfig(gameConfig);
	}

	/** Open an existing configuration.
	 * @param configFile the configuration file
	 */
	private void openConfigFile(File configFile) {
		LOG.debug("Opening the configuration file " + configFile);
		try {
			gameConfig = GameConfig.parse(new LocalDataFileProvider(), configFile.getAbsolutePath());

			setConfig(gameConfig);

		} catch (EngineException e1) {
			LOG.error("Failed to read the configuration file " + configFile);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (CMD_NEW_GAME.equals(cmd)) {

			newConfig();

		} else if (CMD_OPEN_GAME.equals(cmd)) {

			JFileChooser jfc = new JFileChooser(new File("."));

			int result = jfc.showOpenDialog(this);

			if (result == JFileChooser.APPROVE_OPTION) {
				File configFile = jfc.getSelectedFile();

				openConfigFile(configFile);

			}

		} else {
			LOG.error("Unknown command " + cmd);
		}

	}

}
