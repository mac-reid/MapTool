package Backend;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

class Storage {

	Control c;
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

		Map m = c.getMap();
		ArrayList<Token> t = c.getTokenList();
		out.println(m.getBackground());

		for (Token token : t)
			out.println(token);

		out.close();
	}

	public void readMapData(String s) {

	}
}