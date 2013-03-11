package MapTool;

import MapTool.Map;
import MapTool.Tile;
import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * This class serves as a token that can be placed on the map
 *
 * @author Mac Reid
 */
public class Token {
	
	Map map; 
	Image pic; 
	boolean hidden; 
	Vector2f location;
	ArrayList<Tile> tiles; 
	int tokenX,	tokenY, width;

	/**
	 * Constructor that takes a representative image for the token and the 
	 * pixel coordinates that the token is being added at
	 *
	 * @param pic The image that this object represents
	 * @param pixelX The x coordinate on screen that the token goes on
	 * @param pixelY The y coordinate on screen that the token goes on
	 */
	public Token(Image pic, int pixelX, int pixelY) {

		this.pic = pic;
		tokenX = pic.getWidth();
		tokenY = pic.getHeight();
		width = tokenX / map.tokenX;
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

	/**
	 * Returns the location of the token on the map
	 *
	 * @return The location of the token on the map
	 */
	public Vector2f getLocation() {
		return location;
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
		return tokenX;
	}

	/**
	 * Returns the y coordinate of the token in the map
	 *
	 * @return The y coordinate of the token in the map
	 */
	public int getY() {
		return tokenY;
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
	 * Toggles the state of hidden of the token
	 */
	public void toggleHidden() {
		hidden = !hidden;
	}
}