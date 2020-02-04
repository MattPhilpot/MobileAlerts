package com.alerts.mobile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InterfaceSupport extends ListActivity
{
	private ArrayList<InterfaceItems> listItems = new ArrayList<InterfaceItems>();
	ArrayAdapter<InterfaceItems> adapter;
    SharedPreferences getSetting;
	SharedPreferences.Editor editor;
	int clickCounter = 0;
	int numPages = 0;
	int selectedPage = -1;
	String whichSite = "";
	ListView list;
    Vibrator vibe;

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_menu);
        list = getListView();
        getSetting = getSharedPreferences("Interface_Support", 0);
		editor = getSetting.edit();
        numPages = getSetting.getInt("NumPages", 0);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        InterfaceItems temp = null;
        if(numPages > 0)
        {
        	for(int i = 0; i < numPages; i++)
        	{
        		
        		temp = new InterfaceItems(getSetting.getString("Site_Name"+i, "Unknown"), getSetting.getString("Connection"+i, "Unknown"), getSetting.getString("Take_Type"+i, "Unknown"), getSetting.getInt("Position"+i, i), i, getSetting.getString("timeStamp", ""));
                listItems.add(temp);
                temp = null;
        	}
        }
        adapter = new ArrayAdapter<InterfaceItems>(this, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);
        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
        	{
        		if(ActivityRunning.isVibeSet())
				{
					vibe.vibrate(50);
				}
        		selectedPage = arg2;
        		registerForContextMenu(getListView());
        		openContextMenu(getListView());
        	}		
		});
        
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() 
        {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				//onCreateOptionsMenu();
				return false;
			}    	
		});
    }	
	
	
	public void updatePages()
    {
		listItems.clear();
		InterfaceItems temp = null;
        numPages = getSetting.getInt("NumPages", 0);
		for(int i = 0; i < numPages; i++)
    	{
    		temp = new InterfaceItems(getSetting.getString("Site_Name"+i, "Unknown"), getSetting.getString("Connection"+i, "Unknown"), getSetting.getString("Take_Type"+i, "Unknown"), getSetting.getInt("Position"+i, i), i, getSetting.getString("timeStamp", ""));
            listItems.add(temp);
            temp = null;
    	}
		adapter.notifyDataSetChanged();
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
		if(ActivityRunning.isVibeSet())
		{
			vibe.vibrate(50);
		}
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.int_context_menu, menu);
	    menu.setHeaderTitle(listItems.get(selectedPage).siteName + " - " + listItems.get(selectedPage).timeStamp);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
	        case R.id.details:
    			Intent i = new Intent(getApplicationContext(), InterfaceDetails.class);
    			i.putExtra("siteName", listItems.get(selectedPage).siteName);
    			i.putExtra("connectionType", listItems.get(selectedPage).connectionType);
    			i.putExtra("takeType", listItems.get(selectedPage).takeType);
    			startActivity(i);
	            return true;
	        case R.id.clearPage:
				normalizePreferences(selectedPage);
				selectedPage = -1;
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		if(ActivityRunning.isVibeSet())
		{
			vibe.vibrate(50);
		}
	    MenuInflater inflater = getMenuInflater();
	    TextView selected = (TextView)findViewById(R.id.itemSelected);
	    selected.setText(whichSite);
	    inflater.inflate(R.menu.int_menu, menu);
	    return true;
	}		
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case R.id.del:	
				updatePages();
				return true;
			case R.id.clearAll:
				listItems.clear();
				editor.clear();
				editor.commit();
				adapter.notifyDataSetChanged();
				selectedPage = -1;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void normalizePreferences(int x)
	{
		if(listItems.get(x).siteName.toString().contains("voice"))
		{
			String temp = listItems.get(x).connectionType;
			File file = new File(Environment.getExternalStorageDirectory().toString(), temp);
			file.delete();		
		}
		listItems.remove(x);
		editor.clear();
		InterfaceItems bob;
		int y = 0;
		for(y = 0; y < listItems.size(); y++)
		{
			bob = listItems.get(y);
			if(y >= x)
			{
				bob.setArrayPosition();
				bob.setListPosition();	
			}
			
			editor.putString("Site_Name"+y, bob.siteName);
			editor.putString("Connection"+y, bob.connectionType);             
            editor.putString("Take_Type"+y, bob.takeType);  
            editor.putString("timeStamp"+y, bob.timeStamp);
            editor.putInt("Position"+y, y);
		}
		editor.putInt("NumPages", y);
		editor.commit();
		adapter.notifyDataSetChanged();
	}
	
	/* ----------------- BEGIN InterfaceItems ------------------ */
	class InterfaceItems
	{
		private String siteName = "";
		private String connectionType = "";
		private String takeType = "";
		private int listPosition = 0;
		private int arrayPosition = 0;
		private String timeStamp = "";

		public InterfaceItems(String siteName, String connectionType, String takeType, int listPosition, int arrayPosition, String timeStamp)
		{
			this.siteName = siteName;
			this.connectionType = connectionType;
			this.takeType = takeType;
			this.listPosition = listPosition;
			this.arrayPosition = arrayPosition;
			this.timeStamp = timeStamp;
		}
		
		@Override
		public String toString()
		{
			return this.siteName + "\n[" + this.connectionType + "] - " + takeType + "\n" + timeStamp;
		}
		
		void setListPosition()
		{
			listPosition--;
		}
		
		void setArrayPosition()
		{
			arrayPosition--;
		}
	}
	/* ----------------- END InterfaceItems ------------------ */
	
	
	@Override
	protected void onStart() 
	{
		// TODO Auto-generated method stub
		super.onStart();
	}
    
	@Override
	protected void onResume() 
	{
		super.onResume();
		ActivityRunning.activityResumed(this);
		selectedPage = -1;
		TextView selected = (TextView)findViewById(R.id.itemSelected);
	    selected.setText("");
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		ActivityRunning.activityPaused(this);
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
