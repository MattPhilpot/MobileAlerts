package com.alerts.mobile;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class OptionsMenu extends Activity
{

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_menu);
        final SharedPreferences getSetting = getSharedPreferences("Application_Options", 0);
		final SharedPreferences.Editor editor = getSetting.edit();
		
		/* hapticFeedback */
		final CheckBox hapticOption = (CheckBox)findViewById(R.id.vibrate_option);
        final TextView vibrateText = (TextView)findViewById(R.id.vibrate_text);  
        
        
        /* Vibrate on Alert */
        final CheckBox alertVibe = (CheckBox)findViewById(R.id.vibe_alert_option);
        final TextView alertVibeText = (TextView)findViewById(R.id.vibrate_on_alert); 
        
        
        /* Call Suppression */
        final CheckBox callSuppression = (CheckBox)findViewById(R.id.suppression_option);
        final TextView suppressionText = (TextView)findViewById(R.id.call_suppression);            
        
        /* 'Haptic Feedback' presets */
        if(ActivityRunning.isVibeSet())
		{
			vibrateText.setText("Device will vibrate on keypress");
			hapticOption.setChecked(true);
		}
        else
		{
			vibrateText.setText("Device won't vibrate on keypress");
			hapticOption.setChecked(false);
		}
        
        /* 'Vibrate on Alert' presets */
        if(ActivityRunning.isAlertVibe())
		{
        	alertVibeText.setText("Device will vibrate on incoming alerts");
        	alertVibe.setChecked(true);
		}
		else
		{
			alertVibeText.setText("Device won't vibrate on incoming alerts");
			alertVibe.setChecked(false);
		}
        
        /* 'Call Suppression' presets */
        if(ActivityRunning.isCallSuppress())
		{
        	suppressionText.setText("Device will suppress incoming support calls");
        	callSuppression.setChecked(true);
		}
		else
		{
			suppressionText.setText("Device won't suppress incoming support calls");
			callSuppression.setChecked(false);
		}
       
        /* 'Haptic Feedback' Listener */
        hapticOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
				if (isChecked)
		        {
					vibrateText.setText("Device will vibrate on keypress");
					ActivityRunning.vibeYes();
					editor.putBoolean("hapticVibe", true);
					editor.commit();
		        }
				else
				{
					vibrateText.setText("Device won't vibrate on keypress");
					ActivityRunning.vibeNo();
					editor.putBoolean("hapticVibe", false);
					editor.commit();
				}
		    }
		});
        
        /* 'Vibrate on Alert' Listener */
        alertVibe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
				if (isChecked)
		        {
					alertVibeText.setText("Device will vibrate on incoming alerts");
					ActivityRunning.alertVibeYes();
					editor.putBoolean("alertVibe", true);
					editor.commit();
		        }
				else
				{
					alertVibeText.setText("Device won't vibrate on incoming alerts");
					ActivityRunning.alertVibeNo();
					editor.putBoolean("alertVibe", false);
					editor.commit();
				}
		    }
		});
        
        /* 'Call Suppression' Listener */
        callSuppression.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
				if (isChecked)
		        {
					suppressionText.setText("Device will suppress incoming support calls");
					ActivityRunning.callSuppressYes();
					editor.putBoolean("callSuppress", true);
					editor.commit();
		        }
				else
				{
					suppressionText.setText("Device won't suppress incoming support calls");
					ActivityRunning.callSuppressNo();
					editor.putBoolean("callSuppress", false);
					editor.commit();
				}
		    }
		});
        
    }
}
