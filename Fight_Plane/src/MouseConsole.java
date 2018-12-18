import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

public class MouseConsole extends MouseAdapter implements MouseMotionListener {
	
	int x = 0;
	int y = 0;
	Point pClicked = new Point(0, 0);
	Point pMoved = new Point(0, 0);
	GamePanel game;
	
	public MouseConsole(GamePanel game) {
		game.addMouseMotionListener(this);
		game.addMouseListener(this);
		this.game = game;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void mouseMoved(MouseEvent e) {
		pMoved = e.getPoint();
		x = e.getX(); // �ѹ˹����� X
		y = e.getY(); // �ѹ˹����� Y
	}
	
	public void mouseClicked(MouseEvent e) {
		if (!game.play) {
			if (SwingUtilities.isLeftMouseButton(e))
				pClicked = e.getPoint();
		} else {
			if (SwingUtilities.isLeftMouseButton(e))
				game.addBullet(1);
			if (SwingUtilities.isRightMouseButton(e))
				game.addBullet(2);
		}
	}

}
