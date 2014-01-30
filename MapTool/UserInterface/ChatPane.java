package UserInterface;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
//import org.newdawn.slick.gui.TextField;
import org.lwjgl.input.Keyboard;

import java.awt.Font;
import java.io.IOException;
import java.util.*;


public class ChatPane {
	Font chatfont = new Font("Verdana", Font.BOLD, 12);
	TrueTypeFont ttfont = new TrueTypeFont(chatfont, true);
	
	int windowX1, windowX2, windowY1, windowY2;
	int windowwidth = 0;
	int windowheight = 0;
	int lineheight = 0;
	int scrolladjust = 0;
	
	//Used to erase the old chatbox when resizing the window
	int lastX = 0;
	int lastY = 0;
	Graphics graphics;
	
	boolean isActivated = false;
	
	boolean entrycheck = true;
	
	String entryline = "";
	String visibleEntry = "";
	int startSubstring = 0;
	int entrychar = Keyboard.CHAR_NONE;
	int lastchar = Keyboard.CHAR_NONE;
	
	Image textcaret = null;
	Image fillpattern = null;
	
	ArrayList<String> chatlog = null;
	boolean showcaret = true;
	
	private int minimumWidth;
	
	public genUI genUI;
    
    // Default  constructor - currently loads example resources
    public ChatPane(int winwidth, int winheight, int minimum, genUI genUI) throws SlickException {
    	this.genUI = genUI;
    	Keyboard.enableRepeatEvents(false);
    	windowwidth = winwidth;
    	windowheight = winheight;
    	minimumWidth = minimum;
    	
    	lineheight = ttfont.getLineHeight();
    	
    	textcaret = new Image("Resources/textcaret.png");
    	fillpattern = new Image("Resources/crepepaper.png");
    	
    	chatlog = new ArrayList<String>();
    	
    	chatlog.add("Welcome to MapTool!");
    	chatlog.add("");
    	chatlog.add("Here are the available");
    	chatlog.add("chat commands you can use:");
    	chatlog.add("");
    	chatlog.add(" - Type any message and press");
    	chatlog.add("   Enter and it will appear to all");
    	chatlog.add("   other players");
    	chatlog.add("");
    	chatlog.add(" - /roll 2d6+4");
    	chatlog.add("   /r is aliased to /roll");
    	chatlog.add("   To roll the dice, you can enter");
    	chatlog.add("   any formula that is");
    	chatlog.add("   [# dice]d[# sides] + [modifiers]");    	
    }
    
