package com.example.cs252project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

// This class implements a Socket that communicates with a server (modified from the given example in the handout)
// to retrieve data from the MySQL database located on /homes/ksosbe for the highscores table
// To work, the server AND SQL database must both be running already on sslab08

// This class was based on a Socket example found on the Oracle website at the following link
// http://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html


public class Highscores extends Activity 
{
	private TextView scoreList = null;
	InvadersApplication global;
	Toast toast;
	CharSequence text;
	int duration;
	Context context;
	
	Socket serverSocket = null; // Socket to communicate to database/server
	PrintWriter clientOut = null; // This writes to the server
	BufferedReader serverOut = null; // This reads the server's response
	int maxScores = 10; // Maximum number of scores to read/store
	
	// Adapted from a helper function found on stackoverflow.com
	private boolean isOnline() {
		int newduration = Toast.LENGTH_LONG;
		final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		} else {
			text = "Network connection not available. Connect to the internet.";
			toast = Toast.makeText(context, text, newduration);
			toast.show();
		    return false;
		} 
	}
	
	// CALL THIS TO SET A NEW HIGHSCORE
	// USERNAME SHOULD NOT CONTAIN A | SYMBOL
	public static void insertScore(String username, int score, Context context) throws IOException{
		
		int duration = Toast.LENGTH_SHORT;
		
		Socket serverSocket = null;
		PrintWriter clientOut = null;
		BufferedReader serverOut = null;
		
		try {
			serverSocket = new Socket("sslab08.cs.purdue.edu", 51993); // Connect to sslab08, port 51993, this is the server's port
			clientOut = new PrintWriter(serverSocket.getOutputStream(), true); // Establish connection to the server for sending requests
			serverOut = new BufferedReader(new InputStreamReader(serverSocket.getInputStream())); // Establish connection to the server for receiving data
		} catch(UnknownHostException e){
			String text = "Could not connect to host";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} catch(IOException e){
			String text = "IO connection failed";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		
		clientOut.println("FIND SCORE " + score);
		String response = serverOut.readLine();
		int scoresCount = Integer.parseInt(response);
		String scoreResults[] = new String[scoresCount];
		String result;
		int i = 0;
		while((result = serverOut.readLine()) != null){
			scoreResults[i] = result;
			i++;
		}
		int newrank;
		if(scoresCount != 0){
			//Arrays.sort(scoreResults);
			sortTable(scoreResults, scoresCount);
			String subresponse[] = scoreResults[0].split("\\|");
			newrank = Integer.parseInt(subresponse[0]);
		} else {
			newrank = findCount(context);
		}
		callNew(newrank, username, score, clientOut, context);
		
		serverSocket.close();
		clientOut.close();
		serverOut.close();
	}
	
	// Called by insertScore if the new entry is the lowest score
	private static int findCount(Context context) throws IOException{
		
		int duration = Toast.LENGTH_SHORT;
		
		Socket serverSocket = null;
		PrintWriter clientOut = null;
		BufferedReader serverOut = null;
		
		try {
			serverSocket = new Socket("sslab08.cs.purdue.edu", 51993); // Connect to sslab08, port 51993, this is the server's port
			clientOut = new PrintWriter(serverSocket.getOutputStream(), true); // Establish connection to the server for sending requests
			serverOut = new BufferedReader(new InputStreamReader(serverSocket.getInputStream())); // Establish connection to the server for receiving data
		} catch(UnknownHostException e){
			String text = "Could not connect to host";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} catch(IOException e){
			String text = "IO connection failed";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		
		clientOut.println("FIND SCORE -1");
		String response = serverOut.readLine();
		
		serverSocket.close();
		clientOut.close();
		serverOut.close();
		return Integer.parseInt(response) + 1;
	}
	
	// insertScore calls this function, otherwise the server doesn't get a new request
	private static void callNew(int newrank, String username, int score, PrintWriter clientOut, Context context) throws IOException{

		int duration = Toast.LENGTH_SHORT;
		Socket serverSocket = null;
		BufferedReader serverOut = null;
		
		try {
			serverSocket = new Socket("sslab08.cs.purdue.edu", 51993); // Connect to sslab08, port 51993, this is the server's port
			clientOut = new PrintWriter(serverSocket.getOutputStream(), true); // Establish connection to the server for sending requests
			serverOut = new BufferedReader(new InputStreamReader(serverSocket.getInputStream())); // Establish connection to the server for receiving data
		} catch(UnknownHostException e){
			String text = "Could not connect to host";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} catch(IOException e){
			String text = "IO connection failed";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		clientOut.println("NEWSCORE " + newrank + " " + username + " " + score + " current_date " + "current_time");
		
		serverSocket.close();
		clientOut.close();
		serverOut.close();
	}
	
	private static void sortTable(String scoresTable[], int size){
		for(int i = 0; i < size; i++){
			int min = i;
			for(int j = i; j < size; j++){
				if(compare(scoresTable, min, j) == 1){
					min = j;
				}
			}
			swap(scoresTable, i, min);
		}
	}
	
	private static int compare(String scoresTable[], int a, int b){
			
		String scoreLine[] = scoresTable[a].split("\\|");
		
		int val1 = Integer.parseInt(scoresTable[a].split("\\|")[0]);
		int val2 = Integer.parseInt(scoresTable[b].split("\\|")[0]);
		
		
		if(val1 < val2){
			return -1;
		}
		else if(val1 > val2){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	private static void swap(String scoresTable[], int a, int b){
		String temp = scoresTable[a];
		scoresTable[a] = scoresTable[b];
		scoresTable[b] = temp;
	}
	
	private static void sortTable(String scoresTable[][], int size){
		for(int i = 0; i < size; i++){
			int min = i;
			for(int j = i; j < size; j++){
				if(compare(scoresTable, min, j) == 1){
					min = j;
				}
			}
			swap(scoresTable, i, min);
		}
	}
	
	private static int compare(String scoresTable[][], int a, int b){
		int val1 = Integer.parseInt(scoresTable[a][0]);
		int val2 = Integer.parseInt(scoresTable[b][0]);
		if(val1 < val2){
			return -1;
		}
		else if(val1 > val2){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	private static void swap(String scoresTable[][], int a, int b){
		String[] temp = scoresTable[a];
		scoresTable[a] = scoresTable[b];
		scoresTable[b] = temp;
	}
	
	// onCreate is used for populating the highscores table
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscores);
		RelativeLayout rView = (RelativeLayout)findViewById(R.id.relativeLayout1);
		rView.setGravity(0x07);
		
		scoreList = new TextView(this);
		scoreList.setTextColor(Color.parseColor("#F1EA62"));
		scoreList.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/eternal.ttf"));
		scoreList.setText("\t");
	    rView.addView(scoreList);
		
		global = (InvadersApplication)getApplicationContext();
		ImageButton back = (ImageButton) findViewById(R.id.back);
        context = getApplicationContext();
		duration = Toast.LENGTH_SHORT;
		
		back.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
			{
        		finish();
			}
		});
		
		if(!isOnline()){
			Highscores.this.finish();
			return;
		}
		
		try {
			serverSocket = new Socket("sslab08.cs.purdue.edu", 51993); // Connect to sslab08, port 51993, this is the server's port
			clientOut = new PrintWriter(serverSocket.getOutputStream(), true); // Establish connection to the server for sending requests
			serverOut = new BufferedReader(new InputStreamReader(serverSocket.getInputStream())); // Establish connection to the server for receiving data
		} catch(UnknownHostException e){
			text = "Could not connect to host";
			toast = Toast.makeText(context, text, duration);
			toast.show();
			finish();
			return;
		} catch(IOException e){
			text = "IO connection failed";
			toast = Toast.makeText(context, text, duration);
			toast.show();
			finish();
			return;
		}
		
		String response = null;
		String topTen[] = new String[maxScores];
		int count = 0;
		clientOut.println("GETSCORES " + maxScores);
		// Gather the top 0-maxScores high scores in the format rank|username|score|YYYY:MM:DD|HH:MM:SS
		for(int i = 0; i < maxScores; i++){
			try {
				response = serverOut.readLine();
			} catch (IOException e) {
				response = null;
			}
			if(response == null){
				break;
			} else {
			    topTen[count] = response;
			    count++;
			}
		}

		if(count == maxScores + 1){
			count = maxScores;
		}

		if(count > 0){

			// Split each entry at the | character into a 2D array of Strings
			String scoresTable[][] = new String[count][9];
			for(int i = 0; i < count; i++){
				scoresTable[i] = topTen[i].split(" | ");
			}

			sortTable(scoresTable, count);

			// Toasts all of the "rank user score" sets in scoresTable for testing/debugging purposes
			/*
		for(int i = 0; i < count; i++){
		    text = scoresTable[i][0] + " " + scoresTable[i][2] + " " + scoresTable[i][4];
		    toast = Toast.makeText(context, text, duration);
		    toast.show();
		}
			 */

			/*///////////////scoresTable contains the data from the SQL database, ready to be displayed on the screen.///////////////////////// */
			/*///////////////scoresTable[i][0], scoresTable[i][2], scoresTable[i][4] contain rank, user and score respectively //////////////// */
			/*///////////////scoresTable[i][6] and scoresTable[i][8] contain the date and time respectively. ////////////////////////////////// */

			try {
				serverSocket.close();
				clientOut.close();
				serverOut.close();
			} catch (IOException e) {
				text = "Error closing connections";
				toast = Toast.makeText(context, text, duration);
				toast.show();
			}

			for(int i = 0; i < count; i++){
				scoreList.append(scoresTable[i][0] + "\t\t" + scoresTable[i][2] + "\t" + scoresTable[i][4] + "\n\n\t");
			}

		}
		if(count < 10){
			for(int i = count; i < 10; i++){
				scoreList.append((i+1) + "\t\t...\n\n\t");
			}
		}
	}
}
