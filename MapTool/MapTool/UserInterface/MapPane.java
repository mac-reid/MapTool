package UserInterface;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import Backend.Control;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.*;

// added by Mac
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.IOException;

public class MapPane {

    // added by Mac
    private static volatile List<String> changeBuffer = new CopyOnWriteArrayList<String>();
    private static Object sharedLock = new Object();
	 
	Image map = null; 
	Image mapgrid = null;
	Image selectgrid = null;
	Image dragImage = null;
	
	// Indicates if tiles are visible/occupied
	boolean visible[][];
	
	//ArrayList of all tokens on the map
	Tokens tokens;	
	
	// Holds the (X,Y) coordinates of the token being dragged
	Point dragToken = new Point();
	
	// Hold the float (X,Y) coordinates of the 
	Point2D.Float gridOrigin = new Point2D.Float();
	
	// Current token scale
	float tokenScale = 48;
	float mapScale = 1;
	
	
	// Offset of map (from origin) in the display window
	float pxOffsetX = 0;
    float pxOffsetY = 0;
    
    // Size of displayed map by pixel count
    int paneSizeX = 0;
    int paneSizeY = 0;
    
    // Size of map width/height by pixel count
    int pxSizeX = 0;
    int pxSizeY = 0;
    
    // Current map grid location of the cursor
    int currentGridX = 0;
    int currentGridY = 0;
    
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
    // Map scaling is currently taking place
    boolean mapScaling = false;
    
    genUI genUI;
    
    Token selectedToken;
    
