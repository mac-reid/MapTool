package MapTool;

import org.newdawn.slick.Image;
import java.util.ArrayList;
import MapTool.Token;
import MapTool.Tile;

public class Map {

	Tile[][] tiles;
	Image background;
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
		move(t, pixelX, pixelY, false);
	}

	public void addToken(Image pic, int pixelX, int pixelY) {

		tokens.add(new Token(pic));
		move(tokens.get(tokens.size() - 1), pixelX, pixelY, true);
	}

	public void hideArea(int startX, int startY, int endX, int endY) {
		toggleHideArea(true, startX, startY, endX, endY);
	}

	public void unHideArea(int startX, int startY, int endX, int endY) {
		toggleHideArea(false, startX, startY, endX, endY);
	}

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

	public static void main(String[] args) {

		Map m = new Map(1440, 900, 48, 48);
		System.out.println("No Compile Errors, yay.");
	}

}
