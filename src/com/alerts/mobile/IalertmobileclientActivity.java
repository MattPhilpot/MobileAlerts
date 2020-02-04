package com.alerts.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class IalertmobileclientActivity extends Activity 
{
	boolean alreadyRegistered = false;
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences getSetting = getSharedPreferences("Interface_Support", 0);
        alreadyRegistered = getSetting.getBoolean("Registered", false);
        if(getSetting.getBoolean("hapticVibe", false) == true)
        	ActivityRunning.vibeYes();
        else
        	ActivityRunning.vibeNo();
        
        if(getSetting.getBoolean("alertVibe", false) == true)
        	ActivityRunning.alertVibeYes();
        else
        	ActivityRunning.alertVibeNo();
        
        if(getSetting.getBoolean("callSuppress", false) == true)
        	ActivityRunning.vibeYes();
        else
        	ActivityRunning.vibeNo();
        
        
        Thread splashTimer = new Thread()
        {
        	public void run()
        	{
        		try
        		{
        			int timer = 0; //change to for-loop sometime
        			while(timer < 2500)
        			{
        				sleep(100);
        				timer+=100;
        			}
        			if(alreadyRegistered == true)
        				startActivity(new Intent("com.alerts.mobile.MM"));
        			else
        				startActivity(new Intent("com.alerts.mobile.Register"));
        		}
        		catch(InterruptedException e)
        		{
        			e.printStackTrace();
        		}
        		finally
        		{
        			finish();
        		}
        	}
        };
        splashTimer.start();
    }

	@Override
	protected void onStart() 
	{
		// TODO Auto-generated method stub
		super.onStart();
	}
    
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onStop() 
	{
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onRestart() 
	{
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}