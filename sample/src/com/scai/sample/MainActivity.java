package com.scai.sample;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.scai.prizesdk.PeException;
import com.scai.prizesdk.PeSDK;
import com.scai.prizesdk.PeSDKDelegate;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends FragmentActivity implements PeSDKDelegate, ActionBar.TabListener {

	static final String TAG = MainActivity.class.getCanonicalName();
	
	PeSDK prizeSDK;
		
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		String unique_ID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		
		Map<String,String> configMap = new HashMap<String, String>();
        configMap.put("contest_adminID", unique_ID);
        configMap.put("client", "demosdk");
        configMap.put("promo", "instant");
        configMap.put("authkey", "DEMO-SDK1-1234-5678");
        
        try {  	
        	prizeSDK = new PeSDK(this, configMap, getApplicationContext());
        } catch (PeException e) {
        	Log.e(TAG, "Caught PeException:" + e.getNLSMessage() + "  "+ e + " errorCode:"+ e.getNLSErrorCode());
        	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        	alertDialogBuilder.setTitle("Configuration Error");
			// set dialog message
			alertDialogBuilder
					.setMessage(e.getNLSMessage())
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									MainActivity.this.finish();
								}
							});


			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();	
        } 
        
		
	}

	public void setFragments() {
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						Log.i(TAG, "onPageSelected:" + position);
						if(prizeSDK.isAuthenticated) {
							actionBar.setSelectedNavigationItem(position);
						} else {
							actionBar.setSelectedNavigationItem(0);
						}
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Log.i(TAG, "position:" + position);
			Fragment fragment = null;
			switch (position) {
				case 0:
					fragment = new HomeFragment();
					break;
				case 1:
					fragment = new GameFragment();
					break;
				case 2:
					fragment = new HistoryFragment();
					break;
			} 
			
			Bundle args = new Bundle();
			// pass to fragments
			//args.putParcelable("peSDK", prizeSDK);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public void configComplete(){
		
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("username", "demo");
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		        		
		if (prizeSDK.authenticateOnServer(jsonParams)) {
			Log.i(TAG, "****************authenticateOnServer");
		} else {
			Log.i(TAG, "****************not authenticateOnServer");
		}
		
		
		JSONObject updateParams = new JSONObject();
		try {
			updateParams.put("city", "calgary");
			updateParams.put("answer1", "test answer 1");
			updateParams.put("state", "AB");
			updateParams.put("address", "1032 60 Ave.");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		prizeSDK.setUserProfile(updateParams, null);
				
		JSONArray profile =  prizeSDK.getUserProfile();
		JSONObject usernameObj = null;
		try {
			usernameObj = profile.getJSONObject(1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Log.i(TAG, "JSONObject:"+usernameObj);
		
		Log.i(TAG, "getCurrentProfileParams:"+prizeSDK.getCurrentProfileParams());
		setFragments();
    }
	@Override
	public void actionComplete(String action, JSONObject result) {
		if (action.equals("getconfig")) {
			configComplete();
		} else {
			Log.i(TAG, "actionComplete unknown action:" + action);
		}
	}

	@Override
	public void actionCompletedWithError(String action, PeException e) {
		Log.i(TAG, "actionCompletedWithError:" + e);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
	
	public PeSDK getPeSDK() {
		return prizeSDK;
	}

}
