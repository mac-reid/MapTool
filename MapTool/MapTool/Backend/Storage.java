package Backend;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

class Storage {

	Control control;

	public Storage(Control control) {
		this.control = control;
	}

	public void writeMapData(String filePath) {

		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(
			        new FileWriter(filePath, false)));
		} catch(IOException io) {
			System.out.println("File " + filePath + " not found.");
			return;
		}

		Map m = control.getMap();
		ArrayList<Token> t = control.getTokenList();
		out.println(m.getBackground());

		for (Token token : t)
			out.println("tok|" + token);
		out.close();
	}

	public void readMapData(String filePath) {

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filePath));
		} catch (IOException ioe) {
			System.out.println("File " + filePath + " not found.");
			return;
		}

		// read map data stuff
		String line = "";
		try {
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
}