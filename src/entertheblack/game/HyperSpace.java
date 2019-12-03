package entertheblack.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import entertheblack.gui.Screen;
import entertheblack.menu.Assets;
import entertheblack.menu.MainMenu;
import entertheblack.storage.ShipData;

public class HyperSpace extends Screen {
	StarMap map;
	boolean move = false;
	boolean left = false;
	boolean right = false;
	double xShip, yShip, alphaShip, vmax, turn;
	Image shipImg;
	ShipData sd;
	Star cameFrom;
	
	ArrayList<Fleet> fleets;
	
	public HyperSpace(StarMap map, ShipData mainShip, Star current) {
		this.map = map;
		shipImg = mainShip.img;
		vmax = mainShip.vmax;
		turn = mainShip.turnRate;
		xShip = current.x;
		yShip = current.y;
		cameFrom = current;
		sd = mainShip;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == Assets.Controls[1]) {
			right = true;
		}
		if (e.getKeyCode() == Assets.Controls[0]) {
			left = true;
		}
		if (e.getKeyCode() == Assets.Controls[4]) {
			move = true;
		}
		if(e.getKeyChar() == 'm') {
			Assets.screen = Assets.curWorld.map;
			Assets.curWorld.map.activate(this);
		}
		if(e.getKeyCode() == 27) {
			Assets.screen = new MainMenu();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == Assets.Controls[1]) {
			right = false;
		}
		if (e.getKeyCode() == Assets.Controls[0]) {
			left = false;
		}
		if (e.getKeyCode() == Assets.Controls[4]) {
			move = false;
		}
	}

	@Override
	public void mouseUpdate(int x, int y, boolean pressed) {} // Unused.
	
	@Override
	public void update() {
		if(move) {
			xShip += -vmax*Math.sin(alphaShip);
			yShip += vmax*Math.cos(alphaShip);
		}
		if(left) {
			alphaShip -= turn;
		}
		if(right) {
			alphaShip += turn;
		}
		
		// Check if the ship is entering a star system:
		for(Star system : map.systems) {
			double deltaX = system.x + xShip;
			double deltaY = system.y + yShip;
			if(Math.sqrt(deltaX*deltaX+deltaY*deltaY) < system.planets[0].r/2) {
				if(system == cameFrom) // Don't enter system instantly again after leaving.
					continue;
				Assets.screen = system;
				system.activate(sd);
			}
			else if(system == cameFrom) {
				// When the player left proximity of the star allow him to land there again:
				cameFrom = null;
			}
		}
	}

	@Override
	public void paint(Graphics2D g) {
		// TODO: Draw some better background:
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1920, 1080);
		g.scale(0.5, 0.5);
		g.translate(xShip+960, yShip+540);
		for(Star star : map.systems) {
			g.drawImage(star.planets[0].img, (int)(star.x-star.planets[0].r/2), (int)(star.y-star.planets[0].r/2), (int)(star.planets[0].r), (int)(star.planets[0].r), null);
		}
		g.translate(-xShip, -yShip);
		g.rotate(alphaShip);
		g.drawImage(shipImg, -50, -50, 100, 100, null);
		g.rotate(-alphaShip);
		g.translate(-960, -540);
		g.scale(2, 2);
	}
	
}