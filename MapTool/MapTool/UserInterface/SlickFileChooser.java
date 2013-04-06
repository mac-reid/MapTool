package UserInterface;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import java.awt.Font;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.util.*;


public class SlickFileChooser {
	// True if filechooser is initialized and being displayed
	boolean active = false;
	
	Image canx;
	ArrayList<Image> fileImages = new ArrayList<Image>();
	ArrayList<String> fileNames = new ArrayList<String>();
	String[] allFiles;
	
	File directory;
	String filePath;
	String fileSelected = "";
	
	Font tokenfont = new Font("Verdana", Font.BOLD, 9);
	TrueTypeFont ttTokenFont = new TrueTypeFont(tokenfont, true);
	
	// Arraylist index of file currently hovered over
	int hoverIndex = -1;
	int selx = -2;
	int sely = -2;
	int tilesX = 0;
	int tilesY = 0;
	
	
	
	
	SlickFileChooser() {
		try {canx = new Image(System.getProperty("user.dir") + "/MapTool/Resources/canx.png").getScaledCopy(50, 50); }
		catch (SlickException se) {};
	}
	
	
	public boolean getActive() {
		return active;
	}
	
	public String getSelected() {
		return fileSelected;
	}
	
	
	public void setActive(String type) {
		
		
		active = true;
		fileSelected = "";
		fileImages.clear();
		fileNames.clear();
		if (type.equals("Tokens")) {
			filePath = System.getProperty("user.dir") + "\\MapTool\\Resources\\Tokens";
		}
		
		else if (type.equals("Maps")) {
			filePath = System.getProperty("user.dir") + "\\MapTool\\Resources\\Maps";
		}
		
		else
			System.out.println("Incorrect resource type specified");
		
		directory = new File(filePath);
		allFiles = directory.list();
		
		if (allFiles == null)
			return;
		
		for (int i = 0; i < allFiles.length; i++) {
			if (allFiles[i].endsWith(".png")) {
				fileNames.add(allFiles[i].substring(0, (allFiles[i].length() - 4)));
				// Alternative loading / display code should be used for maps (in the future)
				try { fileImages.add(new Image(filePath + "/" + allFiles[i]).getScaledCopy(48, 48)); }
				catch (SlickException se) {System.out.println("Something went horribly wrong in SlickFileChooser.setActive()"); }
			}
		}
	}
	
	
	// File chooser is no longer readied for display
	public void setInactive() { active = false; }
	
	
	// Updates via mouse input
	public void update(Input in, int x, int y) {
		selx = -2;
		sely = -2;
		
		//If the mouse is in bounds, update the hover index.
		if ((in.getMouseX() > (x + 30)) && (in.getMouseX() < (tilesX * 48 - 80)))
			if ((in.getMouseY() > (y + 30)) && (in.getMouseY() < (tilesY * 48 - 80))) { 
				// Index = x + (max_x * y)
				sely = ((in.getMouseY() - y - 30) / 80);
				selx = ((in.getMouseX() - x - 10) / 80);
				selx = selx + (sely * ((tilesX * 48 - y - 90)/65));
				if ((selx >= 0) && (selx < fileNames.size()))
					hoverIndex = selx;
				System.out.println("[[[" + hoverIndex + " / " + selx + "]]]");
			}
		
		
		// Mouseclick event
		if (in.isMousePressed(0)) {
			System.out.println("well, we're here");
			// If cancel button
			if ((in.getMouseY() >= (y + 10)) && (in.getMouseY() <= (y + 60))) {
				if ((in.getMouseX() >= (x + tilesX * 48 - 40)) && (in.getMouseX() <= (x + tilesX * 48 + 10))) {
					this.setInactive();
				}
			}
			
			// If token selected
			if (selx == hoverIndex) {
				fileSelected = filePath + "/" + (fileNames.get(hoverIndex) + ".png");
			}
		}
	}
	
	
	
	// Draw loaded token images
	public void render(float x, float y, int sizeX, int sizeY) {
		tilesX = sizeX;
		tilesY = sizeY;
		int pos = 0;
		float i = x + 30;
		float j = y + 30;
		
		canx.draw((x + sizeX * 48 - 40), (y + 10));
		
		oloop:
		while (j < (sizeY * 48 - 80)) {
			while (i < (sizeX * 48 - 80)) {
				if (fileImages.size() <= pos)
					break oloop;
				fileImages.get(pos).draw(i, j);
				ttTokenFont.drawString(i, j + 50, fileNames.get(pos));
				i += 80;
				pos++;
			}
			j += 80;
			i = (x + 30);
		}
	}
}