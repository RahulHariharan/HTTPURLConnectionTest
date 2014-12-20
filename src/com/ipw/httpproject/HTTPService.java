package com.ipw.httpproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ipw.httpproject.MainActivity.Receiver;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class HTTPService extends IntentService {
	
	private final static String PHOTOS_URL = "http://tablemate.cloudapp.net/index.php/menuitems/getphotos";
	private final static String LOGIN_URL = "http://tablemate.cloudapp.net/index.php/site/loginviaapi";
	public static final String KEY = "com.ipw.app.KEY";
    
	public HTTPService() {
		super("HTTPService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		HttpURLConnection connection = null;
		
		try{
		
		    String result = null;
			
			URL url = new URL(PHOTOS_URL);
	      
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(10000);
			
			/*List<NameValuePair> params= new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("LoginForm[username]","ad9min"));
			params.add(new BasicNameValuePair("LoginForm[password]","admin"));
			
			OutputStream outputStream = connection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
			writer.write(params.toString());
			writer.flush();
			writer.close();
  			outputStream.close();*/
			connection.connect();
			
			InputStream inputStream = (InputStream)connection.getInputStream();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			String metadata = "";
			
			StringBuilder stringBuilder = new StringBuilder();
			
			while((metadata= reader.readLine()) != null){
				
				stringBuilder.append(metadata); 
			}
			
			reader.close();
	        
	        result = stringBuilder.toString();
	        
	        Log.d("Debug mode","Debug mode awesomeness");
	        Log.v("Result",result);
	        
	        Intent broadcastIntent = new Intent();
	        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
	        broadcastIntent.setAction(Receiver.RECEIVER_KEY);
	        broadcastIntent.putExtra(KEY,result);
	        sendBroadcast(broadcastIntent);
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
	
	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException{
		
		StringBuilder result = new StringBuilder();
		boolean first = true;
		
		for(NameValuePair pair : params){
			
			if(first == true){
				
				first = false;
				
			}
			else{
				
				result.append("&");
			}
			
			result.append(URLEncoder.encode(pair.getName(),"UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(),"UTF-8"));
				
		}
		
		return result.toString();
	}

}
