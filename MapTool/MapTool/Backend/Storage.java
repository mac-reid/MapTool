package Backend;

import java.io.*;
import java.util.ArrayList;
import UserInterface.Token;

class Storage {

	Control c;
	BufferedReader in;
	PrintWriter out;

	public Storage(Control c) {
		this.c = c;
	}

	public void writeMapData(String filePath) {

		try {
			out = new PrintWriter(new BufferedWriter(
			        new FileWriter(filePath, false)));
		} catch (IOException io) {
			System.out.println("File " + filePath + " not found.");
			return;
		}

		String mapname = c.getMap();
		out.println(mapname);

		ArrayList<String> tokens = c.map.tokens.getTokenStrings();
		for	(String s : tokens)
			out.println(s);
		out.close();
	}

	public void readMapData(String filePath) {

		try {
			in = new BufferedReader(new FileReader(filePath));
		} catch (IOException ioe) {
			System.out.println("File " + filePath + " not found.");
			return;
		}

		// do something
	}
}