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
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

public class MainTopDownGame extends BasicGame
{
	// Tilesize, resolution and margin before collision
	private static final int tileSize = 32;
	private static final int resolutionX = 800;
	private static final int resolutionY = 600;
	
	// Declaration of map, camera and player
	private TiledMap theMap;
	private PropertyTileBasedMap thePTBMap;
	private Camera camera;
	private Player unitOne;
	private AStarPathFinder pathFinder;
	private Path path;
	private int playerEtape = 0;
	
	// Declaration of player's animation. Need to be integrated in Player class ideally 
	private Animation movingUp, movingDown, movingLeft, movingRight;
	
	// Constructor
	public MainTopDownGame() 
	{
		super("TopDown Game v.01");
	}
	
	// Init method needed by superclass
	@Override
	public void init(GameContainer gc) throws SlickException 
	{
		// Initialization of map, camera, player, pathfinding
		theMap = new TiledMap("res/tilemap01.tmx");
		thePTBMap = new PropertyTileBasedMap(theMap);
		camera = new Camera(0f, 0f);
		unitOne = new Player(64f, 64f);
		pathFinder = new AStarPathFinder(thePTBMap, 100, false);
		
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
		
		unitOne.setMovement(movingDown);
	}
	
	// Update method needed by superclass
	@Override
	public void update(GameContainer gc, int delta) throws SlickException 
	{
		// Declaration and initialization of object that will monitor keypresses
		Input input = gc.getInput();
		
		// camera management
		camera.keyboardMove(input, delta);
		
		// setting destination
		if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON))
		{
			int playerXTiles = (int)(unitOne.getX()/tileSize);
			int playerYTiles = (int)(unitOne.getY()/tileSize);
			int mouseXTiles = (int)((Mouse.getX() - camera.getX())/tileSize);
			int mouseYTiles = (int)((600 - Mouse.getY() - camera.getY())/tileSize);
			path = pathFinder.findPath(null, playerXTiles, playerYTiles, mouseXTiles, mouseYTiles);
			playerEtape = 0;
		}
		
		if (path != null)
		{
			if ( (playerEtape != path.getLength()))
			{
				// moving player to destination
				unitOne.goToDest(path.getX(playerEtape)*tileSize, path.getY(playerEtape)*tileSize, delta, movingUp, movingDown, movingLeft, movingRight);
				
				//testing if player has arrived at destination
				if ((unitOne.getX() == path.getX(playerEtape)*tileSize) && (unitOne.getY() == path.getY(playerEtape)*tileSize))
				{
					playerEtape++;
				}
			}
		}
	}
	
	// Render method needed by superclass
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		try 
		{
			Thread.sleep(10);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		// map rendering
		theMap.render(0 + (int)camera.getX(), 0 + (int)camera.getY());
		
		// player rendering
		unitOne.getMovement().draw(unitOne.getX() + camera.getX(), unitOne.getY() + camera.getY());
		
		// some indicators
		g.drawString("playerX = " + (unitOne.getX() + camera.getX()), 600, 20);
		g.drawString("playerY = " + (unitOne.getY() + camera.getY()), 600, 40);
		g.drawString("mouseX = " + Mouse.getX(), 600, 60);
		g.drawString("mouseY = " + (600 - Mouse.getY()), 600, 80);
		g.drawString("mouseTileX = " + (int)Mouse.getX()/tileSize, 600, 100);
		g.drawString("mouseTileY = " + (int)(600 - Mouse.getY())/tileSize, 600, 120);
		if (path != null) { g.drawString("Path length = " + path.getLength(), 600, 160);}
	}
	
	// Main method
	public static void main(String[] args) 
	{
		try 
		{
			AppGameContainer appgc = new AppGameContainer(new MainTopDownGame());
			appgc.setDisplayMode(resolutionX, resolutionY, false);
			appgc.start();
		} 
		catch (SlickException e) 
		{
			e.printStackTrace();
		}
	}
}