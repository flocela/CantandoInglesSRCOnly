package com.eebolf.CantandoIngles.workers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.eebolf.CantandoIngles.SongsCP;
import com.eebolf.CantandoIngles.utils.ez;

import java.io.File;

public class SongDeleter extends Worker {

  Context mContext;
  int     rowID;

  public SongDeleter (Context context, int rowID) {
    this.mContext = context;
    this.rowID = rowID;
  }

  @Override
  public void work() {
    Uri uriTableName = Uri.parse(SongsCP.CP_URI + SongsCP.song_table);
    ContentResolver resolver = mContext.getContentResolver();
    Cursor cursor = getCursorFromRowId(resolver, uriTableName, rowID);
    if (null != cursor && 0 < cursor.getCount()) {
      // Deleting all rows and files associated with newWorkTitle.
      cursor.moveToFirst();
      String title = cursor.getString(cursor.getColumnIndex(SongsCP.new_work_title));
      String musicFileName = cursor.getString(cursor.getColumnIndex(SongsCP.music_file));
      String musicFileNameWithoutExtension = ez.getNameOnly(musicFileName);
      cursor.close();

      File internalStorageDir = mContext.getDir("songs", Context.MODE_PRIVATE);
      if (internalStorageDir != null && internalStorageDir.isDirectory()) {
        File[] listOfFiles = internalStorageDir.listFiles();
        for (int ii = 0; ii < listOfFiles.length; ii++) {
          String curr = listOfFiles[ii].getName();
          if (ez.getNameOnly(curr).equals(musicFileNameWithoutExtension)) {
            listOfFiles[ii].delete();
          }
        }
      }
      resolver.delete(uriTableName, SongsCP.new_work_title + "='" +
        title + "'", null);
    }
  }

  private Cursor getCursorFromRowId(ContentResolver resolver, Uri tableName, int rowID) {
    Cursor cursor1 = resolver.query(tableName,
                                    new String[]{SongsCP.new_work_title,
                                                 SongsCP.music_file},
                                     ez.where(BaseColumns._ID, rowID),
                        null,
                           null);
    return cursor1;
  }
}
