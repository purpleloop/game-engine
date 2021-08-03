/** The action game module. */
module io.github.purpleloop.gameengine.action.swing {
    
	exports io.github.purpleloop.gameengine.action.gui;
	exports io.github.purpleloop.gameengine.action.gui.keyboard to io.github.purpleloop.commons;
	
	requires commons.logging;
	requires transitive io.github.purpleloop.commons.swing;
	requires transitive io.github.purpleloop.gameengine.core;
	requires transitive io.github.purpleloop.gameengine.sound;
	requires transitive io.github.purpleloop.gameengine.action;
}
