package com.eebolf.CantandoIngles.todowithlists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.collectors.SongInfo;

import java.util.List;

public class SongInfoListAdapter extends ArrayAdapter<SongInfo> {

	private final LayoutInflater mInflater;

	public SongInfoListAdapter (Context context) {
		super(context, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(List<SongInfo> data) {
		clear();
		if (data != null) {
			addAll(data);
		}
	}

	@Override 
	public View getView(int position, View convertView, ViewGroup parent) {
    View view;

    if (convertView == null) {
        view = mInflater.inflate(R.layout.download_set_item, parent, false);
    } else {
        view = convertView;
    }

    SongInfo item = getItem(position);
		if (item != null) {
			TextView temp = ((TextView)view.findViewById(R.id.row_entry));
			TextView diffTemp = ((TextView)view.findViewById(R.id.difficulty));
			if (temp != null) {
				temp.setText(item.getNewWorkTitle());
			}
			if (diffTemp != null) {
				diffTemp.setText(item.getSongType());
			}
		}


    return view;
}


}
