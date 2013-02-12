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

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Animation getMovement() {
		return movement;
	}

	public void setMovement(Animation movement) {
		this.movement = movement;
	}

}
