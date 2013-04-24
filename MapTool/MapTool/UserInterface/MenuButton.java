package UserInterface;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import Backend.Control;


public class MenuButton {
	
	private Image img;
	public int X;
	public int Y;
	public int XSize;
	public int YSize;
	private float imgScale = 1.0f;
	private float growthScale = .00018f;
	private boolean isDecreasing;
	private boolean isActive;
	private double whenActivated;

	public Control control;
	
	public MenuButton(String imgLocation, int locX, int locY, float scale, Control control) throws SlickException{
		this.control = control;
		img = new Image(imgLocation);
		img = img.getScaledCopy(scale);
		X = locX;
		Y = locY;
		XSize = img.getWidth();
		YSize = img.getHeight();
	}
	
	public MenuButton(Image img, int locX, int locY, float scale, Control control) throws SlickException{
		this.control = control;
		this.img = img;
		this.img = img.getScaledCopy(scale);
		X = locX;
		Y = locY;
		XSize = img.getWidth();
		YSize = img.getHeight();
	}
	
	public void render(GameContainer gc){
		img.draw(X - (imgScale*imgScale*100 - 100), Y - (imgScale*100 - 100), imgScale);
	}

	public void update(Input in, int delta){
		int mouseX = in.getMouseX();
		int mouseY = in.getMouseY();
		//if mouse is in correct X posistion
		if (mouseX >= X && mouseX <= X + XSize){
			//if mouse is in correct Y position
			if (mouseY >= Y && mouseY <= Y + YSize){
				//if we click, activate it
				if (in.isMousePressed(0)){
					isActive = true;
					imgScale = 1.0f;
					whenActivated = System.currentTimeMillis();
				}
				if (imgScale <= 1.0f){
					isDecreasing = false;
				}
				if (imgScale >= 1.10f){
					isDecreasing = true;
				}
				//depending on what its on, scale up or down,
				//only if it is not currently selected
				if (!isActive){
					if(isDecreasing){
						imgScale -= growthScale * delta;
						if(imgScale < 1.0) imgScale = 1.0f;
					} else {
						imgScale += growthScale * delta;
						//current fix for HUGE BUTTON BUG
						if(imgScale > 1.10f) imgScale = 1.10f;
					}
				}
			} else imgScale = 1.0f;
		} else imgScale = 1.0f;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
	public void deActivate(){
		isActive = false;
	}
	
	public double whenActivated(){
		return whenActivated;
	}
}
