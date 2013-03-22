
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class CharacterTile {
	Image tileimage = null;
	int locationx = 1;
	int locationy = 1;

	public CharacterTile(String imglocation, int tilex, int tiley) throws SlickException {
		tileimage = new Image(imglocation);
		locationx = tilex;
		locationy = tiley;
	}
	
	public void renderTile(float offx, float offy) {
		tileimage.draw(offx + (locationx * 48), offy + (locationy * 48));
	}
	
	public int getX() {
		return locationx;
	}
	
	public int getY() {
		return locationy;
	}
	
	public void Move(int x, int y) {
		locationx = x;
		locationy = y;
	}
}
