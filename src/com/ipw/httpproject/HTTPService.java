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
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ipw.httpproject.MainActivity.Receiver;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class HTTPService extends IntentService {
	
	
	public static final String KEY = "com.ipw.app.KEY";
    
	public HTTPService() {
		super("HTTPService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		HttpURLConnection connection = null;
		
		try{
		
		    String result = null;
			
			URL url = new URL("ENTER_URL_HERE");
	      
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(10000);
			
			/*List<NameValuePair> params= new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("",""));
			params.add(new BasicNameValuePair("",""));
			
			OutputStream outputStream = connection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
			writer.write(params.toString());
			writer.flush();
			writer.close();
  			outputStream.close();*/
			connection.connect();
				
			if(connection.getResponseCode() == 200){
				
				InputStream inputStream = (InputStream)connection.getInputStream();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				
				String metadata = "";
				
				StringBuilder stringBuilder = new StringBuilder();
				
				while((metadata= reader.readLine()) != null){
					
					stringBuilder.append(metadata); 
				}
				
				reader.close();
				inputStream.close();
		        
		        result = stringBuilder.toString();
		      
		        //Log.d("Result",result);
		        
		        Intent broadcastIntent = new Intent();
		        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		        broadcastIntent.setAction(Receiver.RECEIVER_KEY);
		        broadcastIntent.putExtra(KEY,jsonReader(result));
		        sendBroadcast(broadcastIntent);
			}
		}
		catch(MalformedURLException exception){
			
			exception.printStackTrace();
		}
		catch(IOException exception){
			
			exception.printStackTrace();
		}
		catch(JSONException exception){
			
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
	
	private ArrayList<HashMap<String,String>> jsonReader(String result) throws JSONException{
		
		ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();
		JSONArray jsonArray = new JSONArray(result);
		
		int length = jsonArray.length();
		for(int index = 0; index <length; index++){
			
			JSONObject jsonObject = jsonArray.getJSONObject(index);
			
			HashMap<String,String> map = new HashMap<String,String>();
			map.put(jsonObject.getString("id"),jsonObject.getString("filename"));
			
			mList.add(map);
		}
		
		return mList;
	}

}
