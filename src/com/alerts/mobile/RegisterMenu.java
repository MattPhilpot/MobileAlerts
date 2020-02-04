package com.alerts.mobile;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.*;


public class RegisterMenu extends Activity 
{
	ProgressDialog dialog;
	
	private static String SERVERIP = "10.10.10.10";
	private static final int SERVERPORT = 8080;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_menu);
        final SharedPreferences getSetting = getSharedPreferences("Interface_Support", 0);
		final SharedPreferences.Editor editor = getSetting.edit();		
		
		
		final ImageButton interfaceButton = (ImageButton) findViewById(R.id.largeGoldenButton);
		final EditText username = (EditText) findViewById(R.id.username);
		final EditText password = (EditText) findViewById(R.id.password);
		final TextView isCorrect = (TextView)findViewById(R.id.isCorrect);
		
		interfaceButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View opt) 
			{
				dialog = ProgressDialog.show(RegisterMenu.this, "", "Connecting to Server", true);
				isCorrect.setText("");
				String number = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getLine1Number();
				String temp = number.substring(number.length()-4);
				URL url;
				try 
				{
					isCorrect.setText("");
					url = new URL("ftp://com%5C" + username.getText().toString().trim() + ":" + password.getText().toString().trim() + "@download.com.org/IAlertMobile/newRegistration" + temp + ".txt");
					URLConnection urlc = url.openConnection();
					urlc.setDoOutput(true);
					dialog.setMessage("Communicating with Server");
					OutputStreamWriter out = new OutputStreamWriter(urlc.getOutputStream());				
					out.write(number.substring(1) + "|" + ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getNetworkOperatorName());
					out.close();
					
					
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
				            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,Phone.TYPE_WORK)
				            .withValue(ContactsContract.Contacts.SEND_TO_VOICEMAIL, 1).build());         
					    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					        .withValue(ContactsContract.Contacts.SEND_TO_VOICEMAIL, 1).build());   
					    try 
					    {
					    	getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
					    	Toast.makeText(RegisterMenu.this, "IAlert Contact Created", Toast.LENGTH_SHORT).show(); 
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
					//editor.putBoolean("Registered", true); //original position for this code
					editor.commit();
					startActivity(new Intent("com.alerts.mobile.MM"));

				} 
				catch (MalformedURLException e) 
				{
					isCorrect.setText("Authentication Failed. Please Try Again!");
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					isCorrect.setText("Authentication Failed. Please Try Again!");
					e.printStackTrace();
				}
				editor.putBoolean("Registered", true);
				dialog.dismiss();
			}
		});
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