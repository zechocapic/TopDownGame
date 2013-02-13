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
	private static int margin = 2;
	
	// Declaration of map, camera and player
	private TiledMap theMap;
	private PropertyTileBasedMap thePTBMap;
	private Camera camera;
	private Player meMyself;
	private AStarPathFinder pathFinder;
	private Path path;
	private int playerEtape = 0;
	private boolean destClicked = false;
	
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
		thePTBMap = new PropertyTileBasedMap(theMap);
		camera = new Camera(0f, 0f);
		meMyself = new Player(64f, 64f);
		pathFinder = new AStarPathFinder(thePTBMap, 100, false);
		path = pathFinder.findPath(null, 2, 7, 11, 7);
		if (path != null)
		{
			for (int i=0; i < path.getLength(); i++)
			{
				System.out.println("Etape" + i + " PathX=" + path.getX(i) + " PathY=" + path.getY(i));
			}			
		}
		else
		{
			System.out.println("No path found");
		}
		
		
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
		
		meMyself.setMovement(movingDown);		
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
		
		// test clic souris
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
		{
			destClicked = !destClicked;
		}
		
		if (path != null)
		{
			if ( (playerEtape != path.getLength()) && destClicked)
			{
				// testing if player and stepdestination are close enough to consider it's ok
				if ((meMyself.getX() < path.getX(playerEtape)*tileSize) && (meMyself.getX() + 1 > path.getX(playerEtape)*tileSize))
				{
					meMyself.setX(path.getX(playerEtape)*tileSize);
				}
				else if ((meMyself.getX() > path.getX(playerEtape)*tileSize) && (meMyself.getX() - 1 < path.getX(playerEtape)*tileSize))
				{
					meMyself.setX(path.getX(playerEtape)*tileSize);
				}
				else if ((meMyself.getY() < path.getY(playerEtape)*tileSize) && (meMyself.getY() + 1 > path.getY(playerEtape)*tileSize))
				{
					meMyself.setY(path.getY(playerEtape)*tileSize);
				}
				else if ((meMyself.getY() > path.getY(playerEtape)*tileSize) && (meMyself.getY() - 1 < path.getY(playerEtape)*tileSize))
				{
					meMyself.setY(path.getY(playerEtape)*tileSize);
				}
				
				// moving player to stepdestination
				if (meMyself.getY() > path.getY(playerEtape)*tileSize)
				{
					meMyself.setY(meMyself.getY() - delta * 0.1f);
					meMyself.setMovement(movingUp);
				}
				else if (meMyself.getY() < path.getY(playerEtape)*tileSize)
				{
					meMyself.setY(meMyself.getY() + delta * 0.1f);
					meMyself.setMovement(movingDown);
				}
				else if (meMyself.getX() > path.getX(playerEtape)*tileSize)
				{
					meMyself.setX(meMyself.getX() - delta * 0.1f);
					meMyself.setMovement(movingLeft);
				}
				else if (meMyself.getX() < path.getX(playerEtape)*tileSize)
				{
					meMyself.setX(meMyself.getX() + delta * 0.1f);
					meMyself.setMovement(movingRight);
				}
				
				//testing if player has arrived at destination
				if ((meMyself.getX() == path.getX(playerEtape)*tileSize) && (meMyself.getY() == path.getY(playerEtape)*tileSize))
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
		g.drawString("mouseTileX = " + (int)Mouse.getX()/tileSize, 600, 100);
		g.drawString("mouseTileY = " + (int)(600 - Mouse.getY())/tileSize, 600, 120);
		g.drawString("Tile blocked ? " + thePTBMap.blocked(null, 0, 0), 600, 140);
		if (path != null) { g.drawString("Path length = " + path.getLength(), 600, 160);}
		//g.drawString("PathX=" + path.getX(0) + " PathY=" + path.getY(0), 600, 140);
		//g.drawString("PathX=" + path.getX(1) + " PathY=" + path.getY(1), 600, 160);
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
