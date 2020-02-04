package com.alerts.mobile;

import android.app.Instrumentation;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;


public class SmsListener extends BroadcastReceiver
{	
    @Override
    public void onReceive(Context context, Intent intent) 
    {
        // TODO Auto-generated method stub

    	SharedPreferences getSetting = context.getSharedPreferences("Interface_Support", 0);
		final SharedPreferences.Editor editor = getSetting.edit();
		SharedPreferences getSettings = context.getSharedPreferences("Application_Options", 0);
		final SharedPreferences.Editor editors = getSettings.edit();
    	int count = getSetting.getInt("NumPages", 0);
    	String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);    	
    	
    	
    	if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from = "";
            if (bundle != null)
            {
                //---retrieve the SMS message received---
                try
                {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++)
                    {
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        
                        boolean  bob = getSetting.getBoolean("Registered", false);
                        Log.w("bob", String.valueOf(bob));
                        
                        if(msgBody.contains("ialert") && bob  == true)
                        {
                        	msgBody = msgBody.substring(8);
                        	String temp = "##";
                        	String[] tokens = msgBody.split(temp);
                        	editors.putString("msg_from", msg_from);
                        	editors.commit();
                        	if(tokens[0].contains("voice"))
                        	{  		                  		
                       			editor.putString("Site_Name"+count, tokens[0]);
                           		editor.putString("Connection"+count, tokens[1]);
                                editor.putString("Take_Type"+count, tokens[2]);   
                                Time now = new Time();
                                String time = String.valueOf(now.HOUR) + ":" + String.valueOf(now.MINUTE);
                                editor.putString("timeStamp"+count, time);
                                //Log.w("timeOfDay", time+"|||");
                                editor.putInt("Position"+count, count);
                                count++;
                                editor.putInt("NumPages", count);        
                        	}
                        	else
                        	{
                       			editor.putString("Site_Name"+count, tokens[0]);
                           		editor.putString("Connection"+count, tokens[1]);
                                editor.putString("Take_Type"+count, tokens[2]);   
                                editor.putInt("Position"+count, count);
                                count++;
                                editor.putInt("NumPages", count);
                        	}
                        	editor.commit();
  
                            
                            if(ActivityRunning.isActivityVisible() == false)
                            {
                                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                Notification notification = new Notification(R.drawable.com_symbol, "IAlert Mobile Alert!", System.currentTimeMillis());
                                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                                notification.flags |= Notification.DEFAULT_SOUND;
                                notification.sound = Uri.parse("android.resource://com.alerts.mobile/");
                                Intent notificationIntent = new Intent(context, MainMenu.class);
                                notificationIntent.putExtra("interfacesPaging", true);
                                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
                                notification.setLatestEventInfo(context, tokens[0], tokens[1], contentIntent);
                                mNotificationManager.notify(1, notification);
                                
                                context.startService(new Intent(context, AlertNotification.class));
                            }        
                            else
                            {
                            	final Animation animation = new AlphaAnimation(1,(float) .2);
                                animation.setInterpolator(new LinearInterpolator());
                                animation.setRepeatCount(Animation.INFINITE);
                                animation.setRepeatMode(Animation.REVERSE);
                                animation.setDuration(500);
                                MainMenu.makeBlinky();
                                MainMenu.MM.findViewById(R.id.interfaceButton).setBackgroundResource(R.drawable.selector_interface_support_paging);
                            	MainMenu.MM.findViewById(R.id.interfaceButton).startAnimation(animation);
                            }
                            this.abortBroadcast();
                        }               
                        //Log.i("MessageReceived", msgBody);            
                    }
                    
                    
                }
                catch(Exception e)
                {
//              	Log.d("Exception caught",e.getMessage());
                }
            }
        }
    	Log.i("phoneNumber", TelephonyManager.EXTRA_INCOMING_NUMBER);
    }
}