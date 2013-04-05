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
	
	ArrayList<CharacterTile> tiles = new ArrayList<CharacterTile>();
	
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
    	tiles.add(new CharacterTile("Resources/mslug.png", 2, 2));
    	tiles.add(new CharacterTile("Resources/banshee.png", 6, 4));
    	tiles.add(new CharacterTile("Resources/goomba.png", 7, 6));
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
    		// If empty tile, drag the map
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
    
    // Draws current background and tiles
    public void renderMap(float x, float y, int xSize, int ySize, Graphics g) {
    	map.draw(x, y, x + (xSize * 48), y + (ySize * 48),
    	   mapoffsetx, mapoffsety,
    	   mapoffsetx+ (xSize * 48), mapoffsety+ (ySize * 48));
    	//draw grid
    	drawGrid(x, y, xSize, ySize);
    	// Only draw tiles present in the current window
    	int lowx = (int)mapoffsetx / 48;
    	int lowy = (int)mapoffsety / 48;
    	for(int i = 0; i < tiles.size(); i++) {
    		if ((tiles.get(i).getX() >= lowx) && (tiles.get(i).getX() <= (lowx + mapxsize - 1)))
    			if ((tiles.get(i).getY() >= lowy) && (tiles.get(i).getY() <= (lowy + mapysize - 1)))
    				tiles.get(i).renderTile(x - mapoffsetx, y - mapoffsety);
    	}
    	
    	//draw options
    	options.render(g);
    	
    }
    
    /**
     * An update method
     * @param in
     * @param delta
     * @throws SlickException 
     * @todo add character tile menu action
     */
    public void update(GameContainer gc, int delta, int mXoffset, int mYoffset) throws SlickException{
    	Input in = gc.getInput();
    	int mouseX = in.getMouseX();
    	int mouseY = in.getMouseY();
    	if(options.isActive()) options.update(gc);
    	//The constant 5 right now is the buffer from editor
    	if ((mouseX >= mXoffset && mouseX <= mXoffset + mapxsize*48) && (mouseY >= mYoffset && mouseY <= mYoffset + mapysize*48)) {
        	
        	// If the mouse is pressed down, drag the map, but only if no menu is open
	        dragMap(in.isMouseButtonDown(0), in.getMouseX(), in.getMouseY());
	        // For a right click, check for token
	        if (in.isMousePressed(1)){
	        	for(CharacterTile tile : tiles){
	        		//if both if statements are true, a token is there
	        		if(tile.getX() == mouseX/48 + mapoffsetx){
	        			if(tile.getY() == mouseY/48 + mapoffsety){
	        				System.out.println("You have clicked on a token");
	        				return;
	        			}
	        		}
	        	}
	        	options.resetPort();
	        	options.setX(mouseX);
	        	options.setY(mouseY);
	        	options.setActive(true);
	        	//commenting out the dude for now, kinda messy with the menu
	        	//addTileCoord("Resources/mslug.png", mouseX - 5, mouseY - 5);
	        }
	        // For a middle click, delete a Token
	        if (in.isMousePressed(2))
	        	removeTileCoord(mouseX - 5, mouseY - 5);
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
    
    // Add a tile by map coordinate
    public void addTileCoord(String imglocation, int x, int y) throws SlickException {
    	tiles.add(new CharacterTile(imglocation, (int)((x + mapoffsetx) / 48), (int)((y + mapoffsety) / 48)));
    }
    
    // Add a tile by map coordinate (using an image)
    public void addTileCoord(Image image, int x, int y) throws SlickException {
    	tiles.add(new CharacterTile(image, (int)((x + mapoffsetx) / 48), (int)((y + mapoffsety) / 48)));
    }
    
    // Add a tile by grid location
    public void addTileGrid(String imglocation, int x, int y) throws SlickException {
    	tiles.add(new CharacterTile(imglocation, x, y));
    }
    
    // Remove a tile by coordinate
    public void removeTileCoord(int x, int y) {
    	for(int i = 0; i < tiles.size(); i++) {
    		if (((int)(x + mapoffsetx) / 48) == tiles.get(i).getX() && ((int)(y + mapoffsety) / 48) == tiles.get(i).getY())
    			tiles.remove(i);
    	}
    }
    
    // Remove a tile by grid location
    public void removeTileGrid(int x, int y) {
    	for(int i = 0; i < tiles.size(); i++) {
    		if (x == tiles.get(i).getX() && y == tiles.get(i).getY())
    			tiles.remove(i);
    	}
    }
    
    private void drawGrid(float x, float y, int width, int height) {
    	for (int i = 0; i < width; i++)
    		for (int j = 0; j < height; j++) {
    			mapgrid.draw(x + i * 48, y + j * 48);
    		}
    }
    
    
}

