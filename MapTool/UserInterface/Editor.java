package UserInterface;

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

import Backend.Control;

import java.awt.Toolkit;
import java.awt.font.*;


public class Editor extends BasicGameState{

	private int ID;
	public MapPane mapTool;
	public ChatPane chatBox;
	public MenuPane menuPane;
	public InfoPane infoPane;
	
	//int values used to determine object positioning
	public final int BUFFER = 12; //general spacing between objects
	public final int CHAT_WIDTH_MIN = 250;
	public final int INFO_PANE_HEIGHT = 150;
	public int chatWidth;
	public int menuPaneMin = 0;
	public final int MENU_PANE_HEIGHT = 150;
	//mapTool specific values
	private int mapTopX = 0;
	private int mapTopY = 0;
	private int mapTileWidth;
	private int mapTileHeight;
	private int currentWidth;
	private int currentHeight;
	
	private boolean imgsloaded = false;
	
	public String hostname;
	//frame images
	Image topLeft, topRight, botLeft, botRight, leftA, leftB, rightA, rightB, topA, topB, botA, botB;
	
	public genUI genUI;
	public Editor(int state){
		ID = state;
	}

	/**
	 * Initializes the map, right now it does not ask for which background to load. must fix
	 */
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		genUI = ((genUI)sbg);
		gc.setAlwaysRender(true);
		menuPaneMin = gc.getHeight()/5;
		//find the best height and width of the map
		mapTileWidth = (gc.getWidth() - BUFFER*3 - CHAT_WIDTH_MIN)/48;
		mapTileHeight = (gc.getHeight() - BUFFER*3 - menuPaneMin)/48;
		chatWidth = (gc.getWidth() - BUFFER * 3 - mapTileWidth*48);
		mapTool = new MapPane("Resources/Maps/losttemple.png", mapTileWidth, mapTileHeight, genUI);
		mapTool.renderMap(BUFFER, BUFFER, gc.getGraphics());
		mapTopX = BUFFER;
		mapTopY = BUFFER;
		//Draw the chatwindow
		chatBox = new ChatPane(chatWidth, gc.getHeight() - BUFFER*2 - INFO_PANE_HEIGHT, CHAT_WIDTH_MIN, genUI);
		//chatBox.renderChat(gc.getWidth() - CHAT_WIDTH - BUFFER, BUFFER, gc.getGraphics());
		//draw the video chat box
		menuPane = new MenuPane(this);
		//TODO Replace "" with game name
		infoPane = new InfoPane("", gc.getWidth() - chatWidth - BUFFER, BUFFER, chatWidth, INFO_PANE_HEIGHT, genUI.getAddress(), genUI.control);
		loadImages();
		
