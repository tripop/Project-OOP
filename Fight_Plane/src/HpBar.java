import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HpBar {
	BufferedImage image1;
	BufferedImage image2;
	BufferedImage image;
	File fImage1 = new File("heart1.png");
	File fImage2 = new File("heart2.png");
	int x;
	
	boolean health = false;
	
	public HpBar() throws IOException{
			image1 = ImageIO.read(fImage1);
			image2 = ImageIO.read(fImage2);
		
	}
	
	public BufferedImage getImage(){
		return image;
		}
	public void setImage(){
		image = image2;
	}
	public void setHealth(){
		image = image1;
	}
}
