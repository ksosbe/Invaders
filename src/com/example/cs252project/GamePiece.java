package com.example.cs252project;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class GamePiece {
	
	private int posX;
	private int posY;
	private int width;
	private int height;
	private int savedWidth;
	private int savedHeight;
	private Drawable mDrawable;
	
	public GamePiece(int posX, int posY, int width, int height, Drawable mDrawable){
		this.posX = posX;
		this.posY = posY;
		this.width = 0;
		this.height = 0;
		savedWidth = width;
		savedHeight = height;
		this.mDrawable = mDrawable;
		mDrawable.setBounds(posX, posY, posX + width, posY + height);
	}
	
	public void setPos(int posX, int posY){
		this.posX = posX;
		this.posY = posY;
		mDrawable.setBounds(posX, posY, posX + this.width, posY + this.height);
	}
	
	public void setPosX(int posX){
		this.posX = posX;
		mDrawable.setBounds(posX, posY, posX + this.width, posY + this.height);
	}
	
	public void setPosY(int posY){
		this.posY = posY;
		mDrawable.setBounds(posX, posY, posX + this.width, posY + this.height);
	}
	
	public Point getPos(){
		Point p = new Point(posX, posY);
		return p;
	}
	
	public int getPosX(){
		return posX;
	}
	
	public int getPosY(){
		return posY;
	}
	
	public void setSize(int width, int height){
		savedWidth = width;
		savedHeight = height;
		mDrawable.setBounds(posX, posY, posX + this.width, posY + this.height);
	}
	
	public void setWidth(int width){
		savedWidth = width;
		mDrawable.setBounds(posX, posY, posX + this.width, posY + this.height);
	}
	
	public void setHeight(int height){
		savedHeight = height;
		mDrawable.setBounds(posX, posY, posX + this.width, posY + this.height);
	}
	
	public Point getSize(){
		Point p = new Point(savedWidth, savedHeight);
		return p;
	}
	
	public int getWidth(){
		return savedWidth;
	}
	
	public int getHeight(){
		return savedHeight;
	}
	
	public void setImage(Drawable mDrawable){
		this.mDrawable = mDrawable;
	}
	
	public Drawable getImage(){
		return mDrawable;
	}
	
	public void setVisible(boolean visible){
		if(visible){
			height = savedHeight;
			width = savedWidth;
			mDrawable.setBounds(posX, posY, posX + width, posY + height);
		}
		else{
			height = 0;
			width = 0;
			mDrawable.setBounds(posX, posY, posX + width, posY + height);
		}
	}
}