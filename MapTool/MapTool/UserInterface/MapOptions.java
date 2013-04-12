package UserInterface;

import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
	private boolean showminimap = false;
	private final float minimapsize = 400;
	private float miniMapScale = 0;
	private int minimapwidth = 400;
	private int minimapheight = 300;
	public int minimapx = 0;
	public int minimapy = 0;
	//ints used for the size of the little rectangle
	private float portHeight, portWidth;
	private float portX, portY;
	private boolean showPort = false;
	
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
		if(map.pxSizeY > map.pxSizeX){
			miniMapScale = minimapsize/map.pxSizeY;
			minimapheight = (int) (miniMapScale * map.pxSizeY);
			minimapwidth = (int) (miniMapScale * map.pxSizeX);
		} else {
			miniMapScale = minimapsize/map.pxSizeX;
			minimapheight = (int) (miniMapScale * map.pxSizeY);
			minimapwidth = (int) (miniMapScale * map.pxSizeX);
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
			g.drawString("Change Map", x + 3, y + (2*height/3) + 3);
			if(showminimap){
				//check to make sure map is kept on screen (11 is size of buffers and borders)
				if(x + 11 + minimapwidth + width < (map.tileSizeX * 48)) {
					minimapx = x + width + 5;
					if(y + height + minimapheight + 10 > map.tileSizeY*48) {
						minimapy = y - minimapheight - 5;
					} else {
						minimapy = y + height + 5;
					}
					showMiniMap(g, minimapx, minimapy);
					if(showPort) {
						g.setColor(Color.red);
						g.drawRect(portX, portY, portWidth, portHeight);
					}
				} else {
					minimapx = (map.tileSizeX * 48) - minimapwidth - 5;
					//make sure I dont draw too low
					if(y + height + minimapheight + 10 > map.tileSizeY*48) {
						minimapy = y - minimapheight - 5;
					} else {
						minimapy = y + height + 5;
					}
					showMiniMap(g, minimapx, minimapy);
					if(showPort) {
						g.setColor(Color.red);
						g.drawRect(portX, portY, portWidth, portHeight);
					}
				}
			}
		}
	}
	
	public void update(GameContainer gc){
		Input input = gc.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();

		
		//check mouse hover area while 
		if(showminimap){
			portWidth = miniMapScale * 48 *  map.tileSizeX;
			portHeight = miniMapScale * 48 * map.tileSizeY;
			//check mouse location
			if(mouseX >= minimapx && mouseX <= minimapx + minimapwidth 
					&& mouseY >= minimapy && mouseY <= minimapy + minimapheight){
				//create the bounds for the viewport rectangle
				portX = (float) (minimapx + (mouseX - minimapx) - .5*portWidth);
				portY = (float) (minimapy + (mouseY - minimapy) - .5*portHeight);
				//make sure the port does not extend past the minimap
				if(portX < minimapx) portX = minimapx;
				if(portX + portWidth > minimapx + minimapwidth) portX = minimapx + minimapwidth - portWidth + 1;
				if(portY < minimapy) portY = minimapy;
				if(portY + portHeight > minimapy + minimapheight) portY = minimapy + minimapheight - portHeight + 1;
				showPort = true;
				//if the mouse is clicked
				if(input.isMousePressed(0)){
					map.move((portX - minimapx)/(48*miniMapScale), (portY - minimapy)/(48*miniMapScale));
					setActive(false);
					resetPort();
				}
			}
		}
		
		//reset hoverArea
		hoverArea = 0;
		//check if it is over the right click menu
		if(mouseX >= x && mouseX <= x + width && !showminimap){
			//first item
			if(mouseY >= y && mouseY <= y + (height/3)){
				hoverArea = insertToken;
				//if left-clicked
				if(input.isMousePressed(0)){
					
					// Check it out, I made a file selector and it sorta works!
					//the variables x and y are set when the options menu is started. should solve insert token issues
					map.selectToken(x - 10, y - 10);
					setActive(false);
					
				} 
			}
			//second item
			if(mouseY >= y + (height/3) && mouseY <= y + (2*height/3)){
				hoverArea = move;
				if (input.isMousePressed(0)){
					showminimap = true;
					inputX = mouseX;
					inputY = mouseY;
				}
			}
			//third item
			if(mouseY >= y + (2*height/3) && mouseY <= y + height){
				hoverArea = ping;
				//if the mouse is pressed
				if(input.isMousePressed(0)){
					map.selectMap();
					setActive(false);
				}
			}
		}

		//outside the main menu
		if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + height){
			if(input.isMousePressed(0)){
				if (showminimap){
					//outside the move menu
					if (mouseX < inputX || mouseX > inputX + minimapwidth || mouseY < inputY || mouseY > inputY + minimapheight){
						showminimap = false;
						setActive(false);
					} else {
					}
				}
				setActive(false);
			}
			if(input.isMousePressed(1)){
				showminimap = false;
				setY(mouseY);
				setX(mouseX);
				resetPort();
			}
		}
	}

	/**
	 * Used to get the port (red box) off of screen
	 */
	public void resetPort(){
		portX = -1000;
		portY = -1000;
	}
	
	public void showMiniMap(Graphics g, int x, int y){
		//outline
		g.setColor(Color.lightGray);
		g.fillRect(x - 3, y - 3, minimapwidth + 6, minimapheight + 6);
		//grey background
		g.setColor(Color.lightGray);
		Image miniMap = map.map.getScaledCopy(miniMapScale);
		miniMap.draw(x, y);
		//place a red dot for each token
		for(Token token : map.tokens){
			float currentX = token.getX() * 48 * miniMapScale;
			float currentY = token.getY() * 48 * miniMapScale;
			//put in terms of the move box
			g.setColor(Color.red);
			g.fillOval(x + currentX + (float)(12 * miniMapScale), y + currentY + (float)(12 * miniMapScale), 5, 5);
		}
	}

	//GETTERS AND SETTERS
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		if(this.x + width > 48*map.tileSizeX){
			this.x = x - 5 - width;
		}
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
		showminimap = false;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
