package UserInterface;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Token implements Comparable<Token> {
    public String name;
    public Image tokenImage; 
    public int x;  
    public int y;
    public int size;
    public boolean[] status;
    
    
    public Token (String name, String imglocation, int x, int y, int size, float scale) {
    	
    	this.status = new boolean[8];
    	this.name = name;
    	this.x = x;
    	this.y = y;
    	this.size = size;
    	try { this.tokenImage = new Image(imglocation).getScaledCopy((int)scale, (int)scale); }
		catch (SlickException se){System.out.println("Token constructor (Image creation) failed");}
    }
    
    
    public Image getImage() {
    	return tokenImage;
    }
    
    public String getName() {
    	return name;
    }
    
    
    // Returns true if a portion of the token is visible, based on given coordinates
    public boolean isVisible(float topX, float topY, float botX, float botY, float scale) {
    	if (((x * scale) >= (topX - scale))  &&  ((x * scale) <= botX)  &&  ((y * scale) >= (topY - scale))  &&  ((y * scale) <= botY))
    		return true;
    	System.out.println("NOT VISIBLE");
    	return false;
    }
    
    
    // Renders the token (or an appropriate portion)
    // Offset refers to the absolute screen position the Map is being drawn at
    // Top / Bottom (X/Y) refer to the visible area of the map, in pixels
    public void renderToken(float offx, float offy, float topX, float topY, float botX, float botY, float scale) {
    	// If the token is displayed fully
    	if (((x * scale) >= topX)  &&  ((x * scale) <= (botX - scale))  &&  ((y * scale) >= topX)  &&  ((y * scale) <= (botY - scale)))
    		tokenImage.draw(offx + (x * scale) - topX, offy + (y * scale) - topY);
    	
    	// If the token is on the upper or left border
    	else {
    		float ctop = 0;
    		float cleft = 0;
    		float cright = scale;
    		float cbot = scale;
    				
    		if ((x * scale) < topX)
    			cleft = topX - (x * scale);
    		else if ((x * scale) > (botX - scale))
    			cright = botX - (x * scale);
    		
    		if ((y * scale) < topY)
    			ctop = topY - (y * scale);
    		else if ((y * scale) > (botY - scale))
    			cbot = botY - (y * scale);
    		
    		Image croppedImage = tokenImage.getSubImage((int)cleft, (int)ctop, (int)(cright - cleft), (int)(cbot - ctop));
    		croppedImage.draw(offx + (x * scale) - topX + cleft, offy + (y * scale) - topY + ctop);
    	}
    }
		
		
	public int compareTo(Token O) {
		return(name.compareToIgnoreCase(O.name));
	}
}
	
 
