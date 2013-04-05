package Backend;

import java.util.ArrayList;
import UserInterface.genUI;
import org.newdawn.slick.Image;

public class Control {

	private Map map;
	private genUI genUI;
	private Client client;
	private Server server;
	private Storage store;

	public Control(genUI genUI) {
		this.genUI = genUI;
	}

	public void addToken(Image pic, int x, int y, String name) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(pic, x, y, name);
	}

	public void addToken(String fileName, int x, int y, String name) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(fileName, x, y, name);
		String string = "AddToken~" + fileName + "~" + Integer.toString(x) + "~" + Integer.toString(y) + "~" + name;
		client.broadcast(string);
	}

	public void addToken(String fileName, int x, int y, int tokX, String name) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(null, x, y, tokX, name);
	}
	
	void addTokenB(String fileName, int x, int y, String name) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(fileName, x, y, name);
		map.printMap();
	}

	public void broadcastMessage(String message) {
		
		client.sendChatMessage(message);
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
	
	void hideMapAreaB(int startX, int startY, int endX, int endY) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.hideArea(startX, startY, endX, endY);
		map.printMap();
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
		client = new Client(hostname, port, alias, this);
	}

	public void joinGame(String alias, String hostname) {
		client = new Client(hostname, 8192, alias, this);
	}

	public void joinGame(String host) {
		client = new Client(host, 8192, "Anonymous", this);
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
			System.out.println("Some error here"); 
			return false;
		}		
		String string = "Move~" + s + "~" + Integer.toString(tileX) + "~" + Integer.toString(tileY);
		client.broadcast(string);
		return false;
	}

	public boolean moveToken(Token t, int tileX, int tileY) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return false;
		}
		return map.move(t, tileX, tileY);
	}

	boolean moveTokenB(String s, int tileX, int tileY) {

		Token t = map.getToken(s);
		return moveToken(t, tileX, tileY);
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

	public boolean removeToken(Token t) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return false;
		}
		return map.removeToken(t);
	}

	boolean removeTokenB(String name) {

		boolean yeah = false;
		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return false;
		}
		for (Token t : map.getTokens()) 
			if (t.getName().equals(name)) 
				yeah = removeToken(t);

		map.printMap();		
		return yeah;
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

	public void sendTextToGUI(String user, String message) {

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
	
	void showMapAreaB(int startX, int startY, int endX, int endY) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}

		map.unHideArea(startX, startY, endX, endY);
		map.printMap();
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