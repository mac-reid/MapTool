package Backend;

import java.io.*;
import java.util.ArrayList;
import UserInterface.Token;

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

		String mapname = c.getMap();
		ArrayList<Token> t = c.getTokenList();
		out.println(mapname);

		for (Token token : t)
			out.println(token.getFileName() + "|" + token.getName());

		out.close();
	}

	public void readMapData(String s) {

	}
}