package UserInterface;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import java.awt.Font;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

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
	// Set to filepath+Tokens/ or fileath+Maps/
	String currentPath;
	String fileSelected = "";
	
	Font tokenfont = new Font("Verdana", Font.BOLD, 9);
	TrueTypeFont ttTokenFont = new TrueTypeFont(tokenfont, true);
	
	// Arraylist index of file currently hovered over
	int hoverIndex = -1;
	int selX = -1;
	int selY = -1;
	int selIndex = -1;
	
	int maxX = 0;
	int maxY = 0;
	
	int rowOffset = 0;
	
	
	// Tracks the displayable rows and columns of resource icons	
	int gridRows = 0;
	int gridColumns = 0;
	
	// Display mode: (Tokens : 1, Maps : 2)
	int displayMode = 0;
	
	// Dimensions of a displayed resource, plus spacing and text
	int gridWidth = 0;
	int gridHeight = 0;
	
	// Width/Height of cancel button
	int cnxSize = 40;
	// Draw spacing below cancel button
	int cnxSpacing = 20;
	// Width of border
	int bdrSize = 10;
	// Selection box vertical offset
	int sboxVOff = 0;
	
	// Display size of the Resource
	int resDispSize = 48;
	
	
	
	
	
	
	// Standard constructor, requires no arguments
	SlickFileChooser() {
		// Recursive directory browser (freely available code snippet, I wish I was this good)
		//	Locates the 'Resources' directory from project root
		try {
		    Path startPath = Paths.get(System.getProperty("user.dir"));
		    Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
		        @Override
		        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
		            if (dir.endsWith("Resources"))
		            	filePath = dir.toString() + "/";
		            return FileVisitResult.CONTINUE;
		        }
		        @Override
		        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		        	return FileVisitResult.CONTINUE;
		        }
		        @Override
		        public FileVisitResult visitFileFailed(Path file, IOException e) {
		            return FileVisitResult.CONTINUE;
		        }
		    });
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		
		// Load the cancel image
		try {canx = new Image("Resources/canx.png").getScaledCopy(cnxSize, cnxSize); }
		catch (SlickException se) {};
	}
	
	
	public boolean isActive() {
		return active;
	}
	
	public String getSelected() {
		return fileSelected;
	}
	
	
	// Sets FileChooser ready for display
	public void setActive(String resourceType) {
		active = true;
		fileSelected = "";
		fileImages.clear();
		fileNames.clear();
		
		if (resourceType.equals("Tokens")) {
			currentPath = filePath + "Tokens/";
			gridWidth = 80;
			gridHeight = 80;
			resDispSize = 48;
			sboxVOff = 10;
			displayMode = 1;
		}
		
		else if (resourceType.equals("Maps")) {
			currentPath = filePath + "Maps/";
			gridWidth = 308;
			gridHeight = 330;
			resDispSize = 290;
			sboxVOff = -10;
			displayMode = 2;
		}
		
		else
			System.out.println("Incorrect resource type specified");
		
		
		// Fetch valid (.PNG) resources in the given directory
		directory = new File(currentPath);
		allFiles = directory.list();
		
		if (allFiles == null)
			return;
		
		for (int i = 0; i < allFiles.length; i++) {
			if (allFiles[i].endsWith(".png")) {
				fileNames.add(allFiles[i].substring(0, (allFiles[i].length() - 4)));

				if (resourceType.equals("Tokens")) {
					try { fileImages.add(new Image(currentPath + "/" + allFiles[i]).getScaledCopy(resDispSize, resDispSize)); }
					catch (SlickException se) {}
				}
				
				// Scales the map by its greatest dimension down to 'resDispSize'
				else if (resourceType.equals("Maps")) {
					try {
						Image quickimage = new Image(currentPath + "/" + allFiles[i]);
						if (quickimage.getWidth() > quickimage.getHeight())
							fileImages.add(new Image(currentPath + "/" + allFiles[i]).getScaledCopy((float)resDispSize / quickimage.getWidth()));
						else
							fileImages.add(new Image(currentPath + "/" + allFiles[i]).getScaledCopy((float)resDispSize / quickimage.getHeight()));
					}
					catch (SlickException se) {}
				}
			}
		}
	}
	
	
	// Sets FileChooser is no longer readied for display
	public void setInactive() { active = false; }
	
	
	// Updates via mouse input
	public void update(Input in, int x, int y, int paneSizeX, int paneSizeY) {
		// Store the last mousewheel change (if any)
		int mouseWheel = Mouse.getDWheel();
		
		maxX = x + paneSizeX;
		maxY = y + paneSizeY;
		
		gridColumns = (paneSizeX - bdrSize * 2) / gridWidth;
		gridRows = (paneSizeY - bdrSize * 2 - cnxSize - cnxSpacing)  / gridHeight;
		
		// Adjust the displayed icons based on mousewheel input
		if (mouseWheel < 0)
			rowOffset++;
		if (mouseWheel > 0  &&  rowOffset > 0)
			rowOffset--;
		
		// Prevent over-scrolling the file display
		while (((fileNames.size() - ((gridRows + rowOffset - 1) * gridColumns)) < 0)  &&  rowOffset > 0) {
			rowOffset--;
		}
		
		// Adjust for x-coord render area on screen, border 
		int mouseX = in.getMouseX();
		int adjMouseX = mouseX - x - bdrSize;
		// Adjust for y-coord render area on screen, border, cancel button & spacing
		int mouseY = in.getMouseY();
		int adjMouseY = mouseY - y - bdrSize - (cnxSize + cnxSpacing);
		
		
		// Determine the grid space Mouse currently hovers over.  If not valid, denote with '-1'
		selX = (int)Math.floor((double)adjMouseX / gridWidth);
		selY = (int)Math.floor((double)adjMouseY / gridHeight);
		selIndex = selX + selY * gridColumns;
		System.out.println(selX + " | " + selY + " | " + selIndex);
		if (selX >= 0  &&  selX < gridColumns  &&  selY >= 0  &&  selY < gridRows  &&  selIndex >=0  &&  selIndex < fileNames.size())
			hoverIndex = selIndex;
		else
			hoverIndex = -1;
		
		
		// Mouseclick event
		if (in.isMousePressed(0)) {
			// If cancel button clicked 
			if ((mouseY > (bdrSize + y)) && (mouseY <= y + cnxSize + bdrSize)) {
				if ((mouseX > (maxX - x - bdrSize - cnxSize)) &&  (mouseX < (maxX - x - bdrSize))) {
					this.setInactive();
				}
			}
			
			// If clicked area is a token, stores token path, sets display to inactive
			if ((hoverIndex >= 0)  &&  ((hoverIndex + rowOffset * gridColumns) < fileNames.size())) {
				fileSelected = currentPath + (fileNames.get(hoverIndex + rowOffset * gridColumns) + ".png");
				this.setInactive();
			}
		}
	}
	
	
	
	// Draw loaded token Resources
	public void render(float getX, float getY, Graphics g) {
		g.setColor(Color.gray);
		int pos = 0;
		// Offset of displayed resources, based on mousewheel scrolling
		int fileOffset = rowOffset * gridColumns;
		float x = getX + bdrSize;
		float y = getY + bdrSize;
		
		// Draw the cancel button 10px from upper-right edge
		canx.draw((maxX - cnxSize - bdrSize), y);
		
		while ((pos + fileOffset) < fileImages.size()  &&  pos < (gridRows * gridColumns)) {
			x = getX + bdrSize + gridWidth * (pos % gridColumns); 
			y = getY + bdrSize + gridHeight * (pos / gridColumns) + (cnxSize + cnxSpacing) + 10;
			
			if (pos == hoverIndex)
				g.fillRoundRect(x, y - sboxVOff, gridWidth, gridHeight, 5);
			
			if (displayMode == 1) {
				fileImages.get(pos + fileOffset).draw(x + ((gridWidth - resDispSize) / 2), y);
				ttTokenFont.drawString((x + ((gridWidth - ttTokenFont.getWidth(fileNames.get(pos + fileOffset))) / 2)), (y + gridHeight - 24), fileNames.get(pos + fileOffset));
			}
			
			else if (displayMode == 2) {
				fileImages.get(pos + fileOffset).draw((x + ((gridWidth - fileImages.get(pos + fileOffset).getWidth()) / 2)), y + ((gridHeight - fileImages.get(pos + fileOffset).getHeight()) / 2));
				ttTokenFont.drawString((x + ((gridWidth - ttTokenFont.getWidth(fileNames.get(pos + fileOffset))) / 2)), (y + gridHeight - 16), fileNames.get(pos + fileOffset));
			}
			pos++;
		}
	}
	
}