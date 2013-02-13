package org.topdowngame;

import org.newdawn.slick.Animation;
import org.newdawn.slick.util.pathfinding.Path;

public class Player
{
	private float x, y;
	private Animation movement;
	
	public Player(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void incX(float i) {
		this.x += i;
	}
	
	public void decX(float i) {
		this.x -= i;
	}
	
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void incY(float i) {
		this.y += i;
	}
	
	public void decY(float i) {
		this.y -= i;
	}
	
	public Animation getMovement() {
		return movement;
	}

	public void setMovement(Animation movement) {
		this.movement = movement;
	}
	
	public void goToDest (float toX, float toY, int delta, Animation movingUp, Animation movingDown, Animation movingLeft, Animation movingRight)
	{
		// clamping player to stepdestination when close enough 
		if ((this.getX() < toX) && (this.getX() + 1 > toX))
		{
			this.setX(toX);
		}
		else if ((this.getX() > toX) && (this.getX() - 1 < toX))
		{
			this.setX(toX);
		}
		else if ((this.getY() < toY) && (this.getY() + 1 > toY))
		{
			this.setY(toY);
		}
		else if ((this.getY() > toY) && (this.getY() - 1 < toY))
		{
			this.setY(toY);
		}
		
		if (this.getY() > toY)
		{
			this.decY(delta * 0.1f);
			this.setMovement(movingUp);
		}
		else if (this.getY() < toY)
		{
			this.incY(delta * 0.1f);
			this.setMovement(movingDown);
		}
		else if (this.getX() > toX)
		{
			this.decX(delta * 0.1f);
			this.setMovement(movingLeft);
		}
		else if (this.getX() < toX)
		{
			this.incX(delta * 0.1f);
			this.setMovement(movingRight);
		}
		
	}
	
	public void setDest(float toX, float toY, int delta, Animation movingUp, Animation movingDown, Animation movingLeft, Animation movingRight, Path path, int playerStep)
	{
		if (path != null)
		{
			if ( (playerStep != path.getLength()))
			{
				// moving player to destination
				this.goToDest(toX, toY, delta, movingUp, movingDown, movingLeft, movingRight);
				
				//testing if player has arrived at destination
				if ((this.getX() == toX) && (this.getY() == toY))
				{
					playerStep++;
				}
			}
		}
		
	}

}
