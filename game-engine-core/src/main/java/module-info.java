/** The core module of the game engine. */
module io.github.purpleloop.gameengine.core {

	exports io.github.purpleloop.gameengine.core.interfaces;
	exports io.github.purpleloop.gameengine.core.config;
	exports io.github.purpleloop.gameengine.core.fsm;
	exports io.github.purpleloop.gameengine.core.sound.interfaces;
	exports io.github.purpleloop.gameengine.core.util;
	
	requires transitive io.github.purpleloop.commons;
	requires transitive io.github.purpleloop.commons.swing;
	requires java.xml;
	requires org.apache.commons.logging;
	requires org.apache.commons.lang3;
}
