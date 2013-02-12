package org.topdowngame;

import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class PropertyTileBasedMap implements TileBasedMap {
	
	private TiledMap tmap;
	
	public PropertyTileBasedMap(TiledMap tileMap)
	{	
		this.tmap = tileMap;	
	}

	@Override
	public boolean blocked(PathFindingContext pfc, int x, int y) 
	{
		if (tmap.getTileProperty(tmap.getTileId(x, y, 0), "blocked", "false").equals("true"))
		{
			return true;
		}
		else return false;
		
	}

	@Override
	public float getCost(PathFindingContext pfc, int x, int y) 
	{
		return 1.0f;
	}

	@Override
	public int getHeightInTiles() 
	{
		return tmap.getHeight();
	}

	@Override
	public int getWidthInTiles() 
	{
		return tmap.getWidth();
	}

	@Override
	public void pathFinderVisited(int arg0, int arg1) {	}

}
