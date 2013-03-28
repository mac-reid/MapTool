package Backend;

import java.util.ArrayList;

/**
 * This class is used to store user chat logs for the MapTool
 *
 * @author Mac Reid
 */
public class Pair {

	private String username, oldName;
	private ArrayList<String> text;

	/**
	 * Basic constructor that only takes in a username
	 *
	 * @param username The name of the user to associate the chat logs to
	 */
	public Pair(String username) {

		this.username = username;
		text = new ArrayList<String>();
	}

	public Pair(String username, String text) {

		this.username = username;
		this.text = new ArrayList<String>();
		this.text.add(text);
	}

	/**
	 * Constructor that takes a username and a premade list of chat logs
	 *
	 * @param username The name of the user to associate the chat logs to
	 * @param text A list of text that belongs to the user
	 */
	public Pair(String username, ArrayList<String> text) {

		this.username = username;
		this.text = new ArrayList<String>(text);
	}

	/**
	 * Adds text to the user's logs
	 *
	 * @param text New text to associate to the user
	 */
	public void addText(String text) {
		this.text.add(text);
	}

	/**
	 * Changes the username and saves the old name to associate with chat logs
	 *
	 * @param newName The new username to associate to the chat logs
	 */
	public void changeUserName(String newName) {
		oldName = username;
		username = newName;
	}

	/**
	 * Returns the list of chat logs associated to this user
	 *
	 * @return The list of chat logs associated to this user
	 */
	public ArrayList<String> getText() {
		return text;
	}

	/**
	 * Returns the username associated with this pair
	 *
	 * @return The username associated with this pair
	 */ 
	public String getUser() {
		return username;
	}
}