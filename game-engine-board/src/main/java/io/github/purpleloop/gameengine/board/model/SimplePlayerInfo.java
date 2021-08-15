package io.github.purpleloop.gameengine.board.model;

import io.github.purpleloop.gameengine.board.model.interfaces.IPlayerInfo;

/** A simple implementation of PlayerInfo. */
public class SimplePlayerInfo implements IPlayerInfo {

	/** Player name. */
	private String name;

	/** The player type. */
	private PlayerType type;

	/**
	 * Creates a simple player information.
	 * 
	 * @param name       the player name
	 * @param playerType the player type
	 */
	public SimplePlayerInfo(String name, PlayerType playerType) {
		this.name = name;
		this.type = playerType;
	}

	/**
	 * Creates a simple player information.
	 * 
	 * @deprecated use the fully qualified constructor
	 * 
	 * @param playerType the player type
	 */
	@Deprecated
	public SimplePlayerInfo(PlayerType playerType) {
		this("noname", playerType);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public PlayerType getPlayerType() {
		return type;
	}

	@Override
	public boolean isHumain() {
		return type == PlayerType.HUMAIN;
	}

	/** @return all player names */
	public static String[] getPlayerTypeNames() {
		return PlayerType.names();
	}

	@Override
	public String toString() {
		return name + " (" + type.getDescription() + ")";
	}

}
