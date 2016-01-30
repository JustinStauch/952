package ca.mcgill.mail.stauch.justin.ninefivetwo.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseClickResetter implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (NineFiveTwo.isEndOfTrick()) {
			NineFiveTwo.startNextTrick();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

}