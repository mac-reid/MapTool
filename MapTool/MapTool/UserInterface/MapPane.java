package UserInterface;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import java.util.*;


public class MapPane {
	 
	Image map = null; 
	Image mapgrid = null;
	
	// Indicates if tiles are visible/occupied
	boolean visible[][] = null;
	boolean occupied[][] = null;
	
	//ArrayList of all tokens on the map
	ArrayList<Token> tokens = new ArrayList<Token>();
	
	// Offset of map (from origin) in the display window
	float pxOffsetX = 0;
    float pxOffsetY = 0;    
    // Size of map width/height by tile count
    int tileSizeX = 17;
    int tileSizeY = 13;    
    // Size of map width/height by pixel count
    int pxSizeX = 0;
    int pxSizeY = 0;
    // Holds the intended X/Y pixel location for a resource, while in the FileChooser    
    int temppxX = 0;
    int temppxY = 0;
    
    // Is map currently being dragged
    boolean mapdrag = false;
    //a "timer" used to delay actions and the drag function
    double lastAction = 0;
    // Pixel count of X/Y drag values
    int mapdragx = 0;
    int mapdragy = 0;
    int dragoffsetx = 0;
    int dragoffsety = 0;
    
    MapOptions options;
    SlickFileChooser fileChooser = new SlickFileChooser();
    
    
    // Demo constructor - loads example resources
    public MapPane() throws SlickException {
    	loadMap("Resources/Maps/dwarffort.png");
    	mapgrid = new Image("Resources/Tokens/GreyGrid.png");
    	tokens.add(new Token("Resources/Tokens/mslug.png", 2, 2));
    	tokens.add(new Token("Resources/Tokens/banshee.png", 6, 4));
    	tokens.add(new Token("Resources/Tokens/goomba.png", 7, 6));
    }
    
    
    public MapPane(String maplocation) throws SlickException {
    	map = new Image(maplocation);
    }
    
    
    public MapPane(String maplocation, int sizex, int sizey) throws SlickException {
    	loadMap(maplocation);
    	tileSizeX = sizex;
    	tileSizeY = sizey;
    	//added by Neal
    	mapgrid = new Image("Resources/GreyGrid.png");
    	options = new MapOptions(0, 0, this);
    }
    
    
    private void loadMap(String mapname) throws SlickException {
    	map = new Image(mapname);
    	pxSizeX = map.getWidth();
    	pxSizeY = map.getHeight();
    }
    
    
    public void mapLeft() {
    pxOffsetX += 48;
    }
    
    
    public void mapRight() {
    pxOffsetX -= 48;
    }
    
    
    public void mapUp() {
    pxOffsetY += 48;
    }
    
    
    public void mapDown() {
    pxOffsetY -= 48;
    }
    
    
    
    public void dragMap(boolean dragging, int x, int y) {
    	if (!dragging) {
    		mapdragx = x;
    		mapdragy = y;
    		dragoffsetx = 0;
    		dragoffsety = 0;
    	}
    	else {
    		// If empty Token, drag the map
    		dragoffsetx = ((int)((mapdragx - x) / 48) * 48);
    		dragoffsety = ((int)((mapdragy - y) / 48) * 48);
    		
    		if (dragoffsetx != 0) {
    			pxOffsetX += dragoffsetx;
    			dragoffsetx = 0;
    			mapdragx = x;
    			
    			if(pxOffsetX < 0)
    				pxOffsetX = 0;
    			else if(pxOffsetX > ((pxSizeX - (pxSizeX % 48)) - (tileSizeX * 48)))
    				pxOffsetX = ((pxSizeX - (pxSizeX % 48)) - (tileSizeX * 48));
    		}
    		
    		if (dragoffsety != 0) {
    			pxOffsetY += dragoffsety;
    			dragoffsety = 0;
    			mapdragy = y;
    			
    			if(pxOffsetY < 0)
    				pxOffsetY = 0;
    			else if(pxOffsetY > ((pxSizeY - (pxSizeY % 48)) - (tileSizeY * 48)))
    				pxOffsetY = ((pxSizeY - (pxSizeY % 48)) - (tileSizeY * 48));
    		}    		
    	}
    }
    
