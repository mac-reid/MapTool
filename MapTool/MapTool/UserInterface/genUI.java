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
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class genUI extends StateBasedGame{

	private static final String gamename = "MapTool";
	private static final int menu = 0;
	private static final int editor = 1;
	private static final int FRAME_BORDER = 13;
	private String mapFile;
	public Control control;
	private Image[] icons;
	
	public String name, mapName, hostAddress;
	
	public genUI(String name){
		super(gamename);
		this.addState(new Menu(menu));
		this.addState(new Editor(editor));
		control = new Control(this);
	}

	public void initStatesList(GameContainer gc) throws SlickException {
		//init icons
		initIcons();
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
			app.setDisplayMode(1400, 800, false);
			app.setTargetFrameRate(60);
			app.start();
		}catch(SlickException e){
			e.printStackTrace();
		}
	}
	
	
	//Control stuff and user/map info
	public void receiveChat(String user, String msg){
		((Editor)this.getState(editor)).chatBox.chatReceive(user + ": " + msg);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setAddress(String addr){
		hostAddress = addr;
	}
	
	public String getAddress(){
		return hostAddress;
	}
	
	public Image[] getIcons(){
		return icons;
	}
	
	public void initIcons(){
		icons = new Image[8];
		try {
			Image source = new Image("Resources/icons.png");
			icons[0] = source.getSubImage(32, 2, 18, 18);
			icons[1] = source.getSubImage(88, 3, 20, 20);
			icons[2] = source.getSubImage(149, 3, 20, 20);
			icons[3] = source.getSubImage(209, 2, 20, 20);
			icons[4] = source.getSubImage(32, 48, 20, 20);
			icons[5] = source.getSubImage(87, 48, 20, 20);
			icons[6] = source.getSubImage(148, 45, 25, 25);
			icons[7] = source.getSubImage(207, 43, 25, 25);
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
