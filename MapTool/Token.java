package MapTool;

import MapTool.Map;
import MapTool.Tile;
import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class Token {
	
	Map map; 
	Image pic; 
	boolean hidden; 
	Vector2f location;
	ArrayList<Tile> tiles; 
	int tokenX,	tokenY, width;

	public Token(Image pic, int pixelX, int pixelY) {

		this.pic = pic;
		tokenX = pic.getWidth();
		tokenY = pic.getHeight();
		width = tokenX / map.tokenX;
	}

	public int getWidth() {
		return width;
	}

	public int getX() {
		return tokenX;
	}

	public int getY() {
		return tokenY;
	}

	public ArrayList<Tile> getTiles() {
		return tiles;
	}

	public void addTile(Tile t) {
		tiles.add(t);
	}

	public void clearTiles() {
		tiles.clear();
	}

	public boolean isHidden() {
		return hidden;
	}

	public void toggleHidden() {
		hidden = !hidden;
	}

	public Image getImage() {
		return pic;
	}

	public Vector2f getLocation() {
		return location;
	}
}