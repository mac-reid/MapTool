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

	private Tile[][] tiles;
	private Image background;
	private ArrayList<Token> tokens;

	// the background variables are the size of the background image
	// the token variables are the size of the tiles
	// the tile variables are the number of tiles the map conists of
	private int backgroundX, backgroundY, tokenX, tokenY, tileX, tileY;

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
	public void addToken(Image pic, int pixelX, int pixelY, String name) {

		tokens.add(new Token(pic, pixelX, pixelY, tokenX, name));
		move(tokens.get(tokens.size() - 1), pixelX, pixelY, true);
	}

	public void addToken(Image pic, int pixelX, int pixelY, String name, int m) {

		tokens.add(new Token(pic, pixelX, pixelY, tokenX * m, name));
		move(tokens.get(tokens.size() - 1), pixelX, pixelY, true);
	}


	public Token getToken(int x, int y) {
		return tiles[x / tokenX][y / tokenY].getToken();
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
	 * @param pixelX The x coordinate on screen that the token goes on
	 * @param pixelY The y coordinate on screen that the token goes on
	 * @return       If the token was able to move
	 */
	private boolean move(Token t, int pixelX, int pixelY, boolean first) {

		// the pair x,y is the pixel coordinate to move to
		int x = pixelX / tokenX;
		int y = pixelY / tokenY;

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
	 * @param startX The x coordinate of the starting box for selecting tokens
	 * @param startY The y coordinate of the starting box for selecting tokens
	 * @param endX The x coordinate of the end of the box for selecting tokens
	 * @param endY The y coordinate of the end of the box for selecting tokens
	 */
	private void toggleHideArea(boolean hide, int startX, int startY, int endX,
			int endY){

		int x1 = startX / tokenX;
		int y1 = startY / tokenY;
		int x2 = endX / tokenX;
		int y2 = endY / tokenY;

		for (int i = 0; i < tileX; i++) {
			for (int j = 0; j < tileY; j++) {
				if (i >= x1 && i <= x2 && j >= y1 && j <= y2) {
					if (tiles[i][j].isOccupied()) {
						if (tiles[i][j].getToken().notToggled()) {
							if (hide && !tiles[i][j].getToken().isHidden()) {
								tiles[i][j].getToken().toggleHidden();
								tiles[i][j].getToken().toggleToggle(false);
							}
							else if (tiles[i][j].getToken().isHidden()) {
								tiles[i][j].getToken().toggleHidden();
								tiles[i][j].getToken().toggleToggle(false);
							}
						}
					} 
				}
			}
		}

		for (Token t : tokens) 
			t.toggleToggle(true);
	}

	/**
	 * Test method
	 */
	public static void main(String[] args) {

	/*
		System.out.println("--------------------------------------------------");

		Map m = new Map(480, 480, 48, 48);
		m.addToken(null, 48, 48, "abc");
		m.addToken(null, 96, 96, "fff");

		m.move(m.getToken(48, 48), 96, 48);
		m.printMap();
		System.out.println("--------------------------------------------------");
		m.addToken(null, 144, 144, "dig", 2);
		m.printMap();
		System.out.println("--------------------------------------------------");
		m.move(m.getToken(192, 192), 0, 192);
		m.printMap();
		System.out.println("--------------------------------------------------");
		m.hideArea(0, 0, 192, 192);
		m.printMap();
		System.out.println("--------------------------------------------------");
		m.unHideArea(0, 0, 144, 144);
		m.printMap();
		System.out.println("--------------------------------------------------");
	*/
	}

}
