package com.eebolf.CantandoIngles.start;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;

import com.eebolf.CantandoIngles.SongsCP;

import java.io.File;

public class DeleteAllSongsTask extends AsyncTask<String, Integer, Long> {

  ContentResolver cR;
  String songDirectory;
  public DeleteAllSongsTask (ContentResolver cR, String songDirectory) {
    this.cR = cR;
    this.songDirectory = songDirectory;
  }
  protected Long doInBackground(String... string) {
    Uri uriTableName = Uri.parse(SongsCP.CP_URI + SongsCP.song_table);
    Uri uriAll = Uri.withAppendedPath(uriTableName, ""+ SongsCP.ALL);
    cR.delete(uriAll, null, null);
    deleteSongFiles();
    return null;
  }
  protected void onProgressUpdate(Integer... progress) {}

  protected void onPostExecute(Long result) {}

  private void deleteSongFiles () {
    File songsDirFile = new File(songDirectory);
    if (songsDirFile != null && songsDirFile.exists() && songsDirFile.isDirectory()) {
      File[] files = songsDirFile.listFiles();
      int numOfFiles = files.length;
      for (int ii=0; ii<numOfFiles; ii++) {
        files[ii].delete();
      }
    }
  }


}
