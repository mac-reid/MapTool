package UserInterface;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;


public class VideoChatPane {

	private GameContainer game;
	Image fillpattern;
	float scale;
	private int numFaces = 5;
	private int faceSize = 175;
	private Image[] avatars;
	
	public VideoChatPane(GameContainer gc){
		game = gc;
		faceSize = gc.getHeight()/5 - 20;
		avatars = new Image[numFaces];
		try {
		fillpattern = new Image("Resources/parchment.png");
			avatars[0] = new Image("Resources/Tokens/Elf.png").getScaledCopy(faceSize, faceSize);
			avatars[1] = new Image("Resources/Tokens/Hero.png").getScaledCopy(faceSize, faceSize);
			avatars[2] = new Image("Resources/Tokens/Mage.png").getScaledCopy(faceSize, faceSize);
			avatars[3] = new Image("Resources/Tokens/Mystic.png").getScaledCopy(faceSize, faceSize);
			avatars[4] = new Image("Resources/Tokens/Troll.png").getScaledCopy(faceSize, faceSize);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
	
	public void renderVideoPane(int x, int y, int width, int height, Graphics g){
		Image img = fillpattern.getScaledCopy(width, height);
		img.draw(x, y);
		
		//resize the faces
		faceSize = height - 17;
		if (faceSize * 5 + 30 > width){
			faceSize = width/5 - 30;
		}
		addFaceBoxes(x, y, width, height, numFaces, g);
	}
	
	public void addFaceBoxes(int X, int Y, int width, int height, int num, Graphics g){
		
		int leftover = (width - 5) - (faceSize * num);
		leftover = leftover/num;
		int x = X + 5 + leftover/2;
		int y = Y + (height - faceSize)/2;
		for (int i  = 0; i < num; i++){
			g.setColor(Color.black);
			g.drawRect(x - 1, y - 1, faceSize + 1, faceSize + 1);
			g.setColor(Color.darkGray);
			g.fillRect(x, y, faceSize, faceSize);
			avatars[i] = avatars[i].getScaledCopy(faceSize, faceSize);
			avatars[i].draw(x, y);
			x = x + leftover + faceSize;
		}
	}

}
