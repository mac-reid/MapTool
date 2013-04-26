package Backend;

import java.io.*;
import java.util.*;
import UserInterface.*;
import java.lang.StringBuffer;

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
		store = new Storage(this);
	}

	public void setMap(MapPane getmap) {
		map = getmap;
		// Client broadcast code here, to support changing maps (as a DM option)
		// client.broadcast("Map~" + getmap.getBackground());
	}
	
	void setMapB (String mapname) {
		// For the future, when clients will load maps based on the host
	}

	public void addToken(String fileName, int tileX, int tileY) throws IOException {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		String string = "AddToken~" + fileName + "~" + Integer.toString(tileX) 
			+ "~" + Integer.toString(tileY);
		if (client != null)
			client.broadcast(string);
	}

	void addTokenB(String message) {
	
		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return;
		}
		map.addToken(message);
	}
	
	public void changeStatus(boolean[] statuses, int x, int y) throws IOException {
		
		String message = "Change~" + x + "~" + y + "~";
		
		for (int i = 0; i < statuses.length; i++)
			if (statuses[i] == true)
				message += "t~";
			else 
				message += "f~";
		if (client != null)
			client.broadcast(message);
	}
	
	void changeStatusB(boolean[] statuses, int x, int y) {
		map.changeStatus(statuses, x, y);
		System.out.println("B");
	}

	public String broadcastMessage(String message) throws IOException {

		String tmp = parseInput(message);

		if (client != null)
			client.sendChatMessage(message);
		return tmp;
	}

	public void disconnect() {

		client.disconnect();
		server.kill();
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
		if (client != null)
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
		
		server = new Server(8192);
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
	
	public boolean moveToken(int startX, int startY, int endX, int endY) throws IOException {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return false;
		}		
		String string = "Move~" + startX + "~" + startY + "~" + endX + "~" + endY;
		if (client != null)
			client.broadcast(string);
		return false;
	}

	boolean moveTokenB(int startX, int startY, int endX, int endY) {
		return map.moveToken(startX, startY, endX, endY);
	}

	public boolean removeToken(int x, int y) throws IOException {

		if (!gameLoaded()) { 
			System.out.println("Some error here");
			return false;
		}
		String string = "Remove~" + x + "~" + y;
		if (client != null)
			client.broadcast(string);
		return true;
	}

	boolean removeTokenB(int x, int y) {

		if (!gameLoaded()) { 
			System.out.println("Some error here"); 
			return false;
		}
		return map.removeToken(x, y);
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

	public void sendFile(String file, String user) {
		client.sendFile(file, user);
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
		if (client != null)
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

	public File[] importFiles() {

		PopupWindow popUp = new PopupWindow();
		popUp.start();
		File[] files = popUp.importFiles();
		moveFiles(files, "/Resources/Tokens");
		return files;
	}

	public String parseInput(String message) {

		String ret = "";

		// parse the message for command messages
		if (message.charAt(0) == '/') {

			int[] roll;
			String[] data = message.split(" ");

			// case for no spaces in the input
			if (data.length == 1)
				return "Unrecognized command: " + message;
		
			if (message.length() > 2) {

				// section for /r
				if (message.charAt(1) == 'r' && message.charAt(2) == ' ') {
					roll = parseRoll(message, data);
					if (roll == null) {
						// improperly formatted roll
					} else if (roll.length == 3 && roll[0] != 0 && roll[1] != 0) {
						ret = rollDice(roll[0], roll[1], roll[2]);
					}
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
					if (roll == null) {
						// improperly formatted roll
					}
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
		
		return ret;
	}

	private void moveFiles(File[] files, String subdir) {

		String dest = System.getProperty("user.dir");
		System.out.println(dest + subdir);
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
		ret += ")+" + bonus + '\n' + "= " + total; 
		System.out.println(ret);
		return ret;  
    }

	private int[] parseRoll(String message, String[] data) {
		
		int d = message.indexOf("d");
		if (d == -1) {
			// no d in message, improper formatted /roll
			return null;
		}

		StringBuffer str = new StringBuffer();
		int[] roll = new int[3];
		String sub = message.substring(message.indexOf(" ") + 1, message.length());
		sub = sub.replaceAll(" ", "");

		String[] parts = sub.split("d");
		roll[0] = Integer.parseInt(parts[0]);

		if (parts[1].indexOf("+") != -1) {
			roll[1] = Integer.parseInt(parts[1].substring(0, 
				parts[1].indexOf("+")));
			roll[2] = Integer.parseInt(parts[1].substring(parts[1].indexOf("+")));
		}

		return roll;
	}

	private String[] parseWhisper(String message, String[] data) {

		if (data.length < 3) {
			// improperly formatted /w only one parameter
		}

		return null;
	}

}
