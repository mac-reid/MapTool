import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Andrews.ChatPane;
import Andrews.MapPane;

import java.awt.Toolkit;
import java.awt.font.*;


public class Editor extends BasicGameState{

	private int ID;
	private MapPane mapTool;
	private ChatPane chatBox;
	private VideoChatPane videoChat;
	
	//int values used to determine object positioning
	public final int BUFFER = 10;
	public final int CHAT_WIDTH = 250;
	public int videoChatHeight = 200;
	//mapTool specific values
	private int mapTopX;
	private int mapTopY;
	private int mapTileWidth;
	private int mapTileHeight;
	
	public Editor(int state){
		ID = state;
		
	}

	/**
	 * Initializes the map, right now it does not ask for which background to load. must fix
	 */
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		//find the best height and width of the map
		videoChatHeight = gc.getHeight()/5;
		mapTileWidth = (gc.getWidth() - BUFFER*3 - CHAT_WIDTH)/48;
		mapTileHeight = (gc.getHeight() - BUFFER*3 - videoChatHeight)/48;
		mapTool = new MapPane("Resources/Dwarfort.png", mapTileWidth, mapTileHeight);
		mapTool.renderMap(BUFFER, BUFFER, getTileWidth(gc), getTileHeight(gc));
		mapTopX = BUFFER;
		mapTopY = BUFFER;
		//Draw the chatwindow
		chatBox = new ChatPane(CHAT_WIDTH, gc.getHeight() - BUFFER*2);
		//chatBox.renderChat(gc.getWidth() - CHAT_WIDTH - BUFFER, BUFFER, gc.getGraphics());
		//draw the video chat box
		videoChat = new VideoChatPane(gc);
	}


	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		mapTool.renderMap(BUFFER, BUFFER, getTileWidth(gc), getTileHeight(gc));
		
		chatBox.renderChat(gc.getWidth() - CHAT_WIDTH - BUFFER, BUFFER, CHAT_WIDTH, gc.getHeight() - BUFFER*2, g);
		
		videoChat.renderVideoPane(BUFFER, gc.getHeight() - videoChatHeight - BUFFER,
				gc.getWidth() - 3*BUFFER - CHAT_WIDTH, videoChatHeight, g);
	}


	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
        Input input = gc.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        // If mouse is over Map Pane, Enable Map input
        // This should definitely be cleaned up - passing 'input' to the appropriate object
        // Instead of calling all of these methods here
        
        // If the mouse is inside of the Map Pane:
        if ((mouseX >= mapTopX && mouseX <= mapTopX + getTileWidth(gc)*48) && (mouseY >= mapTopY && mouseY <= mapTopY + getTileHeight(gc)*48)) {
        	
        	// If the mouse is pressed down, drag the map
	        mapTool.dragMap(input.isMouseButtonDown(0), input.getMouseX(), input.getMouseY());
	        // For a right click, create a Token
	        if (input.isMousePressed(1))
	        	mapTool.addTileCoord("Resources/mslug.png", mouseX - mapTopX, mouseY - mapTopY);
	        // For a middle click, delete a Token
	        if (input.isMousePressed(2))
	        	mapTool.removeTileCoord(mouseX - mapTopX, mouseY - mapTopY);
	        
	        // Debug - moves the map based on keyboard input
	        if(input.isKeyPressed(Input.KEY_S))
	            mapTool.mapDown();
	        	        
	        if(input.isKeyPressed(Input.KEY_W))
	            mapTool.mapUp();
	        	 
	        if(input.isKeyPressed(Input.KEY_A)) 
	            mapTool.mapLeft();
	        
	        if(input.isKeyPressed(Input.KEY_D))
	        	mapTool.mapRight();
        }
        
        if (mouseX > (gc.getWidth() - CHAT_WIDTH - BUFFER) && mouseX < (gc.getWidth() - BUFFER) &&
        		input.isMouseButtonDown(0)){
        	chatBox.Activate();
        } else if (input.isMouseButtonDown(0)){
        	chatBox.Deactivate();
        }
        chatBox.updateChat(input);
        
        if (gc.getHeight() < 600){
        	((AppGameContainer) gc).setDisplayMode(gc.getWidth(), 600, false);
        }
        
        if (gc.getWidth() < 800){
        	((AppGameContainer) gc).setDisplayMode(800, gc.getHeight(), false);
        }
	}

	public int getID() {
		return ID;
	}

	/**
	 * No idea if this will be usefull
	 * @param gc
	 * @return
	 */
	public int[] mapBottomRight(GameContainer gc){
		int[] coords = new int[2];
		coords[0] = gc.getWidth() - 2*BUFFER - CHAT_WIDTH;
		coords[1] = gc.getHeight() - 2*BUFFER - videoChatHeight;
		
		return coords;
	}
	
	public int getTileHeight(GameContainer gc){
		return (gc.getHeight() - videoChatHeight - BUFFER * 3)/48;
	}
	
	public int getTileWidth(GameContainer gc){
		return (gc.getWidth() - CHAT_WIDTH - BUFFER * 3)/48;
	}
}
