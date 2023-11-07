package com.eebolf.CantandoIngles.workers;

import android.content.ContentValues;
import android.os.Bundle;

import com.eebolf.CantandoIngles.SongsCP;
import com.eebolf.CantandoIngles.utils.Map;

public class SongImporter {

  ContentValues values = new ContentValues();
  
  public SongImporter(Bundle songinfo) {
    values = new ContentValues();
    //values.put(SongsCP.music_file, songinfo.getString(Map.NAKED_FILENAME) + ".mp3");
    values.put(SongsCP.music_file, songinfo.getString(Map.NAKED_FILENAME) + ".m4a");
    values.put(SongsCP.lyrics_file, songinfo.getString(Map.NAKED_FILENAME) + ".txt");
    values.put(SongsCP.link_to_new_work_license, songinfo.getString(SongsCP.link_to_new_work_license));
    values.put(SongsCP.name_of_new_work_license, songinfo.getString(SongsCP.name_of_new_work_license));
    values.put(SongsCP.new_work_title, songinfo.getString(SongsCP.new_work_title));
    values.put(SongsCP.orig_artist, songinfo.getString(SongsCP.orig_artist));
    values.put(SongsCP.orig_downloaded_at_link, songinfo.getString(SongsCP.orig_downloaded_at_link));
    values.put(SongsCP.orig_downloaded_at_name, songinfo.getString(SongsCP.orig_downloaded_at_name));
    values.put(SongsCP.orig_english_changes_made, songinfo.getString(SongsCP.orig_english_changes_made));
    values.put(SongsCP.orig_english_title, songinfo.getString(SongsCP.orig_english_title));
    values.put(SongsCP.orig_english_uses_sample_from_1, songinfo.getString(SongsCP.orig_english_uses_sample_from_1));
    values.put(SongsCP.orig_license_link, songinfo.getString(SongsCP.orig_license_link));
    values.put(SongsCP.orig_license_name, songinfo.getString(SongsCP.orig_license_name));
    values.put(SongsCP.orig_spanish_changes_made, songinfo.getString(SongsCP.orig_spanish_changes_made));
    values.put(SongsCP.orig_spanish_uses_sample_from_1, songinfo.getString(SongsCP.orig_spanish_uses_sample_from_1));
    values.put(SongsCP.orig_uses_sample_from_link_1, songinfo.getString(SongsCP.orig_uses_sample_from_link_1));
    values.put(SongsCP.orig_uses_sample_from_link_name_1, songinfo.getString(SongsCP.orig_uses_sample_from_link_name_1));
    values.put(SongsCP.song_type, songinfo.getString(SongsCP.song_type));
    values.put(SongsCP.zip_size, songinfo.getString(SongsCP.zip_size));
  }

  public ContentValues getSongValues() {
    return values;
  }

}
