package UserInterface;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Token implements Comparable<Token> {
    public String name;
    public Image tokenImage; 
    public int x;  
    public int y;
    public int size;
    public float scale;
    //initialize all the statuses to false;
    public boolean[] status = {false, false, false, false, false, false, false, false};
    
    
    
    public Token (String name, String imgLocation, int x, int y, int size, float scale, boolean[] status) {
    	
    	// Copy status values
    	if (status != null) {
	    	for (int i = 0; (i < status.length  &&  i < this.status.length); i++) {
	    		this.status[i] = status[i];
	    	}
    	}
    	
    	this.name = name;
    	this.x = x;
    	this.y = y;
    	this.size = size;
    	this.scale = scale;
    	try { this.tokenImage = new Image(imgLocation).getScaledCopy((int)scale, (int)scale); }
		catch (SlickException se){System.out.println("Token constructor (Image creation) failed");}
    }
    
    
    public Token (String name, String imgLocation, int x, int y, int size, float scale) {
    	this(name, imgLocation, x, y, size, scale, new boolean[6]);
    }
    
    
    public Image getImage() {
    	return tokenImage;
    }
    
    public String getName() {
    	return name;
    }
    
    
    // Returns a boolean vector of status
    public boolean[] getStatus() {
    	return status;
    }
    
    
    public void setStatus(boolean[] stats) {
    	for (int i = 0; i < status.length && i < stats.length; i++)
    		status[i] = stats[i];
    }
    
    
    public void setStatus(boolean bool, int index){
		status[index] = bool;
	}
    
    
    // Returns true if a portion of the token is visible, based on given coordinates
    public boolean isVisible(float topX, float topY, float botX, float botY, float scale) {
    	if (((x * scale) >= (topX - scale))  &&  ((x * scale) <= botX)  &&  ((y * scale) >= (topY - scale))  &&  ((y * scale) <= botY))
    		return true;
    	return false;
    }
    
    
    // Renders the token (or an appropriate portion)
    // Offset refers to the absolute screen position the Map is being drawn at
    // Top / Bottom (X/Y) refer to the visible area of the map, in pixels
    public void renderToken(float offx, float offy, float topX, float topY, float botX, float botY, float scale, Image[] icons) {
    	// If the token is displayed fully
    	if (((x * scale) >= topX)  &&  ((x * scale) <= (botX - scale))  &&  ((y * scale) >= topX)  &&  ((y * scale) <= (botY - scale))){
    		tokenImage.draw(offx + (x * scale) - topX, offy + (y * scale) - topY);
    		int buffer = 0;
    		int numStats = 0;
    		int yOffSet = 0;
    		int iconSize = tokenImage.getWidth()/5;
    		//drawing the statuses
    		for(int i = 0; i < 8; i++){
    			if(status[i]){
    				Image scaledIcon = icons[i].getScaledCopy(iconSize, iconSize);
    				scaledIcon.draw(offx + (x * scale) - topX - buffer + tokenImage.getWidth() - iconSize, offy + (y * scale) - topY + yOffSet);
    				numStats++;
    				buffer += iconSize + 2;
    				if(numStats == 4){
    					yOffSet = iconSize + 2;
    					buffer = 0;
    				} 
    			}
    		}
    	}
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
    		
    		int buffer = 0;
    		int numStats = 0;
    		int yOffSet = 0;
    		int iconSize = tokenImage.getWidth()/5;
    		//drawing the statuses
    		for(int i = 0; i < 8; i++){
    			if(status[i]){
    				Image scaledIcon = icons[i].getScaledCopy(iconSize, iconSize);
    				scaledIcon.draw(offx + (x * scale) - topX - buffer + tokenImage.getWidth() - iconSize, offy + (y * scale) - topY + yOffSet);
    				numStats++;
    				buffer += iconSize + 2;
    				if(numStats == 4){
    					yOffSet = iconSize + 2;
    					buffer = 0;
    				} 
    			}
    		}
    	}
    }
		
		
	public int compareTo(Token O) {
		return(name.compareToIgnoreCase(O.name));
	}
	
	
	
	public String toString() {
		String returnString = "";
		
		returnString = name + "~" + tokenImage + "~" + x + "~" + y + "~" + size + "~" + scale;
		for (int i = 0; i < status.length; i++) {
			returnString += "~" + status[i]; 
		}
		return(returnString);
	}
}
	
 
