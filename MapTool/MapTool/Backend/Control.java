package Backend;

import java.util.ArrayList;
import org.newdawn.slick.Image;

public class Control {

	private Map map;
	private genUI genUI;
	private Server server;
	private Storage store;

	public Control(genUI genUI) {
		this genUI = genUI;
	}

	public void joinGame(String host) {
		client = new Client(host, 8192, "Anonymous", this);
	}
	
	public void sendTextToGUI(String user, String message) {

	}

	public void addToken(Image pic, int x, int y, String name) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(pic, x, y, name);
	}

	public void addToken(String s, int x, int y, String name) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(null, x, y, name);
		String string = "AddToken~" + s + "~" + Integer.toString(x) + "~" + Integer.toString(y) + "~" + name;
		client.broadcast(string);
	}
	
	public void addTokenB(String s, int x, int y, String name) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(null, x, y, name);
	}

	public void addToken(String s, int x, int y, int tokX, String name) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(null, x, y, tokX, name);
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
		String string = "Hide~" + Integer.toString(startX) + "~" + Integer.toString(startY) 
				+ "~" + Integer.toString(endX) + "~" + Integer.toString(endY);
		client.broadcast(string);
	}
	
	public void hideMapAreaB(int startX, int startY, int endX, int endY) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.hideArea(startX, startY, endX, endY);
	}

	public void loadGame() {

		map = new Map(480, 480, 48, null);
		store = new Storage(this);
	}

	public void loadSave(String saveFilePath) {

		map = new Map(480, 480, 48, null);
		store = new Storage(this);
		store.readMapData(saveFilePath);
	}

	public boolean moveToken(String s, int tileX, int tileY) {

		if (!gameLoaded()) { 
			Systme.out.println("Some error here"); 
			return false;
		}		
		String string = "Move~" + s + "~" + Integer.toString(tileX) + "~" + Integer.toString(tileY);
		client.broadcast(string);
		return false;
	}
	
	public boolean moveTokenB(String s, int tileX, int tileY) {

		return false;
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
		
		String string = "Remove~" + name;
		client.broadcast(string);
		
		for (Token t : map.getTokens()) 
			if (t.getName().equals(name)) 
				return removeToken(t);
		
		
		return false;
	}
	
	public boolean removeTokenB(String name) {

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

	public void saveGame() {
		String mydir = System.getProperty("user.dir");
		store.writeMapData(mydir + "/saves/default.sav");
	}

	public void saveMap(String saveFilePath) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
	}

	public void hostGame() {

		Server server = new Server(8192);
		server.start();
	}

	public void hostGame(int port) {

		Server server = new Server(port);
		server.start();
	}

	public void joinGame(String alias, int port, String hostname) {

	}

	public void joinGame(String alias, String hostname) {
		

	}

	public void showMapArea(int startX, int startY, int endX, int endY) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.unHideArea(startX, startY, endX, endY);
		String string = "Show~" + Integer.toString(startX) + "~" + Integer.toString(startY) 
				+ "~" + Integer.toString(endX) + "~" + Integer.toString(endY);
		client.broadcast(string);
	}
	
	public void showMapAreaB(int startX, int startY, int endX, int endY) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}

		map.unHideArea(startX, startY, endX, endY);
	}

	protected Map getMap() {
		return map;
	}

	private boolean gameLoaded() {

		if (map == null)
			return false;
		if (store == null)
			return false;
		return true;
	}

	public static void main(String[] args) {

		Control c = new Control(null);

		String mydir = System.getProperty("user.dir");
		c.loadSave(mydir + "/saves/default.sav");

		c.hostGame();
		c.addToken("", 2, 2, 1, "Carl");
		c.addToken("", 2, 3, 1, "Caleb");

		c.saveGame();		
	}
}