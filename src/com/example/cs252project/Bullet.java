package com.example.cs252project;

import android.graphics.drawable.Drawable;


public class Bullet extends GamePiece
{
	int shipX;
	InvadersApplication global;
	
	public Bullet(int posX, int posY, int width, int height, Drawable mDrawable) 
	{
		super(posX, posY, width, height, mDrawable);
		global = new InvadersApplication();
	}
	
}
