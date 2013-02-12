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
	private static final int tileSize = 32;
	private static final int resolutionX = 800;
	private static final int resolutionY = 600;
	private static int margin = 2;
	
	private TiledMap theMap;
	private Camera camera;
	private Player meMyself;

	private Animation playerSprite, movingUp, movingDown, movingLeft, movingRight;
	
	public MainTopDownGame() {
		super("TopDown Game v.01");
	}

	@Override
	public void init(GameContainer gc) throws SlickException 
	{
		theMap = new TiledMap("res/tilemap01.tmx");
		camera = new Camera(400f, 300f);
		meMyself = new Player(100f, 100f);
		
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
		
		playerSprite = movingUp;
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException 
	{
		Input input = gc.getInput();
		
		// up
		if (input.isKeyDown(Input.KEY_UP))
		{
			playerSprite = movingUp;
			int tileIdLeft = theMap.getTileId((int)Math.floor((meMyself.getX())/tileSize), (int)Math.floor((meMyself.getY() - margin)/tileSize), 0);
			int tileIdRight = theMap.getTileId((int)Math.floor((meMyself.getX() + tileSize)/tileSize), (int)Math.floor((meMyself.getY() - margin)/tileSize), 0);
			if (!(theMap.getTileProperty(tileIdLeft, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdRight, "blocked", "false").equals("true")))
			{
				meMyself.setY(meMyself.getY() - delta * 0.1f);
				camera.setY(camera.getY() + delta * 0.1f);
			}
		}
		
		//down
		if (input.isKeyDown(Input.KEY_DOWN))
		{
			playerSprite = movingDown;
			int tileIdLeft = theMap.getTileId((int)Math.floor((meMyself.getX())/tileSize), (int)Math.floor((meMyself.getY() + tileSize + margin)/tileSize), 0);
			int tileIdRight = theMap.getTileId((int)Math.floor((meMyself.getX() + tileSize)/tileSize), (int)Math.floor((meMyself.getY() + tileSize + margin)/tileSize), 0);
			if (!(theMap.getTileProperty(tileIdLeft, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdRight, "blocked", "false").equals("true")))
			{
				meMyself.setY(meMyself.getY() + delta * 0.1f);
				camera.setY(camera.getY() - delta * 0.1f);
			}
		}
		
		//left
		if (input.isKeyDown(Input.KEY_LEFT))
		{
			playerSprite = movingLeft;
			int tileIdUp = theMap.getTileId((int)Math.floor((meMyself.getX() - margin)/tileSize), (int)Math.floor((meMyself.getY())/tileSize), 0);
			int tileIdDown = theMap.getTileId((int)Math.floor((meMyself.getX() - margin)/tileSize), (int)Math.floor((meMyself.getY() + tileSize)/tileSize), 0);
			if (!(theMap.getTileProperty(tileIdUp, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdDown, "blocked", "false").equals("true")))
			{
				meMyself.setX(meMyself.getX() - delta * 0.1f);
				camera.setX(camera.getX() + delta * 0.1f);
			}
		}
		
		//right
		if (input.isKeyDown(Input.KEY_RIGHT))
		{
			playerSprite = movingRight;
			int tileIdUp = theMap.getTileId((int)Math.floor((meMyself.getX() + tileSize + margin)/tileSize), (int)Math.floor((meMyself.getY())/tileSize), 0);
			int tileIdDown = theMap.getTileId((int)Math.floor((meMyself.getX() + tileSize + margin)/tileSize), (int)Math.floor((meMyself.getY() + tileSize)/tileSize), 0);
			if (!(theMap.getTileProperty(tileIdUp, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdDown, "blocked", "false").equals("true")))
			{
				meMyself.setX(meMyself.getX() + delta * 0.1f);
				camera.setX(camera.getX() - delta * 0.1f);
			}
		}
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		theMap.render(0 + (int)camera.getX(), 0 + (int)camera.getY());
		playerSprite.draw(meMyself.getX() + camera.getX(), meMyself.getY() + camera.getY());
		g.drawString("playerX = " + meMyself.getX(), 600, 20);
		g.drawString("playerY = " + meMyself.getY(), 600, 40);
		g.drawString("mouseX = " + Mouse.getX(), 600, 60);
		g.drawString("mouseY = " + (600 - Mouse.getY()), 600, 80);
		int tileId =  11;
		tileId = theMap.getTileId((int)Math.floor((Mouse.getX()/tileSize)), (int)Math.floor((600 - Mouse.getY())/tileSize), 0);
		g.drawString("Tile Id = " + tileId, 600, 100);
		g.drawString("Tile blocked = " + theMap.getTileProperty(tileId, "blocked", "false"), 600, 120);
		
	}

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
