package com.alerts.mobile;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Vibrator;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainMenu extends Activity
{
    SharedPreferences getSetting;
	SharedPreferences.Editor editor;
	SharedPreferences getSettings;
	SharedPreferences.Editor editors;
	private static boolean blinky = false;
	public static MainMenu MM = null;
    Intent intent; 
    Vibrator vibe;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu); 
        getSetting = getSharedPreferences("Interface_Support", 0);
		editor = getSetting.edit();
		getSettings = getSharedPreferences("Application_Options", 0);
		editors = getSettings.edit();
        final ImageButton interfaceButton = (ImageButton) findViewById(R.id.interfaceButton);
        final ImageButton testButton = (ImageButton) findViewById(R.id.testButton);
        final Animation animation = new AlphaAnimation(1,(float) .2);
        ImageButton optionsButton = (ImageButton) findViewById(R.id.optionsButton); 
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(500);
        editor.putBoolean("Registered", true);
        editor.commit();
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        intent = getIntent();
        blinky = intent.getBooleanExtra("interfacesPaging", false);
        if(blinky)
        {
        	interfaceButton.setBackgroundResource(R.drawable.selector_interface_support_paging);
			interfaceButton.startAnimation(animation);
        }
        
		if(getContactDisplayNameByNumber() == false)
		{
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			    ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
	    		.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
	            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
		    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
	            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
	            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
	            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "IAlert")
	            .build());
		    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
	            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
	            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
	            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "2142221125")
	            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,Phone.TYPE_WORK).build());         
		    try 
		    {
		    	getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		    	Toast.makeText(this, "IAlert Contact Created", Toast.LENGTH_SHORT).show(); 
		    } 
		    catch (RemoteException e) 
		    {
		    	e.printStackTrace();
		    } 
		    catch (OperationApplicationException e) 
		    {
		    	e.printStackTrace();
		    }		
		}
     
		interfaceButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View opt) 
			{
				if(ActivityRunning.isVibeSet())
				{
					vibe.vibrate(50);
				}
				interfaceButton.setBackgroundResource(R.drawable.selector_interface_support);
				interfaceButton.clearAnimation();
				stopService(new Intent(MainMenu.this, AlertNotification.class));
				if(blinky == true)
				{
					SmsManager sm = SmsManager.getDefault();
					String msg_from = getSettings.getString("msg_from", "");
					sm.sendTextMessage(msg_from, null, "clear", null, null);
				}
				blinky = false;
				startActivity(new Intent("com.alerts.mobile.IS"));
			}
		});
		testButton.setOnClickListener(new View.OnClickListener() 
		{

			public void onClick(View opt) 
			{		
				if(ActivityRunning.isVibeSet())
				{
					vibe.vibrate(50);
				}
				if(blinky == true)
				{
					interfaceButton.setBackgroundResource(R.drawable.selector_interface_support);
					interfaceButton.clearAnimation();
					blinky = false;
					stopService(new Intent(MainMenu.this, AlertNotification.class));
					SmsManager sm = SmsManager.getDefault();
					String msg_from = getSettings.getString("msg_from", "");
					sm.sendTextMessage(msg_from, null, "clear", null, null);
					editor.clear();
					editor.commit();		
				}
			}
		});	
		
		optionsButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				if(ActivityRunning.isVibeSet())
				{
					vibe.vibrate(50);
				}
				startActivity(new Intent("com.alerts.mobile.Options"));
			}
		});	
    }
	
	public static void makeBlinky()
	{
		blinky = true;
	}	
	
	public boolean getContactDisplayNameByNumber() 
	{
	    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode("2142221125"));
	    boolean found = false;

	    ContentResolver contentResolver = getContentResolver();
	    Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

	    try 
	    {
	        if (contactLookup != null && contactLookup.getCount() > 0) 
	        {
	            contactLookup.moveToNext();
	            found = true;
	        }
	    } 
	    finally 
	    {
	        if (contactLookup != null) 
	        {
	            contactLookup.close();
	        }
	    }
	    return found;
	}
	
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		ActivityRunning.activityResumed(this);
		MM = this;
	}
	
	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
		ActivityRunning.activityPaused(this);
		MM = null;
	}
	
}
