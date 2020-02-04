package com.alerts.mobile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

public class AlertNotification extends Service 
{

    private MediaPlayer player;
    Vibrator vibe;
    

    @Override
    public IBinder onBind(Intent intent) 
    {
    	return null;
    }

    @Override
    public void onCreate() 
    {
        super.onCreate();
        player = MediaPlayer.create(AlertNotification.this, R.raw.alert);
        player.start();
        player.setLooping(true);
        if(ActivityRunning.isAlertVibe())
        {
        	long[] pattern = {100, 50, 100, 50, 100, 50};
        	vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        	vibe.vibrate(pattern, 0);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        player.stop();
        if(ActivityRunning.isAlertVibe())
        {
        	vibe.cancel();
        }
        
    }

}