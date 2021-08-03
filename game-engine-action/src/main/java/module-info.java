/** The action game module. */
module io.github.purpleloop.gameengine.action {
	
	exports io.github.purpleloop.gameengine.action.model.actions;
	exports io.github.purpleloop.gameengine.action.model.environment;
    exports io.github.purpleloop.gameengine.action.model.interfaces;
    exports io.github.purpleloop.gameengine.action.model.events; 
    exports io.github.purpleloop.gameengine.action.model.level;
    exports io.github.purpleloop.gameengine.action.model.objects;
    exports io.github.purpleloop.gameengine.action.model.session;
    exports io.github.purpleloop.gameengine.action.model;    
    
    requires java.xml;
    requires commons.logging;
    requires org.apache.commons.lang3;
    requires transitive io.github.purpleloop.commons;
    requires transitive io.github.purpleloop.commons.swing;
    requires transitive io.github.purpleloop.gameengine.core;    
}
