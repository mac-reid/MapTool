package UserInterface;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Token {
	Image tokenimage = null;
	int tileX = 1;
	int tileY = 1;
	int size = 1;
	String name;
	String[] statuses;

	public Token(String imglocation, int x, int y) throws SlickException {
		this("Redshirt", imglocation, x, y);
	}
	
	public Token(String setname, String imglocation, int x, int y) {
		this(setname, imglocation, x, y, 1);
	}
	
	public Token(String setname, String imglocation, int x, int y, int getsize) {
		name = setname;
		tileX = x;
		tileY = y;
		size = getsize;
		
		int resources;
		resources = imglocation.indexOf("Resources/");
		if (resources != -1) {
			try { tokenimage = new Image(imglocation.substring(resources)).getScaledCopy(48, 48); }
			catch (SlickException se){System.out.println("Token constructor (Image creation) failed");}
		}
		else {
			try { tokenimage = new Image("Resources/" + imglocation).getScaledCopy(48, 48); }
			catch (SlickException se){System.out.println("Token constructor (Image creation) failed");}
		}
	}
	
	public void renderToken(float offx, float offy) {
		tokenimage.draw(offx + (tileX * 48), offy + (tileY * 48));
	}
	
	public Image getImage() {
		return tokenimage;
	}
	
	
	public String getName() {
		return name;
	}
	
	public String getFileName() {
		return tokenimage.getResourceReference();
	}
	
	public int getX() {
		return tileX;
	}
	
	public int getY() {
		return tileY;
	}
	
	public void move(int x, int y) {
		tileX = x;
		tileY = y;
	}
}
