package Backend;

import java.io.*;
import java.net.*;

public class MultiThreadServer extends Thread {
    
    private PrintWriter out;
    private Socket socket = null;
    private MultiServer server;

    public MultiThreadServer(Socket socket, MultiServer server) {
	
		super("MultiThreadServer");
		this.server = server;
		this.socket = socket;
		this.start();
    }

    public void run() {

		try {

	    	out = new PrintWriter(socket.getOutputStream(), true);
	   		BufferedReader in = new BufferedReader(
				    new InputStreamReader(
				    socket.getInputStream()));

	    	String inputLine = "First line woo";
	   		out.println(inputLine);

	   		while ((inputLine = in.readLine()) != null) {
				server.broadCast(inputLine);
				if (inputLine.equals("Bye"))
		   			break;
	    	}
	    	out.close();
	    	in.close();
	    	socket.close();

		} catch (IOException e) {
	    	e.printStackTrace();
		}
    }

    public PrintWriter getOut() {
    	return out;
    }
}