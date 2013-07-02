package com.scai.prizesdk;

import org.json.JSONObject;

/**
 * PeSDK Delegate
 * 
 *
 */
public interface PeSDKDelegate {
	void actionComplete(String action, JSONObject result);
	void actionCompletedWithError(String action, PeException e);

}
