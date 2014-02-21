package com.scai.sample;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryListAdapter extends BaseAdapter {
	static final String TAG = HistoryListAdapter.class.getCanonicalName();
	private Context context;
    private ArrayList<GameHistory> gameHistorys;
    
    
    public HistoryListAdapter(Context context, ArrayList<GameHistory> gameHistorys) {
        this.context = context;
        this.gameHistorys = gameHistorys;
        Log.i(TAG, "gameHistorys:" + this.gameHistorys.toString());
    }
	@Override
	public int getCount() {
		return gameHistorys.size();
	}

	@Override
	public Object getItem(int position) {
		return gameHistorys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row;

        if (convertView == null) {
        	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	row = (View) inflater.inflate(android.R.layout.simple_list_item_2, null);
        } else {
        	row = (View) convertView;
        }

        TextView v = (TextView) row.findViewById(android.R.id.text1);
        v.setText(gameHistorys.get(position).getGameID());
        v = (TextView) row.findViewById(android.R.id.text2);
        v.setText(gameHistorys.get(position).getDate());
       
        return row;
	}

}
