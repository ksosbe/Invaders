package com.example.cs252project;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class Options extends Activity implements OnItemSelectedListener
{

	Spinner spinner;
	String[] colors = {"Blue", "Red", "Green", "Pink", "Yellow"};
	//InvadersApplication global;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		//global = (InvadersApplication)getApplicationContext();
		ImageButton back = (ImageButton) findViewById(R.id.back);
		
		back.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) 
			{
        		finish();
			}
		});
		
		final CheckBox soundbox = (CheckBox) findViewById(R.id.soundbox);
		final CheckBox demobox = (CheckBox) findViewById(R.id.demobox);
		
		if(InvadersApplication.sound){
			soundbox.setChecked(false);
		}
		else{
			soundbox.setChecked(true);
		}
		
		if(InvadersApplication.demo){
			demobox.setChecked(true);
		}
		else{
			demobox.setChecked(false);
		}
		
		soundbox.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				if(soundbox.isChecked())
				{
					InvadersApplication.sound = false;
				}
				if(!soundbox.isChecked())
				{
					InvadersApplication.sound = true;
				}
			}
		});
		
		demobox.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				if(demobox.isChecked())
				{
					InvadersApplication.demo = true;
				}
				if(!demobox.isChecked())
				{
					InvadersApplication.demo = false;
				}
			}
		});
		
		spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter my_Adapter = new ArrayAdapter<Object>(this, R.layout.spinner,colors);
		spinner.setAdapter(my_Adapter);
		spinner.setSelection(InvadersApplication.position);
		spinner.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		int pos = spinner.getSelectedItemPosition();
		switch (pos)
		{
		case 0:
			InvadersApplication.color = "Blue";
			InvadersApplication.position = 0;
			break;
		case 1:
			InvadersApplication.color = "Red";
			InvadersApplication.position = 1;
			break;
		case 2:
			InvadersApplication.color = "Green";
			InvadersApplication.position = 2;
			break;
		case 3:
			InvadersApplication.color = "Pink";
			InvadersApplication.position = 3;
			break;
		case 4:
			InvadersApplication.color = "Yellow";
			InvadersApplication.position = 4;
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	

}
