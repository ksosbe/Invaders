package com.example.cs252project;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;


public class UserName extends Activity
{

	//InvadersApplication global;
	EditText input = null;
	ImageButton enter = null;
	MediaPlayer mp = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//global = (InvadersApplication)getApplicationContext();
		super.onCreate(savedInstanceState);
		
		if(InvadersApplication.sound){
			mp = MediaPlayer.create(UserName.this, R.raw.gameover);
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
		
		setContentView(R.layout.username);
		//global = (InvadersApplication)getApplicationContext();
		
		ImageButton enter = (ImageButton) findViewById(R.id.enter_score);
		ImageButton back = (ImageButton) findViewById(R.id.main_screen);
		input = (EditText) findViewById(R.id.input);
		
		enter.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
			{
        		if(mp != null){
        			try{
            			mp.stop();
            		}catch(Exception e){}
        		}
        		setScore();
			}
		});
		
		back.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
			{
        		if(mp != null){
        			try{
        			mp.stop();
        			}catch(Exception e){}
        		}
        		Intent i = new Intent(UserName.this, MainActivity.class);
        		startActivity(i);
			}
		});
	}
	
	private void setScore(){
		int score = (int)InvadersApplication.score;
		String uname = input.getText().toString().replace('|', 'i');
		
		if(uname.length() > 14){
			uname = uname.substring(0,13);
		}
		
		uname = alphaNumerize(uname);
		
		if(uname.length() > 0 && InvadersApplication.score > 0 && isOnline()){
			try {
				Highscores.insertScore(uname, score, getBaseContext());
			} catch (IOException e) {}
		}
		
		InvadersApplication.score = 0;
		Intent i = new Intent(UserName.this, MainActivity.class);
		startActivity(i);
	}

	private String alphaNumerize(String uname){
		StringBuilder sb = new StringBuilder();
		 sb.append(uname);
		for(int i = 0; i < uname.length(); i++){
			char c = uname.charAt(i);
			if(c < 48 || (c > 57 && c < 65) || (c > 90 && c < 97) || c > 122){
				sb.setCharAt(i, '_');
			}
		}
		String out = sb.toString();
		return out;
	}
	
	// Adapted from a helper function found on stackoverflow.com
		private boolean isOnline() {
			int newduration = Toast.LENGTH_LONG;
			final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
			if (activeNetwork != null && activeNetwork.isConnected()) {
				return true;
			} else {
				String text = "Network connection not available. Connect to the internet.";
				Toast toast = Toast.makeText(this, text, newduration);
				toast.show();
			    return false;
			} 
		}
	
}
