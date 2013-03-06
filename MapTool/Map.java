package MapTool;

import MapTool.Tile;
import MapTool.Token;
import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class Map {

	Tile[][] tiles;
	Image background;
	ArrayList<Token> tokens;
	int backgroundX, backgroundY, tokenX, tokenY, tileX, tileY;

	/**
	 * Constructor that takes the x,y coordinates of the background
	 * image and takes the x,y coordinates of the token size
	 * 
	 * @param mapX The width of the background image
	 * @param mapY The height of the background image
	 * @param tokenX the width of the tokens
	 * @param tokenY the hight of the tokens
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
	}

	/**
	 * Returns the tile at the given x,y pair that is in the map
	 *
	 * @param tileX The x coordinate of the wanted tile
	 * @param tileY The y coordinate of the wanted tile
	 * @return      The tile at the given x,y pair in the map
	 */
	public Tile get(int tileX, int tileY) {
		return tiles[tileX][tileY];
	}

	/**
	 * Wrapper method for the private move method
	 *
	 * @param t The token in question to move
	 * @param pixelX The x coordinate on screen that the token goes on
	 * @param pixelY The y coordinate on screen that the token goes on
	 */
	public void move(Token t, int pixelX, int pixelY) {
		move(t, pixelX, pixelY, false);
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

	public Token removeToken(Token t) {
		// remove the token here
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

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
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
	 * This method moves tokens by determining their tile size (1x1, 2x2, etc)
	 * and removes the tile from its current location on the map and moves it 
	 * to the appropriate tile location, given the coordinates. 
	 *
	 * @param t The token in question to move
	 * @param pixelX The x coordinate on screen that the token goes on
	 * @param pixelY The y coordinate on screen that the token goes on
	 */
	private void move(Token t, int pixelX, int pixelY, boolean first) {

		if (!first) {
			for (int i = 0; i < t.getTiles().size(); i++) {
				t.getTiles().get(i).toggleOccupation();
				t.getTiles().get(i).setToken(null);
			}
			t.clearTiles();
		}	

		int x = pixelX / tokenX;
		int y = pixelY / tokenY;

		for (int i = 0; i <= t.getWidth(); i++) {
			for (int j = 0; j <= t.getWidth(); j++) {
				t.addTile(get(x + i, y + j));
				get(x + i, y + j).toggleOccupation();
				get(x + i, y + j).setToken(t);
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
