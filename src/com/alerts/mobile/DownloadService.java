package com.alerts.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DownloadService extends Service 
{
	String downloadFile;
	//SharedPreferences getSetting = this.getSharedPreferences("Interface_Support", Context.MODE_PRIVATE);
	
	@Override
	public IBinder onBind(Intent arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
    @Override
    public void onCreate()
    {
        super.onCreate();
        //downloadFile = getSetting.getString("url", "");
        try 
        {
			URL url = new URL("ftp://ipftp:FilesNow@download.com.org/IAlertMobile/Voicemail/it.mp3");
			URLConnection urlc = url.openConnection();
			InputStream is = urlc.getInputStream(); // To download
			File file = new File(Environment.getExternalStorageDirectory().toString(), "it.mp3");
			FileOutputStream os = new FileOutputStream(file);
			byte[] buffer = new byte[4096];
			int len;
			
			while ((len = is.read(buffer)) > 0)
			{
				os.write(buffer, 0, len);
			}
			os.close();
			is.close();	
			Toast.makeText(getApplicationContext(), "Voicemail download successful", Toast.LENGTH_SHORT).show();

		} 
        catch (MalformedURLException e) 
		{
			Toast.makeText(getApplicationContext(), "MalformedURLException", Toast.LENGTH_SHORT).show();
			Log.w("newURL", "failed");
			e.printStackTrace();
		} 
        catch (IOException e) 
		{
    		Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

        this.stopSelf();
    }

    @Override
    public void onDestroy() 
    {
        super.onDestroy();
    }

}
