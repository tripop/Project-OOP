import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Result {
	boolean[] activated = new boolean[2];
	Point[] point = new Point[2];
	BufferedImage image;
	File imageF = new File("button.jpg");
	int button_w = 150;
	int button_h = 50;
	GamePanel game;
	Rectangle[] rectangle = new Rectangle[2];
	boolean clicked;
	boolean won = false;
	boolean painted = false;
	Random random;
	int score = 0;
	long tStart = System.currentTimeMillis();
	int oldScore = 0;

	public Result(GamePanel game) throws IOException {
		
		for (int i = 0; i < 2; i++) {
			point[i] = new Point(465, 335 + 50*i);
			rectangle[i] = new Rectangle(point[i], new Dimension(button_w,
					button_h));
		}
		image = ImageIO.read(imageF);
		this.game = game;
	}

	public void draw(Graphics g2d) throws IOException {
		g2d.drawImage(game.backGround, 0, 0, null);
		g2d.drawImage(game.opacity, 0, 0, null);
		if(won){
			
			
			g2d.setColor(Color.GREEN);
			g2d.setFont(game.font);
			g2d.drawString("YOUR SCORE = ", 350, 100);
			g2d.setColor(Color.WHITE);
			g2d.setFont(game.font);
			g2d.drawString((String.valueOf(GamePanel.score)), 600, 100);
		}

		g2d.setColor(Color.white);
		g2d.drawString("Menu", 465, 335);
		g2d.drawString("Exit", 465, 435);
	}

	public Rectangle getBounds(int i) {
		return rectangle[i];
	}

}
