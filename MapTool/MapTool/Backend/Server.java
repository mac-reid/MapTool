package Backend;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

/*
 * The server as a console application
 */
public class Server extends Thread { 

	// the server socket used for server connections
	ServerSocket serverSocket;

	// a uniqueIdque ID for each connection
	private static int uniqueId;

	// an ArrayList to keep the list of the Client
	private ArrayList<ClientThread> al;

	// the port number to listen for connection
	private int port;

	// the boolean that will be turned of to stop the server
	private boolean keepGoing;
		
	public Server(int port) {

		this.port = port;
		keepGoing = true;
		al = new ArrayList<ClientThread>();
	}
	
	public void start() {

		// create socket server and wait for connection requests 
		// the socket used by the server
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException ioe) {
			System.out.println("failure at creating server");
			System.out.println(ioe);
		}

		new ServerListener().start();
	}		

	synchronized void kill() {
		try {
			// I was asked to stop
			serverSocket.close();
			for(int i = 0; i < al.size(); ++i) {
				ClientThread tc = al.get(i);
				tc.sInput.close();
				tc.sOutput.close();
				tc.socket.close();
			}
		} catch (IOException ioe) {} // nothing I can do
	}

	/*
	 *  to broadcast a message to all Clients
	 */
	private synchronized void broadcast(String message) {
		
		// we loop in reverse order in case we would have to remove a Client
		// because it has disconnected
		for(int i = al.size(); --i >= 0;) {
			ClientThread ct = al.get(i);
			// try to write to the Client if it fails remove it from the list
			if(!ct.writeMsg(message)) {
				al.remove(i);
			}
		}
	}

	// for a client who logoff using the LOGOUT message
	synchronized void remove(int id) {
		// scan the array list until we found the Id
		for(int i = 0; i < al.size(); ++i) {
			if(al.get(i).id == id) {
				al.remove(i);
				return;
			}
		}
	}
	
	// instance of this thread will listen for
	// clients connecting to the server
	class ServerListener extends Thread {

		public void run() {		
			
			// infinite loop to wait for connections
			while(keepGoing) {

				// if I was asked to stop
				if(!keepGoing)
					break;
				try {
					Socket socket = serverSocket.accept();  	// accept connection
					ClientThread t = new ClientThread(socket);  // make a thread of it
					al.add(t);									// save it in the ArrayList
					t.start();

				} catch (IOException ioe) {
					System.out.println("failure at creating client thread");
				}
			}
		}
	}

	//  instance of this thread will run for each clipublicent
	class ClientThread extends Thread {

		// the socket where to listen/talk
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;

		// my unique id (easier for deconnection)
		int id;

		// the Username of the Client
		String username;

		// the only type of message a will receive
		ChatMessage cm;

		// Constructor
		ClientThread(Socket socket) {

			// an unique id
			id = ++uniqueId;
			this.socket = socket;

			// Creating both Data Stream 
			System.out.println("Thread trying to create Object Input/Output Streams");
			
			try {

				// create output first
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());

				// read the username
				username = (String) sInput.readObject();

			} catch (IOException | ClassNotFoundException ioe) {} 
		}

		// what will run forever
		public void run() {

			// to loop until LOGOUT
			boolean keepGoing = true;
			while(keepGoing) {

				// read a String (which is an object)
				try {
					cm = (ChatMessage) sInput.readObject();
				} catch (IOException e) {
					break;				
				} catch(ClassNotFoundException e2) {
					break;
				}
				// the messaage part of the ChatMessage
				String message = cm.getMessage();

				// Switch on the type of message receive
				switch(cm.getType()) {

				case ChatMessage.MESSAGE:
					broadcast(message);
					break;
				case ChatMessage.LOGOUT:
					System.out.println(username + " disconnected with a LOGOUT message.");
					keepGoing = false;
					break;
				case ChatMessage.WHOISIN:
					writeMsg("List of the users connected:");
					// scan al the users connected
					for(int i = 0; i < al.size(); ++i) {
						ClientThread ct = al.get(i);
						writeMsg((i+1) + ") " + ct.username);
					}
					break;
				}
			}

			// remove myself from the arrayList containing the list of the
			// connected Clients
			remove(id);
			close();
		}
		
		// try to close everything
		private void close() {
			// try to close the connection
			try {
				if(sOutput != null) sOutput.close();
			} catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			} catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}  catch (Exception e) {}
		}

		/*
		 * Write a String to the Client output stream
		 */
		private boolean writeMsg(String msg) {
			// if Client is still connected send the message to it
			if(!socket.isConnected()) {
				close();
				return false;
			}
			// write the message to the stream
			try {
				sOutput.writeObject(msg);
			}
			// if an error occurs, do not abort just inform the user
			catch(IOException e) {
				System.out.println("Error sending message to " + username);
				System.out.println(e);
			}
			return true;
		}
	}
}