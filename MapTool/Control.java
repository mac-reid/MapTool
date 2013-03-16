package MapTool;

import MapTool.*;
import java.util.ArrayList;
import org.newdawn.slick.Image;

public class Control {

	private Map map;
	private Storage store;

	public Control() {

		map = new Map(0, 0, 0, 0, 48);
		store = new Storage();
	}

	public void addText(String user, String text) {

		// search for user in current chat buffer
		for (Pair p : store.getUsers()) 
			if (p.getUser().equals(user))
				p.addText(text);
		
		if (store.writeUserChat(user, text))
			return;
		else 
			System.out.println("We got a srs problem");

	}

	public void addToken(Image pic, int pixelX, int pixelY, String name) {

	}

	public Token getToken(int x, int y) {
		return null;
	}

	public ArrayList<Token> getTokenList() {
		return null;
	}

	public void hideMapArea(int startX, int startY, int endX, int endY) {

	}

	public void loadGame(String saveFilePath) {

	}

	public boolean moveToken(Token t, int tileX, int tileY) {
		return false;
	}

	public void removeToken(String name) {

	}

	public void removeToken(Token t) {

	}

	public void saveGame(String saveFilePath) {

	}

	public void showMapArea(int startX, int startY, int endX, int endY) {

	}

}