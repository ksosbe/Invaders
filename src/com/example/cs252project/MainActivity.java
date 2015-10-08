package com.example.cs252project;

import java.util.Random;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.KeyEvent;

public class MainActivity extends Activity{
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//final InvadersApplication global = (InvadersApplication)getApplicationContext();
		ImageButton play = (ImageButton) findViewById(R.id.play);
		ImageButton options = (ImageButton) findViewById(R.id.options);
		ImageButton highscores = (ImageButton) findViewById(R.id.high_scores);
		starStorm();
		
		play.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
			{
        		Intent i = new Intent(MainActivity.this, playingBoard.class);
        		startActivity(i);
        		InvadersApplication.score = 0;
			}
		});
		
		options.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
			{
        		Intent i = new Intent(MainActivity.this, Options.class);
        		startActivity(i);
			}
		});
		
		highscores.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				Intent i = new Intent(MainActivity.this, Highscores.class);
				startActivity(i);				
			}
		});
	}
	
	private void starStorm(){
		Random rand = new Random();
		fall((ImageView)findViewById( R.id.star2 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star3 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star4 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star5 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star6 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star7 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star8 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star9 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star10 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star11 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star12 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star13 ), rand.nextInt(16)+1);
		fall((ImageView)findViewById( R.id.star14 ), rand.nextInt(16)+1);
	}
	
	private void fall(ImageView view, int dur){
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		int height = display.getHeight();
		Random rand = new Random();
		int originalPos[] = new int[2];
	    view.getLocationOnScreen( originalPos );
	    int xDest = originalPos[0];
	    int yDest = height;
	    TranslateAnimation anim = new TranslateAnimation( 0, xDest - originalPos[0] , 0, yDest - originalPos[1] );
	    anim.setFillAfter( true );
	    anim.setRepeatCount(Animation.INFINITE);
	    anim.setDuration(800 * dur);
	    anim.setRepeatMode(1);
	    anim.setInterpolator(new LinearInterpolator());
	    view.startAnimation(anim);
	}
	
}
