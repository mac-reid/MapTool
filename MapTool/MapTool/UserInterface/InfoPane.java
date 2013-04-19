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

import Backend.Control;

/**
 * This class is the info Pane that will rest above the chatPane
 * @author Neal
 *
 */
public class InfoPane {
	Font font;
	TrueTypeFont titleFont;
	//Size constraints for panel
	float panelX, panelY;
	float sizeX, sizeY;
	//Global Buffers
	final int edgeBuffer = 10;
	final int itemBuffer = 5;
	final int picSize = 80;
	//String info
	String name, location, gameName, time;
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
	public InfoPane(String pGameName, float pX, float pY, float width, float height){
		try {
			background = new Image("Resources/chatpattern.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameName = pGameName;
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
			InputStream is = InfoPane.class.getResourceAsStream("Resources/Fonts/anglican.tff");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("anglican.tff" + " not loaded.  Using serif font.");
			font = new Font("serif", Font.PLAIN, 24);
		}
		titleFont = new TrueTypeFont(font, true);
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
		return ("" + hours +  ":" + minutes);
	}
	
	/**
	 * Update method, doesnt need to much info, as most of the calls will be made from mapPane
	 */
	public void update(GameContainer gc, Token selection){
		token = selection;
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
			g.drawString(token.getName(), x + itemBuffer * 3 + picSize, y + itemBuffer);
		} else {
			String testTitle = "TEST TITLE";
			int strWidth = titleFont.getWidth(testTitle);
			titleFont.drawString(x + (width - strWidth)/2, y + itemBuffer, testTitle, Color.white);
			titleFont.drawString(x + itemBuffer, y + itemBuffer *2, time, Color.white);
			//how many players are connected
			title.drawString
		}
	}
}
