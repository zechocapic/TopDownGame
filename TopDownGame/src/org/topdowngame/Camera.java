package org.topdowngame;

import org.newdawn.slick.Input;

public class Camera 
{
	private float x;
	private float y;

	public Camera(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void incX(float f) {
		this.x += f;
	}

	public void decX(float f) {
		this.x -= f;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void incY(float f) {
		this.y += f;
	}
	
	public void decY(float f) {
		this.y -= f;
	}
	
	public void keyboardMove (Input input, int delta)
	{
		if (input.isKeyDown(Input.KEY_UP))
		{
			this.incY(delta * 0.1f);
		}
		if (input.isKeyDown(Input.KEY_DOWN))
		{
			this.decY(delta * 0.1f);
		}
		if (input.isKeyDown(Input.KEY_LEFT))
		{
			this.incX(delta * 0.1f);
		}
		if (input.isKeyDown(Input.KEY_RIGHT))
		{
			this.decX(delta * 0.1f);
		}
		
	}

}
