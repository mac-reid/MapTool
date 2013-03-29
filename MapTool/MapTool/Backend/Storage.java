package Backend;

import java.io.*;
import Backend.Pair;
import java.util.HashMap;
import java.util.ArrayList;

public class Storage {

	String filePath;
	PrintWriter out;
	ArrayList<Pair> users;

	public Storage(String saveFilePath) {

		filePath = saveFilePath;

		try {
			out = new PrintWriter(new BufferedWriter(
			        new FileWriter(filePath, true)));
		} catch (IOException io) {
			System.out.println("Something is whacked bro");
		}

		readChatLogs();
	}

	public void closeFile() {
		
		out.close();
	}

	public ArrayList<Pair> getUsers() {
		return users;
	}

	public void writeMapData() {

	}

	public void writeUserChat(String user, String text) {

		out.println("u|" + user + "|" + text);	
	}

	private void readChatLogs() {

		try {

			LineNumberReader lnr = new LineNumberReader(new FileReader(filePath));
			HashMap<String, ArrayList<String>> tempList = 
				new HashMap<String, ArrayList<String>>();

			String line = lnr.readLine();
			while (line != null) {

				String[] parts = line.split("\\|");

				String user = parts[1];
				String text = parts[2];

				if (tempList.get(user) != null) {
					// user exists, add the string to the list
					tempList.get(user).add(text);
				} else {
					// user does not exist, initialize arraylist, and add text
					tempList.put(user, new ArrayList<String>());
					tempList.get(user).add(text);
				}

				line = lnr.readLine(); 
			}

		} catch (FileNotFoundException f) {
			System.out.println("You lost your file br0");
		} catch (IOException io) {
			System.out.println("shit man");
		} 
	}
}