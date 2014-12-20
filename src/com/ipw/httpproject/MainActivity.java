package com.ipw.httpproject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	
	Receiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		receiver = new Receiver();
		
		Intent intent = new Intent(this,HTTPService.class);
		startService(intent);
		
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	protected void onResume(){
		super.onResume();
		
		IntentFilter filterReceiver = new IntentFilter(Receiver.RECEIVER_KEY);
		filterReceiver.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(receiver,filterReceiver);
		
	}
	
	@Override
	protected void onStop(){
		
		unregisterReceiver(receiver);
		
	}
	
	public class Receiver extends BroadcastReceiver{
        
		public static final String RECEIVER_KEY = "com.ipw.rahul.RECEIVER_KEY";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			Log.d("RESULTFROMACTIVITY",intent.getSerializableExtra(HTTPService.KEY).toString());
		    
			
		}
		
	}// end of Receiver
	
	

}