    public void sendText() {}
    public void setTest() {}
    
    
    // Draws current background and tiles
    public void renderChat(float x, float y, int width, int height, Graphics g) {
    	windowX1 = (int)x;
    	windowY1 = (int)y;
    	windowX2 = windowX1 + width;
    	windowY2 = windowY1 + height;
    	
    	// Fill a background pattern
    	g.fillRect(x, y, (float)width, (float)height, fillpattern, 0, 0);
    	g.setColor(Color.black);
    	g.drawRect(x + ((width - minimumWidth)/2) + 3, y + height - 45, minimumWidth - 6, lineheight + 15);
    	
    	
    	// Draw chat history
    	for(int i = 1; (i + scrolladjust) <= chatlog.size(); i++) {
    		if (i * lineheight + 50 > height)
    			break;
    		ttfont.drawString(x + ((width - minimumWidth)/2) + 5, ((y + height) - 50 - (i * lineheight)), chatlog.get(chatlog.size() - (i + scrolladjust)), Color.black);
    	}

    	// Draw entry line
    	ttfont.drawString(x + ((width - minimumWidth)/2) + 5, y + height - (lineheight + 20), visibleEntry, Color.black);
    	
    	
    	// Draw caret
    	if(showcaret && isActivated())
    		textcaret.draw((float)(x + ((width - minimumWidth)/2) + 3 + ttfont.getWidth(visibleEntry)), y + height - (lineheight + 25));
    	
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
    			if (entryline.length() != 0) {
    				try {
						if (entryline.charAt(0) == '/')
							genUI.control.broadcastMessage(entryline);
						else {
							chatlog.add(wrapText(genUI.name + ": " + entryline));
							genUI.control.broadcastMessage(entryline);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				entryline = "";
    				visibleEntry = "";
    				startSubstring = 0;
    			}
    			
    		}
    		// If Backspace, delete a character
    		else if (Keyboard.getEventKey() == Keyboard.KEY_BACK) {
    			if (entryline.length() != 0)
    				entryline = entryline.substring(0, entryline.length() - 1);
    			//update the visible part of the string
    			if(ttfont.getWidth(entryline) + 20 > minimumWidth){
    				startSubstring -= 1;
    				visibleEntry = entryline.substring(startSubstring);
    			} else {
    				visibleEntry = entryline;
    			}
    		}
    		// Else, add pressed key to the entry line
    		else {
    			entryline = entryline + Keyboard.getEventCharacter();
    			if(ttfont.getWidth(entryline) + 20 > minimumWidth){
    				startSubstring += 1;
    				visibleEntry = entryline.substring(startSubstring);
    			} else {
    				visibleEntry = entryline;
    			}
    		}	
    	}
    	lastchar = entrychar;
    }
    
    
    /* Scrolls the chat up/down.  Positive/negative value displays newer/older lines
    	May modify later to retain at least a screen's worth of chat in the pane, if possible */
    public void scrollChat(int adjustment) {
    	scrolladjust += (adjustment / 20);
    	if (scrolladjust < 0)
    		scrolladjust = 0;
    	else if (scrolladjust >= chatlog.size())
    		scrolladjust = chatlog.size() - 1;
    }
    
    private String parseEntry(String addstring) {
    	//If the line is lead by a command
    	if (addstring.charAt(0) == '/') {
    		// Send a message
    		if (addstring.length() >= 8) {
    			if (addstring.substring(1, 5).equals("msg ")) {
    				String message = addstring.substring(5);
    				chatSend(message.substring(0, message.indexOf(' ')), message.substring(message.indexOf(' ') + 1, message.length()));
    				return("(msg->" + message.substring(0, message.indexOf(' ')) + "):" + message.substring(message.indexOf(' ') + 1, message.length()));
    			}
    			else if (addstring.substring(1, 6).equals("roll ")) {
    				// I need to speak with Mac about the structure of dice roll strings
    				System.out.println("A diceroll happens!");
    				return addstring;
    			}
    		}
    	}
    	return wrapText(addstring);
    }
    
    public String wrapText(String pString){
    	if (!(ttfont.getWidth(entryline) + 20 > minimumWidth)){
    		return pString;
    	} else {
    		int upperIndex = pString.length() - startSubstring;
    		String newString = "";
    		String subString = "";
    		//add in the newline characters
    		while(upperIndex < pString.length()){
    			int temp = upperIndex;
    			//make the line breaks on individual words
    			try {
    				while(pString.charAt(upperIndex) != ' ') upperIndex --;
    				//if there was no space, well dont bother separating the word
    			} catch (IndexOutOfBoundsException e) {upperIndex = temp;}
    			subString = pString.substring(0, upperIndex);
    			chatlog.add(subString);
    			pString = pString.substring(upperIndex, pString.length());
    			upperIndex = temp;
    		}
    		//tack on the remainder
    		newString = pString;
    		return newString;
    	}

    }
    public void chatReceive(String inline) {
    	chatlog.add(parseEntry(inline));
    }
    
    // Broadcasts a string, int should indicate which, if not all, users
    public void chatSend(String user, String outline) {
    	System.out.println("NETWORK MESSAGE TO (" + user + ") : " + outline);
    }
    
   
    public void Activate(){
    	isActivated = true;
    	//System.out.println("ACTIVATED!");
    }
    
    public void Deactivate(){
    	isActivated = false;
    	//System.out.println("DEACTIVATED");
    }
    
    public boolean isActivated() {
    	return isActivated;
    }
    
    public int getLineHeight(){
    	return lineheight;
    }
    
    public void addToChat(String s) {
    	chatlog.add(wrapText(s));
    }
}
