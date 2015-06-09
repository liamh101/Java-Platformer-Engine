/**Class manages the sprites for the background
 * 
 * @author Liam
 *
 */
public class Background {
	
	private int bgX, bgY, speedX;
	
	/**Set Background with the height and width at zero. (showing nothing)
	 * 
	 */
	public Background(){
		bgX = 0;
		bgY = 0; 
		speedX = 0; 
	}
	
	/**Set background to the size of image. 
	 * 
	 *E.G. a 1920x1080 image will have the height of 1920 
	 *with the width of 1080.
	 * 
	 * @param x width of background
	 * @param y Height of background
	 */
	public Background(int x, int y){
		bgX = x; 
		bgY = y;
		speedX = 0;
	}

	/** Return the height of the background
	 * 
	 */
	public int getBgX() {
		return bgX;
	}

	/**Set the height of the background
	 * 
	 * @param bgX What size to make the background
	 */
	public void setBgX(int bgX) {
		this.bgX = bgX;
	}

	/**Return width of the background 
	 * 
	 * @return width of current background
	 */
	public int getBgY() {
		return bgY;
	}

	/**Set the size of the current background
	 * 
	 * @param bgY Size of the background
	 */
	public void setBgY(int bgY) {
		this.bgY = bgY;
	}

	/**Return the speed of which the current background is scrolling
	 * 
	 * @return speed int of speed
	 */
	public int getSpeedX() {
		return speedX;
	}
	
	/**Set speed at which the background scrolls
	 * 
	 * @param speedX int of speed of the background
	 */
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
	
	/**update background to be displayed when rendered.
	 * This method should be used in the game loop if classed is used.
	 * 
	 */
	public void update(){
		bgX += speedX;
		
		if(bgX <= -2160){
			bgX += 4320;
		}
	}

	/**
	 * Debug the class variables
	 */
	@Override
	public String toString() {
		return "Background [bgX=" + bgX + ", bgY=" + bgY + ", speedX=" + speedX
				+ "]";
	}
	
}
