package Backend;

import java.io.*;
import java.util.ArrayList;

class Storage {

	Control c;
	BufferedReader in;
	PrintWriter out;

	public Storage(Control c) {
		this.c = c;
	}

	public void writeMapData(File file) {

		try {
			out = new PrintWriter(new BufferedWriter(
					new FileWriter(file.getAbsolutePath() + ".sav", false)));
		} catch (IOException io) {
			System.out.println("File " + file.getAbsolutePath() + " not found.");
			return;
		}

		String mapname = c.getMap();
		out.println(mapname);

		ArrayList<String> tokens = c.map.tokens.getTokenStrings();
		for	(String s : tokens)
			out.println(s);
		out.close();
	}

	public void readMapData(File file) {

		try {
			in = new BufferedReader(new FileReader(file));
		} catch (IOException ioe) {
			System.out.println("File " + file.getAbsolutePath() + " not found.");
			return;
		}

		// do something
		try {
			String line = "";
			String[] data = null;
			c.clear();
			while ((line = in.readLine()) != null) {
				if (line.indexOf("~") == -1){

				} else {
					data = line.split("~");
					c.addTokenL(System.getProperty("user.dir") + "/Resources/Tokens/" + data[0] + ".png", Integer.parseInt(data[2]), Integer.parseInt(data[3]));
					boolean[] statuses = new boolean[8];

					for (int i = 0; i < 8; i++) 
						if (data[i + 4].equals("true"))
							statuses[i] = true;
					c.changeStatusB(statuses, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
					c.addToken(System.getProperty("user.dir") + "/Resources/Tokens/" + data[0] + ".png", Integer.parseInt(data[2]), Integer.parseInt(data[3]));
					c.changeStatus(statuses, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
				}
			}
		} catch (IOException e) {}
	}
}