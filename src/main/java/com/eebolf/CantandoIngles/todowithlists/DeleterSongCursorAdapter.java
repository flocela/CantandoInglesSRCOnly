package com.eebolf.CantandoIngles.todowithlists;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.SongsCP;

public class DeleterSongCursorAdapter extends CursorAdapter {
  private LayoutInflater layoutInflater;
  public DeleterSongCursorAdapter(Activity context, Cursor cursor) {
    super(context, cursor, 0);
    layoutInflater = LayoutInflater.from(context);
  }
  @Override
  public void bindView(View v, Context context, Cursor c) {
    
    String newWorkTitle = c.getString(c.getColumnIndexOrThrow(SongsCP.new_work_title));
    String englishTitle = c.getString(c.getColumnIndex(SongsCP.orig_english_title));
    String origArtist = c.getString(c.getColumnIndex(SongsCP.orig_artist));
    String fileSizes = c.getString(c.getColumnIndex(SongsCP.zip_size));
    if (!fileSizes.contains("MB"))
      fileSizes = fileSizes.concat(" MB");
    Integer rowId = c.getInt(c.getColumnIndex(BaseColumns._ID));
        
    TextView newWorkTitleView = (TextView)v.findViewById(R.id.new_work_title);
    TextView englishTitleView = (TextView)v.findViewById(R.id.english_title);
    TextView fileSizesView = (TextView)v.findViewById(R.id.file_sizes);
    
    newWorkTitleView.setText(newWorkTitle);
    englishTitleView.setText(englishTitle);
    fileSizesView.setText(fileSizes);

    v.setTag(R.id.CP_ROW_ID, rowId);
    v.setTag(R.id.CP_NEW_WORK_TITLE, newWorkTitle);
    v.setTag(R.id.CP_ORIG_ENGLISH_TITLE, englishTitle);
    v.setTag(R.id.CP_ORIG_ARTIST, origArtist);
    v.setClickable(true);
    v.setOnClickListener(new OnClickListener()
    {
        @Override
        public void onClick(View v) 
        {
          Object obj = v.getParent();
          ListView listView = (ListView)obj;
          DeleterSongsListActivity activity = (DeleterSongsListActivity)listView.getContext();
          activity.noticeItemDeleteClick((Integer)((v).getTag(R.id.CP_ROW_ID)),
                                        (String)((v).getTag(R.id.CP_NEW_WORK_TITLE)),
                                        (String)((v).getTag(R.id.CP_ORIG_ENGLISH_TITLE)),
                                        (String)((v).getTag(R.id.CP_ORIG_ARTIST)));
        }
    });
  }
  
  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    View v = layoutInflater.inflate(R.layout.delete_song_item, parent, false);
    return v;
  }
}