    // Draws current background and Tokens
    //		Objects drawn in the MapPane should determine render status via their own isActive() method
    public void renderMap(float x, float y, int xSize, int ySize, Graphics g) {
    	
    	// If we are rendering the file chooser
    	if (fileChooser.getActive())
    		fileChooser.render(x, y, g);
    	
    	// Otherwise, render whatever else
    	else {
	    	map.draw(x, y, x + (xSize * 48), y + (ySize * 48),
	    	   pxOffsetX, pxOffsetY,
	    	   pxOffsetX+ (xSize * 48), pxOffsetY+ (ySize * 48));
	    	//draw grid
	    	drawGrid(x, y, xSize, ySize);
	    	// Only draw Tokens present in the current window
	    	int lowx = (int)pxOffsetX / 48;
	    	int lowy = (int)pxOffsetY / 48;
	    	for(int i = 0; i < tokens.size(); i++) {
	    		if ((tokens.get(i).getX() >= lowx) && (tokens.get(i).getX() <= (lowx + tileSizeX - 1)))
	    			if ((tokens.get(i).getY() >= lowy) && (tokens.get(i).getY() <= (lowy + tileSizeY - 1)))
	    				tokens.get(i).renderToken(x - pxOffsetX, y - pxOffsetY);
	    	}
	    	
	    	//draw options
	    	options.render(g);
    	}
    }
    
    /**
     * An update method
     * @param in
     * @param delta
     * @throws SlickException 
     * @todo add character Token menu action
     */
    public void update(GameContainer gc, int delta, int mXoffset, int mYoffset) throws SlickException {
    	Input in = gc.getInput();
    	int mouseX = in.getMouseX();
    	int mouseY = in.getMouseY();
    	
    	// If we are updating the file chooser
    	if (fileChooser.getActive()) {
    		fileChooser.update(in, mXoffset, mYoffset, tileSizeX, tileSizeY);
    		// If the chooser returns a value, MapPane sets it inactive
    		if (!(fileChooser.getSelected().equals(""))) {
    			addTokenCoord(fileChooser.getSelected(), temppxX, temppxY);
    			fileChooser.setInactive();
    		}
    	}
    	
    	// If we are not drawing the file chooser (a similar Else-If setup could allow for many elements of this nature
    	else {
	    	if(options.isActive()) options.update(gc);
	    	//The constant 5 right now is the buffer from editor
	    	if ((mouseX >= mXoffset && mouseX <= mXoffset + tileSizeX*48) && (mouseY >= mYoffset && mouseY <= mYoffset + tileSizeY*48)) {
	        	
	        	// If the mouse is pressed down, drag the map, but only if no menu is open
		        dragMap(in.isMouseButtonDown(0), in.getMouseX(), in.getMouseY());
		        // For a right click, check for token
		        if (in.isMousePressed(1)) {
		        	for(Token t : tokens) {
		        		//if both if statements are true, a token is there
		        		if(t.getX() == mouseX/48 + pxOffsetX) {
		        				System.out.println("You have clicked on a token");
		        				return;
		        		}
		        	}
		        
			        options.resetPort();
			        options.setX(mouseX);
			        options.setY(mouseY);
			        options.setActive(true);
			        //commenting out the dude for now, kinda messy with the menu
			        //addTokenCoord("Resources/mslug.png", mouseX - 5, mouseY - 5);
		        }
	    	
		        // For a middle click, delete a Token
		        if (in.isMousePressed(2))
		        	removeTokenCoord(mouseX - 5, mouseY - 5);
	    	}
	    }
    }
    
    
    //Testing this, moves the map immediately
    public void move(float tileX, float tileY){
    	pxOffsetX = (int)tileX * 48;
    	pxOffsetY = (int)tileY * 48;
    }
    
    
    public void resize(int tileWidth,int tileHeight){
    	//if the screen got wider compensate so no blank grid
    	if(tileWidth > tileSizeX && pxOffsetX > 0){
    		pxOffsetX -= (tileWidth - tileSizeX)*48;
    	}
    	//if the screen got wider compensate so no blank grids
    	if(tileHeight > tileSizeY && pxOffsetY > 0){
    		pxOffsetY -= (tileHeight - tileSizeY)*48;
    	}
    	tileSizeX = tileWidth;
    	tileSizeY = tileHeight;
    	
    	if(pxOffsetY > (map.getHeight() - (tileSizeY * 48)))
			pxOffsetY = (map.getHeight() - (tileSizeY * 48));
    }
    
    
    // Sets SwingFileChooser active, accepts mouse coordinates where the resource should be placed
    public void selectToken(int x, int y) {
    	fileChooser.setActive("Tokens");
    	temppxX = x;
    	temppxY = y;
    }
    
    
    // Add a Token by map coordinate
    public void addTokenCoord(String imglocation, int x, int y) throws SlickException {
    	tokens.add(new Token(imglocation, (int)((x + pxOffsetX) / 48), (int)((y + pxOffsetY) / 48)));
    }
    
    
    // Add a Token by grid location
    public void addTokenGrid(String imglocation, int x, int y) throws SlickException {
    	tokens.add(new Token(imglocation, x, y));
    }
    
    
    // Remove a token by coordinate
    public void removeTokenCoord(int x, int y) {
    	for(int i = 0; i < tokens.size(); i++) {
    		if (((int)(x + pxOffsetX) / 48) == tokens.get(i).getX() && ((int)(y + pxOffsetY) / 48) == tokens.get(i).getY())
    			tokens.remove(i);
    	}
    }
    
    
    // Remove a Token by grid location
    public void removeTokenGrid(int x, int y) {
    	for(int i = 0; i < tokens.size(); i++) {
    		if (x == tokens.get(i).getX() && y == tokens.get(i).getY())
    			tokens.remove(i);
    	}
    }
    
    
    private void drawGrid(float x, float y, int width, int height) {
    	for (int i = 0; i < width; i++)
    		for (int j = 0; j < height; j++) {
    			mapgrid.draw(x + i * 48, y + j * 48);
    		}
    }
    
    
    // =======================================================================
    // Methods used directly by the controller
    
