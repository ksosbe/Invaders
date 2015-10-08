package com.example.cs252project;
import android.graphics.drawable.Drawable;


public class PowerUp extends GamePiece
{
	InvadersApplication global;
	int type;
	
	public PowerUp(int posX, int posY, int width, int height, int type, Drawable mDrawable) 
	{
		super(posX, posY, width, height, mDrawable);
		this.type = type;
		global = new InvadersApplication();
	}
	
}