import java.awt.GraphicsConfiguration;
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
	public genUI(String name) {
		super(gamename);
		this.addState(new Menu(menu));
		this.addState(new Editor(editor));
	}

	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(menu).init(gc, this);
		this.getState(editor).init(gc, this);
		this.enterState(editor);
	}

	public static void main(String[] args){		
		AppGameContainer app;
		Toolkit kit = Toolkit.getDefaultToolkit();
		int maxWidth = (int)kit.getScreenSize().getWidth();
		int maxHeight = (int)kit.getScreenSize().getHeight();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        Insets in = kit.getScreenInsets(gs[0].getDefaultConfiguration());
		int screenWidth = maxWidth - in.left - in.right - FRAME_BORDER;
		int screenHeight = maxHeight - in.bottom - in.top - 15 - 50;
		try{
			app = new AppGameContainer(new genUI(gamename));
			app.setDisplayMode(screenWidth, screenHeight, false);
			app.setResizable(true);
			app.start();
		}catch(SlickException e){
			e.printStackTrace();
		}
	}
}
