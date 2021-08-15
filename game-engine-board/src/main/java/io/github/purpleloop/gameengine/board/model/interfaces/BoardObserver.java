package io.github.purpleloop.gameengine.board.model.interfaces;

/** Describes a board game observer. */
public interface BoardObserver {

    /** React to an initialization of the observed game board. */
    void boardInitialized();
    
	/** React to change of the observed game board. */
	void boardChanged();

}
