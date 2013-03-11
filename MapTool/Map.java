package MapTool;

import MapTool.Tile;
import MapTool.Token;
import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * This class is the basis for the storage of units on the grid based
 * map that is being displayed on the client's screen. This revision
 * assumes all data passed in uses ints for x,y coordinates on screen
 * 
 * @author Mac Reid
 */
public class Map {

	Tile[][] tiles;
	Image background;
	ArrayList<Token> tokens;

	// the background variables are the size of the background image
	// the token variables are the size of the tiles
	// the tile variables are the number of tiles the map conists of
	int backgroundX, backgroundY, tokenX, tokenY, tileX, tileY;

	/**
	 * Constructor that takes the x,y coordinates of the background
	 * image and takes the x,y coordinates of the token size
	 * 
	 * @param mapX The width of the background image
	 * @param mapY The height of the background image
	 * @param tokenX the width of the tokens
	 * @param tokenY the height of the tokens
	 */
	public Map(int mapX, int mapY, int tokenX, int tokenY) {

		backgroundX = mapX;
		backgroundY = mapY;

		this.tokenX = tokenX;
		this.tokenY = tokenY;

		tileX = backgroundX / tokenX;
		tileY = backgroundY / tokenY;

		tiles = new Tile[tileX][tileY];
		tokens = new ArrayList<Token>();

		// initializes all of the tiles to unoccupied tiles
		for (int i = 0; i < tileX; i++) 
			for (int j = 0; j < tileY; j++) 
				tiles[i][j] = new Tile();
	}

	/** 
	 * Adds a new token to the map with a given Slick2d Image File
	 * and an x & y location on the Map. This uses the move method to 
	 * properly place the token in the appopriate tile locations
	 *
	 * @param pic The Slick2d Image file that the token displays
	 * @param pixelX The x coordinate on screen that the token goes on
	 * @param pixelY The y coordinate on screen that the token goes on
	 */
	public void addToken(Image pic, int pixelX, int pixelY) {

		tokens.add(new Token(pic, pixelX, pixelY));
		move(tokens.get(tokens.size() - 1), pixelX, pixelY, true);
	}

	/**
	 * Wrapper method for toggleHideArea
	 * 
	 * @param startX The x coordinate of the starting box for selecting tokens
	 * @param startY The y coordinate of the starting box for selecting tokens
	 * @param endX The x coordinate of the end of the box for selecting tokens
	 * @param endY The y coordinate of the end of the box for selecting tokens
	 */
	public void hideArea(int startX, int startY, int endX, int endY) {
		toggleHideArea(true, startX, startY, endX, endY);
	}

	/**
	 * Returns the list of tokens stored in the map
	 *
	 * @return The list of tokens stored in the map
	 */
	public ArrayList<Token> getTokens() {
		return tokens;
	}

	/**
	 * Wrapper method for the private move method
	 *
	 * @param t The token in question to move
	 * @param pixelX The x coordinate on screen that the token goes on
	 * @param pixelY The y coordinate on screen that the token goes on
	 * @return       If the token was able to move
	 */
	public boolean move(Token t, int pixelX, int pixelY) {
		return move(t, pixelX, pixelY, false);
	}

	/**
	 * Removes a given token from the map; if the token exists this returns the 
	 * removed token, otherwise this returns null 
	 *
	 * @param t The token to be removed from the map
	 * @return  Whether the token was removed
	 */
	public boolean removeToken(Token t) {
		
		if (tokens.remove(t))
			for (int i = 0; i < t.getTiles().size(); i++) {
				t.getTiles().get(i).toggleOccupation();
				t.getTiles().get(i).setToken(null);
			}
		else 
			return false;
		return true;
	} 

	/**
	 * Wrapper method for toggleHideArea
	 * 
	 * @param startX The x coordinate of the starting box for selecting tokens
	 * @param startY The y coordinate of the starting box for selecting tokens
	 * @param endX The x coordinate of the end of the box for selecting tokens
	 * @param endY The y coordinate of the end of the box for selecting tokens
	 */
	public void unHideArea(int startX, int startY, int endX, int endY) {
		toggleHideArea(false, startX, startY, endX, endY);
	}

	/**
	 * This method moves tokens by determining their tile size (1x1, 2x2, etc)
	 * and removes the tile from its current location on the map and moves it 
	 * to the appropriate tile location, given the coordinates. 
	 *
	 * @param t The token in question to move
	 * @param pixelX The x coordinate on screen that the token goes on
	 * @param pixelY The y coordinate on screen that the token goes on
	 * @return       If the token was able to move
	 */
	private boolean move(Token t, int pixelX, int pixelY, boolean first) {

		// the pair x,y is the pixel coordinate to move to
		int x = pixelX / tokenX;
		int y = pixelY / tokenY;

		// check if destination is occupied
		for (int i = 0; i < t.getWidth(); i++)
			for (int j = 0; j < t.getWidth(); j++)
				if (tiles[x + i][y + i].isOccupied())
					return false;

		// remove token from its current position
		if (!first) {
			for (int i = 0; i < t.getTiles().size(); i++) {
				t.getTiles().get(i).toggleOccupation();
				t.getTiles().get(i).setToken(null);
			}
			t.clearTiles();
		}	

		// add the token to the destination tiles
		for (int i = 0; i <= t.getWidth(); i++) {
			for (int j = 0; j <= t.getWidth(); j++) {
				t.addTile(tiles[x + i][y + j]);
				tiles[x + i][y + j].toggleOccupation();
				tiles[x + i][y + j].setToken(t);
			}
		}

		return true;
	}

	/**
	 * This method hides or shows all the tokens in a given rectangle 
	 * 
	 * @param hide The determining factor if the area will hide or show tokens
	 * @param startX The x coordinate of the starting box for selecting tokens
	 * @param startY The y coordinate of the starting box for selecting tokens
	 * @param endX The x coordinate of the end of the box for selecting tokens
	 * @param endY The y coordinate of the end of the box for selecting tokens
	 */
	private void toggleHideArea(boolean hide, int startX, int startY, int endX,
			int endY){

		int x1 = startX / tileX;
		int y1 = startY / tileY;
		int x2 = endX / tileX;
		int y2 = endY / tileY;

		for (int i = 0; i < tileX; i++) {
			for (int j = 0; j < tileY; j++) {
				if (i >= x1 && i <= x2 && j >= y1 && j <= y2) {
					if (tiles[i][j].isOccupied()) {
						if (hide && !tiles[i][j].getToken().isHidden()) 
							tiles[i][j].getToken().toggleHidden();
						else if (tiles[i][j].getToken().isHidden())
							tiles[i][j].getToken().toggleHidden();
					} 
				}
			}
		}
	}

	/**
	 * Test method
	 */
	public static void main(String[] args) {

		Map m = new Map(1440, 900, 48, 48);
		System.out.println("No Compile Errors, yay.");
	}

}
