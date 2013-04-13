package Backend;

import java.io.*;
import UserInterface.*;
import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Control {

	private MapPane map;
	private genUI genUI;
	private Client client;
	private Server server;
	private Storage store;
	
	public Control(genUI genUI) {
		this.genUI = genUI;
	}

	public void setMap(MapPane getmap) {
		map = getmap;
		// Client broadcast code here, to support changing maps (as a DM option)
	}
	
	void setMapB (String mapname) {
		// For the future, when clients will load maps based on the host
	}

	public void addToken(String fileName, int tileX, int tileY, String name) throws IOException {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		String string = "AddToken~" + fileName + "~" + Integer.toString(tileX) + "~" + Integer.toString(tileY) + "~" + name;
		client.broadcast(string);
	}

	void addTokenB(String fileName, int tileX, int tileY, String name) {
	
		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(fileName, tileX, tileY, name);
	}

	public void broadcastMessage(String message) throws IOException {
		
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
	
	public void hideMapArea(int startX, int startY, int endX, int endY) throws IOException {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
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
	}

	public void hostGame() {
		
		Server server = new Server(8192);
		server.start();
	}
	
	public void joinGame(String alias, String hostname) throws IOException {

		client = new Client(hostname, 8192, alias, this);
		client.start();
	}
	
	public void loadSave(String saveFilePath) {

		// Should call an appropriate MapPane function, such as map.setMap(String mapname)
		store = new Storage(this);
		store.readMapData(saveFilePath);
	}
	
	public boolean moveToken(String s, int tileX, int tileY) throws IOException {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return false;
		}		
		String string = "Move~" + s + "~" + Integer.toString(tileX) + "~" + Integer.toString(tileY);
		client.broadcast(string);
		return false;
	}

	boolean moveTokenB(String s, int tileX, int tileY) {
		return map.moveToken(s, tileX, tileY);
	}

	public boolean removeToken(String name) throws IOException {

		if (!gameLoaded()) { 
			System.out.println("Some error here");
			return false;
		}
		String string = "Remove~" + name;
		client.broadcast(string);
		return true;
	}

	boolean removeTokenB(String name) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return false;
		}
		return map.removeToken(name);
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
		store.writeMapData(saveFilePath);
	}

	
	public void sendTextToGUI(String user, String message) throws IOException {

	}

	public void showMapArea(int startX, int startY, int endX, int endY) throws IOException {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
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
	}

	protected String getMap() {
		return map.getBackground();
	}
	
	private boolean gameLoaded() {

		if (map == null)
			return false;
		if (store == null)
			return false;
		return true;
	}

	/* public static void main(String[] args) {

		Control c = new Control(null);

		String mydir = System.getProperty("user.dir");
		c.loadSave(mydir + "/saves/default.sav");

		c.hostGame();
		c.addToken("", 2, 2, 1, "Carl");
		c.addToken("", 2, 3, 1, "Caleb");

		c.saveGame();		
	} */
}
