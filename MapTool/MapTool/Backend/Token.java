package Backend;

import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * This class serves as a token that can be placed on the map
 *
 * @author Mac Reid
 */
class Token {
	
	private Map map; 
	private Image pic; 
	private String name;
	private int x, y, width;
	private ArrayList<Tile> tiles; 
	private boolean hidden, hasBeenHidden; 

	/**
	 * Constructor that takes a representative image for the token and the 
	 * pixel coordinates that the token is being added at
	 *
	 * @param pic The image that this object represents
	 * @param x The x coordinate on the map that the token goes on
	 * @param y The y coordinate on the map that the token goes on
	 * @param name The name of this token
	 */
	public Token(Image pic, int x, int y, int width, String name) {

		this.x = x;
		this.y = y;
		this.pic = pic;
		this.name = name;
		this.width = width;
		hasBeenHidden = false;
		tiles = new ArrayList<Tile>();
	}

	/**
	 * Adds a tile to add to the list of the token occupies
	 *
	 * @param t The tile to add to the list of the token occupies
	 */
	public void addTile(Tile t) {
		tiles.add(t);
	}

	/**
	 * Clears the list of tiles the token occupies
	 */
	public void clearTiles() {
		tiles.clear();
	}

	/**
	 * Returns the image that this token represents
	 *
	 * @return The image that this token represents
	 */ 
	public Image getImage() {
		return pic;
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns the list of tiles the token occupies
	 *
	 * @return The list of tiles the token occupies
	 */
	public ArrayList<Tile> getTiles() {
		return tiles;
	}

	/**
	 * Returns the width of the token in number of tiles
	 * 
	 * @return The width of the token in number of tiles
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the y coordinate of the token in the map
	 *
	 * @return The y coordinate of the token in the map
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y coordinate of the token in the map
	 *
	 * @return The y coordinate of the token in the map
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns whether the token is hidden from the player's view
	 *
	 * @return Whether the token is hidden from the player's view
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * Returns whether or not this token has been hidden. Only applies to 
	 * tokens of size 2x2 or greater
	 *
	 * @return Whether or not this tokens has been hidden
	 */
	public boolean hasBeenHidden(){
		return hasBeenHidden;
	}

	/**
	 * Sets this token's hidden this pass value. Pertains to tokens of size 
	 * 2x2 or greater as the toggleHidden function in map hits all tiles,
	 * including multiple parts of the same token. This ensures the token 
	 * stays hidden or stays visible.
	 *
	 * @param b The change whether this token has been hidden
	 */
	public void setHasBeenHidden(boolean b) {
		hasBeenHidden = b;
	}

	/**
	 * Toggles the state of hidden of the token
	 */
	public void toggleHidden() {
		hidden = !hidden;
	}

	@Override
	public String toString() {
		return Integer.toString(x)   + "|"
		     + Integer.toString(y)   + "|" 
		     + String.valueOf(width) + "|"
		     + hidden                + "|"
		     + name;
	}
}