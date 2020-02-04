package com.alerts.mobile;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

public class ActivityRunning extends Application
{
	private static boolean activityVisible;
	private static boolean vibeSet;
	private static boolean alertVibe;
	private static boolean callSuppress;

	public static boolean isActivityVisible() 
	{
	    return activityVisible;
	}  
	
	public static boolean isVibeSet() 
	{
	    return vibeSet;
	}
	
	public static boolean isAlertVibe() 
	{
	    return alertVibe;
	}
	
	public static boolean isCallSuppress() 
	{
	    return callSuppress;
	}

	public static void vibeYes()
	{
		vibeSet = true;
	}
	
	public static void vibeNo()
	{
		vibeSet = false;
	}
	
	public static void alertVibeYes()
	{
		alertVibe = true;
	}
	
	public static void alertVibeNo()
	{
		alertVibe = false;
	}
	
	public static void callSuppressYes()
	{
		callSuppress = true;
	}
	
	public static void callSuppressNo()
	{
		callSuppress = false;
	}
	
	public static void activityResumed(Context context) 
	{
		SharedPreferences getSetting = context.getSharedPreferences("Application_Options", 0);
		SharedPreferences.Editor editor = getSetting.edit();
	    activityVisible = true;
	    editor.putBoolean("Registered", true);
	    editor.commit();
	}

	public static void activityPaused(Context context) 
	{
		SharedPreferences getSetting = context.getSharedPreferences("Application_Options", 0);
		SharedPreferences.Editor editor = getSetting.edit();
	    activityVisible = false;
	    editor.putBoolean("Registered", true);
	    editor.commit();
	}
}
