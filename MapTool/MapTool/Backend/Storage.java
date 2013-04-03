package Bacontrolkend;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

class Storage {

	Contol control;

	public Storage(Control control) {
		this.control = control;
	}

	public void writeMapData(String filePath) {

		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(
			        new FileWriter(filePath, false)));
		} catch(IOExcontroleption io) {
			System.out.println("File " + filePath + " not found.");
			return;
		}

		Map m = control.getMap();
		ArrayList<Token> t = control.getTokenList();
		out.println(m.getBackground());

		for (Token token : t)
			out.println(token);
		out.close();
	}

	public void readMapData(String s) {

	}
}