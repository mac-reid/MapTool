package UserInterface;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import UserInterface.Token;


// Tokens class.  Tracks tile occupation for the map
public class Tokens {
	int sizeX = 0;
	int sizeY = 1;
	float scale = 48;
	ArrayList<Token> tokens = new ArrayList<Token>();

	
	
	// Constructor, current accepts no parameters
	public Tokens(float scale) throws SlickException {
		this.scale = scale;
	}
	
	public Tokens() throws SlickException {
	}
	
	
	
	// Rescales all token images to the new size & tracks scale
	public void scaleTokens(float scale) {
		this.scale = scale;
		for (int i = 0; i < tokens.size(); i++) {
			tokens.get(i).tokenImage = tokens.get(i).tokenImage.getScaledCopy((int)scale, (int)scale);
		}
	}
	
	
	// Resets the tokens on the Map
	public void clearTokens() {
		tokens = null;
	}
	
	
	
	// Draws all applicable tokens, cropped as necessary, for the map coordinates (topX, topY) to (botX, botY)
	public void renderTokens(float offX, float offY, float topX, float topY, float botX, float botY, Image[] icons) {
		for (int i = 0; i < tokens.size(); i++) {
			// If the token is within the draw bounds
			if (tokens.get(i).isVisible(topX, topY, botX, botY, scale))
				tokens.get(i).renderToken(offX, offY, topX, topY, botX, botY, scale, icons);
			}
	}
	
	
	
	// Moves a token from one tile location (x,y) to another (x2, y2)
	public boolean moveToken(int x, int y, int x2, int y2) {
		Token check;
		
		// Check if the destination is occupied
		if (this.isOccupied(x2, y2))
			return false;
		
		
		// If not occupied, find the token, update it's location
		for (int i = 0; i < tokens.size(); i++) {
			check = tokens.get(i);
			if (check.x == x  && check.y == y) {
				check.x = x2;
				check.y = y2;
				return true;
			}
		}
		
		// If a token at (x,y) was not found in the list, return false
		return false;
	}
	
	
	
	public boolean moveToken(String tokenname, int x, int y) {
		Token check;
		
		// Check if the destination is occupied
		if (this.isOccupied(x, y))
			return false;
		
		
		// If not occupied, find the token, update it's location
		for (int i = 0; i < tokens.size(); i++) {
			check = tokens.get(i);
			if (check.name.equals(tokenname)) {
				check.x = x;
				check.y = y;
				return true;
			}
		}
		
		// If a token at (x,y) was not found in the list, return false
		return false;
	}
	
	
	
	// Adds a token to grid location (x,y), with an optionally specified name and size
	public boolean addToken(String getname, String imglocation, int x, int y, int getsize) {
		// Check if the location is already occupied
		if (this.isOccupied(x, y))
			return false;
		
		// Generate a unique name for the token
		String uniquename = getname;
		String suffix = "";
		int repcount = 0;
		
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).name.equals(uniquename + suffix)) {
				repcount++;
				suffix = " " + String.format("%02d", repcount);
			}
		}
		uniquename = uniquename + suffix;
		
		// Add the token
		tokens.add(new Token(uniquename, imglocation, x, y, getsize, scale));
		Collections.sort(tokens);
		
		// Debug data print
		System.out.println("Token added to (" + x + "," + y + ") , Name: " + uniquename);
		return true;
	}
	
	// Overloads with valid options
	// TODO: Clean these up
	public boolean addToken(String getname, String imglocation, int x, int y) {
		return this.addToken(getname, imglocation, x, y, 1);
	}
	public boolean addToken(String imglocation, int x, int y, int getsize) {
		return this.addToken(imglocation.substring(imglocation.lastIndexOf("/") + 1, imglocation.lastIndexOf(".")), imglocation, x, y, getsize);
	}
	public boolean addToken(String imglocation, int x, int y) {
		return this.addToken(imglocation, x, y, 1);
	}
	
	
	
	// Remove a token from the Map by it's name
	public boolean removeToken(String tokenName) {
		for (int i = 0; i < tokens.size(); i++) 
			if (tokens.get(i).name.equals(tokenName)) {
				tokens.remove(i);
				return true;
			}
		
		// If the specified token is not found, return false
		return false;
	}
	
	
	// Remove a token by its grid location
	public boolean removeToken(int x, int y) {
		for (int i = 0; i < tokens.size(); i++)
			if (tokens.get(i).x == x  &&  tokens.get(i).y == y) {
				tokens.remove(i);
				return true;
			}
		
		// If the specified Token is not found - return false	
		return false;
	}
	
	
	
	// Returns the image held by the token at a tile location
	public Image getImage(int x, int y) {
		for (int i = 0; i < tokens.size(); i++)
			if (tokens.get(i).x == x  &&  tokens.get(i).y == y)
				return tokens.get(i).tokenImage;
		return null;
	}
	
	
	public String getName(int x, int y) {
		for (int i = 0; i < tokens.size(); i++)
			if (tokens.get(i).x == x  &&  tokens.get(i).y == y)
				return tokens.get(i).name;
		return "";
	}
	
	public Token getToken(int x, int y) {
		for (int i = 0; i < tokens.size(); i++)
			if (tokens.get(i).x == x  &&  tokens.get(i).y == y)
				return tokens.get(i);
		return null;
	}
	
	/*public String getFileName(int x, int y) {
		if (tokenGrid[x][y] != null)
			return tokenGrid[x][y].getFileName();
		return "";
	}*/
	
	public ArrayList<Point> getCoordinates() {
		ArrayList<Point> coords = new ArrayList<Point>();
		for (int i = 0; i < tokens.size(); i++)
			coords.add(new Point(tokens.get(i).x, tokens.get(i).y));
		return coords;
	}
	
	
	public boolean isOccupied (int x, int y) {
		Token check;
		for (int i = 0; i < tokens.size(); i++) {
			check = tokens.get(i);
			if ((x >= check.x  &&  x < (check.x + check.size))  &&  (y >= check.y  &&  y < (check.y + check.size)))
				return true;
		}
		return false;
	}
}
