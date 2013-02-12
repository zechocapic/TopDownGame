package org.topdowngame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

public class MainTopDownGame extends BasicGame
{
	// Tilesize, resolution and margin before collision
	private static final int tileSize = 32;
	private static final int resolutionX = 800;
	private static final int resolutionY = 600;
	private static int margin = 2;
	
	// Declaration of map, camera and player
	private TiledMap theMap;
	private Camera camera;
	private Player meMyself;
	
	// Declaration of player's animation. Need to be integrated in Player class ideally 
	private Animation movingUp, movingDown, movingLeft, movingRight;
	
	// Constructor
	public MainTopDownGame() {
		super("TopDown Game v.01");
	}
	
	// Init method needed by superclass
	@Override
	public void init(GameContainer gc) throws SlickException 
	{
		// Initialisation of map, camera and player
		theMap = new TiledMap("res/tilemap01.tmx");
		camera = new Camera(400f, 300f);
		meMyself = new Player(64f, 64f);
		
		// player's animation. Need a better way to deal with it
		int duration[] = {200, 200, 200};
		SpriteSheet character = new SpriteSheet("res/monsters.png", tileSize, tileSize);
		Image[] walkUp = {character.getSubImage(6, 1), character.getSubImage(7, 1), character.getSubImage(8, 1)};
		Image[] walkDown = {character.getSubImage(0, 1), character.getSubImage(1, 1), character.getSubImage(2, 1)};
		Image[] walkLeft = {character.getSubImage(9, 1), character.getSubImage(10, 1), character.getSubImage(11, 1)};
		Image[] walkRight = {character.getSubImage(3, 1), character.getSubImage(4, 1), character.getSubImage(5, 1)};
		
		movingUp = new Animation(walkUp, duration, true);
		movingDown = new Animation(walkDown, duration, true);
		movingLeft = new Animation(walkLeft, duration, true);
		movingRight = new Animation(walkRight, duration, true);
		
		meMyself.setMovement(movingUp);		
	}
	
	// Update method needed by superclass
	@Override
	public void update(GameContainer gc, int delta) throws SlickException 
	{
		// Declaration and initialization of object that will monitor keypresses
		Input input = gc.getInput();
		
		// player going up
		if (input.isKeyDown(Input.KEY_UP))
		{
			meMyself.setMovement(movingUp);
			int tileIdLeft = theMap.getTileId((int)Math.floor((meMyself.getX())/tileSize), (int)Math.floor((meMyself.getY() - margin)/tileSize), 0);
			int tileIdRight = theMap.getTileId((int)Math.floor((meMyself.getX() + tileSize)/tileSize), (int)Math.floor((meMyself.getY() - margin)/tileSize), 0);
			if (!(theMap.getTileProperty(tileIdLeft, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdRight, "blocked", "false").equals("true")))
			{
				meMyself.setY(meMyself.getY() - delta * 0.1f);
				camera.setY(camera.getY() + delta * 0.1f);
			}
		}
		
		// player going down
		if (input.isKeyDown(Input.KEY_DOWN))
		{
			meMyself.setMovement(movingDown);
			int tileIdLeft = theMap.getTileId((int)Math.floor((meMyself.getX())/tileSize), (int)Math.floor((meMyself.getY() + tileSize + margin)/tileSize), 0);
			int tileIdRight = theMap.getTileId((int)Math.floor((meMyself.getX() + tileSize)/tileSize), (int)Math.floor((meMyself.getY() + tileSize + margin)/tileSize), 0);
			if (!(theMap.getTileProperty(tileIdLeft, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdRight, "blocked", "false").equals("true")))
			{
				meMyself.setY(meMyself.getY() + delta * 0.1f);
				camera.setY(camera.getY() - delta * 0.1f);
			}
		}
		
		// player going left
		if (input.isKeyDown(Input.KEY_LEFT))
		{
			meMyself.setMovement(movingLeft);
			int tileIdUp = theMap.getTileId((int)Math.floor((meMyself.getX() - margin)/tileSize), (int)Math.floor((meMyself.getY())/tileSize), 0);
			int tileIdDown = theMap.getTileId((int)Math.floor((meMyself.getX() - margin)/tileSize), (int)Math.floor((meMyself.getY() + tileSize)/tileSize), 0);
			if (!(theMap.getTileProperty(tileIdUp, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdDown, "blocked", "false").equals("true")))
			{
				meMyself.setX(meMyself.getX() - delta * 0.1f);
				camera.setX(camera.getX() + delta * 0.1f);
			}
		}
		
		// player going right
		if (input.isKeyDown(Input.KEY_RIGHT))
		{
			meMyself.setMovement(movingRight);
			int tileIdUp = theMap.getTileId((int)Math.floor((meMyself.getX() + tileSize + margin)/tileSize), (int)Math.floor((meMyself.getY())/tileSize), 0);
			int tileIdDown = theMap.getTileId((int)Math.floor((meMyself.getX() + tileSize + margin)/tileSize), (int)Math.floor((meMyself.getY() + tileSize)/tileSize), 0);
			if (!(theMap.getTileProperty(tileIdUp, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdDown, "blocked", "false").equals("true")))
			{
				meMyself.setX(meMyself.getX() + delta * 0.1f);
				camera.setX(camera.getX() - delta * 0.1f);
			}
		}		
	}
	
	// Render method needed by superclass
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// map rendering
		theMap.render(0 + (int)camera.getX(), 0 + (int)camera.getY());
		
		// player rendering
		meMyself.getMovement().draw(meMyself.getX() + camera.getX(), meMyself.getY() + camera.getY());
		
		// some indicators
		g.drawString("playerX = " + (meMyself.getX() + camera.getX()), 600, 20);
		g.drawString("playerY = " + (meMyself.getY() + camera.getY()), 600, 40);
		g.drawString("mouseX = " + Mouse.getX(), 600, 60);
		g.drawString("mouseY = " + (600 - Mouse.getY()), 600, 80);
		//int mouseX = (int)Math.floor(((Mouse.getX() + 400)/tileSize));
		//int mouseY = (int)Math.floor((900 - Mouse.getY())/tileSize);
		//int tileId =  11;
		//tileId = theMap.getTileId(0, 0, 0);
		//tileId = theMap.getTileId((int)Math.floor(((Mouse.getX() + 400)/tileSize)), (int)Math.floor((900 - Mouse.getY())/tileSize), 0);
		//g.drawString("Tile Id = " + tileId, 600, 100);
		//g.drawString("Tile blocked ? " + theMap.getTileProperty(tileId, "blocked", "false"), 600, 120);		
	}
	
	// Main method
	public static void main(String[] args) 
	{
		try 
		{
			AppGameContainer appgc = new AppGameContainer(new MainTopDownGame());
			appgc.setDisplayMode(resolutionX, resolutionY, false);
			appgc.start();
		} catch (SlickException e) 
		{
			e.printStackTrace();
		}
		
	}
}
