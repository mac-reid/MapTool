package Backend;

import java.io.*;
import java.net.*;
import java.util.*;


// The Client that can be run both as a console or a GUI 
public class Client  {

	// for I/O
	private Socket socket;
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket

	// the server, the port and the username
	private int port;
	private Control c;
	private String server, username;

	protected boolean keepGoing = true;

	/*  
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

	public void sendFile(String file, String user) {
		try {
			sOutput.writeObject(new ChatMessage(ChatMessage.FILE, new File(file)));
		} catch (IOException ioe ) {}
	}

	public void broadcast(String message) {
		try {
			sOutput.writeObject(new ChatMessage(ChatMessage.MESSAGE, message));
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}

	/*
	 * method used for sending a file to the server to be broadcast out
	 */
	public boolean broadcastFile(String fileOutput) {
		File myFile = null;
		myFile = new File(fileOutput);

		// create socket
		try {
			sOutput.writeObject(new ChatMessage(ChatMessage.FILE, ("" + (int)myFile.length())));
		} catch (IOException e) {
			System.out.println(e);
		}

		Socket s = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;

		try {
			s = new Socket(server, 3142);
		} catch(Exception ec) {
			return false;
		}

		try {
			byte [] mybytearray  = new byte [(int)myFile.length()];

			fis = new FileInputStream(myFile);
			bis = new BufferedInputStream(fis);
			bis.read(mybytearray,0,mybytearray.length);

			os = s.getOutputStream();
			os.write(mybytearray,0,mybytearray.length);

			os.flush();
			bis.close();
			os.close();
			s.close();
		} catch (IOException e) {
			System.out.println(e);
		}

		return true;
	}

	public void whisper(String recipient, String message) {

		try {
			sOutput.writeObject(new ChatMessage(ChatMessage.WHISPER, message));
		} catch (IOException ioe) {}
	}

	/*
	 * To start the dialog
	 */
	public boolean start() {

		// try to connect to the server
		try {
			socket = new Socket(server, port);
		} catch(Exception ec) {
			return false;
		}

		/* Creating both Data Stream */
		try {
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException ioe) {
			return false;
		}

		// creates the Thread to listen from the server 
		new ListenFromServer().start();

		// Send our username to the server this is the only message that we
		// will send as a String. All other messages will be ChatMessage objects
		try	{
			sOutput.writeObject(username);
		} catch (IOException ioe) {
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
			sOutput.writeObject(new ChatMessage(ChatMessage.MESSAGE, 
					"ChatMessage~" + username + "~" + message));
		} catch(IOException e) {
			System.out.println("Exception writing to server: " + e);
		}		
	}

	/*
	 * When something goes wrong
	 * Close the Input/Output streams and disconnect not 
	 * much to do in the catch clause
	 */
	protected void disconnect() {
		try { 
			if(sInput != null) sInput.close();
			if(sOutput != null) sOutput.close();
			if(socket != null) socket.close();
			keepGoing = false;
		} catch (IOException ioe) {} // not much to do
	}

	/*
	 * a class that waits for the message from the server and prints them 
	 */
	class ListenFromServer extends Thread {

		public void run() {

			while(keepGoing) {

				try {

					String msg = (String) sInput.readObject();

					String[] splits = msg.split("~");

					// Incoming file
					if (splits[0].equals("File")) {

						// File size
						int filesize = Integer.parseInt((String) sInput.readObject());

						int bytesRead;
						int current = 0;
						Socket sock = null;
						InputStream is = null;

						// Connect to server on another new socket/port
						try {
							sock = new Socket(server, 3142);
							is = sock.getInputStream();
						} catch (IOException e) {
							System.out.println(e);
						}

						// Receive the new file
						byte [] mybytearray  = new byte [filesize];
						try {
							final String dir = System.getProperty("user.dir") + "/MapTool/Resources";
							FileOutputStream fos = new FileOutputStream(dir);
							BufferedOutputStream bos = new BufferedOutputStream(fos);

							bytesRead = is.read(mybytearray,0,mybytearray.length);
							current = bytesRead;

							do {
								bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
								if(bytesRead >= 0) current += bytesRead;
							} while(bytesRead > -1);

							bos.write(mybytearray, 0 , current);
							bos.flush();

							bos.close();
							sock.close();
						} catch (IOException ex) {
							System.out.println(ex);
						}


					}
					else if (splits[0].equals("ChatMessage")) {

						// Print the message
						c.sendTextToGUI(splits[1], splits[2]);
						System.out.println(splits[1] + ": " + splits[2]);
					}
					else if (splits[0].equals("AddToken")) {

						// Call the function
						c.addTokenB(msg);
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
					else if (splits[0].equals("Move")) {
						int startX = Integer.parseInt(splits[1]);
						int startY = Integer.parseInt(splits[2]);
						int endX = Integer.parseInt(splits[3]);
						int endY = Integer.parseInt(splits[4]);

						System.out.println(c.moveTokenB(startX, startY, endX, endY));
					}
					else if (splits[0].equals("Remove")) {

						// Get the parameters
						int x = Integer.parseInt(splits[1]);
						int y = Integer.parseInt(splits[2]);

						// Call the function
						c.removeTokenB(x, y);

					}
					else if (splits[0].equals("Show")) {

						// Get the parameters
						int startX = Integer.parseInt(splits[1]);
						int startY = Integer.parseInt(splits[2]);
						int endX = Integer.parseInt(splits[3]);
						int endY = Integer.parseInt(splits[4]);

						// Call the function
						c.showMapAreaB(startX, startY, endX, endY);
					} else if (splits[0].equals("Change")) {
						
						boolean[] bools = new boolean[8];
						
						for (int i = 1; i < 9; i++)
							if (splits[i].equals("t"))
								bools[i] = true;
						c.changeStatusB(bools);
						
					}
						

					// should be roll, whisper, setmap

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