package Backend;

import java.util.ArrayList;
import org.newdawn.slick.Image;

/**
 * This class is the basis for the storage of units on the grid based
 * map that is being displayed on the client's screen. This revision
 * assumes all data passed in uses ints for x,y coordinates on screen
 * 
 * @author Mac Reid
 */
class Map {

	private Tile[][] tiles;
	private Image background;
	private ArrayList<Token> tokens;

	// the background variables are the size of the background image
	// the tile variables are the number of tiles the map conists of
	// the tokenWidth variable is the width of the tokens
	private int backgroundX, backgroundY, tileX, tileY, tokenWidth;

	/**
	 * Constructor that takes the x,y coordinates of the background
	 * image and takes the x,y coordinates of the token size
	 * 
	 * @param mapX The width of the background image
	 * @param mapY The height of the background image
	 * @param tokenX The width of the tokens in number of pixels
	 * @param tokenWidth The width of the token in number of tiles
	 */
	public Map(int mapX, int mapY, int tokenX, Image pic) {

		background = pic;
		backgroundX = mapX;
		backgroundY = mapY;
		tokenWidth = tokenX;
		tileX = backgroundX / tokenX;
		tileY = backgroundY / tokenX;
		tiles = new Tile[tileX][tileY];
		tokens = new ArrayList<Token>();

		// initializes all of the tiles to unoccupied tiles
		for (int i = 0; i < tileX; i++) 
			for (int j = 0; j < tileY; j++) 
				tiles[i][j] = new Tile(i, j);
	}

	/** 
	 * Adds a new token to the map with a given Slick2d Image File
	 * and an x & y location on the Map. This uses the move method to 
	 * properly place the token in the appopriate tile locations
	 *
	 * @param pic The Slick2d Image file that the token displays
	 * @param x The x coordinate on screen that the token goes on
	 * @param y The y coordinate on screen that the token goes on
	 * @param name The name given to the token being added
	 */
	public void addToken(Image pic, int x, int y, String name) {

		tokens.add(new Token(pic, x, y, pic.getWidth() / tokenWidth, name));
		move(tokens.get(tokens.size() - 1), x, y, true);
	}

	/** 
	 * Adds a new token to the map with a given Slick2d Image File
	 * and an x & y location on the Map. This uses the move method to 
	 * properly place the token in the appopriate tile locations
	 *
	 * @param pic The Slick2d Image file that the token displays
	 * @param x The x coordinate on screen that the token goes on
	 * @param y The y coordinate on screen that the token goes on
	 * @param tokX The width of the token being added - used for test purposes
	 * @param name The name given to the token being added
	 */
	public void addToken(Image pic, int x, int y, int tokX, String name) {

		tokens.add(new Token(pic, x, y, tokX, name));
		move(tokens.get(tokens.size() - 1), x, y, true);
	}

	/**
	 * Returns the background image to display for this map
	 *
	 * @return The background image to display for this map
	 */
	public Image getBackground() {
		return background;
	}

	/**
	 * Returns the token at the given x,y coordinate of the map
	 *
	 * @param x The x coordinate of the tile in question
	 * @param y The y coordinate of the tile in question
	 * @return  The token at the given x,y coordinate of the map
	 */ 
	public Token getToken(int x, int y) {
		return tiles[x][y].getToken();
	}

	public Token getToken(String s) {
		
		for(Token t : tokens) 
			if (t.getName().equals(s))
				return t;
		return null;
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
	 * @param x The x coordinate on map that the token goes on
	 * @param y The y coordinate on map that the token goes on
	 * @return       If the token was able to move
	 */
	public boolean move(Token t, int x, int y) {
		return move(t, x, y, false);
	}

	// test only
	public void printMap() {
	
		int schmerp = 0;
		int derp = 0;
		int troll = 1;

		for (int j = 0; j < tileY; j++) {
	
			for (int i = 0; i < tileX; i++) {
				System.out.print("[");

				if (tiles[i][j].getToken() != null && 
				    !tiles[i][j].getToken().isHidden()) 
					System.out.format("%3s", tiles[i][j].getToken().getName());
				else 
					System.out.print("   ");
				
				System.out.print("]");
			}

			System.out.println("");
		}	

		for (Token t : tokens)
			System.out.print(t.getName() + ", ");
		System.out.println("");
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
	 * @param x The x coordinate on the map that the token goes on
	 * @param y The y coordinate on the map that the token goes on
	 * @return       If the token was able to move
	 */
	private boolean move(Token t, int x, int y, boolean first) {

		// check if destination is occupied
		for (int i = 0; i < t.getWidth() - 1; i++)
			for (int j = 0; j < t.getWidth() - 1; j++)
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
		for (int i = 0; i <= t.getWidth() - 1; i++) {
			for (int j = 0; j <= t.getWidth() - 1; j++) {
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
	 * @param x1 The x coordinate of the starting box for selecting tokens
	 * @param y1 The y coordinate of the starting box for selecting tokens
	 * @param x2 The x coordinate of the end of the box for selecting tokens
	 * @param y2 The y coordinate of the end of the box for selecting tokens
	 */
	private void toggleHideArea(boolean hide, int x1, int y1, int x2, int y2) {

		for (int i = 0; i < tileX; i++) {
			for (int j = 0; j < tileY; j++) {
				if (i >= x1 && i <= x2 && j >= y1 && j <= y2) {
					if (tiles[i][j].isOccupied()) {
						if (!tiles[i][j].getToken().hasBeenHidden()) {
							if (hide && !tiles[i][j].getToken().isHidden()) {
								tiles[i][j].getToken().toggleHidden();
								tiles[i][j].getToken().setHasBeenHidden(true);
							}
							else if (tiles[i][j].getToken().isHidden()) {
								tiles[i][j].getToken().toggleHidden();
								tiles[i][j].getToken().setHasBeenHidden(true);
							}
						}
					} 
				}
			}
		}

		for (Token t : tokens) 
			t.setHasBeenHidden(false);
	}

}
