package com.eebolf.CantandoIngles.todowithlists;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.ListView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.SongsCP;
import com.eebolf.CantandoIngles.dialogs.ConfirmationDialog;
import com.eebolf.CantandoIngles.dialogs.DeleterSongInfoDialog;
import com.eebolf.CantandoIngles.listeners.DeleterItemListener;
import com.eebolf.CantandoIngles.listeners.DialogDoneListener;
import com.eebolf.CantandoIngles.start.DeleteAllSongsTask;
import com.eebolf.CantandoIngles.start.SongDeleterFragment;
import com.eebolf.CantandoIngles.utils.Map;

public class DeleterSongsListActivity extends ListActivity
                                   implements LoaderManager.LoaderCallbacks<Cursor>, 
                                       DialogDoneListener,
                                       DeleterItemListener {
	private DeleterSongCursorAdapter adapter;		
	private static final String SONG_DELETER_INFO_TAG = "song_deleter_info_tag";
	private static final String SONG_DELETE_CONFIRMATION_TAG = "song_delete_confirmation_tag";
	private static final String SONG_DELETER_TAG = "song_deleter_tag";
	
	@Override
  protected void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);  
      setContentView(R.layout.deleter_song_list);
      
      this.adapter = new DeleterSongCursorAdapter(this, null);
      setListAdapter(this.adapter);      
      ListView lv = getListView();
			lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		  getLoaderManager().initLoader(0, null, this);
	}	
	
	@Override
	public void notifyDialogDone (int requestCode, 
			                          boolean isCancelled, 
			                          Bundle returnedBundle) {
	  switch (requestCode) {
	    case Map.DIALOG_CONFIRM_DELETE_SONG: {
	      if (!isCancelled) {
	        Bundle bundle = new Bundle();
	        bundle.putInt(BaseColumns._ID, returnedBundle.getInt(BaseColumns._ID));
	        SongDeleterFragment songDeleterFragment = SongDeleterFragment.newInstance(returnedBundle);
	        addFragmentRemovePrevious(songDeleterFragment, SONG_DELETER_TAG);
	      }
        break;
	    } 
	  }
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    Uri uri = Uri.parse(SongsCP.CP_URI+SongsCP.song_table);
    String[] projection = new String[] {BaseColumns._ID, 
                                        SongsCP.new_work_title, 
                                        SongsCP.orig_artist, 
                                        SongsCP.orig_english_title,
                                        SongsCP.zip_size};
    String orderByClause = BaseColumns._ID +" ASC";
    return new CursorLoader(this, uri, projection, null, null, orderByClause);
  }

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		this.adapter.swapCursor(data);
	}

  @Override
  public void onLoaderReset(Loader<Cursor> arg0) {
    this.adapter.swapCursor(null);
  }
	
  @Override
  public boolean noticeItemInfoClick(String newWorktTitle, String englishTitle, String origArtist, String zipSize) {
    Bundle bundle = new Bundle();
    bundle.putString(SongsCP.new_work_title, newWorktTitle);
    bundle.putString(SongsCP.orig_english_title, englishTitle);
    bundle.putString(SongsCP.orig_artist, origArtist);
    bundle.putString(SongsCP.zip_size, zipSize);
    DeleterSongInfoDialog songDeleterInfo = DeleterSongInfoDialog.newInstance(bundle);
    addFragmentRemovePrevious(songDeleterInfo, SONG_DELETER_INFO_TAG);
    return false;
  }

  @Override
  public boolean noticeItemDeleteClick(Integer id, String newWorkTitle, String englishTitle, String origArtist) {
    Bundle bundle = new Bundle();
    bundle.putString(Map.PROMPT, "Aceptas borrar " + newWorkTitle + ", " + englishTitle + " de " + origArtist+"?");
    bundle.putInt(BaseColumns._ID, id);    
    DialogFragment songDeleteConfirmationFragment = ConfirmationDialog.newInstance(bundle);
    showDialogFragment(songDeleteConfirmationFragment, SONG_DELETE_CONFIRMATION_TAG, Map.DIALOG_CONFIRM_DELETE_SONG);
    return false;
  }
  
  private void showDialogFragment(DialogFragment fragment, String tag, int requestCode) {
    FragmentManager fm = this.getFragmentManager();
    Fragment foundFragment = fm.findFragmentByTag(tag);
    if (null == foundFragment) {
      FragmentTransaction ft = getFragmentManager().beginTransaction();
      fragment.setTargetFragment(null, requestCode);
      fragment.show(ft, tag);
    }
  }
  
  private void addFragmentRemovePrevious(Fragment fragment, String tag) {
    FragmentManager fm = this.getFragmentManager();
    Fragment foundFragment = fm.findFragmentByTag(tag);
    if (null != foundFragment) {
      fm.beginTransaction().remove(foundFragment).commit();
    }
    fm.beginTransaction().add(fragment, tag).commit();
  }

  /**
   * only added for testing purposes.
   */
  public void clearSongs () {
    new DeleteAllSongsTask(this.getContentResolver(), this.getDir("songs", Context.MODE_PRIVATE).getAbsolutePath()).execute();
  }
}
