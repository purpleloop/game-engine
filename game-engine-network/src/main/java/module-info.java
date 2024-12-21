/** The network module of the game engine. */
module io.github.purpleloop.gameengine.network {

	exports io.github.purpleloop.gameengine.network;
	exports io.github.purpleloop.gameengine.network.connection;
	exports io.github.purpleloop.gameengine.network.exception;
	exports io.github.purpleloop.gameengine.network.message;
    
    requires org.apache.commons.lang3;
    requires org.apache.commons.logging;
    requires transitive io.github.purpleloop.commons;
	requires io.github.purpleloop.gameengine.core;
}
