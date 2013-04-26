package UserInterface;

import java.awt.Font;
import java.awt.Toolkit;
import java.io.InputStream;
import java.util.Date;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Shape;

import Backend.Control;

/**
 * This class is the info Pane that will rest above the chatPane
 * @author Neal
 *
 */
public class InfoPane {
	Font font;
	TrueTypeFont titleFont;
	TrueTypeFont subFont;
	//Size constraints for panel
	float panelX, panelY;
	float sizeX, sizeY;
	//Global Buffers
	final int edgeBuffer = 10;
	final int itemBuffer = 5;
	final int picSize = 70;
	//String info
	String hostname, gameName, time;
	int numPlayers;
	
	//background
	Image background;
	Image icons;
	//the selected token, if there is one
	Token token;
	Control control;
	
	/**
	 * Constructor to initialize necessary variables
	 */
	public InfoPane(String pGameName, float pX, float pY, float width, float height, String host, Control control){
		try {
			background = new Image("Resources/chatpattern.png");
			icons = new Image("Resources/icons.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.control = control;
		gameName = pGameName;
		hostname = host;
		time = getTime();
		//set size
		panelX = pX;
		panelY = pY;
		sizeX = width;
		sizeY = height;
		token = null;
		//create font
		font = null;
		font = new Font("monotype corsiva", Font.PLAIN, 24);
		titleFont = new TrueTypeFont(font, true);
		subFont = new TrueTypeFont(new Font("serif", Font.PLAIN, 13), true);
	}
	
	/**
	 * Gets the current time
	 */
	public String getTime(){
		long hours, minutes;
		long seconds = System.currentTimeMillis()/1000;
		//hard coded time zone stuff
		int secondOffset = 4*3600;
		hours = ((seconds - secondOffset) / 3600 ) % 12;
		if(hours == 0){
			hours = 12;
		}
		minutes = (seconds / 60) % 60;
		if (minutes < 10) {
			return ("" + hours + ":0" + minutes);
		}
		return ("" + hours +  ":" + minutes);
	}
	
	/**
	 * Update method, doesnt need to much info, as most of the calls will be made from mapPane
	 */
	public void update(GameContainer gc, Token selection){
		token = selection;
		time = getTime();
		int mouseX = gc.getInput().getMouseX();
		int mouseY = gc.getInput().getMouseY();
		//if mouse over the icons
		if(mouseX >= panelX && mouseY >= panelY + picSize && mouseY <= panelY + sizeY){
			//if mouse is pressed
			if(gc.getInput().isMousePressed(0)){
				//first row
				if(mouseY >= panelY + picSize + itemBuffer * 2 && mouseY <= panelY + picSize + itemBuffer * 2 + 15){
					for(int i = 0; i < 4; i++){
						int leftSide = (int) (panelX + itemBuffer + (60 * i));
						if(mouseX >= leftSide && mouseX <= leftSide + 15){
							token.status[i] = !token.status[i];
							
						}
					}
				}
				//second row
				if(mouseY >= panelY + picSize + itemBuffer * 2 + 45 && mouseY <= panelY + picSize + itemBuffer * 2 + 15 + 45){
					for(int i = 0; i < 4; i++){
						int leftSide = (int) (panelX + itemBuffer + (60 * i));
						if(mouseX >= leftSide && mouseX <= leftSide + 15){
							token.status[i + 4] = !token.status[i + 4];
						}
					}
				}
			}
		}
	}
	
	/**
	 * Render method, draws everything on the screen
	 */
	public void render(float x, float y, int width, int height, Graphics g){
		background.draw(x, y, width, height);
		panelX = x;
		panelY = y;
		sizeX = width;
		sizeY = height;
		if(token != null){
			token.getImage().getScaledCopy(picSize, picSize).draw(x + itemBuffer, y + itemBuffer);
			g.setColor(Color.white);
			//draw token name (centered)
			titleFont.drawString(x + picSize + (width - picSize - titleFont.getWidth(token.getName()))/2, y + itemBuffer, token.getName(), Color.white);
			icons.draw(x, y + picSize + itemBuffer * 2);
			radioButtons(token, x, y + picSize + itemBuffer * 2, icons.getWidth(), icons.getHeight(), g);
		} else {
			String testTitle = "TEST TITLE";
			int strWidth = titleFont.getWidth(testTitle);
			//draw title (centered)
			titleFont.drawString(x + (width - strWidth)/2, y + itemBuffer, testTitle, Color.white);
			//draw time (centered)
			titleFont.drawString(x + (width - titleFont.getWidth(time))/2, y + titleFont.getHeight() + itemBuffer, time, Color.white);
			//show the IP
			subFont.drawString(x + itemBuffer, height/2, "Game Hostname: " + hostname);
			//how many players are connected
			subFont.drawString(x + itemBuffer, height/2 + itemBuffer + subFont.getHeight(), "Connected Players: " + "(" + "X" + ") " + "TestPlayer1");
			
		}
	}
	
	/**
	 * 
	 * @param token = selected token
	 * @param x = top left x coord
	 * @param y = top left y coord
	 * @param width = width of icons img
	 * @param height = height of the icons img
	 */
	public void radioButtons(Token token, float x, float y, int width, int height, Graphics g){
		//space between squares
		int space = width/4;
		int start = (int) (itemBuffer + panelX);
		//draw the radio buttons
		for(int i = 0; i < 8; i++){
			//draw the outlines
			g.setColor(Color.black);
			//the random 45 is because I did not make the icons half way down the img
			g.drawRect(start + space * (i % 4) , y + 3 + i/4 * 45, 15, 15);
			if(token.status[i]){
				g.setColor(Color.blue);
				g.fillRect(start + space * (i % 4) + 1, y + 3 + i/4 * 45 + 1, 14, 14);
			}
		}
	}
	
}
