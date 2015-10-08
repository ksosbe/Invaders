package com.example.cs252project;

import android.graphics.drawable.Drawable;


public class MonsterBullet extends GamePiece
{
	
	InvadersApplication global;
	double trajectoryX;
	double trajectoryY;
	int cycle;
	int iterator;
	int direction;
	
	public MonsterBullet(int posX, int posY, int width, int height, int trajectoryX, int trajectoryY, Drawable mDrawable) 
	{
		super(posX, posY, width, height, mDrawable);
		double len = Math.sqrt(trajectoryX * trajectoryX + trajectoryY * trajectoryY);
		this.trajectoryX = trajectoryX/len;
		this.trajectoryY = trajectoryY/len;
		cycle = (int)Math.ceil(this.trajectoryY/this.trajectoryX);
		iterator = 0;
		if(trajectoryX < 0){
			direction = -1;
		}
		else if(trajectoryX > 0){
			direction = 1;
		}
		else{
			direction = 0;
		}
		global = new InvadersApplication();
	}
	
}