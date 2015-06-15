import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import framework.Animation;
/***
 *  Manages and maintains game state. Holds all necessary variables to make the game work. Renders game to the display.
 *  
 * @author Liam
 *
 */

public class bootloader extends Applet implements Runnable, KeyListener {

	private Image image, currentSprite, character, character2, character3, characterJ, characterD,flyingEnemy,flyingEnemy2,flyingEnemy3, background;
	private static Image tiledirt,tilegrass,tiledecograss,tileocean,tilespikefloor,tilespikeceiling;
	private Animation animP, animE;
	private Graphics second;
	private URL base;
	
	private Thread thread;
	private static Player player;
	private static FlyingEnemy en1;
	private static int score;
	private ArrayList<Tile> tilearray;
	private static Background bg1, bg2; 
	private Font font;
	private boolean debug;
	private GameState state;
	
	
	enum GameState {
		Running, Dead;
	}
	
	@Override
	public void init() {
		
		//Create Window
		setSize(800,480);
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this); 
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Platformer Test");	

		try {
			base = getDocumentBase();
		} catch (Exception e){
			
		}
		
		//Image Setups
		character = getImage(base, "res/playertest.png");
		character2 = getImage(base, "res/playertest2.png");
		character3 = getImage(base, "res/playertest3.png");
		
		characterJ = getImage(base, "res/playerj.png");
		characterD = getImage(base, "res/playerd.png");
		
		
		flyingEnemy = getImage(base, "res/enemy.png");
		flyingEnemy2 = getImage(base, "res/enemy2.png");
		flyingEnemy3 = getImage(base, "res/enemy3.png");
		
		tiledirt = getImage(base, "res/Tile1.png");
		tilegrass = getImage(base, "res/Tile2.png");
		tiledecograss = getImage(base, "res/Tile3.png");
		tileocean = getImage(base, "res/Tile4.png");
		tilespikefloor = getImage(base, "res/SpikeFloor.png");
		tilespikeceiling = getImage(base, "res/SpikeCeiling.png");
		
		background = getImage(base, "res/background.png");
		
		animP = new Animation();
		animP.addFrame(character, 1250);
		animP.addFrame(character2, 50);
		animP.addFrame(character3, 50);
		animP.addFrame(character2, 50);
		
		animE = new Animation();
		animE.addFrame(flyingEnemy, 1250);
		animE.addFrame(flyingEnemy2, 50);
		animE.addFrame(flyingEnemy3, 50);
		animE.addFrame(flyingEnemy2, 50);
		
