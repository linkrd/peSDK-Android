package com.scai.sample;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HistoryList extends ListFragment {
	private static final String TAG = HistoryList.class.getCanonicalName();
		
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        //setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countries));
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1,countries);
		//setListAdapter(adapter);
		
		return super.onCreateView(inflater, container, savedInstanceState);		
	}
	
	 @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Notify the parent activity of selected item
		// mCallback.onArticleSelected(position);
		Log.i(TAG, "onListItemClick:" + position);
		// Set the item as checked to be highlighted when in two-pane layout
		getListView().setItemChecked(position, true);
	}
	
}
