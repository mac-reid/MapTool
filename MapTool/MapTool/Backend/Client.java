package Backend;

import java.net.*;
import java.io.*;
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
	private String server, username;
	private int port;

	/*
	 *  Constructor called by console mode
	 *  server: the server address
	 *  port: the port number
	 *  username: the username
	 */
	Client(String server, int port, String username) {
		this.server = server;
		this.port = port;
		this.username = username;
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
			System.out.println("Error connectiong to server: " + ec);
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
	/*
	 * To start the Client in console mode use one of the following command
	 * > java Client
	 * > java Client username
	 * > java Client username portNumber
	 * > java Client username portNumber serverAddress
	 * at the console prompt
	 * If the portNumber is not specified 8192 is used
	 * If the serverAddress is not specified "localHost" is used
	 * If the username is not specified "Anonymous" is used
	 * > java Client 
	 * is equivalent to
	 * > java Client Anonymous 8192 localhost 
	 */
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
		Client client = new Client(serverAddress, portNumber, userName);
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
			else 	                                 // default to ordinary message
				client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, msg));
			
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
					System.out.println(msg);
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

