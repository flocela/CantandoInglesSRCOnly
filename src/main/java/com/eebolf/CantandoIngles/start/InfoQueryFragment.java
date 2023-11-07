package com.eebolf.CantandoIngles.start;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;

import com.eebolf.CantandoIngles.SongsCP;
import com.eebolf.CantandoIngles.listeners.IconQueryDoneListener;
import com.eebolf.CantandoIngles.utils.Map;
import com.eebolf.CantandoIngles.utils.ez;

public class InfoQueryFragment extends Fragment {
  
  int cpRow = 0;
  
  public static InfoQueryFragment newInstance(Bundle bundle) {
    InfoQueryFragment iconQueryFragment = new InfoQueryFragment();
    iconQueryFragment.setArguments(bundle);    
    return iconQueryFragment;
  }
  
  boolean mReady = false;
  boolean mDestroyed = false;
  
  final Thread mThread = new Thread() {
  
    @Override
    public void run() {  
      ContentResolver cR = null;
      synchronized (this) {
        while (!mReady) { 
          try {
            wait();
          }catch (InterruptedException e) {}
        }
        cR = InfoQueryFragment.this.getActivity().getContentResolver();
      }
      
      Cursor cursor = 
          cR.query(Uri.parse(SongsCP.CP_URI),
          new String[] { SongsCP.link_to_new_work_license,
                         SongsCP.name_of_new_work_license,
                         SongsCP.new_work_title,
                         SongsCP.orig_artist,
                         SongsCP.orig_downloaded_at_link,
                         SongsCP.orig_downloaded_at_name,
                         SongsCP.orig_english_changes_made,
                         SongsCP.orig_english_title,
                         SongsCP.orig_english_uses_sample_from_1,
                         SongsCP.orig_license_link,
                         SongsCP.orig_license_name,
                         SongsCP.orig_spanish_changes_made,
                         SongsCP.orig_spanish_uses_sample_from_1,
                         SongsCP.orig_uses_sample_from_link_1,
                         SongsCP.orig_uses_sample_from_link_name_1,
                         SongsCP.song_type, 
                         SongsCP.zip_size},
          ez.where(BaseColumns._ID, cpRow), 
          null, 
          null);
      Bundle bundle = new Bundle();
      if (null != cursor && 0 < cursor.getCount()) {
        cursor.moveToFirst();
      
        int linkToNewWorkLicenseColumnIdx = cursor.getColumnIndex(SongsCP.link_to_new_work_license);
        int nameOfNewWorkLicenseColumnIdx = cursor.getColumnIndex(SongsCP.name_of_new_work_license);
        int newWorkTitleColumnIdx = cursor.getColumnIndex(SongsCP.new_work_title);
        int origArtistColumnIdx = cursor.getColumnIndex(SongsCP.orig_artist);
        int origDownloadedAtLinkColumnIdx = cursor.getColumnIndex(SongsCP.orig_downloaded_at_link);
        int origDownloadedAtNameColumnIdx = cursor.getColumnIndex(SongsCP.orig_downloaded_at_name);
        int origEnglishChangesMadeColumnIdx = cursor.getColumnIndex(SongsCP.orig_english_changes_made);
        int origEnglishTitleColumnIdx = cursor.getColumnIndex(SongsCP.orig_english_title);
        int origEnglishUsesSampleFrom1ColumnIdx = cursor.getColumnIndex(SongsCP.orig_english_uses_sample_from_1);
        int origLicenseLinkColumnIdx = cursor.getColumnIndex(SongsCP.orig_license_link);
        int origLicenseNameColumnIdx = cursor.getColumnIndex(SongsCP.orig_license_name);
        int origSpanishChangesMadeColumnIdx = cursor.getColumnIndex(SongsCP.orig_spanish_changes_made);
        int origSpanishUsesSampleFrom1ColumnIdx = cursor.getColumnIndex(SongsCP.orig_spanish_uses_sample_from_1);
        int origUsesSampleFromLink1ColumnIdx = cursor.getColumnIndex(SongsCP.orig_uses_sample_from_link_1);
        int origUsesSampleFromLinkName1ColumnIdx = cursor.getColumnIndex(SongsCP.orig_uses_sample_from_link_name_1);
        int zipSizeColumnIdx = cursor.getColumnIndex(SongsCP.zip_size);
        
        bundle.putString(SongsCP.link_to_new_work_license, cursor.getString(linkToNewWorkLicenseColumnIdx));
        bundle.putString(SongsCP.name_of_new_work_license, cursor.getString(nameOfNewWorkLicenseColumnIdx));
        bundle.putString(SongsCP.new_work_title, cursor.getString(newWorkTitleColumnIdx));
        bundle.putString(SongsCP.orig_artist, cursor.getString(origArtistColumnIdx));
        bundle.putString(SongsCP.orig_downloaded_at_link, cursor.getString(origDownloadedAtLinkColumnIdx));
        bundle.putString(SongsCP.orig_downloaded_at_name, cursor.getString(origDownloadedAtNameColumnIdx));
        bundle.putString(SongsCP.orig_english_changes_made, cursor.getString(origEnglishChangesMadeColumnIdx));
        bundle.putString(SongsCP.orig_english_title, cursor.getString(origEnglishTitleColumnIdx));
        bundle.putString(SongsCP.orig_english_uses_sample_from_1, cursor.getString(origEnglishUsesSampleFrom1ColumnIdx));
        bundle.putString(SongsCP.orig_license_link, cursor.getString(origLicenseLinkColumnIdx));
        bundle.putString(SongsCP.orig_license_name, cursor.getString(origLicenseNameColumnIdx));
        bundle.putString(SongsCP.orig_spanish_changes_made, cursor.getString(origSpanishChangesMadeColumnIdx));
        bundle.putString(SongsCP.orig_spanish_uses_sample_from_1, cursor.getString(origSpanishUsesSampleFrom1ColumnIdx));
        bundle.putString(SongsCP.orig_uses_sample_from_link_1, cursor.getString(origUsesSampleFromLink1ColumnIdx));
        bundle.putString(SongsCP.orig_uses_sample_from_link_name_1, cursor.getString(origUsesSampleFromLinkName1ColumnIdx));
        bundle.putString(SongsCP.zip_size, cursor.getString(zipSizeColumnIdx));        
      }
      cursor.close();
      synchronized (this) {
        while (!mReady) { 
          try {
            wait();
          }catch (InterruptedException e) {}
        }
        ((IconQueryDoneListener)(InfoQueryFragment.this.getActivity())).noticeIconQueryDoneAsync(bundle);
      }
    }
  };
 

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    mThread.start();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Bundle args = getArguments();
    this.cpRow = args.getInt(Map.QUERY_ID);
    synchronized (mThread) {
      mReady = true;
      mThread.notify();
    }
  }

  @Override
  public void onDetach() {
    synchronized (mThread) {
      mReady = false;
      mThread.notify();
    }
    super.onDetach();
  }
  
  @Override
  public void onDestroy() {    
    synchronized (mThread) {
      mDestroyed = true;
      mReady = false;
      mThread.notify();
    }
    super.onDestroy();
  }
}
