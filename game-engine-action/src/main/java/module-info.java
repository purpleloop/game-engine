/** The action game module. */
module game.engine.action {
	
	exports io.github.purpleloop.gameengine.action.model.actions;
	exports io.github.purpleloop.gameengine.action.model.environment;
    exports io.github.purpleloop.gameengine.action.model.interfaces;
    exports io.github.purpleloop.gameengine.action.model.events; 
    exports io.github.purpleloop.gameengine.action.model.level;
    exports io.github.purpleloop.gameengine.action.model.objects;
    exports io.github.purpleloop.gameengine.action.model.session;
    exports io.github.purpleloop.gameengine.action.model;    
    
    requires transitive io.github.purpleloop.commons;
    requires transitive io.github.purpleloop.commons.swing;
    requires transitive io.github.purpleloop.gameengine.core;    
	requires commons.logging;
	requires java.xml;
	requires org.apache.commons.lang3;
}
