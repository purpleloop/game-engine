package io.github.purpleloop.gameengine.board.model;

import java.util.Arrays;

/** The player types. */
public enum PlayerType {

	/** No player. */
	NONE("None"),

	/** Player is human. */
	HUMAIN("Human"),

	/** Simulated player. */
	CPU("Computer");

	/** Player type description. */
	private String description;

	/**
	 * Creates a new player type.
	 * 
	 * @param description The player type description.
	 */
	PlayerType(String description) {
		this.description = description;
	}

	/** @return player types names */
	public static String[] names() {
		return Arrays.stream(values()).map(type -> type.description).toArray(size -> new String[size]);
	}

	/** @return player type description */
	public String getDescription() {
		return description;
	}

}
