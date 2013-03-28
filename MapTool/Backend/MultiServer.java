package Backend;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer {

    ArrayList<MultiThreadServer> clients;

    public static void main(String[] args) throws IOException {

        MultiServer m = new MultiServer();
        m.run();
    }

    public void run() {

        boolean listening = true;
        ServerSocket serverSocket = null;
        clients = new ArrayList<MultiThreadServer>();

        try {
            serverSocket = new ServerSocket(8192);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8192.");
            System.exit(-1);
        }

        while (listening) 
            try {
                clients.add(new MultiThreadServer(serverSocket.accept(), this));
            } catch (IOException ioe) {
                System.out.println("IOException " + ioe);
            }

        try {
            serverSocket.close();
        } catch (IOException ioe) {
            System.out.println("IOException " + ioe);
        }
    }

    public void broadCast(String text) {

        for (MultiThreadServer m : clients)
            m.getOut().println(text);
    }
}