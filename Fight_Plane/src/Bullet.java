import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet {
	BufferedImage image;
	File fImage = new File("bullet2.png");
	File fImage1 = new File("bullet3.png");
	int x;
	int y;
	boolean fired = false;
	double theta;
	double cos;
	double sin;
	
	public Bullet(int x, int y , double theta) throws IOException{

		image = ImageIO.read(fImage);
		
		this.x = x;
		this.y = y;
		this.theta = theta;
	}
	public Bullet(int i) throws IOException{
			image = ImageIO.read(fImage1);
	}
	
	public BufferedImage getImage(){
		return image;
		}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, image.getWidth(), image.getHeight() );
	}
}
