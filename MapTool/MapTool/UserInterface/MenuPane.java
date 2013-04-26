package UserInterface;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import Backend.Control;


public class MenuPane {

	Image background;
	int width, height, imgWidth, imgHeight;
	int hoverarea = 0;
	float scale = 1;
	public MenuButton changeMap, importToken, importMap, save, load, quit;
	public Editor editor;

	public MenuPane(Editor editor){
		this.editor = editor;
		Control control = this.editor.genUI.control;
		//initialize the width and height
		width = 0;
		height = editor.MENU_PANE_HEIGHT;
		try{
			background = new Image("Resources/vidChatBack.png");
			changeMap = new MenuButton("Resources/changeMap.png", 0, 0, scale, control);
			importToken = new MenuButton("Resources/importIcon.png", 0, 0, scale, control);
			importMap = new MenuButton("Resources/ImportMap.png", 0, 0, scale, control);
			save = new MenuButton("Resources/SaveGame.png", 0, 0, scale, control);
			load = new MenuButton("Resources/LoadGame.png", 0, 0, scale, control);
			quit = new MenuButton("Resources/quit.png", 0, 0, scale, control);
			imgWidth = save.img.getWidth();
			imgHeight = save.img.getHeight();
		} catch (SlickException e){
			e.printStackTrace();
		}
	}

	
	public void renderMenuPane(int x, int y, Graphics g){
		background = background.getScaledCopy(width, height);
		g.drawImage(background, x, y);
		
		g.setColor(Color.blue);
		switch(hoverarea){
		case 0:
			break;
		case 1:
			g.fillRect(changeMap.getX() - 2, changeMap.getY() - 2, imgWidth + 4, imgHeight + 4);
			break;
		case 2:
			g.fillRect(importToken.getX() - 2, importToken.getY() - 2, imgWidth + 4, imgHeight + 4);
			break;
		case 3:
			g.fillRect(importMap.getX() - 2, importMap.getY() - 2, imgWidth + 4, imgHeight + 4);
			break;
		case 4:
			g.fillRect(save.getX() - 2, save.getY() - 2, imgWidth + 4, imgHeight + 4);
			break;
		case 5:
			g.fillRect(load.getX() - 2, load.getY() - 2, imgWidth + 4, imgHeight + 4);
			break;
		case 6:
			g.fillRect(quit.getX() - 2, quit.getY() - 2, imgWidth + 4, imgHeight + 4);
			break;
		}
		g.setColor(Color.black);
		
		changeMap.render(g);
		importToken.render(g);
		importMap.render(g);
		save.render(g);
		load.render(g);
		quit.render(g);
	}
	
	public void update(GameContainer gc, Graphics g){
		width = gc.getWidth() - editor.CHAT_WIDTH_MIN - editor.BUFFER * 3;
		Input in = gc.getInput();
		int mouseX = in.getMouseX();
		int mouseY = in.getMouseY();
		int imgWidth = save.img.getWidth();
		int imgHeight = save.img.getHeight();
		int buffer = editor.BUFFER;
		//reset the position of the buttons
		//set X
		changeMap.setX(buffer * 2);
		importToken.setX(buffer * 2);
		importMap.setX(buffer * 2);
		save.setX(gc.getWidth() - buffer * 3 - editor.CHAT_WIDTH_MIN - imgWidth);
		load.setX(gc.getWidth() - buffer * 3 - editor.CHAT_WIDTH_MIN - imgWidth);
		quit.setX(gc.getWidth() - buffer * 3 - editor.CHAT_WIDTH_MIN - imgWidth);
		//setY
		int top = gc.getHeight() - editor.MENU_PANE_HEIGHT;
		changeMap.setY(top);
		importToken.setY(top + imgHeight + buffer);
		importMap.setY(top + 2 * imgHeight + buffer * 2);
		save.setY(top);
		load.setY(top + imgHeight + buffer);
		quit.setY(top + 2 * imgHeight + buffer * 2);
		
		//check the mouse location
		//left side
		if(mouseX >= changeMap.getX() && mouseX <= changeMap.getX() + imgWidth){
			if(mouseY >= changeMap.getY() && mouseY <= changeMap.getY() + imgHeight){
				hoverarea = 1;
			}
			if(mouseY >= importToken.getY() && mouseY <= importToken.getY() + imgHeight){
				hoverarea = 2;
			}
			if(mouseY >= importMap.getY() && mouseY <= importMap.getY() + imgHeight){
				hoverarea = 3;
			}
		} else if(mouseX >= save.getX() && mouseX <= save.getX() + imgWidth){
			if(mouseY >= save.getY() && mouseY <= save.getY() + imgHeight){
				hoverarea = 4;
			}
			if(mouseY >= load.getY() && mouseY <= load.getY() + imgHeight){
				hoverarea = 5;
			}
			if(mouseY >= quit.getY() && mouseY <= quit.getY() + imgHeight){
				hoverarea = 6;
			}
		} else {
			hoverarea = 0;
		}
		
		//if you click a button
		if(in.isMousePressed(0)){
			switch(hoverarea){
			case 0:
				break;
			case 1:
				editor.mapTool.fileChooser.setActive("Maps");
				editor.mapTool.loadMode = 1;
				break;
			case 2:
				//TODO import token
				editor.genUI.control.importTokens();
				break;
			case 3:
				//TODO import map
				editor.genUI.control.importMaps();
				break;
			case 4:
				//TODO save
				editor.genUI.control.save();
				break;
			case 5:
				//TODO load
				editor.genUI.control.load();
				break;
			case 6:
				editor.genUI.control.disconnect();
				editor.genUI.enterState(0);
				editor.genUI.control.disconnect();
				Toolkit kit = Toolkit.getDefaultToolkit();
				int width = (int)(.8*kit.getScreenSize().getWidth());
				//the original image was 1280/800 res, so thats what those numbers are for
				int height = (int)(800*width/1280);
				//make sure it isnt drawn huge
				if(width > 1280) width = 1280;
				if(height > 800) height = 800;
				try {
					((AppGameContainer) gc).setDisplayMode(width, height, false);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	

}




























