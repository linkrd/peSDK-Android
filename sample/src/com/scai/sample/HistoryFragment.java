package com.scai.sample;

import java.util.ArrayList;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryFragment extends Fragment implements PeSDKDelegate {
	private static final String TAG = HistoryFragment.class.getCanonicalName();
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */

	private static View view;
	PeSDK prizeSDK = null;
	private ListView listView ;
	
	public HistoryFragment() {
	
	}
	
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getArguments();
		
		if (bundle != null) {
			prizeSDK = bundle.getParcelable("peSDK");
		}
		MainActivity activity = (MainActivity) getActivity();
		prizeSDK = activity.getPeSDK();
		prizeSDK.delegate = this;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		GameHistory game;
		
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
				
		try {
			
			view = inflater.inflate(R.layout.history_fragment, container, false);
			listView =  (ListView) view.findViewById(R.id.listview);
			
			JSONArray array = prizeSDK.getUserHistory(null);

			ArrayList<GameHistory> gameHistorys = new ArrayList<GameHistory>();
							
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				game = new GameHistory();
				game.setGameID("GameID:" + obj.getString("gameID"));
				game.setDate("Date:" + obj.getString("date_issued_short"));
				game.setJsonObj(obj);
				gameHistorys.add(game);
			}

			HistoryListAdapter adapter = new HistoryListAdapter(view.getContext(), gameHistorys);
	
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			
			listView.setOnItemClickListener(new OnItemClickListener() {
				  @Override
				  public void onItemClick(AdapterView<?> parent, View view,
				    int position, long id) {
					
					GameHistory game = (GameHistory) parent.getItemAtPosition(position);
					Log.i(TAG, "Game History: " + game.getJsonObj().toString() );
				    Toast.makeText(view.getContext().getApplicationContext(),
				      "Click ListItem Number " + position, Toast.LENGTH_LONG)
				      .show();
				  }
				}); 
		} catch (JSONException e) {
			Log.i(TAG, "JSONException:" + e);
		} catch (InflateException e) {
			Log.i(TAG, "InflateException:" + e);
		} catch (Exception e) {
			Log.i(TAG, "Exception:" + e);
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
}
