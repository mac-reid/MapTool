package UserInterface;

import java.awt.Toolkit;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;


public class ChatWindow {

	private String[] visibleChat;


	public ChatWindow(Graphics g, int width, int x, int y){
		int h = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 2*y);
		//draw border
		g.setColor(Color.lightGray);
		g.fillRect(x-2, y-2, width + 4, h + 4);
		//find height, draw the rectangle
		g.setColor(Color.white);
		g.fillRect(x, y, width, h);

	}
}
