package io.github.purpleloop.gameengine.board.model.interfaces;

import io.github.purpleloop.gameengine.board.model.PlayerType;

/** Models information about a player. */
public interface IPlayerInfo {
	
	/**  @return Is the player human ? */
	boolean isHumain();
	
	/** @return type of player. */
	PlayerType getPlayerType();
	
	/**  @return Player name. */
	String getName();

}
