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
	boolean visible[][] = null;
	
	ArrayList<Token> tokens = new ArrayList<Token>();
	
	float mapoffsetx = 0;
    float mapoffsety = 0;
    
    int mapxsize = 17;
    int mapysize = 13;
    
    int mappxwidth = 0;
    int mappxheight = 0;
    
    boolean mapdrag = false;
    int mapdragx = 0;
    int mapdragy = 0;
    int dragoffsetx = 0;
    int dragoffsety = 0;
    
    MapOptions options;
    
    // Default  constructor - loads example resources
    public MapPane() throws SlickException {
    	loadMap("Resources/Dwarfort.png");
    	mapgrid = new Image("Resources/GreyGrid.png");
    	tokens.add(new Token("Resources/mslug.png", 2, 2));
    	tokens.add(new Token("Resources/banshee.png", 6, 4));
    	tokens.add(new Token("Resources/goomba.png", 7, 6));
    }
    
    public MapPane(String maplocation) throws SlickException {
    	map = new Image("maplocation");
    }
    
    public MapPane(String maplocation, int sizex, int sizey) throws SlickException {
    	loadMap(maplocation);
    	mapxsize = sizex;
    	mapysize = sizey;
    	//added by Neal
    	mapgrid = new Image("Resources/GreyGrid.png");
    	options = new MapOptions(0, 0, this);
    }
    
    private void loadMap(String mapname) throws SlickException {
    	map = new Image(mapname);
    	mappxwidth = map.getWidth();
    	mappxheight = map.getHeight();
    }
    
    public void mapLeft() {
    mapoffsetx += 48;
    }
    
    public void mapRight() {
    mapoffsetx -= 48;
    }
    
    public void mapUp() {
    mapoffsety += 48;
    }
    
    public void mapDown() {
    mapoffsety -= 48;
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
    			mapoffsetx += dragoffsetx;
    			dragoffsetx = 0;
    			mapdragx = x;
    			
    			if(mapoffsetx < 0)
    				mapoffsetx = 0;
    			else if(mapoffsetx > ((mappxwidth - (mappxwidth % 48)) - (mapxsize * 48)))
    				mapoffsetx = ((mappxwidth - (mappxwidth % 48)) - (mapxsize * 48));
    		}
    		
    		if (dragoffsety != 0) {
    			mapoffsety += dragoffsety;
    			dragoffsety = 0;
    			mapdragy = y;
    			
    			if(mapoffsety < 0)
    				mapoffsety = 0;
    			else if(mapoffsety > ((mappxheight - (mappxheight % 48)) - (mapysize * 48)))
    				mapoffsety = ((mappxheight - (mappxheight % 48)) - (mapysize * 48));
    		}    		
    	}
    }
    
    // Draws current background and Tokens
    public void renderMap(float x, float y, int xSize, int ySize, Graphics g) {
    	map.draw(x, y, x + (xSize * 48), y + (ySize * 48),
    	   mapoffsetx, mapoffsety,
    	   mapoffsetx+ (xSize * 48), mapoffsety+ (ySize * 48));
    	//draw grid
    	drawGrid(x, y, xSize, ySize);
    	// Only draw Tokens present in the current window
    	int lowx = (int)mapoffsetx / 48;
    	int lowy = (int)mapoffsety / 48;
    	for(int i = 0; i < tokens.size(); i++) {
    		if ((tokens.get(i).getX() >= lowx) && (tokens.get(i).getX() <= (lowx + mapxsize - 1)))
    			if ((tokens.get(i).getY() >= lowy) && (tokens.get(i).getY() <= (lowy + mapysize - 1)))
    				tokens.get(i).renderToken(x - mapoffsetx, y - mapoffsety);
    	}
    	
    	//draw options
    	options.render(g);
    	
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
    	if(options.isActive()) options.update(gc);
    	//The constant 5 right now is the buffer from editor
    	if ((mouseX >= mXoffset && mouseX <= mXoffset + mapxsize*48) && (mouseY >= mYoffset && mouseY <= mYoffset + mapysize*48)) {
        	
        	// If the mouse is pressed down, drag the map, but only if no menu is open
	        dragMap(in.isMouseButtonDown(0), in.getMouseX(), in.getMouseY());
	        // For a right click, check for token
	        if (in.isMousePressed(1)) {
	        	for(Token t : tokens) {
	        		//if both if statements are true, a token is there
	        		if(t.getX() == mouseX/48 + mapoffsetx) {
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
    
    //Testing this, moves the map immediately
    public void move(float tileX, float tileY){
    	mapoffsetx = tileX * 48;
    	mapoffsety = tileY * 48;
    }
    
    public void resize(int tileWidth,int tileHeight){
    	//if the screen got wider compensate so no blank grid
    	if(tileWidth > mapxsize && mapoffsetx > 0){
    		mapoffsetx -= (tileWidth - mapxsize)*48;
    	}
    	//if the screen got wider compensate so no blank grids
    	if(tileHeight > mapysize && mapoffsety > 0){
    		mapoffsety -= (tileHeight - mapysize)*48;
    	}
    	mapxsize = tileWidth;
    	mapysize = tileHeight;
    	
    	if(mapoffsety > (map.getHeight() - (mapysize * 48)))
			mapoffsety = (map.getHeight() - (mapysize * 48));
    }
    
    
    // Add a Token by map coordinate
    public void addTokenCoord(String imglocation, int x, int y) throws SlickException {
    	tokens.add(new Token(imglocation, (int)((x + mapoffsetx) / 48), (int)((y + mapoffsety) / 48)));
    }
    
    
    // Add a Token by grid location
    public void addTokenGrid(String imglocation, int x, int y) throws SlickException {
    	tokens.add(new Token(imglocation, x, y));
    }
    
    
    // Remove a token by coordinate
    public void removeTokenCoord(int x, int y) {
    	for(int i = 0; i < tokens.size(); i++) {
    		if (((int)(x + mapoffsetx) / 48) == tokens.get(i).getX() && ((int)(y + mapoffsety) / 48) == tokens.get(i).getY())
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

