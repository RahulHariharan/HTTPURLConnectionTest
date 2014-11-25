package com.ipw.httpproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class HTTPService extends IntentService {

	public HTTPService() {
		super("HTTPService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		HttpURLConnection connection = null;
		Log.v("HI","HI");
		try{
		
		    String result = null;
			
			URL url = new URL("Enter URL here");
	      
			connection = (HttpURLConnection)url.openConnection();
			
			InputStream inputStream = (InputStream)connection.getInputStream();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			String metadata = "";
			
			StringBuilder stringBuilder = new StringBuilder();
			
			while((metadata= reader.readLine()) != null){
				
				stringBuilder.append(metadata); 
			}
			
			reader.close();
	        
	        result = stringBuilder.toString();
	        
	        Log.v("Result",result);
		}
		catch(MalformedURLException exception){
			
			exception.printStackTrace();
		}
		catch(IOException exception){
			
			exception.printStackTrace();
		}
		finally{
		    
			connection.disconnect();
			
		}
		
		
	}

}
