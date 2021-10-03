/** Module definition for the sound engine. */
module io.github.purpleloop.gameengine.sound {

	exports io.github.purpleloop.gameengine.sound;
	
	requires commons.logging;
	requires org.apache.logging.log4j;
    requires io.github.purpleloop.commons;
    requires io.github.purpleloop.gameengine.core;
    requires transitive com.jcraft.jorbis;
}
