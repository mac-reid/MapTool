package UserInterface;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Backend.Control;


public class Menu extends BasicGameState{

	//
	private MenuButton host, join, testMode;
	private MenuOptions hostOptions;
	private MenuOptions joinOptions;
	private String usrName;
	private String targetIP;
	private String sourceIP;
	private Image bkgnd;
	private Image selectionGlow;

	public Control control;
	
	public Menu(int state){
		
	}

	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		control = ((genUI)sbg).control;
		bkgnd = new Image("Resources/d20.png");
		bkgnd = bkgnd.getScaledCopy(gc.getWidth(), gc.getHeight());
		//get the scaled versions of host and join game buttons
		Image hostImg = (new Image("Resources/hostGame.png")
			.getScaledCopy(gc.getWidth()/3, gc.getHeight()/8));
		Image joinImg = (new Image("Resources/joinGame.png")
		.getScaledCopy(gc.getWidth()/3, gc.getHeight()/8));
		Image testImg = (new Image("Resources/test.png")
		.getScaledCopy(gc.getWidth()/3, gc.getHeight()/8));
		host = new MenuButton(hostImg, gc.getWidth()-hostImg.getWidth() - hostImg.getWidth()/8, 
				hostImg.getHeight()/8, 1, control);
		join = new MenuButton(joinImg, gc.getWidth()-joinImg.getWidth() - joinImg.getWidth()/8, 
				joinImg.getHeight() + 2*hostImg.getHeight()/8, 1, control);
		hostOptions = new MenuOptions(0, host, gc);
		joinOptions = new MenuOptions(1, join, gc);
		testMode = new MenuButton(testImg, gc.getWidth()-joinImg.getWidth() - joinImg.getWidth()/8, 
				2*joinImg.getHeight() + 3*hostImg.getHeight()/8, 1, control);
	}


	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		bkgnd.draw(0, 0);
		host.render(gc);
		join.render(gc);
		testMode.render(gc);
		//if only host is activated
		if (host.isActive() && !join.isActive()){
			hostOptions.render(gc);
		}
		//if only join is activated
		if (join.isActive() && !host.isActive()){
			joinOptions.render(gc);
		}
		if (join.isActive() && host.isActive()){
			if (join.whenActivated() > host.whenActivated()){
				joinOptions.render(gc);
				host.deActivate();
				//clear the fields
				for(int i = 0; i < hostOptions.fields.length; i++){
					hostOptions.fields[i].value = "";
				}
			} else {
				hostOptions.render(gc);
				join.deActivate();
				//clear the fields
				for(int i = 0; i < joinOptions.fields.length; i++){
					joinOptions.fields[i].value = "";
				}
			}
		}
	}


	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();
		((AppGameContainer) gc).setResizable(false);
		if(hostOptions.showDialog || joinOptions.showDialog){
			if(input.isMousePressed(0) || input.isMousePressed(1)){
				hostOptions.showDialog = false;
				joinOptions.showDialog = false;
				joinOptions.button.deActivate();
				hostOptions.button.deActivate();
			}
		}
		else {
			int mouseX = input.getMouseX();
			int mouseY = input.getMouseY();
			host.update(input, delta);
			join.update(input, delta);
			testMode.update(input, delta);
			if (join.isActive()){
				joinOptions.update(gc, gc.getInput(), sbg);
			}
			if (host.isActive()){
				hostOptions.update(gc, gc.getInput(), sbg);
			}
			if (testMode.isActive()){
				((genUI)sbg).setName("Test Mode");
				((genUI)sbg).setAddress("---Testing with no host---");
				sbg.enterState(1);
				((AppGameContainer) gc).setResizable(true);
			}
		}
	}

	public int getID() {
		return 0;
	}

}