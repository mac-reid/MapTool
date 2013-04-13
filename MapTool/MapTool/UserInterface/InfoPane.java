package UserInterface;

import java.awt.Toolkit;
import java.util.Date;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import Backend.Control;

/**
 * This class is the info Pane that will rest above the chatPane
 * @author Neal
 *
 */
public class InfoPane {
	//Size constraints for panel
	float panelX, panelY;
	float sizeX, sizeY;
	//Global Buffers
	final int edgeBuffer = 10;
	final int itemBuffer = 5;
	//String info
	String name, location, gameName, time;
	//Status variables (need to make status class within Token class)
	//Status[] statuses;
	
	//Clock
	
	//the selected token, if there is one
	Token token;
	
	//Game stuff
	GameContainer gc;
	Control control;
	Graphics g;
	
	//used to determine what state to show
	//0 = general info
	//1 = token info;
	private int state;
	
	/**
	 * Constructor to initialize necessary variables
	 */
	public InfoPane(String pGameName, float pX, float pY, float width, float height){
		gameName = pGameName;
		state = 0;
		time = getTime();
		//set size
		panelX = pX;
		panelY = pY;
		sizeX = width;
		sizeY = height;
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
	public void update(){
		
	}
	
	/**
	 * Render method, draws everything on the screen
	 */
	public void render(Graphics g){
		g.setColor(Color.blue);
		g.fillOval(panelX + edgeBuffer, panelY + edgeBuffer, 50, 50);
	}
}
