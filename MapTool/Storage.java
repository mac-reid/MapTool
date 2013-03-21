package MapTool;

import java.io.*;
import MapTool.Pair;
import java.util.ArrayList;

public class Storage {

	File chatLog;
	String filePath;
	PrintWriter out;
	ArrayList<Pair> users;

	public Storage() {

		try {

			filePath = "MapTool/logs/chat.MapTool";
			chatLog = new File(filePath);
			if (!chatLog.exists())
				chatLog.createNewFile();

			out = new PrintWriter(new BufferedWriter(
			            new FileWriter(filePath, true)));

		} catch (IOException e) {
			
		}
	}

	public void closeFile() {
		out.close();
	}

	public ArrayList<Pair> getUsers() {
		return users;
	}

	public boolean writeUserChat(String user, String text) {

		return true;
	}

	private void readChatLogs() {

	}

	public static void main(String[] args) {

		Storage s = new Storage();
		// change
		s.closeFile();
	} 
}