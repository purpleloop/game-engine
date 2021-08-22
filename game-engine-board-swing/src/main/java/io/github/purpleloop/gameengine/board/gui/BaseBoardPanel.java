package io.github.purpleloop.gameengine.board.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import io.github.purpleloop.gameengine.board.model.interfaces.BoardObserver;

/** A base for board panels. */
public class BaseBoardPanel extends JPanel implements BoardObserver, MouseListener, MouseMotionListener {

    /** Serial tag. */
	private static final long serialVersionUID = -7829415309287177210L;

	@Override
    public void mouseClicked(MouseEvent e) {
    }

	@Override
    public void mousePressed(MouseEvent e) {
    }

	@Override
    public void mouseReleased(MouseEvent e) {
    }

	@Override
    public void mouseEntered(MouseEvent e) {
    }

	@Override
    public void mouseExited(MouseEvent e) {
    }

	@Override
    public void mouseDragged(MouseEvent e) {
    }

	@Override
    public void mouseMoved(MouseEvent e) {
    }

	@Override
    public void boardChanged() {
        repaint();
    }   
    
	@Override
    public void boardInitialized() {
        repaint();
    }

}
