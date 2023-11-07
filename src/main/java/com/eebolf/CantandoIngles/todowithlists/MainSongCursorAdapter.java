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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.SongsCP;
import com.eebolf.CantandoIngles.mainactivity.MainActivity;

public class MainSongCursorAdapter extends CursorAdapter {
  private LayoutInflater layoutInflater;
  public MainSongCursorAdapter(Activity context, Cursor cursor) {
    super(context, cursor);
    layoutInflater = LayoutInflater.from(context);
  }
  @Override
  public void bindView(View v, Context context, Cursor c) {
    
    String newWorkTitle = c.getString(c.getColumnIndexOrThrow(SongsCP.new_work_title));
    String englishTitle = c.getString(c.getColumnIndexOrThrow(SongsCP.orig_english_title));
    String origArtist = c.getString(c.getColumnIndex(SongsCP.orig_artist));
    String lyricsFileName = c.getString(c.getColumnIndex(SongsCP.lyrics_file));
    String musicFileName = c.getString(c.getColumnIndex(SongsCP.music_file));
    // musicFileName = "amazing_grace_rich_tuttle.mp3"
    Integer rowId = c.getInt(c.getColumnIndex(BaseColumns._ID));
        
    
    TextView origArtistTextView = (TextView)v.findViewById(R.id.orig_artist);
    TextView lyricsFileTextView = (TextView)v.findViewById(R.id.lyrics_file);
    TextView musicFileTextView = (TextView)v.findViewById(R.id.music_file);
    TextView englishTitleTextView = (TextView)v.findViewById(R.id.english_title);  
    TextView newWorkTitleTextView = (TextView)v.findViewById(R.id.new_work_title);
    
    newWorkTitleTextView.setText(newWorkTitle);
    origArtistTextView.setText(origArtist);
    lyricsFileTextView.setText(lyricsFileName);
    musicFileTextView.setText(musicFileName);
    englishTitleTextView.setText(englishTitle);
    
    ImageView image= (ImageView)v.findViewById(R.id.item_icon);
    image.setTag(R.id.CP_ROW_ID, rowId);
    image.setClickable(true);
    image.setOnClickListener(new OnClickListener() 
    {
        @Override
        public void onClick(View v) 
        {
          Object obj = v.getParent().getParent().getParent();
          ListView listView = (ListView)obj;
          MainActivity activity = (MainActivity)listView.getContext(); 
          activity.noticeQueryIconClicked((Integer)(((ImageView)v).getTag(R.id.CP_ROW_ID)));
        }
    });
  }
  
  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    View v = layoutInflater.inflate(R.layout.song_list_item, parent, false);
    return v;
  }
}
