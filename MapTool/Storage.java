package MapTool;

import java.io.*;

public class Storage {

	File chatLog;
	String filePath;

	public Storage() {

		// temp shit
		try {

			filePath = "MapTool/logs/chat.MapTool";

			chatLog = new File(filePath);
			if (!chatLog.exists())
				chatLog.createNewFile();

			PrintWriter out = new PrintWriter(new BufferedWriter(
			                      new FileWriter(filePath, true)));
			out.println("ffff");
			out.close();

		} catch (IOException e) {
			System.out.println("Error: Failed to write chat log. " + 
			                   e.getMessage());
		}
	}

	public static void main(String[] args) {

		Storage s = new Storage();
	} 
}