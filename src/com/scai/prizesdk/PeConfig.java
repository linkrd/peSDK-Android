/*
 * Copyright ©2013 SCA interactive
 * 
 * @author Thieu Huynh
 */
package com.scai.prizesdk;

import android.util.Log;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.scai.prizesdk.PeConstant.peErrorCode;

/**
 * PeSDK Configuration Class
 * 
 *
 */
public class PeConfig {
	
	private static final String TAG = PeConfig.class.getCanonicalName();
	private static PeConfig instance = null;
    
    private static Map<String,String> parameterMap;
    private static JSONObject configuration;
       
    protected PeConfig() {
        // Exists only to defeat instantiation.
     }
    
    public static PeConfig getInstance(Map<String,String> configMap) {
    	if(instance == null) {
    		instance = new PeConfig();
    		try {
				init(configMap);
			} catch (PeException e) {
				Log.i("peSDK PeConfig getInstance", "Exception results: " + e.getLocalizedMessage());
			}
    	}
    	return instance;
    }
    
    /**
     * init  
     * 
     * @param params
     * @throws PeException
     */
    private static void init(Map<String,String> params) throws PeException {

    	if (params.get("client") != null && params.get("promo") != null && params.get("contest_adminID") != null && params.get("authkey") != null) {
    		PeClient.getInstance(params);        
        } else {
        	throw new PeException("missing_required_config", peErrorCode.CONFIG_ERROR);
        }
    }
    
    
    /**
     * 
     * 
     * @return 	<code>true</code> if instant win contest
     */
    public static Boolean isInstantWin(){
        try {
			if (configuration.getString("game_type").equals("instantwin")) {
			    return true;
			} else {
				return false;
			}
		} catch (JSONException e) {
			return false;
		}
        
    }

    /**
     * 
     * 
     * @return <code>true</code> if sweeps contest
     */
    public static Boolean isSweeps() {
    	try {
			if (configuration.getString("game_type").equals("sweeps")) {
			    return true;
			} else {
				return false;
			}
		} catch (JSONException e) {
			return false;
		}
      
    }

    /**
     * 
     * @return	<code>true</code> if contest is open
     */
    public static Boolean isOpen() {
        try {
			if (configuration.getBoolean("is_open")) {
			    return true;
			}
		} catch (JSONException e) {
			return false;
		}
        return false;
    }

    /**
     * 
     * 
     * @return entry period information from getConfig
     */
    public static JSONObject getEntryPeriod() {

    	JSONObject playPeriodJSON = new JSONObject();
    	
    	try {
			playPeriodJSON.put("open_unixtime", configuration.getString("open_unixtime"));
			playPeriodJSON.put("close_unixtime", configuration.getString("close_unixtime"));
	    	playPeriodJSON.put("is_open", configuration.getString("is_open"));
	    	playPeriodJSON.put("open_date", configuration.getString("open_date"));
	    	playPeriodJSON.put("close_date", configuration.getString("close_date"));
	    	playPeriodJSON.put("time_zone", configuration.getString("time_zone"));
	    	return playPeriodJSON;
		} catch (JSONException e) {
			return null;
		}
    }
    
    /**
     * 
     * 
     * @return prize information from getConfig
     */
    public static JSONArray getPrizingInfo() {
        if (PeConfig.isInstantWin()) {
            try {
				return (JSONArray) configuration.get("prizing");
			} catch (JSONException e) {
				return null;
			}
        } 
        return null;
    }
    
    /**
     * 
     * @return profile fields
     */
    public static JSONArray getProfileFields() {
        try {
			return (JSONArray) configuration.get("profile_fields");
		} catch (JSONException e) {
			return null;
		}
    }
    
    /**
     * 
     * @return count from getConfig call
     */
    public static Object  getCount() {
        try {
        	JSONObject jsonObject = (JSONObject) configuration.get("count");
			return jsonObject;
		} catch (JSONException e) {
			return null;
		}
    }
    