		currentSprite = animP.getImage();

	}

	
	@Override
	public void start() {
		thread = new Thread(this);
		bg1 = new Background(0,0);
		bg2 = new Background(2160,0);
		player = new Player();
		state = GameState.Running;
		tilearray = new ArrayList<Tile>();
		//Initialize Tiles
		
		try{
			loadMap("maps/test1.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		score = 0; 
		font = new Font(null, Font.BOLD, 30);
		en1 = new FlyingEnemy(340,300);
		thread.start();
	}
	
	public void restart() {
		player.restart();
		en1.restart();
		state = GameState.Running;
		bg1.setBgX(0);
		bg1.setBgY(0);
		bg2.setBgX(2160);
		bg2.setBgY(0);
		tilearray = null;
		tilearray = new ArrayList<Tile>();
		score = 0;
		
		try {
			loadMap("maps/test1.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() { 
		super.stop();
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void run() {
		//Game Loop (currently set at 60fps)
		
		

		
		if(state == GameState.Running){
			while (true) {
				if(player.getHealth() <= 0){
					state = GameState.Dead;
					System.out.println("Dead");
				}
				player.update();
				if(player.isJumped()){
					currentSprite = characterJ;
				}else if(player.isJumped() == false && player.isDucked() == false){
					currentSprite = animP.getImage();
				}
				
				ArrayList<Projectile> projectiles = player.getProjectiles();
				for(int i = 0; i < projectiles.size();i++){
					Projectile p = (Projectile) projectiles.get(i);
					if(p.isVisible() == true){
						p.update();
					} else {
						projectiles.remove(i);
					}
				}
				
				ArrayList<Projectile> enProjectiles = en1.getProjectiles();
				for(int i = 0; i < enProjectiles.size(); i++){
					Projectile p = (Projectile) enProjectiles.get(i);
					if(p.isVisible() == true){
						p.update();
					} else {
						enProjectiles.remove(i);
					}
				}
				
				updateTiles();
				en1.update();
				bg1.update();
				bg2.update();
				
				animate();
				repaint();
				try {
					Thread.sleep(17);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(player.getCenterY() > 1000){
					state = GameState.Dead;
				}
			}
		}
		
		
	}

	@Override
	public void update(Graphics g){
		if(image == null){
			image = createImage(this.getWidth(),this.getHeight());
			second = image.getGraphics();
		}
		
		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);
		
		g.drawImage(image,0,0,this);
	}
	
	public void paint(Graphics g){

		if(state == GameState.Running){
			g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
			g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
			paintTiles(g);
			
			ArrayList<Projectile> projectiles = player.getProjectiles();
			for(int i = 0; i < projectiles.size(); i++){
				Projectile p = (Projectile) projectiles.get(i);
				g.setColor(Color.YELLOW);
				if(p.getDirection() == 0 || p.getDirection() == 1)
					g.fillRect(p.getX(), p.getY(), 10, 5);
				else
					g.fillRect(p.getX(), p.getY(), 5, 10);
			}
			
			ArrayList<Projectile> enProjectiles = en1.getProjectiles();
			for(int i = 0; i < enProjectiles.size(); i++){
				Projectile p = (Projectile) enProjectiles.get(i);
				g.setColor(Color.YELLOW);
				if(p.getDirection() == 0 || p.getDirection() == 1)
					g.fillRect(p.getX(), p.getY(), 10, 5);
				else
					g.fillRect(p.getX(), p.getY(), 5, 10);
			}
			
			g.drawImage(currentSprite, player.getCenterX() - 61 , player.getCenterY() - 63, this);
			g.drawImage(animE.getImage(), en1.getCenterX() - 48, en1.getCenterY() - 48, this);
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString(Integer.toString(score), 740, 30);
			
			
			if(debug){
				g.drawRect((int)Player.getBottom().getX(), (int)Player.getBottom().getY(), (int)Player.getBottom().getWidth(), (int)Player.getBottom().getHeight());
				g.drawRect((int)Player.getHead().getX(), (int)Player.getHead().getY(), (int)Player.getHead().getWidth(), (int)Player.getHead().getHeight());
				g.drawRect((int)Player.getLeftHand().getX(), (int)Player.getLeftHand().getY(), (int)Player.getLeftHand().getWidth(), (int)Player.getLeftHand().getHeight());
				g.drawRect((int)Player.getRightHand().getX(), (int)Player.getRightHand().getY(), (int)Player.getRightHand().getWidth(), (int)Player.getRightHand().getHeight());
				g.drawRect((int)Player.getCheck().getX(), (int)Player.getCheck().getY(), (int)Player.getCheck().getWidth(), (int)Player.getCheck().getHeight());
				g.drawRect((int)en1.getHitbox().getX(), (int)en1.getHitbox().getY(), (int)en1.getHitbox().getWidth(), (int)en1.getHitbox().getHeight());
			}
		} else if (state == GameState.Dead){
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 480);
			g.setColor(Color.WHITE);
			g.drawString("Dead", 360, 240);
		}
		
		
		
	}
	
	private void updateTiles(){
		
		for(int i = 0; i < tilearray.size(); i++){
			Tile t = (Tile) tilearray.get(i);
			t.update();
		}
	}
	
	private void paintTiles(Graphics g){
		for(int i = 0; i < tilearray.size(); i++){
			Tile t = (Tile)tilearray.get(i);
			g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY(), this);
		}
	}

	public static Player getPlayer(){
		return player;
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		switch(e.getKeyCode()){
		
		case KeyEvent.VK_W:
			player.setLookup(true);
			break;
			
		case KeyEvent.VK_S:
			currentSprite = characterD;
			if(player.isJumped() == false){
				player.setDucked(true);
				player.setSpeedX((byte) 0);
			}
			break;
			
		case KeyEvent.VK_A:
			player.moveLeft();
			player.setMovingLeft(true);
			break;
			
		case KeyEvent.VK_D:
			player.moveRight();
			player.setMovingRight(true);
			break;
			
		case KeyEvent.VK_SPACE:
			player.jump();
			break;
			
		case KeyEvent.VK_CONTROL:
			player.shoot();
			player.setReadyToFire(false);
			break;
			
			
		case KeyEvent.VK_F12:
			if(debug)
				debug = false;
			else
				debug = true;
			break;
			
		case KeyEvent.VK_ESCAPE:
			if(state == GameState.Dead){
				restart();
			}
				
		}
		
		
			
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		switch(e.getKeyCode()){
		
		case KeyEvent.VK_W:
			player.setLookup(false);
			break;
			
		case KeyEvent.VK_S:
			currentSprite = animP.getImage();
			player.setDucked(false);
			break;
			
		case KeyEvent.VK_A:
			player.stopLeft();
			break;
			
		case KeyEvent.VK_D:
			player.stopRight();
			break;
			
		case KeyEvent.VK_SPACE:
			System.out.println("Stop jumping");
			break;
			
		case KeyEvent.VK_CONTROL:
			player.setReadyToFire(true);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static Background getBg1() {
		return bg1;
	}
	
	public static Background getBg2() { 
		return bg2;
	}
	
	public void animate(){
		animP.update(10);
		animE.update(50);
	}
	
	public static Image getDirtTile(){
		return tiledirt;
	}
	
	public static Image getGrassTile(){
		return tilegrass;
	}
	public static Image getDecoGrassTile(){
		return tiledecograss;
	}
	public static Image getOceanTile(){
		return tileocean;
	}
	
	public static Image getSpikeTileFloor(){
		return tilespikefloor;
	}
	public static Image getSpikeTileCeiling(){
		return tilespikeceiling;
	}
		public static FlyingEnemy getFlyingEmemy(){
		return en1;
	}
	
	public static int getScore() {
		return score;
	}

	public static void setScore(int score) {
		bootloader.score = score;
	}

	private void loadMap(String filename) throws IOException {
		ArrayList lines = new ArrayList();
		int width = 0;
		int height = 0; 
		
		
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		
		while (true) {
			String line = reader.readLine();
			
			if( line == null) {
				reader.close();
				break;
			}
			
			if (!line.startsWith("#")) {
				lines.add(line);
				width = Math.max(width, line.length());
			}
		}
		height = lines.size();
		
		for(int j = 0; j < 12; j++){
			String line = (String) lines.get(j);
			for(int i = 0; i <width; i++){
				
				if(i < line.length()) {
					char ch = line.charAt(i);
					Tile t = new Tile(i, j, Character.getNumericValue(ch));
					tilearray.add(t);
				}
			}
		}
		
	}
	
}
