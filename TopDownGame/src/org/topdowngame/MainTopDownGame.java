package org.topdowngame;

import java.util.ArrayList;

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

public class MainTopDownGame extends BasicGame
{
	// Tile size, resolution and margin before collision
	private static final int tileSize = 32;
	private static final int resolutionX = 800;
	private static final int resolutionY = 600;
	
	// Declaration of map, camera and player
	private TiledMap theMap;
	private PropertyTileBasedMap thePTBMap;
	private Camera camera;
	private ArrayList<Player> units = new ArrayList<Player>();
	//private Player unitTwo;
	private AStarPathFinder pathFinder;
	
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
		for (int i = 0; i < 2; i++)
		{
			units.add(new Player(64f, 64f + i*tileSize));
		}
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
		
		for (int i = 0; i < units.size(); i++)
		{
			units.get(i).setMovement(movingDown);
		}
	}
	
	// Update method needed by superclass
	@Override
	public void update(GameContainer gc, int delta) throws SlickException 
	{
		// Declaration and initialization of object that will monitor keypresses
		Input input = gc.getInput();
		
		// camera management
		camera.keyboardMove(input, delta);
		
		// adding new unit
		if (input.isMousePressed(Input.MOUSE_MIDDLE_BUTTON))
		{
			units.add(new Player((int)((Mouse.getX() - camera.getX())), (int)((600 - Mouse.getY() - camera.getY()))));
			units.get(units.size() - 1).setMovement(movingDown);
		}
		
		// selecting units
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
		{
			for (int i = 0; i < units.size(); i++)
			{
				int mouseXTiles = (int)((Mouse.getX() - camera.getX())/tileSize);
				int mouseYTiles = (int)((600 - Mouse.getY() - camera.getY())/tileSize);
				int unitXTiles = (int)(units.get(i).getX()/tileSize);
				int unitYTiles = (int)(units.get(i).getY()/tileSize);
				if ((unitXTiles == mouseXTiles) && (unitYTiles == mouseYTiles))
				{
					units.get(i).setSelected(true);
					System.out.println("Unit " + i + " has been selected");
				}
			}
		}

		// setting destination
		if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON))
		{
			for (int i = 0; i < units.size(); i++)
			{
				if (units.get(i).isSelected())
				{
					System.out.println("Before Path");
					int playerXTiles = (int)(units.get(i).getX()/tileSize);
					int playerYTiles = (int)(units.get(i).getY()/tileSize);
					int mouseXTiles = (int)((Mouse.getX() - camera.getX())/tileSize);
					int mouseYTiles = (int)((600 - Mouse.getY() - camera.getY())/tileSize);
					if ((mouseXTiles < thePTBMap.getWidthInTiles()) && (mouseYTiles < thePTBMap.getHeightInTiles()) && (mouseXTiles >= 0) && (mouseYTiles >= 0))
					{
						units.get(i).setPath(pathFinder.findPath(null, playerXTiles, playerYTiles, mouseXTiles, mouseYTiles));
					}
					units.get(i).setStep(0);
					units.get(i).setSelected(false);
					System.out.println("Unit " + i + " launched");				
				}
			}
		}
		
		
		for (int i = 0; i < units.size(); i++)
		{
			if (units.get(i).getPath() != null)
			{
				if ( (units.get(i).getStep() != units.get(i).getPath().getLength()))
				{
					// moving player to destination
					units.get(i).goToDest(units.get(i).getPath().getX(units.get(i).getStep())*tileSize, units.get(i).getPath().getY(units.get(i).getStep())*tileSize, delta, movingUp, movingDown, movingLeft, movingRight);
					
					//testing if player has arrived at destination
					if ((units.get(i).getX() == units.get(i).getPath().getX(units.get(i).getStep())*tileSize) && (units.get(i).getY() == units.get(i).getPath().getY(units.get(i).getStep())*tileSize))
					{
						units.get(i).incStep();
					}
				}
				else
				{
					System.out.println("unit " + i + " arrived");
					units.get(i).setPath(null);
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
		for (int i = 0; i < units.size(); i++)
		{
			units.get(i).getMovement().draw(units.get(i).getX() + camera.getX(), units.get(i).getY() + camera.getY());
		}
		
		// some indicators
		g.drawString("mouseX = " + Mouse.getX(), 600, 60);
		g.drawString("mouseY = " + (600 - Mouse.getY()), 600, 80);
		g.drawString("mouseTileX = " + (int)Mouse.getX()/tileSize, 600, 100);
		g.drawString("mouseTileY = " + (int)(600 - Mouse.getY())/tileSize, 600, 120);
		for (int i = 0; i < units.size(); i++)
		{
			if (units.get(i).getPath() != null) { g.drawString("u" + i + " Path length = " + units.get(i).getPath().getLength(), 600, 140 + i * 20);}
		}
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