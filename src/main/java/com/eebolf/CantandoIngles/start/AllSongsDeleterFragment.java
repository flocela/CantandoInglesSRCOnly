package com.eebolf.CantandoIngles.start;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

import com.eebolf.CantandoIngles.SongsCP;
import com.eebolf.CantandoIngles.utils.ez;

import java.io.File;

/**
 * Requires SongsCP row id for the song to be deleted. SongsCP.;
 **/

public class AllSongsDeleterFragment extends Fragment  {
  private boolean mReady = false;
  private boolean mQuitting = false;
  private Uri uriTableName;
  private Integer rowId = null;
  private File internalStorageDir = null;
      
  public static AllSongsDeleterFragment newInstance (Bundle startingBundle) {
    AllSongsDeleterFragment songDeleterFragment = new AllSongsDeleterFragment();
    songDeleterFragment.setArguments(startingBundle);
     return songDeleterFragment;
  }
  
  final Thread mThread = new Thread() {   
    private ContentResolver tResolver;
    
    @Override
    public void run () {      
      synchronized(this) {
        while (!mReady) {                 
          if (mQuitting) 
            return;
          try {
            wait();
          } catch (InterruptedException e) {}
        }
        uriTableName = Uri.parse(SongsCP.CP_URI+ SongsCP.song_table);
        tResolver = getActivity().getContentResolver();
        Cursor cursorFromRowId = getCursorFromRowId();
        
        if (null != cursorFromRowId && 0 < cursorFromRowId.getCount()) {
          cursorFromRowId.moveToFirst();
          String musicFileName = cursorFromRowId.getString(cursorFromRowId.getColumnIndex(SongsCP.music_file));
          String lyricsFileName = cursorFromRowId.getString(cursorFromRowId.getColumnIndex(SongsCP.lyrics_file));
          String zipFilename = null;
          if (null != lyricsFileName) {
            zipFilename = lyricsFileName.replace(".txt", ".zip");
          }
           
          cursorFromRowId.close();
          Cursor cursorFromMusicFile = getCursorFromMusicFileName(musicFileName);
          
          if (null != cursorFromMusicFile && 1 >= cursorFromMusicFile.getCount()) {
            cursorFromMusicFile.close();
            internalStorageDir = AllSongsDeleterFragment.this.getActivity().getDir("songs", Context.MODE_PRIVATE);
            if (null != musicFileName) {
              deleteFile (musicFileName);
            }
            if (null != lyricsFileName) {
              deleteFile (lyricsFileName);
            }
            if (null != zipFilename) {
              deleteFile (zipFilename);
            }
          }          
          Uri uriWithRow = Uri.withAppendedPath(uriTableName, ""+ rowId);
          tResolver.delete(uriWithRow, null, null);              
        }
        else {
          return;
        }
      }
    }
    private Cursor getCursorFromRowId () {      
      Cursor cursor1 = tResolver.query(uriTableName, 
                                       new String [] {SongsCP.lyrics_file,
                                       SongsCP.music_file},
                                       ez.where(BaseColumns._ID, rowId ), 
                                       null,
                                       null);
      return cursor1;
    }
    
    private Cursor getCursorFromMusicFileName (String musicFileName) {
      Cursor cursor2 = tResolver.query(uriTableName, 
          new String [] {BaseColumns._ID},
          ez.where(SongsCP.music_file, musicFileName), 
          null,
          null);
      return cursor2;
    }
    
    private void deleteFile (String fileName) {      
      String fullfileName = internalStorageDir.getAbsolutePath() + "/" + fileName;
      File file = new File(fullfileName);
      if (file.exists()) {
        file.delete();
      }
    }
  };
  
   @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setRetainInstance(true);         
         Bundle args = (null != savedInstanceState)? savedInstanceState : getArguments();         
         if (null != args) {
           this.rowId = args.getInt(BaseColumns._ID);
         }  
         mThread.start();
     }
   
   @Override
     public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);
         synchronized (mThread) {
             mReady = true;
             mThread.notify();
         }
     }
     
   @Override
     public void onDestroy() {
         synchronized (mThread) {
             mReady = false;
             mQuitting = true;
             mThread.notify();            
         }      
         super.onDestroy();
         
     }
   
   @Override
     public void onDetach() {
         synchronized (mThread) {
             mReady = false;
             mThread.notify();
         }
         super.onDetach();
     }
}
