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
	final int picSize = 80;
	//String info
	String hostname, gameName, time;
	int numPlayers;
	//Status variables (need to make status class within Token class)
	//Status[] statuses;
	
	//Clock
	
	//background
	Image background;
	//the selected token, if there is one
	Token token;
	
	
	/**
	 * Constructor to initialize necessary variables
	 */
	public InfoPane(String pGameName, float pX, float pY, float width, float height, String host){
		try {
			background = new Image("Resources/chatpattern.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		try{
			font = new Font("anglican", Font.TRUETYPE_FONT, 24);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("anglican.tff" + " not loaded.  Using serif font.");
			font = new Font("serif", Font.PLAIN, 20);
		}
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
	 * Inner class that will have the radio button feeling for the statuses
	 * @author nrjohnso
	 *
	 */
	public class statusGrid{
		
		//shape array for the icon for the stuff
		Shape[] icons;
		
		public statusGrid(Token token, int width, int height){
			initIcons();
		}
		
		public void initIcons(){
			icons = new Shape[8];
			icons[0] = null;
		}
		
	}
}
