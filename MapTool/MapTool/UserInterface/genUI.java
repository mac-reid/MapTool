package UserInterface;

import java.awt.GraphicsConfiguration;
import Backend.Control;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class genUI extends StateBasedGame{

	private static final String gamename = "MapTool";
	private static final int menu = 0;
	private static final int editor = 1;
	private static final int FRAME_BORDER = 13;
	private String mapFile;
	private Control control;
	
	public genUI(String name){
		super(gamename);
		this.addState(new Menu(menu));
		this.addState(new Editor(editor));
		control = new Control(this);
	}

	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(menu).init(gc, this);
		this.getState(editor).init(gc, this);
		this.enterState(menu);
	}

	public void setMapFile(String file){
		mapFile = file;
	}
	
	public String getMapFile(){
		return mapFile;
	}
	public static void main(String[] args){		
		AppGameContainer app;
		try{
			Toolkit kit = Toolkit.getDefaultToolkit();
			int width = (int)(.8*kit.getScreenSize().getWidth());
			//the original image was 1280/800 res, so thats what those numbers are for
			int height = (int)(800*width/1280);
			app = new AppGameContainer(new genUI(gamename));
			app.setDisplayMode(width, height, false);
			app.setTargetFrameRate(60);
			app.start();
		}catch(SlickException e){
			e.printStackTrace();
		}
	}
}
