package UserInterface;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * The options panel that pops up when you right click on an empty map tile
 * @author Neal
 *
 */
public class MapOptions {

	private int x;
	private int y;
	private final int width = 125;
	private final int height = 60;
	//used to tell the options what is being hovered over
	private int hoverArea = 0;
	private boolean moveBox = false;
	private final int moveBoxWidth = 400;
	private final int moveBoxHeight = 300;
	private int inputX;
	private int inputY;
	
	private final int insertToken = 1;
	private final int move = 2;
	private final int ping = 3;
	private boolean active = false;
	
	private MapPane map;
	
	private JFileChooser fc;
	
	public MapOptions(int x, int y, MapPane map){
		this.x = x;
		this.y = y;
		this.map = map;
		fc = new JFileChooser();
		
		//create the mapBox sizes
		if(map.mappxheight > map.mappxwidth){
			
		}
	}
	
	public void render(Graphics g){
		if (active){
			//draw initial rectangle
			g.setColor(Color.black);
			g.drawRect(x, y, (float)width, (float)height);
			//fill in the background
			g.setColor(Color.lightGray);
			g.fillRect(x+1, y+1, width-1, height - 1);
			//check if a place is hovered over
			switch(hoverArea){
			case 0:
				break;
			case 1:
				g.setColor(Color.blue);
				g.fillRect(x+1, y+1, width-1, height/3);
				break;
			case 2:
				g.setColor(Color.blue);
				g.fillRect(x+1, y+1 + (height/3), width-1, height/3);
				break;
			case 3:
				g.setColor(Color.blue);
				g.fillRect(x+1, y+1 + (2*height/3), width-1, height/3);
			}
			//draw the options
			g.setColor(Color.black);
			//draw the option labels
			g.drawString("Insert Token", x + 3, y + 3);
			g.drawString("Move", x + 3, y + (height/3) + 3);
			g.drawString("Ping", x + 3, y + (2*height/3) + 3);
			if(moveBox){
				showMoveBox(g, x + width + 5, inputY);
			}
		}
	}
	
	public void update(GameContainer gc){
		Input mouse = gc.getInput();
		int mouseX = mouse.getMouseX();
		int mouseY = mouse.getMouseY();
		
		//reset hoverArea
		hoverArea = 0;
		//check if it is in right horizontal place
		if(mouseX >= x && mouseX <= x + width){
			//first item
			if(mouseY >= y && mouseY <= y + (height/3)){
				hoverArea = insertToken;
				//if left-clicked
				if(mouse.isMousePressed(0)){
					fc = new JFileChooser();
					JFrame frame = new JFrame();
					fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					int returnVal = fc.showOpenDialog(frame);
				}
			}
			//second item
			if(mouseY >= y + (height/3) && mouseY <= y + (2*height/3)){
				hoverArea = move;
				if (mouse.isMousePressed(0)){
					moveBox = true;
					inputX = mouseX;
					inputY = mouseY;
				}
			}
			//third item
			if(mouseY >= y + (2*height/3) && mouseY <= y + height){
				hoverArea = ping;

			}
		}

		//actions from a left click
		if(mouse.isMouseButtonDown(0)){
			//outside the main menu
			if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + height){
				if (moveBox){
					//outside the move menu
					if (mouseX < inputX || mouseX > inputX + moveBoxWidth || mouseY < inputY || mouseY > inputY + moveBoxHeight){
						moveBox = false;
					} else {
						//move map to coords
					}
				}
			}
		}
		

	}

	public void showMoveBox(Graphics g, int x, int y){
		//outline
		g.setColor(Color.lightGray);
		g.fillRect(x - 3, y - 3, moveBoxWidth + 6, moveBoxHeight + 6);
		//grey background
		g.setColor(Color.lightGray);
		Image miniMap = map.map.getScaledCopy(moveBoxWidth, moveBoxHeight);
		miniMap.draw(x, y);
		//find the "you are here"
		float currentX = map.mapoffsetx;
		float currentY = map.mapoffsety;
		//put in terms of the move box
		
	}
	
	//GETTERS AND SETTERS
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHoverArea() {
		return hoverArea;
	}

	public void setHoverArea(int hoverArea) {
		this.hoverArea = hoverArea;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
		moveBox = false;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
