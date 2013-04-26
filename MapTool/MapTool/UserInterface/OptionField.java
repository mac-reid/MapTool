/*package UserInterface;

import java.awt.Font;
import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;


*//**
 * This class is the small fields in the menu that you can enter text into
 * eg target IP, name, whatever else
 * @author Neal Johnson
 *
 *//*
public class OptionField {
	//font type
	Font chatfont = new Font("Verdana", Font.BOLD, 12);
	TrueTypeFont ttfont = new TrueTypeFont(chatfont, true);
	//metadata?
	public String label;
	public String value;
	private Image background;
	private boolean isActive;
	private char lastchar;
	int numChars;
	private boolean showcaret;
	//used to determine where to start printing text
	private int labelLength;
	private int XPos;
	private int YPos;
	private int XSize;
	private int YSize;
	//if the field opens up a file
	private boolean opensFiles = false;
	private boolean isShowing = false;
	public SlickFileChooser fc;
	
	//general constructor for non 
	public OptionField(String label, int x, int y) throws SlickException{
		this.label = label;
		labelLength = label.length();
		background = new Image("Resources/parchment2.png");
		XPos = x;
		YPos = y;
		XSize = background.getWidth();
		YSize = background.getHeight();
		value = "";
		//calculate the number of characters the field can hold;
		numChars = (XSize - ttfont.getWidth(label) - 20)/ttfont.getWidth("a");
		fc = new SlickFileChooser();
	}
	
	//constructor used for when we want this field to open up files
	public OptionField(String label, int x, int y, boolean fileOpener) throws SlickException{
		this.label = label;
		labelLength = label.length();
		background = new Image("Resources/parchment2.png");
		XPos = x;
		YPos = y;
		XSize = background.getWidth();
		YSize = background.getHeight();
		value = "";
		opensFiles = fileOpener;
		numChars = (XSize - ttfont.getWidth(label) - 26)/ttfont.getWidth("a");
		fc = new SlickFileChooser();
	}
	
	
	public void render(GameContainer gc){
		Graphics g = gc.getGraphics();
		g.drawImage(background, XPos, YPos);
		if (value.length() > numChars){
			ttfont.drawString(XPos + 10, YPos + (YSize - ttfont.getLineHeight())/2,
					label + value.subSequence(0, numChars) + "...", Color.black);
		} else{
			ttfont.drawString(XPos + 10, YPos + (YSize - ttfont.getLineHeight())/2, label + value, Color.black);
		}
		
		//draw the caret
		if(showcaret && isActive){
			//draw it twice to make it extra long
			ttfont.drawString(XPos + ttfont.getWidth(label) + ttfont.getWidth(value) + 8, 
					YPos + (YSize - ttfont.getLineHeight())/2 - 3, "|", Color.black);
			ttfont.drawString(XPos + ttfont.getWidth(label) + ttfont.getWidth(value) + 8, 
					YPos + (YSize - ttfont.getLineHeight())/2 + 3, "|", Color.black);
		}
		
		if(fc.isActive()){
			fc.render(10, 10, gc.getGraphics());
		}
	}

	public void update(GameContainer gc, Input in, StateBasedGame sbg){
		showcaret = ((System.currentTimeMillis() % 2000) > 1000);
		int mouseX = in.getMouseX();
		int mouseY = in.getMouseY();
		if (mouseX >= XPos && mouseX <= (XPos + XSize) && mouseY >= YPos && mouseY <= (YPos + YSize)){
			if(in.isMousePressed(0)){
				System.out.println("hovering over" + label);
			}
		}
		//if you click on it, activate it/open the file
		if(in.isMousePressed(0)){
			if (mouseX >= XPos && mouseX <= (XPos + XSize) && mouseY >= YPos && mouseY <= (YPos + YSize)){
				isActive = true;
				System.out.println(label + " is active");
				//this is super buggy right now (only works on PC)
				if(opensFiles){
					fc.setActive("Maps");
				} 
				//if we click somewhere else, diable this
			} else {
				isActive = false;
				if(opensFiles){
					fc.setInactive();
				}
			}
		}

		if(opensFiles && fc.isActive()){
			fc.update(in, 10, 10, 700, 1000);

			if (!(fc.getSelected().equals(""))){
				int index = fc.getSelected().indexOf("/Maps");
				value = fc.getSelected().substring(index);

			}
		}


		//if we dont open files, but are a simple text entry thingy
		if (!opensFiles){
			char entrychar = Keyboard.getEventCharacter();

			// If the new keypress is different from the last, and not NULL
			if ((entrychar != lastchar) && (entrychar != 0) && isActive) {

				// If Backspace, delete a character
				if (Keyboard.getEventKey() == Keyboard.KEY_BACK) {
					if (value.length() != 0)
						value = value.substring(0, value.length() - 1);
				}
				// Else, add pressed key to the entry line
				else {
					if (ttfont.getWidth(value) + 30 < XSize)
						value = value + Keyboard.getEventCharacter();
				}	
			}
			lastchar = entrychar;
		}
	}

}
*/
package UserInterface;

