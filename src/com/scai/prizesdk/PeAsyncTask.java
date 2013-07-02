package com.scai.prizesdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * PeSDK Asynchronous Class
 * 
 *
 */
public class PeAsyncTask extends AsyncTask<JSONObject, Void, JSONObject> {

	private static final String TAG = PeAsyncTask.class.getCanonicalName();
	//private Exception exception = null;
	private PeSDKDelegate delegate;
	private Context context;
	ProgressDialog dialog;
	
	public PeAsyncTask (PeSDKDelegate peSDKDelegate, Context context) {
		setDelegate(peSDKDelegate);
		setContext(context);
		try {
			dialog = new ProgressDialog(context);
		} catch(Exception e) {
			Log.e(TAG, "Exception:" + e);
		}
	}
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
//        dialog.setTitle("Please wait");
//        dialog.show();
        
	}
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		String resultString = peSend(params[0]);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(resultString);
		} catch (JSONException e) {
			delegate.actionCompletedWithError("", new PeException("invalid_data_returned"));
			return null;
		} 
		
		return jsonObject;
	}
	
	@Override
	protected void onPostExecute(JSONObject jsonObject) {

		JSONObject result = new JSONObject();
		String action = null;
		
//		dialog.dismiss();
		
		try {
			result = jsonObject.getJSONObject("result");
			if (result.getInt("success") == 1) {
 
					JSONObject config = new JSONObject();
					config = result.getJSONObject("config");
				
				action = result.getString("action");
				
				PeConfig.setConfiguration(config);
									
				delegate.actionComplete(action, result);
				
			} else {
				JSONArray errors = result.getJSONArray("errors");
				delegate.actionCompletedWithError("", new PeException(errors.getString(0)));
			}
		} catch (JSONException e) {
			delegate.actionCompletedWithError("", new PeException("invalid_data_returned"));
		}
	}

	
	public String peSend(JSONObject params) {
		StringBuilder stringBuilder = new StringBuilder();

		HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT) 
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        
//        String[] pins         = new String[] {"740e114d70547085cff087680173b1327cccfe4f"};
        
//        HttpClient client = PinningHelper.getPinnedHttpClient(this.context, pins);
		PeHttpClient client = new PeHttpClient(httpParameters, context);
		
		HttpPost httpPost = new HttpPost(PeClient.getUrl());
					
		try {
				           
	        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

	        Iterator<?> keys = params.keys();

	        while( keys.hasNext() ){
	        	String key = (String)keys.next();
	        	paramsList.add(new BasicNameValuePair(key, (String) params.get(key)));
	        }
	        
			httpPost.setEntity(new UrlEncodedFormEntity(createListFromJSONObject(params)));

		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		} catch (Exception e) {
		    e.printStackTrace();
		} 
		try {
			
			HttpResponse response = client.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
			} else {
				Log.e(TAG, "Failed with status code:" + statusCode);
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}
	
	public List<NameValuePair> createListFromJSONObject(JSONObject params) {

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

		Iterator<?> keys = params.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			try {
				paramsList.add(new BasicNameValuePair(key, (String) params.get(key)));
			} catch (JSONException e) {
				delegate.actionCompletedWithError("", new PeException("invalid_data_returned"));
			}
		}
		return paramsList;
	}

	public PeSDKDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(PeSDKDelegate peSDKDelegate) {
		this.delegate = peSDKDelegate;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
}
