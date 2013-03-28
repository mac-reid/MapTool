package UserInterface;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class testMapTool {

	private int x;
	private int y;
	public testMapTool(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void render(Graphics g){
		g.setColor(Color.blue);
		g.drawString("Is this painting at " + x + ", " + y, x, y);
	}
	
	public void update(){
		
	}
}
