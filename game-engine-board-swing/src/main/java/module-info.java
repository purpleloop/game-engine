/** The Swing board game module. */
module io.github.purpleloop.gameengine.board.swing {

    exports io.github.purpleloop.gameengine.board.gui;
    exports io.github.purpleloop.gameengine.board.gui.dialog;
    exports io.github.purpleloop.gameengine.board.gui.net;

    requires org.apache.commons.logging;
    requires transitive io.github.purpleloop.commons;
    requires io.github.purpleloop.gameengine.core;
    requires io.github.purpleloop.gameengine.sound;
    requires io.github.purpleloop.gameengine.board;
}