import java.awt.Font;
import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;


/**
 * This class is the small fields in the menu that you can enter text into
 * eg target IP, name, whatever else
 * @author Neal Johnson
 *
 */
public class OptionField {
	//font type
	Font chatfont = new Font("Verdana", Font.BOLD, 12);
	TrueTypeFont ttfont = new TrueTypeFont(chatfont, true);
	//metadata?
	public String label;
	public String value;
	private Image background;
	public boolean isActive;
	private char lastchar;
	int numChars;
	private boolean showcaret;
	//used to determine where to start printing text
	private int labelLength;
	private int XPos;
	private int YPos;
	private int XSize;
	private int YSize;
	//if the field opens up a file
	public boolean opensFiles;
	public boolean isShowing = false;
	
	//general constructor for non 
	public OptionField(String label, int x, int y) throws SlickException{
		this.label = label;
		labelLength = label.length();
		background = new Image("Resources/parchment2.png");
		XPos = x;
		YPos = y;
		XSize = background.getWidth();
		YSize = background.getHeight();
		value = "";
		//calculate the number of characters the field can hold;
		numChars = (XSize - ttfont.getWidth(label) - 20)/ttfont.getWidth("a");

	}
	
	//constructor used for when we want this field to open up files
	public OptionField(String label, int x, int y, boolean fileOpener) throws SlickException{
		this.label = label;
		labelLength = label.length();
		background = new Image("Resources/parchment2.png");
		XPos = x;
		YPos = y;
		XSize = background.getWidth();
		YSize = background.getHeight();
		value = "";
		opensFiles = fileOpener;
		numChars = (XSize - ttfont.getWidth(label) - 26)/ttfont.getWidth("a");
	}
	
	
	public void render(GameContainer gc){
		Graphics g = gc.getGraphics();
		g.drawImage(background, XPos, YPos);
		if (value.length() > numChars){
			ttfont.drawString(XPos + 10, YPos + (YSize - ttfont.getLineHeight())/2,
					label + value.subSequence(0, numChars) + "...", Color.black);
		} else{
			ttfont.drawString(XPos + 10, YPos + (YSize - ttfont.getLineHeight())/2, label + value, Color.black);
		}
		
		//draw the caret
		if(showcaret && isActive){
			//draw it twice to make it extra long
			ttfont.drawString(XPos + ttfont.getWidth(label) + ttfont.getWidth(value) + 8, 
					YPos + (YSize - ttfont.getLineHeight())/2 - 3, "|", Color.black);
			ttfont.drawString(XPos + ttfont.getWidth(label) + ttfont.getWidth(value) + 8, 
					YPos + (YSize - ttfont.getLineHeight())/2 + 3, "|", Color.black);
		}
	}

	public void update(GameContainer gc, Input in, StateBasedGame sbg){
		showcaret = ((System.currentTimeMillis() % 2000) > 1000);
		int mouseX = in.getMouseX();
		int mouseY = in.getMouseY();
		//if you click on it, activate it/open the file
		if (mouseX >= XPos && mouseX <= (XPos + XSize) && mouseY >= YPos && mouseY <= (YPos + YSize)){
			if (in.isMousePressed(0)){
				isActive = true;
				//this is super buggy right now (only works on PC)
				if(opensFiles){
					//makes it look better
					
				}
			} 
		  //if we click somewhere else, diable this
		} else if (in.isMouseButtonDown(0)){
			isActive = false;
		}


		//if we dont open files, but are a simple text entry thingy
		if (!opensFiles){
			char entrychar = Keyboard.getEventCharacter();

			// If the new keypress is different from the last, and not NULL
			if ((entrychar != lastchar) && (entrychar != 0) && isActive) {

				// If Backspace, delete a character
				if (Keyboard.getEventKey() == Keyboard.KEY_BACK) {
					if (value.length() != 0)
						value = value.substring(0, value.length() - 1);
				}
				// Else, add pressed key to the entry line
				else {
					if (value.length() < 15)
						value = value + Keyboard.getEventCharacter();
				}	
			}
			lastchar = entrychar;
		}
	}
	
	public void showChooser(boolean isShowing){
		if(isShowing) return;
		else {
			isShowing = true;
		}
	}
}