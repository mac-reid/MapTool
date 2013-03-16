package MapTool;

import MapTool.Pair;
import MapTool.Storage;
import java.util.ArrayList;

public class Control {

	private Storage store;
	private int count, timer;
	private ArrayList<Pair> userChatBuffer;

	public Control() {

		userChatBuffer = new ArrayList<Pair>();
		count = 0;
		store = new Storage();

		// this value determines how often to write to the chat log file
		timer = 5;
	}

	public void sentChat(String user, String text) {

		addText(user, text);

	}

	private void addText(String user, String text) {

		// search for user in current chat buffer
		for (Pair p : userChatBuffer) 
			if (p.getUser().equals(user))
				p.addText(text);
		
		// make user in the chat buffer
		userChatBuffer.add(new Pair(user, text));

		// check if it is time to write to the chat log file
		if (count == timer) {

			if (store.writeOutUserChat(userChatBuffer))
				userChatBuffer.clear();
			else 
				// need a way to force writing chat logs
				System.out.println("We got a srs problem");

		} else count++;
	}

}