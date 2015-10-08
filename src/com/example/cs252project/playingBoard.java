package com.example.cs252project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class playingBoard extends Activity implements SensorEventListener
{
	/** Called when the activity is first created. */
	ShipView mShipView;
	public static int xAccel;
	static int shipWidth;
	static int shipHeight;
	int screenW;
	int screenH;
	//InvadersApplication global;
	PlayerShip ship;
	ArrayList<Bullet> shots;
	ArrayList<Alien> monsters;
	ArrayList<PowerUp> powers;
	ArrayList<MonsterBullet> enemyFire;
	int level = 1;
	int monsterSize;
	int redraw = 0;
	boolean activeGame = false; // Check for active game to compare if a level ended or a game ended
	Random rand;
	Toast toast;
	MediaPlayer mp;

	int bulletSpeed = 7;   //change bullet speed here!
	int monsterSpeed = 1;
	int monsterHP = 1;
	int powerSpeed = 3;
	double sineWave = 0;
	int startgame = 0;
	boolean boss1Direction = false;

	int alienPoints = 10;   

	private SensorManager sensorManager = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		//global = (InvadersApplication)getApplicationContext();
		shots = new ArrayList<Bullet>();
		monsters = new ArrayList<Alien>();
		powers = new ArrayList<PowerUp>();
		enemyFire = new ArrayList<MonsterBullet>();
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		screenH = getWindowManager().getDefaultDisplay().getHeight();
		screenW = getWindowManager().getDefaultDisplay().getWidth();
		shipWidth = screenW/9;
		shipHeight = screenH/11;
		mShipView = new ShipView(this);
		monsterSize = screenW/16;
		Context context = getApplicationContext();
		CharSequence text;
		int duration = Toast.LENGTH_SHORT;
		rand = new Random();

		spawnAliens(level);
		activeGame = true;

		mShipView.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				Resources res = getResources();
				if(true)
				{

					Drawable b = getResources().getDrawable(R.drawable.bullet);;
					if(ship.color.equals("blue")){
						b = res.getDrawable(R.drawable.blue_bullet);
					}
					else if(ship.color.equals("red")){
						b = res.getDrawable(R.drawable.red_bullet);
					}
					else if(ship.color.equals("green")){
						b = res.getDrawable(R.drawable.green_bullet);
					}
					else if(ship.color.equals("pink")){
						b = res.getDrawable(R.drawable.pink_bullet);
					}
					else if(ship.color.equals("yellow")){
						b = res.getDrawable(R.drawable.yellow_bullet);
					}

					if(ship.power == 1){
						Bullet bullet1 = new Bullet(ship.getPosX()+shipWidth, ship.getPosY(), 5, 10, b);
						Bullet bullet2 = new Bullet(ship.getPosX(), ship.getPosY(), 5, 10, b);
						bullet1.setVisible(true);
						bullet2.setVisible(true);
						shots.add(bullet1);
						shots.add(bullet2);
					}
					
					else{
						Bullet bullet = new Bullet(ship.getPosX()+(shipWidth/2)-5, ship.getPosY(), 5, 10, b);
						bullet.setVisible(true);
						shots.add(bullet);
					}
				}
				return false;
			}
		});

		setContentView(mShipView);
	}

	private void spawnGap(Resources res){
		int gapLoc = rand.nextInt(screenW/(monsterSize*2)-1);
		int scndGap = gapLoc+1;
		for(int j = 1; j < (3+level/3)+1 && j < 13; j++){
			for(int i = 0; i < screenW/(monsterSize*2); i++){
				if(i == gapLoc || i == scndGap){
					continue;
				}
				Alien a = new Alien(monsterSize*2*i, -monsterSize*2*j, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				a.setVisible(true);
				monsters.add(a);
			}
		}
	}

	private void spawnChevron(Resources res){
		for(int j = -8*monsterSize; j > 4*-8*monsterSize; j-=8*monsterSize){
			for(int i = 0; i < screenW/(monsterSize*2); i++){
				int yOffset;
				if(i >= screenW/(4*monsterSize)){
					yOffset = j+(monsterSize*2*(screenW/(monsterSize*2)-i-1));
				} else {
					yOffset = j+(monsterSize*2*i);
				}
				Alien a = new Alien(monsterSize*2*i, yOffset, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				a.setVisible(true);
				monsters.add(a);
			}
		}
	}

	private void spawnBlock(Resources res){
		for(int j = 1; j < (3+level/3)+1 && j < 13; j++){
			for(int i = 0; i < screenW/(monsterSize*2); i++){
				Alien a = new Alien(monsterSize*2*i, -monsterSize*2*j, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				a.setVisible(true);
				monsters.add(a);
			}
		}
	}

	private void spawnRanks(Resources res){
		for(int j = 1; j < (3+level/3)+1 && j < 13; j++){
			for(int i = 0; i < screenW/(monsterSize*2); i++){
				if(i > 0 && (i+1)%3 == 0){
					continue;
				}
				Alien a = new Alien(monsterSize*2*i, -monsterSize*2*j, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				a.setVisible(true);
				monsters.add(a);
			}
		}
	}

	private void spawnCircles(Resources res){
		boolean offset;
		for(int j = 0; j < 3; j++){
			offset = false;
			for(int i = 0; i < screenW/(monsterSize*2); i++){
				if(offset){
					Alien a = new Alien(monsterSize*2*i, -2*monsterSize-(j*8*monsterSize), monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
					Alien b = new Alien(monsterSize*2*i, -8*monsterSize-(j*8*monsterSize), monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));    			
					a.setVisible(true);
					b.setVisible(true);
					monsters.add(a);
					monsters.add(b);
					if(i+1 < screenW/(monsterSize*2) && i > 0){
						Alien c = new Alien(monsterSize*2*(i+1), -2*monsterSize-(j*8*monsterSize), monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
						Alien d = new Alien(monsterSize*2*(i+1), -8*monsterSize-(j*8*monsterSize), monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
						c.setVisible(true);
						d.setVisible(true);
						monsters.add(c);
						monsters.add(d);
						i++;
					}
					offset = false;
				} else {		
					Alien a = new Alien(monsterSize*2*i, -4*monsterSize-(j*8*monsterSize), monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
					Alien b = new Alien(monsterSize*2*i, -6*monsterSize-(j*8*monsterSize), monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
					a.setVisible(true);
					b.setVisible(true);
					monsters.add(a);
					monsters.add(b);
					if(i+1 < screenW/(monsterSize*2) && i > 0){
						Alien c = new Alien(monsterSize*2*(i+1), -4*monsterSize-(j*8*monsterSize), monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
						Alien d = new Alien(monsterSize*2*(i+1), -6*monsterSize-(j*8*monsterSize), monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
						c.setVisible(true);
						d.setVisible(true);
						monsters.add(c);
						monsters.add(d);
						i++;
					}
					offset = true;
				}
			}
		}
	}

	private void spawnCross(Resources res){
		for(int i = 0; i < screenW/(monsterSize*2); i++){
			if(i == 0 || i == 1 || i == 6 || i == 7){
				Alien a = new Alien(monsterSize*2*i, -2*monsterSize, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				Alien b = new Alien(monsterSize*2*i, -10*monsterSize, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				Alien c = new Alien(monsterSize*2*i, -16*monsterSize, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				Alien d = new Alien(monsterSize*2*i, -24*monsterSize, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				a.setVisible(true);
				b.setVisible(true);
				c.setVisible(true);
				d.setVisible(true);
				monsters.add(a);
				monsters.add(b);
				monsters.add(c);
				monsters.add(d);
			} else {
				if(i == 3 || i == 4){
					Alien e = new Alien(monsterSize*2*i, -6*monsterSize, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
					Alien f = new Alien(monsterSize*2*i, -20*monsterSize, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
					e.setVisible(true);
					f.setVisible(true);
					monsters.add(e);
					monsters.add(f);
				}
				Alien a = new Alien(monsterSize*2*i, -4*monsterSize, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				Alien b = new Alien(monsterSize*2*i, -8*monsterSize, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				Alien c = new Alien(monsterSize*2*i, -18*monsterSize, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				Alien d = new Alien(monsterSize*2*i, -22*monsterSize, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
				a.setVisible(true);
				b.setVisible(true);
				c.setVisible(true);
				d.setVisible(true);
				monsters.add(a);
				monsters.add(b);
				monsters.add(c);
				monsters.add(d);
			}
		}
	}

	private void spawnSpeeders(Resources res){
		Random rand = new Random();
		for(int i = 0; i < screenW/(monsterSize*2); i++){
			int r = rand.nextInt(screenH / monsterSize);
			Alien a = new Alien(monsterSize*2*i, -r*monsterSize, monsterSize, monsterSize, 3, monsterHP, res.getDrawable(R.drawable.alien3));
			a.setVisible(true);
			monsters.add(a);
		}
	}
		
	private void spawnSiners(Resources res){
		for(int i = 0; i < 3; i ++){
			for(int j = 0; j < screenW/(monsterSize*2); j++){
				Alien a = new Alien(monsterSize*2*j, -(i+1)*2*monsterSize, monsterSize, monsterSize, 2*((i%2)+1), monsterHP, res.getDrawable(R.drawable.alien2));
				a.setVisible(true);
				monsters.add(a);
			}
		}
	}
	
	private void spawnMix(Resources res){
		for(int i = 0; i < 5; i ++){
			if(i == 1 || i == 3){
				for(int j = 0; j < screenW/(monsterSize*2); j++){
					Alien a = new Alien(monsterSize*2*j, -(i+1)*2*monsterSize, monsterSize, monsterSize, (i+1), monsterHP, res.getDrawable(R.drawable.alien2));
					a.setVisible(true);
					monsters.add(a);
				}
			}
			else{
				for(int j = 0; j < screenW/(monsterSize*2); j++){
					Alien a = new Alien(monsterSize*2*j, -(i+1)*2*monsterSize, monsterSize, monsterSize, 1, monsterHP, res.getDrawable(R.drawable.alien1));
					a.setVisible(true);
					monsters.add(a);
				}
			}
		}
	}
	
	private void spawnBoss1(Resources res){
		int bossSize = screenH/3;
		Alien a = new Alien(screenW/2 - bossSize/2, -bossSize, bossSize, bossSize, 5, level*15,  res.getDrawable(R.drawable.boss1));
		a.setVisible(true);
		monsters.add(a);
		if(InvadersApplication.sound)
		{
			mp = MediaPlayer.create(playingBoard.this, R.raw.boss);
			mp.setVolume(.1f, .1f);
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
	}
	
	//Spawns aliens for each level
	private void spawnAliens(int level)
	{
		Resources res = getResources();
		
		if(InvadersApplication.demo){
			if(level <= 1){
				spawnBlock(res);
				return;
			}
			else if(level == 2){
				spawnSpeeders(res);
				return;
			}
			else if(level == 3){
				spawnSiners(res);
				return;
			}
			else if(level == 4){
				spawnMix(res);
				return;
			}
			else if(level == 5){
				spawnBoss1(res);
				return;
			}
			else if(level <= 6){
				mShipView.gameOver();
				return;
			}
		}
		
		int formationChoice;
		/////// Following block will slowly present new formations to the player. Currently it just randomly picks one of the 6 available
		if(level < 3){
			formationChoice = rand.nextInt(4);
		} else if(level < 5){
			formationChoice = rand.nextInt(4) + 3;
		} else {
			formationChoice = rand.nextInt(4) + 5;
		}
		
		if(level % 5 == 0){
			spawnBoss1(res);
			return;
		}
		
		switch(formationChoice){
		case 0:
			spawnGap(res);
			break;
		case 1:
			spawnRanks(res);
			break;
		case 2:
			spawnBlock(res);
			break;
		case 3:
			spawnChevron(res);
			break;
		case 4:
			spawnCircles(res);
			break;
		case 5:
			spawnCross(res);
			break;
		case 6:
			spawnSpeeders(res);
			break;
		case 7:
			spawnSiners(res);
			break;
		case 8:
			spawnMix(res);
			break;
		default:
			spawnBlock(res);
		}
	}


	// This method will update the UI on new sensor events
	public void onSensorChanged(SensorEvent sensorEvent)
	{
		{
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				xAccel = (int) sensorEvent.values[0];
			}

			if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {

			}
		}
	}

	public void onAccuracyChanged(Sensor arg0, int arg1)
	{
		//DO NOT CHANGE/ADD
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// Register this class as a listener for the accelerometer sensor
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		// ...and the orientation sensor
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop()
	{
		sensorManager.unregisterListener(this);
		super.onStop();
	}

	public class ShipView extends View
	{
		public ShipView(Context context)
		{
			super(context);


			Resources res = getResources();

			if(InvadersApplication.color.equals("Blue")){
				ship = new PlayerShip((screenW - shipWidth)/2, screenH - shipHeight, shipWidth, shipHeight, 2,"blue",res.getDrawable(R.drawable.blueship));
			}
			if(InvadersApplication.color.equals("Red")){
				ship = new PlayerShip((screenW - shipWidth)/2, screenH - shipHeight, shipWidth, shipHeight, 2,"red",res.getDrawable(R.drawable.redship));
			}
			if(InvadersApplication.color.equals("Green")){
				ship = new PlayerShip((screenW - shipWidth)/2, screenH - shipHeight, shipWidth, shipHeight, 2,"green",res.getDrawable(R.drawable.greenship));
			}
			if(InvadersApplication.color.equals("Pink")){
				ship = new PlayerShip((screenW - shipWidth)/2, screenH - shipHeight, shipWidth, shipHeight, 2,"pink",res.getDrawable(R.drawable.pinkship));
			}
			if(InvadersApplication.color.equals("Yellow")){
				ship = new PlayerShip((screenW - shipWidth)/2, screenH - shipHeight, shipWidth, shipHeight, 2,"yellow", res.getDrawable(R.drawable.yellowship));
			}

			ship.setVisible(true);

		}

		//Return ship to unpowered state
		private void powerDown(){
			Resources res = getResources();
			if(ship.power == 0);
			else{
				ship.power = 0;
				if(InvadersApplication.color.equals("Blue")){
					ship.setImage(res.getDrawable(R.drawable.blueship));
				}
				else if(InvadersApplication.color.equals("Red")){
					ship.setImage(res.getDrawable(R.drawable.redship));
				}
				else if(InvadersApplication.color.equals("Green")){
					ship.setImage(res.getDrawable(R.drawable.greenship));
				}
				else if(InvadersApplication.color.equals("Pink")){
					ship.setImage(res.getDrawable(R.drawable.pinkship));
				}
				else if(InvadersApplication.color.equals("Yellow")){
					ship.setImage(res.getDrawable(R.drawable.yellowship));
				}
			}
			redraw = 1;
		}

		//Start the level over
		private void levelRestart(){
			while(!shots.isEmpty()){
				shots.remove(0);
			}
			while(!monsters.isEmpty()){
				monsters.remove(0);
			}
			while(!powers.isEmpty()){
				powers.remove(0);
			}
			while(!enemyFire.isEmpty()){
				enemyFire.remove(0);
			}
			if(!InvadersApplication.demo){
				powerDown();
			}
			spawnAliens(level);
		}

		//Game Over. Removes all GamePieces besides ship,
		//Resets HUD vals
		private void gameOver(){
			
			while(!shots.isEmpty()){
				shots.remove(0);
			}
			while(!monsters.isEmpty()){
				monsters.remove(0);
			}
			while(!powers.isEmpty()){
				powers.remove(0);
			}
			
			int tempScore = (int)InvadersApplication.score;
			activeGame = false;
			
			if(InvadersApplication.demo){
				Intent i = new Intent(playingBoard.this, DemoOver.class);
				startActivity(i);
			}
			
			else if(InvadersApplication.score > 0 && isOnline()){
				Intent i = new Intent(playingBoard.this, UserName.class);
				startActivity(i);
			}
			
			else{
				Intent i = new Intent(playingBoard.this, MainActivity.class);
				startActivity(i);
			}
		}

		private boolean isOnline() {
			int newduration = Toast.LENGTH_SHORT;
			final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
			if (activeNetwork != null && activeNetwork.isConnected()) {
				return true;
			} else {
				String text = "Network connection not available. Unable to post score.";
				toast = Toast.makeText(getContext(), text, newduration);
				toast.show();
			    return false;
			} 
		}
		
		protected void onDraw(Canvas canvas)
		{
			//redraw necessary after calling ship.setImage
			
			if(redraw != 0){
				ship.setPosX(ship.getPosX());
				redraw = 0;
			}

			sineWave += .1;
			if(sineWave >= 2*Math.PI){
				sineWave = 0;
			}
			
			//Retrieve resources
			Resources res = getResources();

			//Draw ship
			if(xAccel < 0 && ship.getPosX() < (screenW - shipWidth) && (ship.getPosX() - xAccel < (screenW - shipWidth))){
				ship.setPosX(ship.getPosX() - 2*(int)xAccel);
			}
			else if(xAccel > 0 && ship.getPosX() > 0 && (ship.getPosX() - xAccel > 0)){
				ship.setPosX(ship.getPosX() - 2*(int)xAccel);
			}
			
			if(startgame == 0)
			{
				if(InvadersApplication.sound)
				{
					mp = MediaPlayer.create(playingBoard.this, R.raw.opener);
					mp.setVolume(.5f, .5f);
					mp.start();
					mp.setOnCompletionListener(new OnCompletionListener() 
			        {
			            @Override
			            public void onCompletion(MediaPlayer mp) 
			            {
			                mp.release();
			            }
	
			        });
				}
				startgame = 1;
			}
			
			ship.getImage().draw(canvas);

			// Check for end of level, spawn new aliens and increment level if reached
			if(monsters.isEmpty() && activeGame){
				level++;
				if(InvadersApplication.demo){
					String text = null;
					ship.lives = 2;
					
					if(level == 3){
						ship.power = 3;
						ship.setImage(res.getDrawable(R.drawable.armor_ship));
						redraw = 1;
						text = "Armor Acquired";
					}
					else if(level == 4){
						ship.power = 2;
						ship.setImage(res.getDrawable(R.drawable.penetrating_ship));
						redraw = 1;
						text = "Powershot Acquired";
					}
					else if(level == 5){
						ship.power = 1;
						ship.setImage(res.getDrawable(R.drawable.spreadshot_ship));
						redraw = 1;
						text = "Wideshot Acquired";
					}
					if(InvadersApplication.sound && (level == 3 || level == 4 || level == 5)){
						mp = MediaPlayer.create(playingBoard.this, R.raw.power);
						mp.setVolume(.3f, .3f);
						mp.setVolume(.3f, .3f);
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
					
					else if(InvadersApplication.sound){
						mp = MediaPlayer.create(playingBoard.this, R.raw.level);
						mp.setVolume(.3f, .3f);
						mp.setVolume(.3f, .3f);
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
					
					if(text != null){
						toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
						toast.show();
					}
				}
				else if(InvadersApplication.sound && level%5 != 0)
				{
					mp = MediaPlayer.create(playingBoard.this, R.raw.level);
					mp.setVolume(.3f,.3f);
					mp.start();
					mp.setOnCompletionListener(new OnCompletionListener() 
			        {
			            @Override
			            public void onCompletion(MediaPlayer mp) 
			            {
			                mp.release();
			            }

			        });
				}
				alienPoints += 10;
				if(level == 7){
					monsterSpeed = 2;
				}
				if(level == 14){
					monsterHP = 2;
				}
				if(level == 21){
					monsterHP = 3;
				}
				spawnAliens(level);
			}

			//Monster loop. Detects ship collision
			if(!monsters.isEmpty()){
				for(int i = 0; i < monsters.size(); i++)
				{
					Alien a = monsters.get(i);
					int c1X = a.getPosX();
					int midX = c1X + (monsterSize/2);
					int c2X = midX + (monsterSize/2);
					if(a.getPosY() > screenH){
						monsters.get(i).setPosY(-monsterSize);
						continue;
					}
					if(a.getPosY() + monsterSize < screenH - shipHeight){}
					else if((midX >= ship.getPosX() && midX <= ship.getPosX()+shipWidth)
							|| (c1X >= ship.getPosX() && c1X <= ship.getPosX()+shipWidth)
							|| (c2X >= ship.getPosX() && c2X <= ship.getPosX()+shipWidth)){
						if(ship.power == 3){
							powerDown();
							monsters.remove(i); i--;
						}
						else{
							if(ship.lives > 0){
								ship.lives--;
								
								if(InvadersApplication.sound)
								{
									mp = MediaPlayer.create(playingBoard.this, R.raw.boom);
									mp.setVolume(.15f, .15f);
									mp.start();
									mp.setOnCompletionListener(new OnCompletionListener() 
							        {
							            @Override
							            public void onCompletion(MediaPlayer mp) 
							            {
							                mp.release();
							            }

							        });
								}
								
								levelRestart();
								break;
							}
							else if(!InvadersApplication.demo){
								gameOver();
							}
							else{
								ship.lives = 2;
								levelRestart();
							}
						}
					}
					if(a.type == 2){
						a.setPosY(a.getPosY() + (int)(Math.ceil(monsterSpeed*.75)));
						a.setPosX(a.getPosX() + (int)(3.5 * Math.sin(sineWave)));
					}
					else if(a.type == 4){
						a.setPosY(a.getPosY() + (int)(Math.ceil(monsterSpeed*.75)));
						a.setPosX(a.getPosX() - (int)(3.5 * Math.sin(sineWave)));
					}
					else if(a.type == 3){
						a.setPosY(a.getPosY() + (int)(Math.ceil(monsterSpeed*1.5)));
					}
					else if(a.type == 5){
						if(a.getPosY() < 25){
							a.setPosY(a.getPosY() + 1);
						}
						else{
							if(boss1Direction){
								a.setPosX(a.getPosX() + 2);
							}
							else{
								a.setPosX(a.getPosX() - 2);
							}
							if(a.getPosX()+a.getWidth() >= screenW - 4){
								boss1Direction = false;
							}
							if(a.getPosX() <= 4){
								boss1Direction = true;
							}
							
							if(Math.sin(sineWave)  == 0){
								MonsterBullet b = new MonsterBullet(
										a.getPosX() + a.getWidth()/2, a.getPosY() + a.getHeight()/2, 6, 6,
										ship.getPosX()-a.getPosX(), ship.getPosY() - a.getPosY(),
										res.getDrawable(R.drawable.enemy_shot));
								b.setVisible(true);
								enemyFire.add(b);
							}
							
						}
					}
					else{
						a.setPosY(a.getPosY() + monsterSpeed);
					}
					
					if(a.getPosX() <= 0 - monsterSize){
						monsters.remove(i); i--;
					}
					if(a.getPosX() >= screenW){
						monsters.remove(i); i--;
					}
					
					a.getImage().draw(canvas);
				}
			}

			//Bullet loop
			if(!shots.isEmpty())
			{
				for(int i = 0; i < shots.size(); i++)
				{
					Bullet loopbullet = shots.get(i);
					if (loopbullet.getPosY() < 20)
					{
						shots.remove(i);
						i--; continue;
					}
					loopbullet.setPosY(loopbullet.getPosY()-bulletSpeed);
					loopbullet.getImage().draw(canvas);
					if(!monsters.isEmpty()){
						for(int j = 0; j < monsters.size(); j++){
							Point b = loopbullet.getPos();
							Point a = monsters.get(j).getPos();
							if((b.x >= a.x && b.x <= (a.x + monsters.get(j).getWidth())) && (b.y >= a.y && b.y <= (a.y + monsters.get(j).getHeight()))){
								if(monsters.get(j).health <= 1 || (monsters.get(j).health <= 2 && ship.power == 2)){
									monsters.get(j).setVisible(false);
									int r = rand.nextInt(35)+1;
									if(InvadersApplication.demo){ r = 0;}
									if(r == 1){
										PowerUp p = new PowerUp(monsters.get(j).getPosX(), monsters.get(j).getPosY(),
												monsterSize, monsterSize, 1, res.getDrawable(R.drawable.spreadshot));
										p.setVisible(true);
										powers.add(p);
									}
									else if(r == 2){
										PowerUp p = new PowerUp(monsters.get(j).getPosX(), monsters.get(j).getPosY(),
												monsterSize, monsterSize, 2, res.getDrawable(R.drawable.penetrating));
										p.setVisible(true);
										powers.add(p);
									}
									else if(r == 3){
										PowerUp p = new PowerUp(monsters.get(j).getPosX(), monsters.get(j).getPosY(),
												monsterSize, monsterSize, 3, res.getDrawable(R.drawable.armor));
										p.setVisible(true);
										powers.add(p);
									}
									else if(r == 4){
										PowerUp p = new PowerUp(monsters.get(j).getPosX(), monsters.get(j).getPosY(),
												monsterSize, monsterSize, 4, res.getDrawable(R.drawable.blueship));
										p.setVisible(true);
										powers.add(p);
									}

									if(monsters.get(j).type == 5 && InvadersApplication.sound){
										mp = MediaPlayer.create(playingBoard.this, R.raw.smash);
										mp.setVolume(.1f, .1f);
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
									
									if(monsters.get(j).type == 5){
										while(!enemyFire.isEmpty()){ enemyFire.remove(0); }
									}
									
									monsters.remove(j);
									j--;

									InvadersApplication.score += alienPoints;
								}
								else{
									monsters.get(j).healthDown();
									if(ship.power == 2){
										monsters.get(j).healthDown();
									}
								}

								shots.remove(i); i--;

							}
						}
					}
				}
			}

			//Update positions of powerups
			if(!powers.isEmpty()){
				for(int i = 0; i < powers.size(); i++){
					powers.get(i).setPosY(powers.get(i).getPosY() + powerSpeed);
					powers.get(i).getImage().draw(canvas);
					Point s = ship.getPos();
					Point p = powers.get(i).getPos();
					if(p.x+10 >= s.x && p.x+10 <= s.x + shipWidth && p.y+10 >= s.y && p.y+10 <= s.y + shipHeight){
						if(powers.get(i).type == 1){
							ship.setImage(res.getDrawable(R.drawable.spreadshot_ship));
						}
						else if(powers.get(i).type == 2){
							ship.setImage(res.getDrawable(R.drawable.penetrating_ship));
						}
						else if(powers.get(i).type == 3){
							ship.setImage(res.getDrawable(R.drawable.armor_ship));
						}
						else if(powers.get(i).type == 4){
							ship.lives++;
						}
						if(powers.get(i).type != 4){
							ship.power = powers.get(i).type;
						}
						powers.remove(i); i--;
						redraw = 1;
						if(InvadersApplication.sound){
							mp = MediaPlayer.create(playingBoard.this, R.raw.power);
							mp.setVolume(.3f, .3f);
							mp.setVolume(.3f, .3f);
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
						continue;
					}
					if(powers.get(i).getPosY() > screenH){
						powers.remove(i); i--;
					}
				}
			}
			
			//Enemy fire loop
			if(!enemyFire.isEmpty()){
				
				for(int i = 0; i < enemyFire.size(); i++){
					MonsterBullet b = enemyFire.get(i);
					if(enemyFire.get(i).getPosY() > screenH){
						enemyFire.remove(i); i --;
						continue;
					}
					if(b.getPosY() >= screenH - shipHeight){
						if(b.getPosX() >= ship.getPosX() && b.getPosX() <= ship.getPosX()+shipWidth){
							if(ship.power == 3){
								powerDown();
								enemyFire.remove(i); i--;
							}
							else{
								if(ship.lives > 0){
									enemyFire.remove(i); i --;
									ship.lives--;
									powerDown();
									if(InvadersApplication.sound)
									{
										mp = MediaPlayer.create(playingBoard.this, R.raw.boom);
										mp.setVolume(.15f, .15f);
										mp.start();
										mp.setOnCompletionListener(new OnCompletionListener() 
								        {
								            @Override
								            public void onCompletion(MediaPlayer mp) 
								            {
								                mp.release();
								            }

								        });
									}
									
									break;
								}
								else{
									enemyFire.remove(i); i --;
									if(!InvadersApplication.demo){
										gameOver();
									}
									else{
										ship.lives = 2;
										levelRestart();
									}
								}
							}
						}
					}
					b.setPosY(b.getPosY() + (level/5));
					if(b.iterator % b.cycle == 0){
						b.setPosX(b.getPosX() + ((level/5)*b.direction));
					}
					b.iterator++;
					b.getImage().draw(canvas);
				}
			}

			//Paint the gray HUD rectangle
			Paint paint = new Paint();
			Rect rt = new Rect(0, 0, screenW, 20);
			paint.setColor(Color.GRAY);
			paint.setStyle(Style.FILL);
			canvas.drawRect(rt,paint);

			//Paint the HUD text
			paint.setColor(Color.WHITE); 
			paint.setTextSize(16); 
			canvas.drawText("Score: "+ InvadersApplication.score, 10, 15, paint);
			canvas.drawText("Level "+level, (screenW/2) - 30, 15, paint);
			canvas.drawText("Lives:  " + ship.lives, screenW - 85, 15, paint);

			invalidate();
		}
	}
}