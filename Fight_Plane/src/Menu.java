import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Menu {
	String projectName = "Fight Plane";
	String about = "About"; String help = "Help"; String exit = "Exit";
	String play = "Play";
	boolean[] activated = new boolean[7];
	Point[] point = new Point[7];
	int button_w = 150;
	int button_h = 50;
	GamePanel game;
	Rectangle[] rectangle = new Rectangle[7];
	boolean clicked;
	
	public Menu(GamePanel game) {
		
		for (int i = 0; i < 4; i++) {
			point[i] = new Point(60 + i * 270, 500);
			rectangle[i] = new Rectangle(point[i], new Dimension(button_w,
					button_h));
		}

		for (int i = 0; i < 3; i++) {
			point[i + 4] = new Point(60, 281 + 81 * i);
			rectangle[i + 4] = new Rectangle(point[i+4], new Dimension(button_w,
					button_h));
		}
		this.game = game;
	}
	
	public void draw(Graphics g) {
		g.drawImage(game.backGround, 0, 0, null);
		g.drawImage(game.opacity, 0, 0, null);
		g.setColor(Color.BLUE);
		g.setFont(game.font);
		g.setColor(Color.WHITE);
		g.setFont(game.font);
		g.drawString(projectName, 400, 135);

		if (clicked) {
			g.setColor(Color.WHITE);
			g.drawString("New Game", 93, 400);
		}
		g.setColor(Color.white);
		g.drawString("Play", 90, 535);
		g.drawString("Help", 365, 535);
		g.drawString("About", 630, 535);
		g.drawString("Exit", 905, 535);
	}
}
