package com.scai.sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.scai.prizesdk.PeException;
import com.scai.prizesdk.PeSDK;
import com.scai.prizesdk.PeSDKDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class HomeFragment extends Fragment implements PeSDKDelegate, OnClickListener {
	private static final String TAG = HomeFragment.class.getCanonicalName();
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private static View view;
	EditText username_txt;
	private PeSDK prizeSDK;

	public HomeFragment(){
		
	}
		
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getArguments();
		Log.i(TAG, "onCreate bundle:" +bundle);
		if (bundle != null) {
			//prizeSDK = bundle.getParcelable("peSDK");
			//prizeSDK.delegate = this;	
		}
		MainActivity activity = (MainActivity) getActivity();
		prizeSDK = activity.getPeSDK();
		prizeSDK.delegate = this;
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
		
		try {
			view = inflater.inflate(R.layout.home_fragment, container, false);
			username_txt =  (EditText) view.findViewById(R.id.username_text);
			Button b = (Button) view.findViewById(R.id.submit_button);
	        b.setOnClickListener(this);
	        
			JSONArray profile =  prizeSDK.getUserProfile();
			JSONObject usernameObj = null;
			try {
				usernameObj = profile.getJSONObject(1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			Log.i(TAG, "JSONObject:"+usernameObj);
			username_txt.setText(usernameObj.getString("value"));
			
		} catch (InflateException e) {
			Log.i(TAG, "InflateException:" + e);
		} catch (JSONException e) {
			Log.i(TAG, "JSONException:" + e);
		}

		return view;
	}

	@Override
	public void actionComplete(String action, JSONObject result) {
		Log.i(TAG, "actionComplete:" + result);
	}

	@Override
	public void actionCompletedWithError(String action, PeException e) {
		Log.i(TAG, "actionCompletedWithError:" + e);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
    	alertDialogBuilder.setTitle("Error");
		alertDialogBuilder
				.setMessage(e.getNLSMessage())
				.setCancelable(false)
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});


		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}


	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.submit_button:
			try {
				if(!username_txt.getText().equals("")){
					JSONObject obj = new JSONObject();
					
						obj.put("username", username_txt.getText());
					
					if (prizeSDK.authenticateOnServer(obj)) {
						Log.i(TAG, "authenticateOnServer");
						Log.i(TAG, "getCurrentProfileParams:"+prizeSDK.getCurrentProfileParams());
					} else {
						Log.i(TAG, "not authenticateOnServer");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		
			break;
		}

	}
		
}
