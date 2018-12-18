import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy {
	BufferedImage image;
	BufferedImage image1;

	File fImage = new File("enemy1.png");
	File fImage1 = new File("enemy.png");
	int x;
	int y;
	boolean Alive = true;
	double theta;
	AffineTransform at = new AffineTransform();

	public Enemy() throws IOException {
	
			image = ImageIO.read(fImage);
		
		if (image == null)
			image.createGraphics();
		setLocation();
	}

	public Enemy(int i) throws IOException {
			image = ImageIO.read(fImage1);
		
		if (image == null)
			image.createGraphics();

		setLocation();
	}

	public BufferedImage getImage() {
		return image;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, image.getWidth() - 5, image.getHeight() - 5);
	}

	public void draw(Graphics2D g2d) {
		at.setToIdentity();
		at.rotate(theta, x + 50, y + 40);
		at.translate(x, y);
		g2d.drawImage(image, at, null);
	}

	public void setLocation() {
		int i, j;
		i = (int)(Math.random()*2 +1);
		j = (int)(Math.random()*2 +1);
		if(i == 1) x = (int)(Math.random()*220 + 1);
		else x = (int)(Math.random()*220 + 811) ;
		if(j == 1) y = (int)(Math.random()*115 + 1);
		else y = (int)(Math.random()*115 + 496) ;
		
		
	}
}
