/*
 * Copyright ©2013 SCA interactive
 * 
 * @author Thieu Huynh
 */
package com.scai.prizesdk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * PeSDK Promotions Engine
 * 
 * 
 * @author Thieu Huynh
 */
public class PeSDK {

	private static final String TAG = PeSDK.class.getCanonicalName();
	private Map<String, String> configMap;
	private JSONArray userProfile;
	private JSONObject params;
	public Boolean isAuthenticated = false;
	private FragmentActivity activity = null;
	public PeSDKDelegate delegate;
	

	public PeSDK(PeSDKDelegate delegate, Map<String, String> configMap, Context context)
			throws PeException {
		this.delegate = delegate;
		this.configMap = configMap;
		PeConfig.getInstance(this.configMap);
		PeClient.setDelegate(delegate);
		PeClient.setContext(context);
		PeClient.getConfig();
	}

	/**
	 * 
	 * @return <code>true</code> if contest is open
	 */
	public Boolean isOpen() {
		try {
			return PeConfig.isOpen();
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 
	 * @return JSONArray of required fields
	 */
	public JSONArray getRequierdFields() {

		return PeConfig.getRequiredFields();
	}

	/**
	 * 
	 * @return JSONArray of required fields
	 */
	public JSONArray getAllFields() {

		return PeConfig.getAllFields();
	}

	/**
	 * 
	 * @return JSONObject of Entry Period
	 */
	public JSONObject getEntryPeriod() {

		return PeConfig.getEntryPeriod();
	}

	/**
	 * 
	 * @return JSONObject of Entry Period
	 */
	public JSONArray getPrizingInfo() {

		return PeConfig.getPrizingInfo();
	}

	/**
	 * Performs authentication on the server side
	 * 
	 * @param params JSONObject containing user parameters, optional is already set
	 *            
	 * @return bool is authentication is successful
	 */

	public Boolean authenticateOnServer(JSONObject params) {

		if (params != null) {
			this.params = params;
		}
		String action = "auth_only";
		
		buildAndSend(action, this.params); 

		return this.isAuthenticated;
	}

	public JSONArray getCurrentProfileParams() {
		JSONArray userProfileArray = this.getUserProfile();
		JSONArray currentProfileArray = new JSONArray();
		
		for(int i = 0; i < userProfileArray.length(); i++){
		    JSONObject profile;
			try {
				profile = userProfileArray.getJSONObject(i);
				String name = profile.getString("name");
				if (!name.equals("auth")) {
					if (!profile.isNull("value")) {
						Log.i(TAG, "getCurrentProfileParams:" + profile.getString("value"));
						currentProfileArray.put(profile);
					}
				}

			} catch (JSONException e) {
				Log.i(TAG, "JSONException:" + e);
				e.printStackTrace();
			}		    
		}
		return currentProfileArray;
	}

	public Boolean canEnter(JSONObject params) {
		
		if (params != null) {
			this.params = params;
		}
		String action = "canplay";
		JSONObject jsonResponse = new JSONObject();
		
		jsonResponse = buildAndSend(action, this.params); 
		
		try {
			return jsonResponse.getBoolean("canplay");

		} catch (JSONException e) {
			delegate.actionCompletedWithError(action, new PeException("invalid_data_returned"));
		}

		return false;
	}

	/**
	 * 
	 * @param params optional parameter, this is already set
	 * @return String of next play or "now" if user can play
	 */
	public String nextPlay(JSONObject params) {
		
		String action = "canplay";
		JSONObject jsonResponse = new JSONObject();
		if (params != null) {
			this.params = params;
		}

		jsonResponse = buildAndSend(action, this.params); 

		JSONObject result = new JSONObject();

		try {
			result = jsonResponse.getJSONObject("result");
			if (result.getBoolean("canplay")) {
				return "now";
			} else {
				return result.getString("next_play");
			}

		} catch (JSONException e) {
			delegate.actionCompletedWithError(action, new PeException("invalid_data_returned", 
					PeConstant.peErrorCode.DATA_ERROR));
		}

		return null;
	}
	
	/**
	 * 
	 * @param params
	 * @return JSONObject of response data from sever on success or null
	 */
	public JSONObject enterSweeps(JSONObject params) {
		
		String action = "instantwin";
		JSONObject jsonResponse = new JSONObject();
		if (params != null) {
			this.params = params;
		}
		
		if (this.isOpen()) {
			if (PeConfig.isSweeps()) {

				jsonResponse = buildAndSend(action, params);

				try {
					if (jsonResponse.get("result") != null) {
						return this.gameData(jsonResponse.getJSONObject("game"), "sweeps");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			} else {
				delegate.actionCompletedWithError(action, new PeException(
						"sweeps_wrong_game_type",
						PeConstant.peErrorCode.DATA_ERROR));
			}
		} else {
			delegate.actionCompletedWithError(action, new PeException("invalid_data_returned", 
					PeConstant.peErrorCode.API_ERROR));
		}
		return jsonResponse;
	}
	
	
	/**
	 * 
	 * @param params
	 * @return JSONObject of response data from sever on success or null
	 */
	public JSONObject enterInstantWin(JSONObject params) {

		String action = "instantwin";
		JSONObject jsonResponse = new JSONObject();
		if (params != null) {
			this.params = params;
		}
				
		if (this.isOpen()) {
			if (PeConfig.isInstantWin()) {
				jsonResponse = buildAndSend(action, this.params);

				try {
					return this.gameData(jsonResponse.getJSONObject("game"), "instantwin");
				} catch (JSONException e) {
					return null;
				}
			} else {
				delegate.actionCompletedWithError(action, new PeException("instant_wrong_game_type", PeConstant.peErrorCode.DATA_ERROR));
			}
		} else {
			delegate.actionCompletedWithError(action, new PeException("invalid_data_returned", PeConstant.peErrorCode.API_ERROR));
		}
		return jsonResponse;
	}

	/**
	 * 
	 * @param params
	 * @return JSONArray of game history
	 */
	public JSONArray getUserHistory(JSONObject params) {
		
		String action = "gamehistory";
		JSONObject jsonResponse = new JSONObject();
		if (params != null) {
			this.params = params;
		}
		
		
		jsonResponse = buildAndSend(action, this.params);
				
		JSONArray history = new JSONArray();
		try {
			history = (JSONArray) jsonResponse.get("history");
			return this.gameHistoryData(history) ;
		} catch (JSONException e) {
			// history does not exist return null
			return null;
		}
	}
	/**
	 * 
	 * @return Map of configuration
	 */
	public Map<String, String> getConfigMap() {
		return configMap;
	}

	/**
	 * 
	 * @param configMap
	 */
	public void setConfigMap(Map<String, String> configMap) {
		this.configMap = configMap;
	}

	public JSONArray getUserProfile() {
		return this.userProfile;
	}

	public void setUserProfile(JSONObject profile_fields, JSONObject params) {
		String action = "updateprofile";
		JSONObject jsonResponse = new JSONObject();
		JSONArray newProfileArray = new JSONArray();
		
		if (params != null) {
			this.params = params;
		}
		
		JSONObject updateParams = new JSONObject();
		updateParams = this.params;
		
		Iterator<?> keys = profile_fields.keys();

        while( keys.hasNext() ){
        	String key = (String)keys.next();
        	try {
				updateParams.put( key, (String) profile_fields.get(key));
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        		
		if (this.isOpen()) {
			List<String> updates = new ArrayList<String>(); 
			
			jsonResponse = buildAndSend(action, updateParams);
						
			JSONArray fieldsArray = new JSONArray();
			
			try {
				fieldsArray = jsonResponse.getJSONArray("fields");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			for(int i = 0; i < fieldsArray.length(); i++){
				JSONObject field;
				try {
					field = (JSONObject) fieldsArray.get(i);
					if (field.getString("nls_string").equals("updated_successfully")) {
						updates.add(field.getString("field"));
					} 
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
			JSONArray userProfileArray = this.getUserProfile();
				
			for(int i = 0; i < userProfileArray.length(); i++){
			    JSONObject profile;
				try {
					profile = userProfileArray.getJSONObject(i);
					String name = profile.getString("name");
					profile.put("updated", false);
					if(PeSDK.in_array(updates, name)) {
						profile.put("updated", true);
					}

				    newProfileArray.put(profile);
				} catch (JSONException e) {
					e.printStackTrace();
				}		    
			}
		} else {
			delegate.actionCompletedWithError(action, new PeException("contest_not_open", PeConstant.peErrorCode.API_ERROR));
		}
		
		this.userProfile = newProfileArray;
	}

	public JSONObject getParameters() {
		return this.params;
	}

	public void setParameters(JSONObject jsonParams) {
		this.params = jsonParams;
	}

	/**
	 * 
	 * @return <code>true</code> user is authenticated
	 */
	public Boolean isAuthenticated() {
		return this.isAuthenticated;
	}

	/**
	 * 
	 * @param isAuthenticated set variable
	 *            
	 */
	private void setIsAuthenticated(Boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	private void setProfileData(JSONObject profile_data) {
        JSONArray profileFieldsArray = PeConfig.getProfileFields();

        JSONArray user_profile = new JSONArray();
		
		for(int i = 0; i < profileFieldsArray.length(); i++){
		    JSONObject field;
			try {
				field = profileFieldsArray.getJSONObject(i);
				String name = field.getString("name");
			    if (!name.equals("auth")) {
			    	if (profile_data.get(name) != null) {
			    		field.put("value", profile_data.get(name));
			    	} else {
			    		field.put("value", null);
			    	}
				}
			    user_profile.put(field);
			} catch (JSONException e) {
				e.printStackTrace();
			}		    
		}
       
        this.userProfile = user_profile;
    }

	/**
	 * 
	 * @param action
	 * @param params
	 * @return JSONObject of response data from sever
	 */
	private JSONObject buildAndSend(String action, JSONObject params) {
		
		JSONObject jsonResponse = new JSONObject();
		if (PeConfig.hasRequiredFields(params) == true) {
			String userkey = PeConfig.getAuthUserField();
			String uservalue = null;
			try {
				uservalue = params.getString(userkey);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			jsonResponse = PeClient.doSyncAction(action, userkey, uservalue, params);
			JSONObject jsonAuth = new JSONObject();
			JSONObject jsonResult = new JSONObject();

			try {
				jsonAuth = jsonResponse.getJSONObject("auth");
				if (jsonAuth.getBoolean("success")) {
					this.setIsAuthenticated(true);
					this.setProfileData(jsonResponse.getJSONObject("user_profile"));
				} else {
					JSONArray errors = (JSONArray) jsonResult.get("errors");
					delegate.actionCompletedWithError(action, new PeException(errors.getString(0), PeConstant.peErrorCode.API_ERROR));
				}
				
				jsonResult = jsonResponse.getJSONObject("result");
				//Log.i(TAG, "buildAndSend jsonResult.getBoolean:" + jsonResult.getBoolean("success"));
				if (jsonResult.getBoolean("success")) {
					//Log.i(TAG, "buildAndSend:" + jsonResult);
					return jsonResult;
				} else {
					JSONArray errors = (JSONArray) jsonResult.get("errors");
					delegate.actionCompletedWithError(action, new PeException(errors.getString(0), PeConstant.peErrorCode.API_ERROR));
				}

			} catch (JSONException e) {
				Log.i(TAG, "JSONException:" + e);
				delegate.actionCompletedWithError(action, new PeException("invalid_data_returned", PeConstant.peErrorCode.API_ERROR));
			} catch (Exception e) {
				Log.i(TAG, "Exception:" + e);
				delegate.actionCompletedWithError(action, new PeException("invalid_data_returned", PeConstant.peErrorCode.API_ERROR));
			}

		} else {
			delegate.actionCompletedWithError(action, new PeException("missing_required_fields", PeConstant.peErrorCode.API_ERROR));
		}
		return jsonResponse;
	}
	
	
	private JSONArray gameHistoryData (JSONArray history_array) {
		
		JSONArray server_data = new JSONArray();
		String game_type;
		if (PeConfig.isSweeps()) {
			game_type = "sweeps"; 
		} else {
			game_type = "instant";
		}

		for(int i = 0; i < history_array.length(); i++){
			JSONObject game;
			try {
				game = (JSONObject) history_array.get(i);
				game.putOpt("date_issued_long", game.get("long_date"));
				game.putOpt("date_issued_short", game.get("short_date"));
				server_data.put(this.gameData(game, game_type));
			} catch (JSONException e) {

				e.printStackTrace();
			}
			
		}

        return server_data;

    }

	/**
	 * 
	 * @param server_data
	 * @param game_type string of game type can be "instant" or "sweeps"
	 * @return JSONObject of sever data
	 */
	private JSONObject gameData(JSONObject server_data, String game_type) {

        server_data.remove("long_date");
        server_data.remove("short_date");
        server_data.remove("comdata");
        server_data.remove("rng_draw");
        server_data.remove("max_win_level");
        server_data.remove("maxlevel");
        server_data.remove("min_win_level");
        server_data.remove("minlevel");
        server_data.remove("num_choose");
        server_data.remove("wingame");
        server_data.remove("date_issued");
        server_data.remove("pin");
        server_data.remove("pin1");
        server_data.remove("score");
        server_data.remove("numpicks");
        server_data.remove("winlevel");

        if (game_type.compareTo("sweeps") == 1) {
        	server_data.remove("winlevel");
        	server_data.remove("game_is_winner");
            try {
				if (server_data.get("result") != null) {
				    server_data.putOpt("result_text", "entered");
				    server_data.remove("result");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        if (game_type.compareTo("instant") == 1) {
            try {
				if (!server_data.getBoolean("game_is_winner")) {
				    if (server_data.get("result_text") == "" || server_data.get("result_text") == null) {
				    	server_data.putOpt("result_text", "loss");
				    }
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

        }

        return server_data;
    }

	public static boolean in_array(List<String> list, String findName) {
	    for(int i=0;i<list.size();i++) {
	        if(list.get(i).toString().equals(findName)) {
	            return true;
	        }
	    }
	    return false;
	}

	public FragmentActivity getActivity() {
		return activity;
	}

}
