package Andrews;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
//import org.newdawn.slick.gui.TextField;
import org.lwjgl.input.Keyboard;

import java.awt.Font;
import java.util.*;


public class ChatPane {
	Font chatfont = new Font("Verdana", Font.BOLD, 12);
	TrueTypeFont ttfont = new TrueTypeFont(chatfont, true);
	
	int windowwidth = 0;
	int windowheight = 0;
	int lineheight = 0;
	
	//Used to erase the old chatbox when resizing the window
	int lastX = 0;
	int lastY = 0;
	Graphics graphics;
	
	boolean isActivated = false;
	
	boolean entrycheck = true;
	
	String entryline = "";
	int entrychar = Keyboard.CHAR_NONE;
	int lastchar = Keyboard.CHAR_NONE;
	
	Image textcaret = null;
	Image fillpattern = null;
	
	ArrayList<String> chatlog = null;
	boolean showcaret = true;
	
	
	
    
    // Default  constructor - currently loads example resources
    public ChatPane(int winwidth, int winheight) throws SlickException {
    	Keyboard.enableRepeatEvents(false);
    	
    	windowwidth = winwidth;
    	windowheight = winheight;
    	
    	lineheight = ttfont.getLineHeight();
    	
    	textcaret = new Image("Resources/textcaretwhite.png");
    	fillpattern = new Image("Resources/chatpattern.png");
    	
    	chatlog = new ArrayList<String>();
    	for(int i = 0; i<5; i++)
    		chatlog.add(i + ":  bazooper!!!!1111 sampletext");
    	
    }
    
    public void sendText() {}
    public void setTest() {}
    
    
    // Draws current background and tiles
    public void renderChat(float x, float y, float width, float height, Graphics g) throws SlickException {
    	Image img = new Image("Resources/parchment.png");
    	Image scaled = img.getScaledCopy((int)width, (int)height + 5);
    	scaled.draw(x, y);
    	g.setColor(Color.black);
    	g.drawRect(x + 5, y + height - 45, width - 10, lineheight + 5);
    	
    	
    	// Draw chat history
    	for(int i = 1; i <= chatlog.size(); i++) {
    		if (i * lineheight + 50 > height)
    			break;
    		ttfont.drawString(x + 10, ((y + height) - 55 - (i * lineheight)), chatlog.get(chatlog.size() - i), Color.black);
    	}
    	
    	// Draw entry line
    	ttfont.drawString(x + 10, y + height - (lineheight + 25), entryline,  Color.black);
    	
    	
    	// Draw caret
    	if(showcaret) {
    		ttfont.drawString(x + 10 + ttfont.getWidth(entryline), y + height - (lineheight + 25), "|", Color.black);
    		ttfont.drawString(x + 10 + ttfont.getWidth(entryline), y + height - (lineheight + 30), "|", Color.black);
    	}
    	lastX = (int)x;
    	lastY = (int)y;
    	graphics = g;
    }
    
    public void updateChat(Input chatin) {
    	// Blink the caret
    	showcaret = ((System.currentTimeMillis() % 2000) > 1000);
    	
    	
    	entrychar = Keyboard.getEventCharacter();
    	//System.out.println("[" + entrychar + "]");
    	
    	// If the new keypress is different from the last, and not NULL
    	if ((entrychar != lastchar) && (entrychar != 0) && isActivated) {
    		// If ENTER, send message to log
    		if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
    			chatlog.add(entryline);
    			entryline = "";
    		}
    		// If Backspace, delete a character
    		else if (Keyboard.getEventKey() == Keyboard.KEY_BACK) {
    			if (entryline.length() != 0)
    				entryline = entryline.substring(0, entryline.length() - 1);
    		}
    		// Else, add pressed key to the entry line
    		else {
    			if (ttfont.getWidth(entryline) + 30 < windowwidth)
    				entryline = entryline + Keyboard.getEventCharacter();
    		}	
    	}
    	lastchar = entrychar;
    	
    }
   
    public void Activate(){
    	isActivated = true;
    }
    
    public void Deactivate(){
    	isActivated = false;
    }
    
    public int getLineHeight(){
    	return lineheight;
    }
}
