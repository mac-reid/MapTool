package Backend;

import java.io.*;
import java.net.*;
import java.util.*;

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

		// start listening for new clients connecting 
		new ServerListener().start();
	}		

	synchronized void whisper(String message) {

		String[] data = message.split("~");
		for(int i = al.size(); --i >= 0;) {

			// checks if this is the recipient
			if (al.get(i).username == data[0]) {

				// try to write to the Client if fail, remove from the list
				if(!al.get(i).writeMsg(message)) 
					al.remove(i);
			}
		}
	}

	// kills the server connections
	synchronized void kill() {
		try {

			// I was asked to stop
			serverSocket.close();
			keepGoing = false;
			for(ClientThread c : al) 
				c.close();
			al.clear();
		} catch (IOException ioe) {} // nothing I can do
	}

	// to broadcast a message to all Clients
	private synchronized void broadcast(String message, int id) {

		// we loop in reverse order in case we would have to remove a Client
		// because it has disconnected
		for(int i = al.size(); --i >= 0;) {

			// checks if this is the client that sent the message
			if (al.get(i).id != id) {

				// try to write to the Client if it fails remove it from the list
				if(!al.get(i).writeMsg(message)) 
					al.remove(i);
			}
		}
	}


	// To transfer a file to all Clients
	synchronized void fileTransfer(String message, int from, int to) {
	
		// Initialize new server socket, socket, thread, and thread list
		ServerSocket servSock = null;
		Socket socket = null;
		ClientThread t = null;
		ArrayList<ClientThread> alct = new ArrayList<ClientThread>();

		// Create new server socket/port
		try {
			servSock = new ServerSocket(3142);
		} catch (IOException e) {
			System.out.println(e);
		}

		// For each of the clients, inform them that a file is incoming and what its size will be
		for(int i = al.size(); --i >= 0;) {
			
			if (al.get(i).id == to) {

				if(!al.get(i).writeMsg("File"))
					al.remove(i);

				if(!al.get(i).writeMsg(message))
					al.remove(i);
			}
			
			// Wait for each existing client to connect on a different port
			try {
				socket = servSock.accept();
				t = new ClientThread(socket);
				alct.add(t);
				t.start();
			} catch (IOException e) {
				System.out.println(e);
			}
			
			// Write the input stream to the output stream
			try {
				t.sOutput.writeObject(t.sInput);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		
		// Close all sockets
		try {
			for(int i = 0; i < alct.size(); ++i) {
				alct.get(i).sInput.close();
				alct.get(i).sOutput.close();
				alct.get(i).socket.close();
			}
			servSock.close();
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	// for a client who log off using the LOGOUT message
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
					Socket socket = serverSocket.accept();	  	// accept connection
					ClientThread t = new ClientThread(socket);  // make a thread of it
					al.add(t);									// save it in the ArrayList
					t.start();

				} catch (IOException ioe) {
					System.out.println("failure at creating client thread");
				}
			}
		}
	}

	//  instance of this thread will run for each client
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
				} catch (IOException | ClassNotFoundException e) {
					break;				
				}

				String message = cm.getMessage();

				// Switch on the type of message receive
				switch(cm.getType()) {

				case ChatMessage.MESSAGE:
					broadcast(message, id);
					break;
				case ChatMessage.LOGOUT:
					System.out.println(username + 
							" disconnected with a LOGOUT message.");
					keepGoing = false;
					break;
				case ChatMessage.WHOISIN:
					writeMsg("List of the users connected:");
					// scan all the users connected
					for(int i = 0; i < al.size(); ++i) {
						writeMsg((i + 1) + ") " + al.get(i).username);
					}
					break;
				case ChatMessage.WHISPER:
					whisper(message);
					break;
				case ChatMessage.FILE:
					fileTransfer(message, id, id);
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
				if(sInput != null) sInput.close();
				if(socket != null) socket.close();
			} catch (IOException ioe) {} // not much to do here
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
				return false;
			}
			return true;
		}
		
		public boolean sendFile(File f) {
				
			if(!socket.isConnected()) {
				close();
				return false;
			}

			// write the message to the stream
			try {
				sOutput.writeObject(f);
			}
			// if an error occurs, do not abort just inform the user
			catch(IOException e) {
				System.out.println("Error sending message to " + username);
				System.out.println(e);
				return false;
			}
			return true;
		}
	}
}