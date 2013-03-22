package Andrews;

import Andrews.Token;

/**
 * This class represents a tile on the map and only serves as a storage
 * place for any tokens that may appear on the Map
 * 
 * @author Mac Reid
 */
public class Tile {
	
	private int x, y;
	private Token unit;
	private boolean occupied;

	/**
	 * Basic constructor that makes an unoccupied tile
	 */
	public Tile(int x, int y) {

		this.x = x;
		this.y = y;
		unit = null;
		occupied = false;
	}

	/**
	 * Basic constructor that makes an occupied tile
	 * 
	 * @param unit The token that will occupy this tile
	 */
	public Tile(Token unit) {

		occupied = true;
		this.unit = unit;
	}

	/**
	 * Returns the token stored in the tile
	 *
	 * @return the token stored in the tile
	 */
	public Token getToken() {
		return unit;
	}

	/**
	 * Returns the current state of occupation
	 *
	 * @return the current state of occupation
	 */
	public boolean isOccupied() {
		return occupied;
	}

	/**
	 * Toggles the tile's state of occupation and returns the previous state
	 *
	 * @return Returns the previous state of occupation
	 */
	public boolean toggleOccupation() {

		boolean temp = occupied;
		occupied = !occupied;
		return temp;
	}

	/**
	 * Places a given token in this tile
	 *
	 * @param newToken The new token to occupy this tile
	 */
	public void setToken(Token newToken) {
		unit = newToken;
	}

	/**
	 * Returns the x coordinate of the tile in the map
	 *
	 * @return The x coordinate of the tile in the map
	 */
	public int X() {
		return x;
	}

	/**
	 * Returns the y coordinate of the tile in the map
	 *
	 * @return The y coordinate of the tile in the map
	 */
	public int Y() {
		return y;
	}

}