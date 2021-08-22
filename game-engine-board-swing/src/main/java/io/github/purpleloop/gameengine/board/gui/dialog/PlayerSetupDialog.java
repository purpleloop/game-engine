package io.github.purpleloop.gameengine.board.gui.dialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import io.github.purpleloop.commons.swing.SwingUtils;
import io.github.purpleloop.gameengine.board.model.AbstractBoardGame;
import io.github.purpleloop.gameengine.board.model.PlayerType;
import io.github.purpleloop.gameengine.board.model.SimplePlayerInfo;
import io.github.purpleloop.gameengine.board.model.interfaces.IPlayerInfo;
import io.github.purpleloop.gameengine.core.util.Message;

/** A dialog box for configuring players types. */
public class PlayerSetupDialog extends JDialog implements ActionListener {

	/** Serial tag. */
	private static final long serialVersionUID = 3421711235147631780L;

	/** Validation command. */
	public static final String CMD_OK = "CMD_OK";

	/** Cancel command. */
	public static final String CMD_CANCEL = "CMD_CANCEL";

	/** Maximal length for player name. */
	private static final int PLAYER_NAME_MAX_LENGTH = 20;

	/** Information on players. */
	private List<IPlayerInfo> players;

	/** Minimal number of players. */
	private int minPlayers;

	/** Maximal number of players. */
	private int maxPlayers;

	/** Text fields for player names. */
	private JTextField[] tfPlayerName;

	/** Combo boxes for selecting player types. */
	private JComboBox<String>[] cbPlayerType;

	/** Is the form valid ? */
	private boolean formValid;

	/** Constructor of the dialog box.
	 * @param owner owner frame
	 * @param game the board game for playing configuration
	 */
	@SuppressWarnings("unchecked")
	public PlayerSetupDialog(JFrame owner, AbstractBoardGame game) {

		super(owner, true);
		this.formValid = false;

		// Get the minimum and the maximum number of authorized number of players
		this.minPlayers = game.getMinPlayers();
		this.maxPlayers = game.getMaxPlayers();

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		setContentPane(mainPanel);

		// Prepare players fields
		tfPlayerName = new JTextField[maxPlayers];
		cbPlayerType = new JComboBox[maxPlayers];

		JPanel playerDefPanel = new JPanel();
		playerDefPanel.setLayout(new GridLayout(0, 3));

		for (int playerIndex = 0; playerIndex < maxPlayers; playerIndex++) {

			String role = game.getRoleName(playerIndex);
			if (role == null || role.equals("")) {
				role = "Player " + playerIndex;
			}
			JLabel labPlayer = new JLabel(role);

			playerDefPanel.add(labPlayer);

			tfPlayerName[playerIndex] = new JTextField(PLAYER_NAME_MAX_LENGTH);
			playerDefPanel.add(tfPlayerName[playerIndex]);

			cbPlayerType[playerIndex] = new JComboBox<>(PlayerType.names());

			playerDefPanel.add(cbPlayerType[playerIndex]);
		}

		mainPanel.add(playerDefPanel, BorderLayout.CENTER);

		JPanel cmdPanel = new JPanel();		
		SwingUtils.createButton(Message.getMessage("action.ok"), CMD_OK, cmdPanel, this, true);
		SwingUtils.createButton(Message.getMessage("action.cancel"), CMD_CANCEL, cmdPanel, this, true);
		mainPanel.add(cmdPanel, BorderLayout.SOUTH);

		setList(game.getPlayers());

		pack();

	}

	/** Prepare the list of players.
	 * @param playerList the list of players
	 */
	private void setList(List<IPlayerInfo> playerList) {
		this.players = new ArrayList<>();
		for (IPlayerInfo playerInfo : playerList) {
			players.add(new SimplePlayerInfo(playerInfo.getName(), playerInfo.getPlayerType()));
		}

		for (int playerIndex = 0; playerIndex < maxPlayers; playerIndex++) {
			if (playerIndex < players.size()) {

				IPlayerInfo pl = players.get(playerIndex);
				tfPlayerName[playerIndex].setText(pl.getName());
				cbPlayerType[playerIndex].setSelectedIndex(pl.getPlayerType().ordinal());

			} else {
				tfPlayerName[playerIndex].setText("");
				cbPlayerType[playerIndex].setSelectedIndex(PlayerType.NONE.ordinal());
			}
		}
	}

	/** @return the list of players */
	public List<IPlayerInfo> getPlayerInfoList() {
		return this.players;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		if (command == null) {
			return;
		}
		if (command.equals(CMD_OK)) {

			int activePlayers = countActivePlayers();

			if (activePlayers < minPlayers || activePlayers > maxPlayers) {

				JOptionPane.showMessageDialog(this, Message.getMessage("error.players.count", minPlayers, maxPlayers));

			} else {

				updateModel();
				this.formValid = true;
				setVisible(false);
			}

		} else if (command.equals(CMD_CANCEL)) {

			setVisible(false);
		} else {
			JOptionPane.showMessageDialog(this, Message.getMessage("command.unknown", command),
					Message.getMessage("error.dialog.title"), JOptionPane.ERROR_MESSAGE);

		}

	}

	/** @return the number of active players */
	private int countActivePlayers() {

		int activePlayers = 0;

		int type = 0;
		for (int playerIndex = 0; playerIndex < maxPlayers; playerIndex++) {

			type = cbPlayerType[playerIndex].getSelectedIndex();

			if (type != PlayerType.NONE.ordinal()) {
				activePlayers++;
			}
		}
		return activePlayers;
	}

	/** Update the model according to the form. */
	private void updateModel() {

		this.players.clear();

		int type;
		for (int playerIndex = 0; playerIndex < maxPlayers; playerIndex++) {

			type = cbPlayerType[playerIndex].getSelectedIndex();

			if (type != PlayerType.NONE.ordinal()) {

				players.add(new SimplePlayerInfo(tfPlayerName[playerIndex].getText(),
						PlayerType.values()[cbPlayerType[playerIndex].getSelectedIndex()]));
			}
		}

	}
	
	/** @return true if the form valid, false otherwise */
	public boolean isFormValid() {
		return formValid;
	}

}
