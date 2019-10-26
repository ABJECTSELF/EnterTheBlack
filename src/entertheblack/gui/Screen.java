package entertheblack.gui;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public abstract class Screen {
	public abstract void keyPressed(KeyEvent e);
	public abstract void keyReleased(KeyEvent e);
	public abstract void paint(Graphics2D g);
	
	public void update() {}
}