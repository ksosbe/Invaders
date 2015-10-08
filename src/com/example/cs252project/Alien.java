package com.example.cs252project;
import android.graphics.drawable.Drawable;


public class Alien extends GamePiece
{
	InvadersApplication global;
	int health;
	int type;

	public Alien(int posX, int posY, int width, int height, int type, int health, Drawable mDrawable) 
	{
		super(posX, posY, width, height, mDrawable);
		this.health = health;
		this.type = type;
		global = new InvadersApplication();
	}
	
	public void healthDown(){
		health--;
	}
	
}