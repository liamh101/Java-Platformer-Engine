import java.awt.Image;
import java.awt.Rectangle;


public class Tile {

	private int tileX, tileY, type, damage, pass;
	private byte speedX;
	private Image tileImage;
	private Player player;
	private Background bg; 
	private Rectangle r;
	
	/**Custom Constructor that gives a tile within a 2D array a image to render on screen.
	 * 
	 * @param x tile location on the X axis
	 * @param y tile location on the Y axis
	 * @param typeInt item within the array 
	 */
	public Tile(int x, int y, int typeInt) {
		tileX = x * 40; 
		tileY = y * 40;
		
		player = bootloader.getPlayer();
		bg = bootloader.getBg1();
		
		type = typeInt;
		
		r = new Rectangle();
		
		if (type == 1){
			tileImage = bootloader.getDirtTile();
			pass = 0;
			damage =0;
		}
		else if (type == 2){
			tileImage = bootloader.getGrassTile();
			pass = 0;
			damage =0; 
		}
		else if (type == 3){
			tileImage = bootloader.getOceanTile();
			pass = 1;
			damage = 100;
		}
		else if (type == 4){
			tileImage = bootloader.getSpikeTileFloor();
			pass = 1;
			//damage =100;
		}
		else if (type == 5){
			tileImage = bootloader.getSpikeTileCeiling();
			pass = 0;
			//damage =100;
		}
		else if (type == 9){
			tileImage = bootloader.getDecoGrassTile();
			pass = 1;
			damage = 0;
		}
		else {
			type = 0;
			pass = 1;
			damage =0;
		}
		
	}
	
	/**Set the speed at which the tiles move when the player moves
	 * This is also used for parallax scrolling. 
	 */
	public void update() { 
		speedX = (byte) (bg.getSpeedX()*5);
		tileX += speedX;
		r.setBounds(tileX, tileY,40,40);
		
		if( r.intersects(Player.getCheck()) &&  type != 0){
			checkVerticalCollision(Player.getBottom(), Player.getHead());
			checkSideCollision(Player.getLeftHand(), Player.getRightHand());
		}
	}

	public int getTileX() {
		return tileX;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
	}
	
	public void checkVerticalCollision(Rectangle rbot, Rectangle rtop){
		if(pass == 0){		
			if(rtop.intersects(r)){
			
			}
		
			if(rbot.intersects(r)){
				player.setJumped(false);
				player.setSpeedY((byte) 0);
				player.setCenterY(tileY);
				player.setHealth(player.getHealth() - damage);

			}
		}
	}
	public void checkSideCollision(Rectangle rleft, Rectangle rright){
		if(pass == 0 ) {
			
			if(rleft.intersects(r)) {
				System.out.println("in left side intersect");
				player.setCenterX(tileX + 88);
				player.setSpeedX((byte) 0);
			}
		/*	else if (leftFoot.intersects(r)) {
				System.out.println("in left foot intersect");
				player.setCenterX(tileX);
				player.setSpeedX(0);
			} */ 
			
			if(rright.intersects(r)) {
				System.out.println("in right side intersect");
				player.setCenterX(tileX + 10);
				player.setSpeedX((byte) 0);
			}
			
			/* else if (rightFoot.intersects(r)){
				System.out.println("in right foot intersect");
				player.setCenterX(tileX);
				player.setSpeedX(0);
			 } */
			
			
		}
		
		
	}
	
	
	
}
