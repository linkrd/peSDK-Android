/*
 * Copyright ©2013 SCA interactive
 * 
 * @author Thieu Huynh
 */
package com.scai.prizesdk;

import org.json.JSONObject;

import android.content.Context;

/**
 * PeSDK Synchronous Class
 * 
 * @author Thieu Huynh
 *
 */
public class PeSyncTask extends PeAsyncTask {
	
	PeSDKDelegate delegate;
	
	public PeSyncTask(PeSDKDelegate delegate, Context context) {
		super(delegate, context);
		this.delegate = delegate;
	}

	@Override
	protected void onPostExecute(JSONObject jsonObject) {

	}
}
