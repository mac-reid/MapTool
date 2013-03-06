package MapTool;

import MapTool.Map;
import MapTool.Token;

public class Tile {
	
	int x, y;
	Token unit;
	boolean occupied;

	public Tile() {

		occupied = true;
		unit = null;
	}

	public Tile(Token unit) {

		occupied = true;
		this.unit = unit;
	}

	public boolean toggleOccupation() {

		boolean temp = occupied;
		occupied = !occupied;
		return temp;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public Token getToken() {
		return unit;
	}

	public void setToken(Token newToken) {
		unit = newToken;
	}

	public int X() {
		return x;
	}

	public int Y() {
		return y;
	}

}