 // Add a token to this map with default size (1)
    public void addToken(String file, int x, int y, String tokenname) {
    	tokens.add(new Token(tokenname, file, x, y));
    }
    
    // Add a token to this map with custom size
    public void addToken(String file, int x, int y, int size, String tokenname) {
    	tokens.add(new Token(tokenname, file, x, y));
    }
    
    // Returns token at grid location X,Y
    public Token getToken(int x, int Y) {
    	return null;
    }
    
    // Returns an array list of tokens held by the map
    public ArrayList<Token> getTokens() {
    	return null;
    }
    
    // Hides an area of the map
    public void hideArea(int startX, int startY, int endX, int endY) {
    	for (int x = startX; x <= endX; x++) {
    		for (int y = startY; y <= endY; y++) {
    			visible[x][y] = false;
    		}
    	}
    }
    public void unHideArea(int startX, int startY, int endX, int endY) {
    	for (int x = startX; x <= endX; x++) {
    		for (int y = startY; y <= endY; y++) {
    			visible[x][y] = false;
    		}
    	}
    }
    
    // Moves a token by name to X,Y
    public boolean moveToken(String name, int x, int y) {
    	for (int i = 0; i < tokens.size(); i++) {
    		if (tokens.get(i).getName() == name) {
    			tokens.get(i).move(x, y);
    			return true;
    		}
    	}
    	return false;
    }
    
    // Removes a token by its given name
    public boolean removeToken(String tokenname) {
    	for (int i = 0; i < tokens.size(); i++) {
    		if (tokens.get(i).getName() == tokenname) {
    			tokens.remove(i);
    			return true;
    		}    		
    	}
    	return false;
    }
    
    public String getBackground() {
    	return map.getResourceReference();
    }
    
    
}

