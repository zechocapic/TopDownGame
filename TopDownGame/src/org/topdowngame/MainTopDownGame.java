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
	
	private Animation playerSprite, movingUp, movingDown, movingLeft, movingRight;
	private float playerX = 100f, playerY = 100f;
	private TiledMap theMap; 

	public MainTopDownGame() {
		super("TopDown Game v.01");
	}

	@Override
	public void init(GameContainer gc) throws SlickException 
	{
		int duration[] = {200, 200, 200};
		SpriteSheet character = new SpriteSheet("res/monsters.png", 32, 32);
		Image[] walkUp = {character.getSubImage(6, 1), character.getSubImage(7, 1), character.getSubImage(8, 1)};
		Image[] walkDown = {character.getSubImage(0, 1), character.getSubImage(1, 1), character.getSubImage(2, 1)};
		Image[] walkLeft = {character.getSubImage(9, 1), character.getSubImage(10, 1), character.getSubImage(11, 1)};
		Image[] walkRight = {character.getSubImage(3, 1), character.getSubImage(4, 1), character.getSubImage(5, 1)};
		
		movingUp = new Animation(walkUp, duration, true);
		movingDown = new Animation(walkDown, duration, true);
		movingLeft = new Animation(walkLeft, duration, true);
		movingRight = new Animation(walkRight, duration, true);
		
		playerSprite = movingUp;
		
		theMap = new TiledMap("res/tilemap01.tmx");
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException 
	{
		Input input = gc.getInput();
		
		// up
		if (input.isKeyDown(Input.KEY_UP))
		{
			playerSprite = movingUp;
			playerY -= delta * 0.1f;
			int tileIdLeft = theMap.getTileId((int)Math.floor((playerX + delta * 0.1f)/32), (int)Math.floor((playerY + delta * 0.1f)/32), 0);
			int tileIdRight = theMap.getTileId((int)Math.floor((playerX + 32 + delta * 0.1f)/32), (int)Math.floor((playerY + delta * 0.1f)/32), 0);
			if (theMap.getTileProperty(tileIdLeft, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdRight, "blocked", "false").equals("true"))
			{
				playerY += delta * 0.1f;				
			}
		}
		
		//down
		if (input.isKeyDown(Input.KEY_DOWN))
		{
			playerSprite = movingDown;
			playerY += delta * 0.1f;
			int tileIdLeft = theMap.getTileId((int)Math.floor((playerX + delta * 0.1f)/32), (int)Math.floor((playerY + 32 + delta * 0.1f)/32), 0);
			int tileIdRight = theMap.getTileId((int)Math.floor((playerX + 32 + delta * 0.1f)/32), (int)Math.floor((playerY + 32 + delta * 0.1f)/32), 0);
			if (theMap.getTileProperty(tileIdLeft, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdRight, "blocked", "false").equals("true"))
			{
				playerY -= delta * 0.1f;				
			}
		}
		
		//left
		if (input.isKeyDown(Input.KEY_LEFT))
		{
			playerSprite = movingLeft;
			playerX -= delta * 0.1f;
			int tileIdUp = theMap.getTileId((int)Math.floor((playerX - delta * 0.1f)/32), (int)Math.floor(playerY/32), 0);
			int tileIdDown = theMap.getTileId((int)Math.floor((playerX - delta * 0.1f)/32), (int)Math.floor((playerY + 32 + delta * 0.1f)/32), 0);
			if (theMap.getTileProperty(tileIdUp, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdDown, "blocked", "false").equals("true"))
			{
				playerX += delta * 0.1f;				
			}
		}
		
		//right
		if (input.isKeyDown(Input.KEY_RIGHT))
		{
			playerSprite = movingRight;
			playerX += delta * 0.1f;
			int tileIdUp = theMap.getTileId((int)Math.floor((playerX + 32 + delta * 0.1f)/32), (int)Math.floor(playerY/32), 0);
			int tileIdDown = theMap.getTileId((int)Math.floor((playerX + 32 + delta * 0.1f)/32), (int)Math.floor((playerY + 32 + delta * 0.1f)/32), 0);
			if (theMap.getTileProperty(tileIdUp, "blocked", "false").equals("true") || theMap.getTileProperty(tileIdDown, "blocked", "false").equals("true"))
			{
				playerX -= delta * 0.1f;				
			}
		}
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		theMap.render(0, 0);
		playerSprite.draw(playerX, playerY);
		g.drawString("playerX = " + playerX, 600, 20);
		g.drawString("playerY = " + playerY, 600, 40);
		g.drawString("mouseX = " + Mouse.getX(), 600, 60);
		g.drawString("mouseY = " + (600 - Mouse.getY()), 600, 80);
		int tileId =  11;
		tileId = theMap.getTileId((int)Math.floor((Mouse.getX()/32)), (int)Math.floor((600 - Mouse.getY())/32), 0);
		g.drawString("Tile Id = " + tileId, 600, 100);
		g.drawString("Tile blocked = " + theMap.getTileProperty(tileId, "blocked", "false"), 600, 120);
		
	}

	public static void main(String[] args) 
	{
		try 
		{
			AppGameContainer appgc = new AppGameContainer(new MainTopDownGame());
			appgc.setDisplayMode(800, 600, false);
			appgc.start();
		} catch (SlickException e) 
		{
			e.printStackTrace();
		}
		
	}

}