    /**
     * 
     * @return array of required fields
     */
    public static JSONArray  getRequiredFields() {
    	JSONArray resultArray = new JSONArray();
        try {
        	JSONArray jsonArray = (JSONArray) configuration.get("profile_fields");
        	for (int i = 0; i < jsonArray.length(); ++i) {
        	    JSONObject obj = jsonArray.getJSONObject(i);
        	    if (obj.getInt("is_required") == 1 && !obj.getString("name").equals("auth") ) {
        	    	resultArray.put(obj);
        	    }   
        	}
		} catch (JSONException e) {
			Log.i(TAG, "JSONException: " + e);
			return null;
		} catch (Exception e) {
			Log.e(TAG, "Exception: " + e);
			return null;
		}
        return resultArray;
    }
   
    /**
     * 
     * @return array of all fields
     */
    public static JSONArray getAllFields() {
    	JSONArray resultArray = new JSONArray();
        try {
        	JSONArray jsonArray = (JSONArray) configuration.get("profile_fields");
        	for (int i = 0; i < jsonArray.length(); ++i) {
        	    JSONObject obj = jsonArray.getJSONObject(i);
        	    if (!obj.getString("name").equals("auth") ) {
        	    	resultArray.put(obj);
        	    }   
        	}
		} catch (JSONException e) {
			return null;
		}
        return resultArray;
    }
    
  /**
   * 
   * @return string of required field names
   */
    public static String getRequiredFieldNames() {
    	StringBuilder sb = new StringBuilder();
        try {
        	JSONArray jsonArray = PeConfig.getRequiredFields();
 
        	if (jsonArray.length() > 0) {
        		
        		JSONObject obj = jsonArray.getJSONObject(0);
        		if (obj.getInt("is_required") == 1 && !obj.getString("name").equals("auth") ) {
        	    	sb.append(obj.getString("name"));
        	    }  
        		
        		for (int i = 1; i < jsonArray.length(); ++i) {
        			JSONObject obj1 = jsonArray.getJSONObject(i);
        			if (obj1.getInt("is_required") == 1 && !obj1.getString("name").equals("auth") ) {
            	    	sb.append(",");
            	    	sb.append(obj1.getString("name"));
            	    }  
        		}
        		sb.toString();
        	}

		} catch (JSONException e) {
			return null;
		}
        return sb.toString();
    }
   
    /**
     * 
     * @return string of all field names
     */
    public static String getAllFieldNames() {
    	StringBuilder sb = new StringBuilder();
        try {
        	JSONArray jsonArray = PeConfig.getAllFields();
 
        	if (jsonArray.length() > 0) {
        		
        		JSONObject obj = jsonArray.getJSONObject(0);
        		if (!obj.getString("name").equals("auth") ) {
        	    	sb.append(obj.getString("name"));
        	    }  
        		
        		for (int i = 1; i < jsonArray.length(); ++i) {
        			JSONObject obj1 = jsonArray.getJSONObject(i);
        	    	sb.append(",");
        	    	sb.append(obj1.getString("name"));
        		}
        		sb.toString();
        	}

		} catch (JSONException e) {
			return null;
		}
        return sb.toString();
    }

    /**
     * 
     * @return String of user auth field
     */
    public static String getAuthUserField() {
        try {
			return (String) configuration.get("user_auth_field");
		} catch (JSONException e) {
			return null;
		}
    }
    
    /**
     * 
     * @param params list of parameters to check if required fields are met
     * @return <code>true</code> if has all required fields
     */
    public static  Boolean hasRequiredFields(JSONObject params) {
    	JSONArray resultArray = new JSONArray();
    	resultArray = PeConfig.getRequiredFields();
    	
    	for (int i = 1; i < resultArray.length(); ++i) {
			JSONObject obj1;
			try {
				obj1 = resultArray.getJSONObject(i);
				String name = obj1.getString("name");	
				if (params.get(name) == null ) {
					return false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}	
	    }

        return true;
    }
    
    /**
     * 
     * @return getconfig action result in JSONObject
     */
    public static JSONObject getAll() {
        return configuration;
    }

   
	public static Map<String,String> getParameterMap() {
		return parameterMap;
	}

	public static void setParameterMap(Map<String,String> params) {
		PeConfig.parameterMap = params;
	}

	public static JSONObject getConfiguration() {
		return configuration;
	}

	public static void setConfiguration(JSONObject _configuration) {
		PeConfig.configuration = _configuration;
	}
}
