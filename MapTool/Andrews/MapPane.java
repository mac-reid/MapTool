package Andrews;
import org.newdawn.slick.Image;
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
    
    boolean mapdrag = false;
    int mapdragx = 0;
    int mapdragy = 0;
    int dragoffsetx = 0;
    int dragoffsety = 0;
    
    // Default  constructor - currently loads example resources
    public MapPane() throws SlickException {
    	map = new Image("Resources/Dwarfort.png");
    	mapgrid = new Image("Resources/GreyGrid.png");
    	tiles.add(new CharacterTile("Resources/mslug.png", 2, 2));
    	tiles.add(new CharacterTile("Resources/banshee.png", 6, 4));
    	tiles.add(new CharacterTile("Resources/goomba.png", 7, 6));
    }
    
    public MapPane(String maplocation) throws SlickException {
    	map = new Image("maplocation");
    }
    
    public MapPane(String maplocation, int sizex, int sizey) throws SlickException {
    	map = new Image(maplocation);
    	mapxsize = sizex;
    	mapysize = sizey;
    	//added by Neal
    	mapgrid = new Image("Resources/GreyGrid.png");
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
    			else if(mapoffsetx > (map.getWidth() - (mapxsize * 48)))
    				mapoffsetx = (map.getWidth() - (mapxsize * 48));
    		}
    		
    		if (dragoffsety != 0) {
    			mapoffsety += dragoffsety;
    			dragoffsety = 0;
    			mapdragy = y;
    			
    			if(mapoffsety < 0)
    				mapoffsety = 0;
    			else if(mapoffsety > (map.getHeight() - (mapysize * 48)))
    				mapoffsety = (map.getHeight() - (mapysize * 48));
    		}    		
    	}
    }
    
    // Draws current background and tiles
    public void renderMap(float x, float y, int xSize, int ySize) {
    	map.draw(x, y, x + (xSize * 48), y + (ySize * 48),
    	   mapoffsetx, mapoffsety,
    	   mapoffsetx+ (xSize * 48), mapoffsety+ (ySize * 48));
    	
    	drawGrid(x, y, xSize, ySize);
    	
    	// Only draw tiles present in the current window
    	int lowx = (int)mapoffsetx / 48;
    	int lowy = (int)mapoffsety / 48;
    	for(int i = 0; i < tiles.size(); i++) {
    		if ((tiles.get(i).getX() >= lowx) && (tiles.get(i).getX() <= (lowx + mapxsize - 1)))
    			if ((tiles.get(i).getY() >= lowy) && (tiles.get(i).getY() <= (lowy + mapysize - 1)))
    				tiles.get(i).renderTile(x - mapoffsetx, y - mapoffsety);
    	}
    	
    }
    
    // Add a tile by map coordinate
    public void addTileCoord(String imglocation, int x, int y) throws SlickException {
    	tiles.add(new CharacterTile(imglocation, (int)((x + mapoffsetx) / 48), (int)((y + mapoffsety) / 48)));
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
