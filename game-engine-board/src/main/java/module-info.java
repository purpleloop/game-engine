/** The board game module. */
module io.github.purpleloop.gameengine.board {

	exports io.github.purpleloop.gameengine.board.model;
	exports io.github.purpleloop.gameengine.board.model.interfaces;
	exports io.github.purpleloop.gameengine.board.model.exception;
	exports io.github.purpleloop.gameengine.board.model.net;
    
    requires java.xml;
    requires org.apache.commons.lang3;
    requires commons.logging;
    requires transitive io.github.purpleloop.commons;
    requires transitive io.github.purpleloop.commons.swing;
    requires transitive io.github.purpleloop.gameengine.network;
	requires io.github.purpleloop.gameengine.core;
	
}
