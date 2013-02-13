package org.topdowngame;

import org.newdawn.slick.Animation;

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

}
