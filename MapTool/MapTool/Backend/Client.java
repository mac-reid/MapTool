package Backend;

import java.io.*;
import java.net.*;
import java.util.*;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client  {

	// for I/O
	private Socket socket;
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket

	// the server, the port and the username
	private int port;
	private Control c;
	private String server, username;

	/*  Constructor called by console mode
	 *  server: the server address
	 *  port: the port number
	 *  username: the username
	 */
	Client(String server, int port, String username, Control control) {
		this.server = server;
		this.port = port;
		this.username = username;
		c = control;
	}

	public void broadcast(String s) {
		try {
			sOutput.writeObject(new ChatMessage(1, s));
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}

	/*
	 * To start the dialog
	 */
	public boolean start() {
		// try to connect to the server
		try {
			socket = new Socket(server, port);
		} 
		// if it failed not much I can so
		catch(Exception ec) {
			System.out.println("Error connecting to server: " + ec);
			return false;
		}

		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		System.out.println(msg);

		/* Creating both Data Stream */
		try {
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException eIO) {
			System.out.println("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		// creates the Thread to listen from the server 
		new ListenFromServer().start();
		// Send our username to the server this is the only message that we
		// will send as a String. All other messages will be ChatMessage objects
		try	{
			sOutput.writeObject(username);
		} catch (IOException eIO) {
			System.out.println("Exception doing login: " + eIO);
			disconnect();
			return false;
		}
		// success we inform the caller that it worked
		return true;
	}

	/*
	 * To send a message to the server
	 */
	void sendMessage(ChatMessage msg) {
		try {
			sOutput.writeObject(msg);
		} catch(IOException e) {
			System.out.println("Exception writing to server: " + e);
		}
	}

	public void sendChatMessage(String message) {
		try {
			sOutput.writeObject(new ChatMessage(1, username + "~" + message));
		} catch(IOException e) {
			System.out.println("Exception writing to server: " + e);
		}		
	}

	/*
	 * When something goes wrong
	 * Close the Input/Output streams and disconnect not much to do in the catch clause
	 */
	private void disconnect() {
		try { 
			if(sInput != null) sInput.close();
		} catch(Exception e) {} // not much else I can do
		try {
			if(sOutput != null) sOutput.close();
		} catch(Exception e) {} // not much else I can do
		try{
			if(socket != null) socket.close();
		} catch(Exception e) {} // not much else I can do

	}

	public static void main(String[] args) {
		// default values
		int portNumber = 8192;
		String serverAddress = "localhost";
		String userName = "Loser";

		// depending of the number of arguments provided we fall through
		switch(args.length) {
		// > java Client username portNumber serverAddr
		case 3:
			serverAddress = args[2];
			// > java Client username portNumber
		case 2:
			try {
				portNumber = Integer.parseInt(args[1]);
			} catch(Exception e) {
				System.out.println("Invalid port number.");
				System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
				return;
			}
			// > java Client username
		case 1: 
			userName = args[0];
			// > java Client
		case 0:
			break;
			// invalid number of arguments
		default:
			System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
			return;
		}
		// create the Client object
		Client client = new Client(serverAddress, portNumber, userName, null);
		// test if we can start the connection to the Server
		// if it failed nothing we can do
		if(!client.start())
			return;

		// wait for messages from user
		Scanner scan = new Scanner(System.in);
		// loop forever for message from the user
		while(true) {
			
			String msg = scan.nextLine(); 		     // read message from user
			if(msg.equalsIgnoreCase("LOGOUT")) {     // logout if message is LOGOUT
				client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
				// break to do the disconnect
				break;
			}
			else if (msg.substring(0,2).equals("!!")) { // temporary testing for function calls
				String temp = msg.substring(2, msg.length());
				client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, temp));
			}
			else if(msg.equalsIgnoreCase("WHOISIN")) { // message WhoIsIn 
				client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));
			}
			else {	                                 // default to ordinary message
				msg = "ChatMessage~" + msg;
				client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, msg));
			}

		}
		// done disconnect
		client.disconnect();	
	}

	/*
	 * a class that waits for the message from the server and prints them 
	 */
	class ListenFromServer extends Thread {

		public void run() {

			while(true) {

				try {
					String msg = (String) sInput.readObject();
					String[] splits = msg.split("~");
					if (splits[0].equals("ChatMessage")) {
						// Print the message
						c.sendTextToGUI(splits[1], splits[2]);
						System.out.println(splits[1] + ": " + splits[2]);
					}
					else if (splits[0].equals("AddToken")) {
						// Get the parameters
						String s = splits[1];
						int x = Integer.parseInt(splits[2]);
						int y = Integer.parseInt(splits[3]);
						String name = splits[4];
						
						// Call the function
						c.addTokenB(s, x, y, name);
					}
					else if (splits[0].equals("Hide")) {
						// Get the parameters
						int startX = Integer.parseInt(splits[1]);
						int startY = Integer.parseInt(splits[2]);
						int endX = Integer.parseInt(splits[3]);
						int endY = Integer.parseInt(splits[4]);
						
						// Call the function
						c.hideMapAreaB(startX, startY, endX, endY);
					}
					else if (splits[0].equals("MoveToken")) {
						String s = splits[1];
						int tileX = Integer.parseInt(splits[2]);
						int tileY = Integer.parseInt(splits[3]);
						
						c.moveTokenB(s, tileX, tileY);
					}
					else if (splits[0].equals("RemoveToken")) {
						// Get the parameters
						String name = splits[1];
						
						// Call the function
						c.removeTokenB(name);
						
					}
					else if (splits[0].equals("Show")) {
						// Get the parameters
						int startX = Integer.parseInt(splits[1]);
						int startY = Integer.parseInt(splits[2]);
						int endX = Integer.parseInt(splits[3]);
						int endY = Integer.parseInt(splits[4]);
						
						// Call the function
						c.showMapAreaB(startX, startY, endX, endY);
					}
					
					
				} catch(IOException e) {
					System.out.println("Server has closed the connection.");
					break;
				} catch(ClassNotFoundException e2) {
					System.out.println("Ermac");
				}
			}
		}
	}
}

