package com.example.cs252project;

import android.graphics.drawable.Drawable;


public class PlayerShip extends GamePiece
{
	InvadersApplication global;
	String color;
	int power;
	int lives;
	
	public PlayerShip(int posX, int posY, int width, int height, int lives, String color, Drawable mDrawable) 
	{
		super(posX, posY, width, height, mDrawable);
		this.color = color;
		global = new InvadersApplication();
		power = 0;
		this.lives = lives;
	}
	
}