import java.awt.Rectangle;
/***
 *  Used by the Player to attack the enemy.
 * 
 * @author Liam
 *
 */

public class Projectile {

	private int x, y, speedX,limit, direction; 
	private boolean visible, enemy;
	private Rectangle hitbox; 
	
	public Projectile(){
		x = 0;
		y = 0;
		speedX = 7;
		limit = 800;
		visible = true;
		
		hitbox = new Rectangle(0, 0, 0, 0);
	}
	
	public Projectile(int startX, int startY, int direction){
		x = startX;
		y = startY;
		// 0= forward 1= behind 2= bellow 
		this.direction = direction;
		speedX = 7; 
		limit = 800;
		visible = true;
		
		hitbox = new Rectangle(0, 0, 0, 0);
	}
	
	public Projectile(int startX, int startY, int direction, int speedX, int limit){
		x = startX;
		y = startY; 
		// 0= forward 1= behind 2= bellow 
		this.direction = direction;
		this.speedX = speedX;
		this.limit = limit;
		
		hitbox = new Rectangle(0, 0, 0, 0);
	}
	
	public void update(){
		if(direction == 0)
			x += speedX;
		else if(direction == 1)
			x -= speedX;
		else if (direction == 2)
			y += speedX;
		
		hitbox.setBounds(x,y, 10, 5);
		
		if(x > limit){
			visible = false;
			hitbox = null;
		}
		
		if(x < limit){
			checkCollision();
		}
	}
	
	private void checkCollision(){
		if(hitbox.intersects(bootloader.getFlyingEmemy().getHitbox()) && !enemy){
			
			Enemy en = bootloader.getFlyingEmemy();
			visible = false;
			
			if(en.getCurrentHealth() > 0){
				en.setCurrentHealth(en.getCurrentHealth()-25);
			}
			if(en.getCurrentHealth() == 0){
				en.setCenterX(-100);
				bootloader.setScore( bootloader.getScore()+5);
			}
			
			bootloader.setScore((bootloader.getScore() + 1));
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isEnemy() {
		return enemy;
	}

	public void setEnemy(boolean enemy) {
		this.enemy = enemy;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	@Override
	public String toString() {
		return "Projectile [x=" + x + ", y=" + y + ", speedX=" + speedX
				+ ", limit=" + limit + ", visible=" + visible + "]";
	}
	
	
	
}
