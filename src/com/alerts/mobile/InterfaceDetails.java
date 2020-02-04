package com.alerts.mobile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class InterfaceDetails extends Activity
{
	MediaPlayer player;
   	SharedPreferences getSetting;
    Vibrator vibe;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_detail);
        getSetting = getSharedPreferences("Interface_Support", 0);
        Display dm = getWindowManager().getDefaultDisplay();
        player = new MediaPlayer();
        Intent intent = getIntent();
        String siteName = intent.getStringExtra("siteName");
        final String connectionType = intent.getStringExtra("connectionType");
        String takeType = intent.getStringExtra("takeType");
        TextView _siteName = (TextView)findViewById(R.id.siteName);
        TextView _siteIP = (TextView)findViewById(R.id.siteIP);
        TextView _takeType = (TextView)findViewById(R.id.siteConnectionType);
        Button _play = (Button)findViewById(R.id.Play);
        Button _stop = (Button)findViewById(R.id.Stop);
        _siteName.setText(siteName);
        _siteIP.setText(connectionType);
        _takeType.setText(takeType);   
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(siteName.contains("voicemail"))
        {
        	_play.setVisibility(View.VISIBLE);
        	_stop.setVisibility(View.VISIBLE);
        }
        
        final File file = new File(Environment.getExternalStorageDirectory().toString(), connectionType);
        if(!file.exists() && siteName.contains("voice"))
        	InterfaceDetails.this.startService(new Intent(InterfaceDetails.this, DownloadService.class)); 
        
        _play.setOnClickListener(new View.OnClickListener() 
		{			
			public void onClick(View v) 
			{		
				if(ActivityRunning.isVibeSet())
				{
					vibe.vibrate(50);
				}
				try 
				{
					FileInputStream fis = new FileInputStream(file);		
					player.setDataSource(fis.getFD());
					player.prepare();
			        player.start();
			        player.setLooping(false); 
				} 
				catch (IllegalArgumentException e) 
				{
					Toast.makeText(InterfaceDetails.this, "IllegalArgumentException", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} 
				catch (IllegalStateException e) 
				{
					Toast.makeText(InterfaceDetails.this, "IllegalStateException", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					Toast.makeText(InterfaceDetails.this, "IOException", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});
        
        _stop.setOnClickListener(new View.OnClickListener() 
		{
			
			public void onClick(View v) 
			{
				if(ActivityRunning.isVibeSet())
				{
					vibe.vibrate(50);
				}
				if(player.isPlaying())
				{
					player.stop();
					player.reset();
				}

			}
		});
    }
	
	
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		ActivityRunning.activityResumed(this);
	}
	
	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
		ActivityRunning.activityPaused(this);
	}
	
}
