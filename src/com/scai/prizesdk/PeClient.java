/*
 * Copyright ©2013 SCA interactive
 * 
 * @author Thieu Huynh
 */
package com.scai.prizesdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
 
/**
 * PeSDK Client Class
 * 
 * 
 */
public class PeClient {

	private static final String TAG = PeClient.class.getCanonicalName();
	private static PeClient instance = null;

	private static String url;
	private static String contest_adminID;
	private static String authkey;
	private static PeSDKDelegate delegate;
	private static Context context;

	public PeClient() {
		// Exists only to defeat instantiation.
	}

	public PeClient(PeSDKDelegate delegate, Context context) {
		setDelegate(delegate);
		setContext(context);
		PeClient.context = context;
	}

	public static PeClient getInstance(Map<String, String> configMap) {
		if (instance == null) {
			instance = new PeClient();
			init(configMap);
		}
		return instance;
	}

	public static void init(Map<String, String> configMap) {
		final String proto = (PeConstant.IS_SSL) ? "https" : "http";
		StringBuilder urlString = new StringBuilder();
		urlString.append(proto).append("://").append(PeConstant.SERVICE_HOST)
				.append("/").append(configMap.get("client")).append("/")
				.append(configMap.get("promo")).append("/")
				.append(PeConstant.SERVICE);
		setUrl(urlString.toString());
		setContest_adminID(configMap.get("contest_adminID"));
		setAuthkey(configMap.get("authkey"));

	}

	public static void getConfig() {

		JSONObject jsonParameters = new JSONObject();
		jsonParameters = buildJSONParams("getconfig", "contest_adminID",
				getContest_adminID(), null);

		Log.i(TAG, "getConfig context:" + context);
		PeAsyncTask peTask = new PeAsyncTask(getDelegate(), context);
		peTask.execute(jsonParameters);

	}

	public static JSONObject doSyncAction(String action, String userkey,
			String uservalue, JSONObject extraParams) {

		JSONObject jsonParameters = new JSONObject();
		jsonParameters = PeClient.buildJSONParams(action, userkey, uservalue,
				extraParams);

		PeSyncTask peTask = new PeSyncTask(PeClient.delegate, context);

		JSONObject jsonResponse = new JSONObject();

		try {
			jsonResponse = peTask.execute(jsonParameters).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return jsonResponse;
	}

	/**
	 * 
	 * @param action
	 *            String of action
	 * @param userkey
	 *            String of userkey
	 * @param uservalue
	 *            String of uservalue
	 * @param extraParams
	 *            JSONObject of extra parameters
	 * @return JSONObject of parameters
	 */
	public static JSONObject buildJSONParams(String action, String userkey,
			String uservalue, JSONObject extraParams) {

		JSONObject jsonParameters = new JSONObject();
		if (extraParams != null) {
			jsonParameters = extraParams;
		}

		try {
			jsonParameters.put("action", action);
			jsonParameters.put("client", "demosdk");
			jsonParameters.put("promo", "instant");
			jsonParameters.put(userkey, uservalue);
			jsonParameters.put("response_format", PeConstant.RESPONSE_TYPE);
			jsonParameters.put("auth", buildAuth(uservalue));
		} catch (JSONException e) {
			Log.i(TAG, "JSONException:" + e);
			e.printStackTrace();
		}

		return jsonParameters;
	}

	private static String buildAuth(String key) {
		return md5(key + getAuthkey());
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		PeClient.url = url;
	}

	public static String getContest_adminID() {
		return contest_adminID;
	}

	public static void setContest_adminID(String contest_adminID) {
		PeClient.contest_adminID = contest_adminID;
	}

	public static String getAuthkey() {
		return authkey;
	}

	public static void setAuthkey(String authkey) {
		PeClient.authkey = authkey;
	}

	/**
	 * 
	 * @param str
	 * @return md5 string
	 */
	public static final String md5(final String str) {
		try {

			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(str.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @return the delegate
	 */
	public static PeSDKDelegate getDelegate() {
		return delegate;
	}

	public static void setDelegate(PeSDKDelegate delegate) {
		PeClient.delegate = delegate;
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context _context) {
		context = _context;
	}
}
