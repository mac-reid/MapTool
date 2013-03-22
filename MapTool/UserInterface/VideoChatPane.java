import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;


public class VideoChatPane {

	private GameContainer game;
	Image fillpattern;
	Image face;
	float scale;
	private int numFaces = 5;
	final int faceSize = 175;
	
	public VideoChatPane(GameContainer gc){
		game = gc;
		try {
			fillpattern = new Image("Resources/chatpattern.png");
			face = new Image("Resources/goomba.png");
			scale = ((float)faceSize)/(float)face.getWidth();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void renderVideoPane(int x, int y, int width, int height, Graphics g){
		g.fillRect((float)x, (float)y, (float)width, (float)height, fillpattern, 0, 0);
		
		addFaceBoxes(x, y, width, height, numFaces, g);
	}
	
	public void addFaceBoxes(int X, int Y, int width, int height, int num, Graphics g){
		int leftover = (width - 5) % (faceSize * num);
		leftover = leftover/num;
		int x = X + 5 + leftover/2;
		int y = Y + (height % faceSize)/2;
		for (int i  = 0; i < num; i++){
			g.setColor(Color.black);
			g.drawRect(x - 1, y - 1, faceSize + 2, faceSize + 2);
			face.draw(x, y, scale);
			x = x + leftover + faceSize;
		}
	}

}
