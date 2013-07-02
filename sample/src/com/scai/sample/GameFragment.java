package com.scai.sample;

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
import android.widget.TextView;

public class GameFragment extends Fragment implements PeSDKDelegate, OnClickListener{
	private static final String TAG = GameFragment.class.getCanonicalName();
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static View view;
	PeSDK prizeSDK = null;
	
	public GameFragment() {
	
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getArguments();
				
		if (bundle != null) {
//			prizeSDK = bundle.getParcelable("peSDK");
//			prizeSDK.delegate = this;	
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
			view = inflater.inflate(R.layout.game_fragment, container, false);			
		} catch (InflateException e) {
			Log.i(TAG, "InflateException:" + e);
		} 
			
		return view;
	}

	@Override 
	public void onStart(){
		
		super.onStart();
		
		if (prizeSDK != null) {
			TextView textView = (TextView) view.findViewById(R.id.textView1);
			Button b = (Button) view.findViewById(R.id.startBtn);
	
			if (prizeSDK.canEnter(null)) {
				textView.setText("Can play");
			} else {
				textView.setText("Already played.");
				b.setVisibility(View.GONE);
			}
		}
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
        case R.id.startBtn:
        	JSONObject result =  prizeSDK.enterInstantWin(null);
        	Log.i(TAG, "Enter instant win" + result);

            break;
        }
		
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
	

}
