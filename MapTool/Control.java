package MapTool;

import MapTool.*;
import java.util.ArrayList;
import org.newdawn.slick.Image;

public class Control {

	private Map map;
	private Storage store;

	public Control() {

	}

	public void addText(String user, String text) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}

		// search for user in current chat buffer
		for (Pair p : store.getUsers()) 
			if (p.getUser().equals(user))
				p.addText(text);
		
		if (store.writeUserChat(user, text))
			return;
		else 
			System.out.println("We got a srs problem");

	}

	public void addToken(Image pic, int x, int y, String name) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(pic, x, y, name);
	}

	public Token getToken(int x, int y) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return null;
		}
		return map.getToken(x,y);
	}

	public ArrayList<Token> getTokenList() {
		
		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return null;
		}
		return map.getTokens();
	}

	public void hideMapArea(int startX, int startY, int endX, int endY) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.hideArea(startX, startY, endX, endY);
	}

	public void loadSave(String saveFilePath) {

		map = new Map(480, 480, 48, null);
		store = new Storage();
	}

	public boolean moveToken(Token t, int tileX, int tileY) {
		
		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return false;
		}
		return map.move(t, tileX, tileY);
	}

	public boolean removeToken(String name) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return false;
		}
		for (Token t : map.getTokens()) 
			if (t.getName().equals(name)) 
				return removeToken(t);
		return false;
	}

	public boolean removeToken(Token t) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return false;
		}
		return map.removeToken(t);
	}

	public void saveMap(String saveFilePath) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
	}

	public void showMapArea(int startX, int startY, int endX, int endY) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}

		map.unHideArea(startX, startY, endX, endY);
	}

	private boolean gameLoaded() {

		if (map == null)
			return false;
		if (store == null)
			return false;
		return true;
	}

	public static void main(String[] args) {

		Control c = new Control();

		c.getTokenList();
		c.loadSave("");
	}

}