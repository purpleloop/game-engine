package io.github.purpleloop.gameengine.board.model;

/** Describes a game observer. */
public interface GameObserver {

    /** React to a change in the observed game. */
	void gameChanged();

}
