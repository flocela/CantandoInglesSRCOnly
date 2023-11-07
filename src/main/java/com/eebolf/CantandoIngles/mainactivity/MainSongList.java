package com.eebolf.CantandoIngles.mainactivity;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eebolf.CantandoIngles.SongsCP;
import com.eebolf.CantandoIngles.todowithlists.MainSongCursorAdapter;

public class MainSongList extends ListFragment 
implements LoaderManager.LoaderCallbacks<Cursor> {
  private boolean loadHasFinished = false;
  private MainSongCursorAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState); 
  }

  @Override
  public void onActivityCreated (Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    this.loadHasFinished = false;
    this.adapter = new MainSongCursorAdapter(getActivity(), null);

    setListAdapter(this.adapter);
    ListView lv = getListView();
    lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    lv.setCacheColorHint(Color.BLACK);
    getLoaderManager().initLoader(0, null, this); 

    lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) { 
        clearSelection();         
        int position =  arg0.getPositionForView(v);
        LinearLayout firstLinearLayout = (LinearLayout)v;
        TextView lyricsFileView = (TextView)(firstLinearLayout.getChildAt(0));
        TextView musicFileView = (TextView)(firstLinearLayout.getChildAt(1));
        MainSongList.this.getListView().setItemChecked(position, false);
        ((MainListFragmentListener)MainSongList.this.getActivity()).
        noticeListItemClick(
            (musicFileView).getText().toString(),
            (lyricsFileView).getText().toString());
      }
    });
  }   

  public void clearSelection () {
    MainSongList.this.getListView().clearChoices();
    MainSongList.this.getListView().requestLayout();
  }


  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    Uri uri = Uri.parse(SongsCP.CP_URI+SongsCP.song_table);
    String[] projection = new String[] {BaseColumns._ID, 
                                        SongsCP.new_work_title, 
                                        SongsCP.orig_artist, 
                                        SongsCP.orig_english_title, 
                                        SongsCP.lyrics_file, 
                                        SongsCP.music_file};
    String orderByClause = BaseColumns._ID +" ASC";
    return new CursorLoader(getActivity(), uri, projection, null, null, orderByClause);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    this.adapter.swapCursor(data);
    this.loadHasFinished = true;
    ((MainListFragmentListener)MainSongList.this.getActivity()).noticeLoadFinished();
  }

  @Override
  public void onLoaderReset(Loader<Cursor> arg0) {
    this.adapter.swapCursor(null);
  }

  public boolean isLoadHasFinished () {
    return loadHasFinished;
  }
}
