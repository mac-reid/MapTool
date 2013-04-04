package UserInterface;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class MenuOptions {
	
	//decides which options (host or join) to load
	private int state;
	private int startX = 0;
	private int startY = 0;
	private int yChange = 60;
	private String targetIP;
	private String name;
	//the possible option fields that can be generated
	private OptionField[] fields;
	private Image cancelContinue;
	
	private double scale;
	
	private boolean isActive;
	private MenuButton control;
	
	public MenuOptions(int state, MenuButton control, GameContainer gc) throws SlickException{
		this.control = control;
		this.state = state;
		//make the cancelcontinue button different
		cancelContinue = new Image("Resources/cancelORcontinue.png")
			.getScaledCopy(gc.getWidth()/3, gc.getHeight()/6);
		yChange = 60;
		startX = gc.getWidth()*5/8;
		startY = gc.getHeight()/2;
		switch(state){
		case 0: {
			fields = new OptionField[3];
			//make IP field
			fields[0] = new OptionField("Game Name: ", startX, startY);
			fields[1] = new OptionField("Map File: ", startX, startY + yChange, true);
			fields[2] = new OptionField("Avatar Name: ", startX, startY + 2*yChange);
			break;
		}
		case 1: {
			fields = new OptionField[2];
			fields[0] = new OptionField("IP Addr: ", startX, startY);
			fields[1] = new OptionField("Avatar Name: ", startX, startY + yChange);
			break;
		}
		}
	}
	
	public void render(GameContainer gc){
		try{
			for(int i = 0; i < fields.length; i++){
				fields[i].render(gc);
			}
			cancelContinue.draw(startX, gc.getHeight() - cancelContinue.getHeight() - 15, .90f);
		}catch (NullPointerException e){		}

	}

	public void update(GameContainer gc, Input in, StateBasedGame sbg){
		try {
			for(int i = 0; i < fields.length; i++){
				fields[i].update(gc, gc.getInput(), sbg);
			}
			
			int mouseX = in.getMouseX();
			int mouseY = in.getMouseY();
			int continueTop = gc.getHeight() - cancelContinue.getHeight() - 15;
			int separation = gc.getHeight() - cancelContinue.getHeight()/2 - 15;
			int cancelBot = gc.getHeight() - 15;
			//if the mouse gets pressed
			if (in.isMouseButtonDown(0)){
				if (mouseX >= startX && mouseX <= startX + cancelContinue.getWidth()){
					//continue
					if (mouseY <= separation && mouseY >= continueTop){
						sbg.enterState(1);
						((AppGameContainer) gc).setResizable(true);
					}
					//cancel
					if (mouseY <= cancelBot && mouseY > separation){
						control.deActivate();
					}
				}
			}
		}catch (NullPointerException e){		}
	}
	
}