		frame(gc, gc.getGraphics());
	}


	/**
	 * The default render method. It draws the mapTool object, chatBox object, and video chat object. 
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		//make sure the size is OK
		if (gc.getHeight() < 600){
        	((AppGameContainer) gc).setDisplayMode(gc.getWidth(), 600, false);
        }
        
        if (gc.getWidth() < 800){
        	((AppGameContainer) gc).setDisplayMode(800, gc.getHeight(), false);
        }
		//draw things
		mapTool.renderMap(BUFFER, BUFFER, g);
		
		frame(gc, g);
		//get chat height and width
		chatWidth = (gc.getWidth() - BUFFER * 3 - getMapWidth(gc));
		//makes chat, remeber that CHAT_WIDTH is standard chat width
		chatBox.renderChat(getMapWidth(gc) + BUFFER * 2, BUFFER * 2 + INFO_PANE_HEIGHT, 
				chatWidth, gc.getHeight() - BUFFER*3 - INFO_PANE_HEIGHT, g);
		//render the info pane
		infoPane.render(gc.getWidth() - chatWidth - BUFFER, BUFFER, chatWidth, INFO_PANE_HEIGHT, g);
		//makes video chat pane, remember videoChatHeight is final int, standard height
		menuPane.renderMenuPane(BUFFER, gc.getHeight() - BUFFER - MENU_PANE_HEIGHT, g);
		
	}


	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		if (gc.getHeight() < 600){
        	((AppGameContainer) gc).setDisplayMode(gc.getWidth(), 600, false);
        }
        
        if (gc.getWidth() < 800){
        	((AppGameContainer) gc).setDisplayMode(800, gc.getHeight(), false);
        }
        Input input = gc.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        
        mapTool.update(BUFFER, BUFFER, getMapWidth(gc), getMapHeight(gc), gc, delta);
        // If mouse is over Map Pane, Enable Map input
        // This should definitely be cleaned up - passing 'input' to the appropriate object
        // Instead of calling all of these methods here
        
        /*// If the mouse is inside of the Map Pane:
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
        }*/
        
        //Chatbox 
        if (mouseX > (gc.getWidth() - CHAT_WIDTH_MIN - BUFFER) && mouseX < (gc.getWidth() - BUFFER) &&
        		input.isMouseButtonDown(0)){
        	chatBox.Activate();
        } else if (input.isMouseButtonDown(0)){
        	chatBox.Deactivate();
        }
        chatBox.updateChat(input);
        
        infoPane.update(gc, mapTool.getSelectedToken());
        
        menuPane.update(gc, gc.getGraphics());
	}
	
	
	/**
	 * Override of BasicGameState mouseWheelMoved listener.  
	 *    Wheel-up (negative).  Wheel-down (positive).
	 * @param change
	 * @return
	 */
	@Override
	public void mouseWheelMoved(int change) {
		if (chatBox.isActivated())
			chatBox.scrollChat(change);
		//else
		//	mapTool.zoomMap(change);
		}
	

	public int getID() {
		return ID;
	}

	/**
	 * This returns the bottom left in pixels of where the map will be drawn
	 * @param gc
	 * @return
	 */
	public int[] mapBottomRight(GameContainer gc){
		int[] coords = new int[2];
		coords[0] = gc.getWidth() - 2*BUFFER - CHAT_WIDTH_MIN;
		coords[1] = gc.getHeight() - 2*BUFFER - MENU_PANE_HEIGHT;
		
		return coords;
	}
	
	/**
	 * Finds how many tiles high the map can be in the window
	 * 3 buffers from bottom to top of map
	 * 48 pixel tiles
	 */
	public int getMapHeight(GameContainer gc){
		return (gc.getHeight() - MENU_PANE_HEIGHT - BUFFER * 3);
	}
	
	/**
	 * finds how many tiles wide the map can be
	 * @param gc
	 * @return
	 */
	public int getMapWidth(GameContainer gc){
		return (gc.getWidth() - CHAT_WIDTH_MIN - BUFFER * 3);
	}
	
	public void frame(GameContainer gc, Graphics g){
		int height = gc.getHeight();
		int width = gc.getWidth();
		int drawnWidth = 0;
		int drawnHeight = 0;
		//draw bottom of info pane
		drawnWidth = 0;
		while (drawnWidth < chatWidth){
			g.drawImage(botA, width - chatWidth - BUFFER + drawnWidth, INFO_PANE_HEIGHT + BUFFER);
			drawnWidth += botA.getWidth();
		}
		//draw vertical lines
		while(drawnHeight < height){
			g.drawImage(leftA, 0, drawnHeight);
			g.drawImage(leftA, BUFFER + getMapWidth(gc), drawnHeight);
			g.drawImage(leftB, gc.getWidth() - BUFFER, drawnHeight);
			drawnHeight = drawnHeight + leftA.getHeight();
		}
		drawnWidth = 0;
		//draw horizontal lines
		while(drawnWidth < width){
			g.drawImage(topA, drawnWidth, 0);
			g.drawImage(botA, drawnWidth, height - BUFFER);
			drawnWidth = drawnWidth + topA.getWidth();
		}
		//draw most of the middle line
		drawnWidth = BUFFER;
		while(drawnWidth < getMapWidth(gc) - botA.getWidth()){
			g.drawImage(botA, drawnWidth, getMapHeight(gc) + BUFFER);
			drawnWidth = drawnWidth + botA.getWidth();
		}
		//draw the last bit of the bottom line
		g.drawImage(botA.getSubImage(0, 0, getMapWidth(gc) - drawnWidth + BUFFER, botA.getHeight()), drawnWidth, getMapHeight(gc) + BUFFER);
		//draw four corners
		g.drawImage(topLeft, 0, 0);
		g.drawImage(topRight, width - topRight.getWidth(), 0);
		g.drawImage(botLeft, 0, height - botLeft.getHeight());
		g.drawImage(botRight, width - topRight.getWidth(), height - botRight.getHeight());
	}
	
	//loads all the images you need to draw the frame
	public void loadImages(){
		try {
		topLeft = new Image("Resources/Frame/FrameTopLeft.png");
		topRight = new Image("Resources/Frame/FrameTopRight.png");
		botLeft = new Image("Resources/Frame/FrameBotLeft.png");
		botRight = new Image("Resources/Frame/FrameBotRight.png");
		leftA = new Image("Resources/Frame/FrameVerticalLeftA.png");
		leftB = new Image("Resources/Frame/FrameVerticalLeftB.png");
		rightA = new Image("Resources/Frame/FrameVerticalRightA.png");
		topA = new Image("Resources/Frame/FrameTopA.png"); 
		botA = new Image("Resources/Frame/FrameBotA.png");
		} catch (SlickException e){System.out.print("load img error");}
	}
	
}
