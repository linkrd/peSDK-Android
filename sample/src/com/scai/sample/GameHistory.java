package com.scai.sample;

import org.json.JSONObject;

import android.support.v4.app.Fragment;

public class GameHistory extends Fragment {
	
	String gameID;
	String date;
	JSONObject object;
	
	public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    public JSONObject getJsonObj() {
        return object;
    }

    public void setJsonObj(JSONObject obj) {
        this.object = obj;
    }
}
