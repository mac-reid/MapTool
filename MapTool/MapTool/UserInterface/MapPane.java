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
	Image selectgrid = null;
	Image dragImage = null;
	
	Token dragToken = null;
	
	// Indicates if tiles are visible/occupied
	boolean visible[][];
	boolean occupied[][];
	
	
	//ArrayList of all tokens on the map
	ArrayList<Token> tokens = new ArrayList<Token>();
	
	// Offset of map (from origin) in the display window
	float pxOffsetX = 0;
    float pxOffsetY = 0;
    int gridOffsetX = 0;
    int gridOffsetY = 0;
    // Size of map displayed width/height by tile count
    int tileSizeX = 17;
    int tileSizeY = 13;    
    // Size of map width/height by pixel count
    int pxSizeX = 0;
    int pxSizeY = 0;
    
    // Current map grid location of the cursor
    int currGridX = 0;
    int currGridY = 0;
    
    // Last grid location of the cursor (while in full menus such as FileChooser)
    int tempGridX = 0;
    int tempGridY = 0;
    
    
    // Pixel count of X/Y drag values
    int mapdragx = 0;
    int mapdragy = 0;
    int dragoffsetx = 0;
    int dragoffsety = 0;
    
    // Current location of the mouse, via Update
    int mouseX = 0;
    int mouseY = 0;
    
    // Map draging mode (0: Normal, 1: Dragging Map, 2: Dragging Token)
    int dragMode = 0;
    // File loading mode (0: None, 1: Maps, 2: Tokens)
    int loadMode = 0;
    
    
    MapOptions options;
    SlickFileChooser fileChooser = new SlickFileChooser();
    
    
    // Demo constructor - loads example resources
    public MapPane() throws SlickException {
    	loadMap("Resources/Maps/Dwarfort.png");
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
    	//Since i didnt have this file...
    	//selectgrid = new Image("Resources/selectgrid.png");
    	//THIS FILE!
    	selectgrid = new Image("Resources/Redgrid.png");
    	options = new MapOptions(0, 0, this);
    }
    
    
    private void loadMap(String mapname) throws SlickException {
    	map = new Image(mapname);
    	
    	pxOffsetX = 0;
    	pxOffsetY = 0;
    	gridOffsetX = 0;
    	gridOffsetY = 0;
    	pxSizeX = map.getWidth();
    	pxSizeY = map.getHeight();
    	    	
    	int tilesXSize = pxSizeX / 48;
    	int tilesYSize = pxSizeY / 48;
    	occupied = new boolean[tilesXSize][tilesYSize];
    	visible = new boolean[tilesXSize][tilesYSize];
    }
    
    
    // Initialize the map drag
    public void setDrag(int x, int y) {
    	mapdragx = x;
		mapdragy = y;
		dragoffsetx = 0;
		dragoffsety = 0;
    }
    
    // Maintain the map drag
    public void dragMap(int x, int y) {
    	
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
    
    
    // Draws current background and Tokens
    //		Objects drawn in the MapPane should determine render status via their own isActive() method
    public void renderMap(float x, float y, int xSize, int ySize, Graphics g) {
    	
    	// If we are rendering the file chooser
    	if (fileChooser.isActive())
    		fileChooser.render(x, y, g);
    	
    	// Otherwise, render whatever else
    	else {
	    	map.draw(x, y, x + (xSize * 48), y + (ySize * 48),
	    	   pxOffsetX, pxOffsetY, pxOffsetX + (xSize * 48), pxOffsetY + (ySize * 48));
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
	    	
	    	// If a token is being dragged
	    	if (dragMode == 2) {
	    		dragImage.draw(mouseX, mouseY);
	    	}
	    	
	    	// If Active, draw the options menu
	    	if (options.isActive()) {
	    		options.render(g);
	    	}
    	}
    }
    
    /**
     * An update method
     * @param in
     * @param delta
     * @throws SlickException 
     * @todo add character Token menu action
     */
    public void update(int mXoffset, int mYoffset, GameContainer gc, int delta) throws SlickException {
    	Input in = gc.getInput();
    	mouseX = in.getMouseX();
    	mouseY = in.getMouseY();
    	
    	currGridX = (mouseX - mXoffset) / 48;
    	currGridY = (mouseY - mYoffset) / 48;
    	gridOffsetX = (int)pxOffsetX / 48;
    	gridOffsetY = (int)pxOffsetY / 48;
    	
    	
    	// If the file chooser is active, update
    	if (fileChooser.isActive()) {
    		fileChooser.update(in, mXoffset, mYoffset, tileSizeX, tileSizeY);
    		// If the chooser returns a value, MapPane sets it inactive
    		if (!(fileChooser.getSelected().equals(""))) {
    			if (loadMode == 2)
    				addTokenGrid(fileChooser.getSelected(), tempGridX, tempGridY);
    			else if (loadMode == 1)
    				loadMap(fileChooser.getSelected());
    			loadMode = 0;
    		}
    	}
    	
    	// If the options are active, update
    	else if (options.isActive()) {
    		options.update(gc);
    	}
    	
    	// If the Menus are not being used, a Drag is possibly taking place
    	else {
	    	// If the mouse is within the map bounds
	    	if ((mouseX >= mXoffset && mouseX <= mXoffset + tileSizeX*48) && (mouseY >= mYoffset && mouseY <= mYoffset + tileSizeY*48)) {
	    		
	    		// If LeftMouse is down, initialize or maintain a drag
	    		if(in.isMouseButtonDown(0)) {
	    			if (dragMode == 0) {
	    				if (occupied[currGridX + gridOffsetX][currGridY + gridOffsetY] == true) {
		    				for(Token t : tokens) {
		    					if((t.getX() == (currGridX + gridOffsetX))  &&  (t.getY() == currGridY + gridOffsetY)) {
		    						// If token verified present, copy and set draw mode
		    						System.out.println("WE'VE HOOKED A TOKEN");
		    						dragMode = 2;
		    						dragToken = t;
		    						dragImage = t.getImage().copy();
		    						dragImage.setAlpha(0.75f);
		    					}		
		    				}
	    				}
	    				// Initialize the drag
	    				else {
	    					dragMode = 1;
	    					this.setDrag(in.getMouseX(), in.getMouseY());
	    				}	
	    			}
	    			
	    			// If the draw mode has been set to 1, drag the map
	    			if (dragMode == 1)
	    				dragMap(in.getMouseX(), in.getMouseY());
	    		}
	    		
	    		
	    		// If LeftMouse is not down, but a state is set, clear the state appropriately
	    		else if(dragMode == 1) {
	    			dragMode = 0;
	    			dragToken = null;
	    		}
	    		else if(dragMode == 2) {
	    			if (!occupied[currGridX + gridOffsetX][currGridY + gridOffsetY]) {
	    				occupied[dragToken.getX()][dragToken.getY()] = false;
	    				dragToken.move(currGridX + gridOffsetX, currGridY + gridOffsetY);
	    				occupied[currGridX + gridOffsetX][currGridY + gridOffsetY] = true;
	    			}
	    			dragMode = 0;
	    		}
	    			
		        
	    		// For Right Click, check for token
		        if (in.isMousePressed(1)) {
		        	if(occupied[currGridX + gridOffsetX][currGridY + gridOffsetY]) {
		        		System.out.println("You have clicked on a token");
		        		return;
		        	}
		            
		        	options.resetPort();
			        options.setX(mouseX);
			        options.setY(mouseY);
			        options.setActive(true);
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
    	loadMode = 2;
    	tempGridX = x/48 + gridOffsetX;
    	tempGridY = y/48 + gridOffsetY;
    }
    
    public void selectMap() {
    	fileChooser.setActive("Maps");
    	loadMode = 1;
    }
    
    
    // Add a Token by map coordinate
    public void addTokenCoord(String imglocation, int x, int y) throws SlickException {
    	this.addTokenGrid(imglocation, (int)((x + pxOffsetX) / 48), (int)((y + pxOffsetY) / 48));
    }
    
    
    // Add a Token by grid location
    public void addTokenGrid(String imglocation, int x, int y) throws SlickException {
    	if (!occupied[x][y]) {
    		tokens.add(new Token(imglocation, x, y));
    		occupied[x][y] = true;
    		System.out.println("Added token: " + x + " " + y);
    	}
    }
    
    
    // Remove a token by coordinate
    public void removeTokenCoord(int x, int y) {
    	this.removeTokenGrid((int)((x + pxOffsetX) / 48), (int)((y + pxOffsetY) / 48));
    }
    
    
    // Remove a Token by grid location
    public void removeTokenGrid(int x, int y) {
    	for(int i = 0; i < tokens.size(); i++) {
    		if (x == tokens.get(i).getX() && y == tokens.get(i).getY()) {
    			tokens.remove(i);
    			occupied[x][y] = false;
    		}
    	}
    }
    
    
    private void drawGrid(float getx, float gety, int width, int height) {
    	float drawx = getx; 
    	float drawy = gety;
    	for (int x = 0; x < width; x++) {
    		for (int y = 0; y < height; y++) {
    			mapgrid.draw(drawx, drawy);
    			if (x == currGridX  &&  y == currGridY && !options.isActive())
    				selectgrid.draw(drawx, drawy);
    			drawy += 48;
    		}
    		drawx += 48;
    		drawy = gety;
    	}
    }
    
    public Token getSelectedToken(){
    	return dragToken;
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