    MapOptions options;
    SlickFileChooser fileChooser = new SlickFileChooser();
    
    
    public MapPane(String maplocation, int sizex, int sizey, genUI genUI) throws SlickException {
    	loadMap(maplocation);
    	selectedToken = null;
    	this.genUI = genUI;
    	paneSizeX = sizex;
    	paneSizeY = sizey;
    	//added by Neal
    	mapgrid = new Image("Resources/GreyGrid.png");
    	//Since i didnt have this file...
    	//selectgrid = new Image("Resources/selectgrid.png");
    	//THIS FILE!
    	selectgrid = new Image("Resources/Redgrid.png");
    	options = new MapOptions(0, 0, this);
    	tokens = new Tokens(tokenScale);
    }
    
    
    private void loadMap(String mapname) throws SlickException {
    	map = new Image(mapname);
    	//map.clampTexture();
    	pxOffsetX = 0;
    	pxOffsetY = 0;
    	pxSizeX = map.getWidth();
    	pxSizeY = map.getHeight();
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
    	
		dragoffsetx = (int)(mapdragx - x);
		dragoffsety = (int)(mapdragy - y);
		
		if (dragoffsetx != 0) {
			// If drag is within bounds
			if ((pxOffsetX + dragoffsetx) > 0  && (pxOffsetX + dragoffsetx) < (pxSizeX - paneSizeX - (pxSizeX % tokenScale)))
				pxOffsetX += dragoffsetx;
			dragoffsetx = 0;
			mapdragx = x;
		}
		
		
		if (dragoffsety != 0) {
			// If drag is within bounds
			if ((pxOffsetY + dragoffsety) > 0  && (pxOffsetY + dragoffsety) < (pxSizeY - paneSizeY - (pxSizeY % tokenScale)))
				pxOffsetY += dragoffsety;
			dragoffsety = 0;
			mapdragy = y;
		}   		
	}
    
    
    // Draws current background and Tokens
    //		Objects drawn in the MapPane should determine render status via their own isActive() method
    public void renderMap(float x, float y, Graphics g) {
    	
    	// If we are rendering the file chooser
    	if (fileChooser.isActive())
    		fileChooser.render(x, y, g);
    	
    	// Otherwise, render whatever else
    	else {
    		// If the window size is larger than one or both map dimensions, center the map and grid
    		if (paneSizeX >= pxSizeX  ||  paneSizeY >= pxSizeY) {
    			if (paneSizeX >= pxSizeX  &&  paneSizeY >= pxSizeY) {
    				map.draw(x + ((paneSizeX - pxSizeX) / 2), y + (paneSizeY - pxSizeY) / 2);
    				drawGrid(x + ((paneSizeX - pxSizeX) / 2), y + (paneSizeY - pxSizeY) / 2, g);
    			}
    			else if (paneSizeX >= pxSizeX) {
    				map.draw(x + ((paneSizeX - pxSizeX) / 2), y - pxOffsetY);
    				drawGrid(x + (paneSizeX - pxSizeX) / 2, y, g);
    			}
    			else if (paneSizeY >= pxSizeY) {
    				map.draw(x - pxOffsetX, y + (paneSizeY - pxSizeY) / 2);
    				drawGrid(x, y + (paneSizeY - pxSizeY) / 2, g);
    			}
    		}
    		
    		// Otherwise, draw the map and grid based fully on the mouse-drag offset
    		else {
    			System.out.println(pxOffsetX + " | (" + pxSizeX + " | " + paneSizeX + ")");
    			// Handles map overdraw during a window resize
    			if (pxOffsetX + paneSizeX > pxSizeX)
        			pxOffsetX = pxSizeX - paneSizeX;
        		if (pxOffsetY + paneSizeY > pxSizeY)
        			pxOffsetY = pxSizeY - paneSizeY;
        		
	    		map.draw(x - pxOffsetX, y - pxOffsetY);
		    	drawGrid(x, y, g);
    		}
	    	
	    	// Only draw Tokens present in the current window
	    	tokens.renderTokens(x, y, pxOffsetX, pxOffsetY, pxOffsetX + paneSizeX, pxOffsetY + paneSizeY, genUI.getIcons());
	    	
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
    public void update(int mXoffset, int mYoffset, int mapXsize, int mapYsize, GameContainer gc, int delta) throws SlickException {

        // Added by Mac
        // search for the update buffer
        List<String> tempList;
        synchronized (sharedLock) {
            tempList = new ArrayList<String>(changeBuffer);
        }

        if (tempList.size() > 0) {
            List<String> nealNeedsMoreWater = new ArrayList<String>();
            for (String s : tempList) {
                String[] data = s.split("~");
                tokens.addToken(data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                nealNeedsMoreWater.add(s);
            }
            synchronized (sharedLock) {
                changeBuffer.removeAll(nealNeedsMoreWater);
            }
        }

    	Input in = gc.getInput();
    	
    	if(in.isKeyDown(in.KEY_LSHIFT))
    		System.out.println("SHIFT IS DOWN HELLA");
    	
    	mouseX = in.getMouseX();
    	mouseY = in.getMouseY();
    	paneSizeX = mapXsize;
    	paneSizeY = mapYsize;
    	
    	currentGridX = (int)((mouseX + pxOffsetX - mXoffset) / tokenScale);
    	currentGridY = (int)((mouseY + pxOffsetY - mYoffset) / tokenScale);
    	
        if (in.isKeyPressed(in.KEY_0))
            genUI.control.importFiles();

        if (in.isKeyPressed(in.KEY_9)) {
            genUI.control.sendFile("/temp/python/stuff.txt", "");
            System.out.println("sendfilesmannananna");
        }
    	
    	// If the file chooser is active, update
    	if (fileChooser.isActive()) {
    		fileChooser.update(in, mXoffset, mYoffset, paneSizeX, paneSizeY);  //TODO: FIX FILECHOOSER
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
	    	if ((mouseX >= mXoffset && mouseX <= mXoffset + paneSizeX) && (mouseY >= mYoffset && mouseY <= mYoffset + paneSizeY)) {
	    		
	    		// If LeftMouse is down, initialize or maintain a drag
	    		if(in.isMouseButtonDown(0)) {
	    			// If the space is occupied, initialize a Token Drag
	    			if (dragMode == 0) {
	    				if (tokens.isOccupied(currentGridX, currentGridY)) {
		    				dragMode = 2;
	    					dragToken = new Point(currentGridX, currentGridY);
		    				dragImage = tokens.getImage(currentGridX, currentGridY).copy();
	    					dragImage.setAlpha(0.75f);
	    					//set the selected token
	    					selectedToken = tokens.getToken(dragToken.x, dragToken.y);
		    			}		
		    		
	    			// If the tile is empty, initialize a Map Drag
	    				else {
	    					dragMode = 1;
	    					this.setDrag(in.getMouseX(), in.getMouseY());
	    					//de-select the token
	    					selectedToken = null;
	    				}	
	    			}
	    			
	    			// If the draw mode has been set to 1, drag the map
	    			if (dragMode == 1)
	    				dragMap(in.getMouseX(), in.getMouseY());
	    		}
	    		
	    		
	    		// If LeftMouse is not down, but a state is set, clear the state appropriately
	    		else if(dragMode == 1) {
	    			dragMode = 0;
	    		}
	    		else if(dragMode == 2) {
	    			// Indicates the mouse was released from a token drag.  Attempt to move the token
	    			tokens.moveToken((int)dragToken.getX(), (int)dragToken.getY(), currentGridX, currentGridY);

                    // added by Mac
                    try {
                        genUI.control.moveToken((int)dragToken.getX(), (int)dragToken.getY(), currentGridX, currentGridY);
                    } catch (IOException ioe) {}
                    
	    			dragToken = null;
	    			dragImage = null;
	    			dragMode = 0;
	    		}
	    			
		        
// Modes have not been set, menus are not in use - addition click functions (right/middle) go here
	    		
	    		// [Right Click] - Check for token
		        if (in.isMousePressed(1)) {
		        	if(tokens.isOccupied(currentGridX, currentGridY)) {
		        		System.out.println("You have clicked on a token");
		        		return;
		        	}
		            
		        	options.resetPort();
			        options.setX(mouseX);
			        options.setY(mouseY);
			        options.setActive(true);
			    }
	    	
		        // [Middle Click] - Delete a Token
		        if (in.isMousePressed(2))
		        	removeTokenCoord(mouseX - mXoffset, mouseY - mYoffset);
		        
		        
		        // [MouseWheel Polling] - Used to scale and orient the grid (via Shift/Control/Arrowkeys)
		        if(in.isKeyDown(in.KEY_LCONTROL)) {
		        	mapScaling = true;
		        	int scaleshift = Mouse.getDWheel();
		        	Point2D.Float originshift = new Point2D.Float();
		        	
		        	
		        	if (in.isKeyDown(in.KEY_LEFT))
		        		originshift.x--;
		        	if (in.isKeyDown(in.KEY_RIGHT))
		        		originshift.x++;
		        	if (in.isKeyDown(in.KEY_UP))
		        		originshift.y--;
		        	if (in.isKeyDown(in.KEY_DOWN))
		        		originshift.y++;
		        	
		        	if (scaleshift != 0)
		        		scaleshift = (scaleshift / Math.abs(scaleshift));
		        	
		        	if(in.isKeyDown(in.KEY_LSHIFT)) {
		        		tokenScale += (float)(scaleshift / 10.0);
		        		originshift.x /= 10.0f;
		        		originshift.y /= 10.0f;
		        	}
		        	else {
		        		tokenScale += (float)(scaleshift);
		        	}	
		        	
		        	gridOrigin.x += originshift.x;
		        	gridOrigin.y += originshift.y;
		        	this.setScale(this.getScale() + scaleshift);
		        }
		        else {
		        	if (mapScaling == true) {
		        		mapScaling = false;
		        		// CONTROLLER.BROADCASTSCALECHANGE (this.getScale())  or 'tokenScale'
		        	}
		        		
		        }
	    	}
	    }
    }
    
    
    // Testing this, moves the map immediately
    public void move(float tileX, float tileY){
    	//pxOffsetX = (int)(tileX * tokenScale);
    	//pxOffsetY = (int)(tileY * tokenScale);
    	pxOffsetX = tileX;
    	pxOffsetY = tileY;
    }
    
    
    // Returns the Grid/Token scale
    public float getScale() {
    	return tokenScale;
    }
    
    // Sets the Grid/Token scale
    public void setScale(float tokenScale) {
    	this.tokenScale = tokenScale;
    	tokens.scaleTokens(tokenScale);
    }
    
    // Increments/Decrements the Grid/Token scale (used in conjunction with live mousewheel input)
    public void changeScale(int change) {
    	if (change > 0  &&  tokenScale < 256)
    		tokenScale += 0.1;
    	else if (change < 0  &&  tokenScale > 8)
    		tokenScale -= 0.1;
    	tokens.scaleTokens(tokenScale);
    	System.out.println("Changed scale: " + change);
    }
    
    
    
    // Don't ever use this thing
    public void zoomMap(int change) {
    	if (change > 1  && mapScale < 8) {
    		mapScale = mapScale * 1.25f;
    		if (mapScale > 8)
    			mapScale = 8;
    	}
    	else if (change < 1  &&  mapScale > 0.25f) {
    		mapScale = mapScale / 0.8f;
    		if (mapScale < 0.25f)
    			mapScale = 0.25f;
    	}
    }
    
    
    
    // Gets token currently selected in MapPane
    public Token getSelectedToken() {
    	return selectedToken;
    }
    
    
    
    // Sets SwingFileChooser active, accepts mouse coordinates where the resource should be placed
    public void selectToken(int x, int y) {
    	fileChooser.setActive("Tokens");
    	loadMode = 2;
    	tempGridX = currentGridX;
    	tempGridY = currentGridY;
    }
    
    public void selectMap() {
    	fileChooser.setActive("Maps");
    	loadMode = 1;
    }
    
    
    // Add a Token by map coordinate
    public void addTokenCoord(String imglocation, int x, int y) throws SlickException {
    	this.addTokenGrid(imglocation, (int)((x + pxOffsetX) / tokenScale), (int)((y + pxOffsetY) / tokenScale));

        // added by Mac
        try {
            genUI.control.addToken(imglocation, x, y);
        } catch (IOException ioe) {}
    }
    
    
    // Add a Token by grid location
    public void addTokenGrid(String imglocation, int x, int y) throws SlickException {
    	tokens.addToken(imglocation, x, y);
        
        // added by Mac
        try {
            genUI.control.addToken(imglocation, x, y);
        } catch (IOException ioe) {}
    }
    
    
    // Remove a token by coordinate
    public void removeTokenCoord(int x, int y) {
    	this.removeTokenGrid((int)((x + pxOffsetX) / tokenScale), (int)((y + pxOffsetY) / tokenScale));
    }
    
    
    // Remove a Token by grid location
    public void removeTokenGrid(int x, int y) {
    	tokens.removeToken(x, y);

        // added by Mac
        try {
            genUI.control.removeToken(x, y);
        } catch (IOException ioe) {}
    }
    
    
    private void drawGrid(float getX, float getY, Graphics g) {
    	for (float x = 0; x < pxSizeX; x += tokenScale)
    		g.drawLine(getX + x - pxOffsetX, getY - pxOffsetY, getX + x - pxOffsetX, getY - pxOffsetY + pxSizeY);
    	
    	for (float y = 0; y < pxSizeY; y += tokenScale)
    		g.drawLine(getX - pxOffsetX, getY + y - pxOffsetY, getX + pxOffsetX + pxSizeX, getY + y - pxOffsetY);
    }
    
    
    
    
    
    // =======================================================================
    // Methods used the controller (client<->host operations)
    
    // Add a token to this map with default size (1)
    public void addToken(String file, int x, int y, String tokenname) {
    	tokens.addToken(tokenname, file, x, y);
    }
    
    // Add a token to this map with custom size
    public void addToken(String file, int x, int y, int size, String tokenname) {
    	tokens.addToken(tokenname, file, x, y, size);
    }

    // added by Mac - used for client adding
    public void addToken(String message) {
        synchronized (sharedLock) {
            changeBuffer.add(message);
        }
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
    public boolean moveToken(int x1, int y1, int x2, int y2 ) {
        return tokens.moveToken(x1, y1, x2, y2);
    }
    
    // Removes a token by its given name
    public boolean removeToken(int x, int y) {

        // changed by Mac
        return tokens.removeToken(x, y);
    }
    
    public String getBackground() {
    	return map.getResourceReference();
    }
}

