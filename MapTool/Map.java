package MapTool;

import org.newdawn.slick.Image;
import java.util.ArrayList;
import MapTool.Token;
import MapTool.Tile;

public class Map {

	Tile[][] tiles;
	ArrayList<Token> tokens;
	int backgroundX, backgroundY, tokenX, tokenY, tileX, tileY;

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

	public Tile get(int tileX, int tileY) {
		return tiles[tileX][tileY];
	}

	public void move(Token t, int pixelX, int pixelY) {

		for (int i = 0; i < t.getTiles().size(); i++) {
			t.getTiles().get(i).toggleOccupation();
			t.getTiles().get(i).setToken(null);
		}

		t.clearTiles();
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

	public void addToken() {

	}

	public static void main(String[] args) {

		System.out.println("No Compile Errors, yay.");
	}
}
