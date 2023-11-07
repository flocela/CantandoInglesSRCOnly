package com.eebolf.CantandoIngles;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import com.eebolf.CantandoIngles.start.UriDeterminer;
import com.eebolf.CantandoIngles.utils.ez;

public class SongsCP extends ContentProvider {
  public static final String AUTHORITY = "com.eebolf.CantandoIngles.SongsCP";
  private static final String database_name = "songs.db";
  public static final String CP_URI = "content://" + AUTHORITY+ "/";
  public static final String song_table = "song_table";
  
  public static final String music_file = "music_file";
  public static final String lyrics_file = "lyrics_file";
  public static final String link_to_new_work_license = "link_to_new_work_license";
  public static final String name_of_new_work_license = "name_of_new_work_license";
  public static final String new_work_title = "new_work_title";
  public static final String orig_artist = "orig_artist";
  public static final String orig_downloaded_at_link = "orig_downloaded_at_link";
  public static final String orig_downloaded_at_name = "orig_downloaded_at_name";
  public static final String orig_english_changes_made = "orig_english_changes_made";
  public static final String orig_english_title = "orig_english_title";
  public static final String orig_english_uses_sample_from_1 = "orig_english_uses_sample_from_1";
  public static final String orig_license_link = "orig_license_link";
  public static final String orig_license_name = "orig_license_name";
  public static final String orig_spanish_changes_made = "orig_spanish_changes_made";
  public static final String orig_spanish_uses_sample_from_1 = "orig_spanish_uses_sample_from_1";
  public static final String orig_uses_sample_from_link_1 = "orig_uses_sample_from_link_1";
  public static final String orig_uses_sample_from_link_name_1 = "orig_uses_sample_from_link_name_1";
  public static final String song_type = "song_type"; 
  public static final String zip_size = "zip_size";

  public static final String ALL = "all"; // used for deleting all rows.
  private SongsDatabaseHelper dbHelper;

  protected static final class SongsDatabaseHelper extends SQLiteOpenHelper {
    SongsDatabaseHelper(Context context) {
      super(context, database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table " +
          "song_table " +
          "(" +
          " _id integer primary key autoincrement," +
          " " + music_file                        + " " + "text,"+
          " " + lyrics_file                       + " " + "text,"+
          " " + link_to_new_work_license          + " " + "text,"+
          " " + name_of_new_work_license          + " " + "text,"+
          " " + new_work_title                    + " " + "text,"+
          " " + orig_artist                       + " " + "text,"+
          " " + orig_downloaded_at_link           + " " + "text,"+         
          " " + orig_downloaded_at_name           + " " + "text,"+  
          " " + orig_english_changes_made         + " " + "text,"+
          " " + orig_english_title                + " " + "text,"+
          " " + orig_english_uses_sample_from_1   + " " + "text,"+
          " " + orig_license_link                 + " " + "text,"+
          " " + orig_license_name                 + " " + "text,"+
          " " + orig_spanish_changes_made         + " " + "text,"+
          " " + orig_spanish_uses_sample_from_1   + " " + "text,"+
          " " + orig_uses_sample_from_link_1      + " " + "text,"+
          " " + orig_uses_sample_from_link_name_1 + " " + "text,"+
          " " + song_type                         + " " + "text,"+
          " " + zip_size                          + " " + "text);"
          );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
  }// END OF DATABASE_HELPER
  
  @Override
  public Uri insert(Uri uri, ContentValues values) {
    SQLiteDatabase dB = dbHelper.getWritableDatabase();
    long rowsAffected = 0;
    dB.beginTransaction ();
    try {
      if (values.containsKey(new_work_title)) {
        rowsAffected = dB.insertOrThrow(song_table, null, values);
      }
      if (0 != rowsAffected) {
        getContext().getContentResolver().notifyChange(uri, null);
        dB.setTransactionSuccessful();
      }
    }finally {
      dB.endTransaction();
    }    
    return Uri.withAppendedPath(uri, ""+rowsAffected);
  }

  /**
   * rowId is a variable.
   * Uri uriWithRow = Uri.withAppendedPath(uriTableName, ""+ rowId);
   * tResolver.delete(uriWithRow, null, null);
   */

  @Override
  public int delete(Uri uri, String where, String[] selectionArgs) {
    String idNumber = UriDeterminer.getRowKey(uri);
    if (null != idNumber && !idNumber.equals(ALL)) {
      where = ez.where(BaseColumns._ID, idNumber);
      SQLiteDatabase dB = dbHelper.getWritableDatabase();
      dB.beginTransaction ();
      int returnInt = 0;
      try {
        returnInt = dB.delete(song_table, where, null);
        if (0 < returnInt) {
          Uri uriTableName = Uri.parse(CP_URI+ song_table);
          getContext().getContentResolver().notifyChange(uriTableName, null);
          dB.setTransactionSuccessful();      
        }
      }finally {
        dB.endTransaction();
      } 
      return returnInt;
    }
    else if (null != idNumber && idNumber.equals(ALL)) {
      SQLiteDatabase dB = dbHelper.getWritableDatabase();
      dB.beginTransaction();
      int returnInt = 0;
      try {
        returnInt = dB.delete(song_table, null, null);
        if (0 < returnInt) {
          Uri uriTableName = Uri.parse(CP_URI + song_table);
          getContext().getContentResolver().notifyChange(uriTableName, null);
          dB.setTransactionSuccessful();
        }
      } finally {
        dB.endTransaction();
      }
      return returnInt;
    }
    else {
      SQLiteDatabase dB = dbHelper.getWritableDatabase();
      dB.beginTransaction();
      int returnInt = 0;
      try {
        returnInt = dB.delete(song_table, where, null);
        if (0 < returnInt) {
          Uri uriTableName = Uri.parse(CP_URI + song_table);
          getContext().getContentResolver().notifyChange(uriTableName, null);
          dB.setTransactionSuccessful();
        }
      } finally {
        dB.endTransaction();
      }
      return returnInt;
    }
    
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

 

  @Override
  public boolean onCreate() {
    dbHelper = new SongsDatabaseHelper(
        getContext()
    );

    return true;

  }

  /**
   * cR.query(Uri.parse(SongsCP.CP_URI),
              new String[] { SongsCP.link_to_new_work_license,
                             SongsCP.name_of_new_work_license,
                             SongsCP.new_work_title,
                            SongsCP.orig_artist},
              ez.where(BaseColumns._ID, cpRow),
              null,
              null);
   */
  @Override
  public Cursor query(Uri uri, String[] columns, String selection,
      String[] selectionArgs, String sortOrder) {
    Cursor returnCursor = null;
    SQLiteDatabase dB = dbHelper.getWritableDatabase();
    returnCursor = dB.query(song_table,
        columns, selection, selectionArgs, null, null, sortOrder, null);
    returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
    return returnCursor;
  }

  @Override
  public int update(Uri uri, ContentValues cv, String selection, String[] selectionArgs) {
    SQLiteDatabase dB = dbHelper.getWritableDatabase();
    dB.beginTransaction();
    int totalAffected = 0;

    try {
      totalAffected = dB.update(song_table, cv, selection, null);
      if (0 < totalAffected) {
        Uri uriTableName = Uri.parse(CP_URI + song_table);
        getContext().getContentResolver().notifyChange(uriTableName, null);
        dB.setTransactionSuccessful();
      }
    } finally {
      dB.endTransaction();
    }
    return totalAffected;
  }
}
