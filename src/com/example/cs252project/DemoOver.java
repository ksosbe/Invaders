package com.example.cs252project;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class DemoOver extends Activity{

	MediaPlayer mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_over);
		
		ImageButton back = (ImageButton) findViewById(R.id.demoOverBack);
		
		if(InvadersApplication.sound){
			mp = MediaPlayer.create(DemoOver.this, R.raw.gameover);
			mp.setVolume(.34f, .34f);
			mp.setOnCompletionListener(new OnCompletionListener() 
			{
				@Override
				public void onCompletion(MediaPlayer mp) 
				{
					mp.release();
				}

			});
			mp.start();
		}
		
		back.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
			{
        		if(mp != null){
        			try{
            			mp.stop();
            		}catch(Exception e){}
        		}
        		Intent i = new Intent(DemoOver.this, MainActivity.class);
        		startActivity(i);
			}
		});
	}
	
}
