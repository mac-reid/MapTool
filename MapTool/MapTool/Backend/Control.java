package Backend;

import java.io.*;
import java.util.*;
import UserInterface.*;
import org.newdawn.slick.*;

/* The controller class that interacts between the GUI elements,
 * the network elements, and the file i/o elements.
 *
 * This class has two types of methods, method names ending in
 * the letter 'B' and the same methods without ending in the
 * the letter 'B'. The methods ending with the letter 'B' are
 * in place for the Client to call when updating locally. The
 * methods not ending in the letter 'B' are in place for the
 * GUI elements to call when broadcasting to all other clients.
 */
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
		String string = "AddToken~" + fileName + "~" + Integer.toString(tileX) 
			+ "~" + Integer.toString(tileY) + "~" + name;
		client.broadcast(string);
	}

	void addTokenB(String fileName, int tileX, int tileY, String name) {
	
		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(fileName, tileX, tileY, name);
	}

	public String broadcastMessage(String message) throws IOException {

		String tmp = parseInput(message);

		client.sendChatMessage(message);
		return "";
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
		String string = "Hide~" + Integer.toString(startX) + "~" 
			+ Integer.toString(startY) + "~" + Integer.toString(endX) 
			+ "~" + Integer.toString(endY);
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
		System.out.println(hostname);
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
		genUI.receiveChat(user, message);
		System.out.println(user + "   " + message);
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

	public String parseInput(String message) {

		// parse the message for command messages
		if (message.charAt(0) == '/') {

			String[] data = message.split(" ");
			int[] roll;

			// case for no spaces in the input
			if (data.length == 1)
				return "Unrecognized command: " + message;
		
			if (message.length() > 2) {

				// section for /r
				if (message.charAt(1) == 'r' && message.charAt(2) == ' ') {
					roll = parseRoll(message, data);
				}

				// section for /w
				else if (message.charAt(1) == 'w' && message.charAt(2) == ' ') {

					parseWhisper(message, data);
				}
			
			} else if (message.length() > 6) {

				// section for /roll
				if (message.substring(0, 4).equalsIgnoreCase("/roll")
				    	&& message.charAt(5) == ' ') {
					roll = parseRoll(message, data);
				} 

			} else if (message.length() > 8) {

				// section for /gmroll
				if (message.substring(0,6).equalsIgnoreCase("/gmroll") 
				    	&& message.charAt(7) == ' ') {
					roll = parseRoll(message, data);
				}

			} else if (message.length() > 9) {

				//section for /whisper
				if (message.substring(0,7).equalsIgnoreCase("/whisper") 
				    	&& message.charAt(8) == ' ') {
					parseWhisper(message, data);
				}
			
			} else 

				// section for returning an error message stating the given
				// command is improperly formatted e.g. /r (no trailing characters)
				return "Unrecognized command: " + message;
		}
		
		return "";
	}

	private String rollDice(int numRolls, int nSides, int bonus) { 

		int temp = 0;
		int total = 0;
		String ret = "";
		Random  r = new Random(); 

		ret = "Rolling " + numRolls + "d" + nSides + " + " + bonus + '\n' + "(";

		for(int i = 0; i < numRolls; i++) { 

			temp = (r.nextInt(nSides) + 1);
			ret += temp;
			total += temp;
			if (i != numRolls -1)
				ret += " + ";
		} 
		ret += ")+" + bonus + '\n' + "=" + total; 
		return ret;  
    }

	private int[] parseRoll(String message, String[] data) {
		
		int d = message.indexOf("d");
		if (d == -1) {}
			// no d in message, improper formatted /roll

		int[] roll = new int[3];

		return null;
	}

	private String[] parseWhisper(String message, String[] data) {

		if (data.length < 3) {
			// improperly formatted /w only one parameter
		}

		return null;
	}

}
