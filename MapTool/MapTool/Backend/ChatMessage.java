package Backend;

import java.io.*;

/*
 * This class defines the different type of messages that will be exchanged 
 * between the Clients and the Server. 
 * When talking from a Java Client to a Java Server a lot easier to pass Java 
 * objects, no need to count bytes or to wait for a line feed at the end of the frame
 */
public class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	// The different types of message sent by the Client
	// WHOISIN to receive the list of the users connected
	// MESSAGE an ordinary message
	// LOGOUT to disconnect from the Server
	static final int WHOISIN = 0, MESSAGE = 1, 
			LOGOUT = 2, FILE = 3, WHISPER = 4, ROLL = 5;
	
	private int type;
	private File file = null;
	private String message;
	
	// constructor for a generic string message
	ChatMessage(int type, String message) {
		this.type = type;
		this.message = message;
	}

	// constructor for file transfer (maybe?)
	ChatMessage(int type, File file) {
		this.type = type;
		this.file = file;
	}
	
	// getters
	int getType() {
		return type;
	}

	String getMessage() {
		return message;
	}

	File getFile() {
		return file;
	}
}

