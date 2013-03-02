package MapTool;

import org.newdawn.slick.Image;
import java.util.ArrayList;
import MapTool.Map;
import MapTool.Tile;

public class Token {
	
	Map map;
	Image pic;
	ArrayList<Tile> tiles;
	int tokenX, tokenY, width;

	public Token(Image pic) {

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

	public static void main(String[] args) {

	}

}