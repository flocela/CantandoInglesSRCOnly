package com.eebolf.CantandoIngles.utils;

import android.os.Bundle;

import com.eebolf.CantandoIngles.SongsCP;
import com.eebolf.CantandoIngles.collectors.SongInfo;

public class ez {
  public static Bundle intBundle(String stringValue, int intValue) {
    Bundle bundle = new Bundle();
    bundle.putInt(stringValue, intValue);
    return bundle;
  }

  public static Bundle stringBundle (String key, String value) {
    Bundle bundle = new Bundle();
    bundle.putString(key, value);
    return bundle;
  }

  public static Bundle booleanBundle (String key, boolean value) {
    Bundle bundle = new Bundle();
    bundle.putBoolean(key, value);
    return bundle;
  }
  
  public static String where(String where, String equals) {    
    return new StringBuilder(where).append("='")
                               .append(equals)
                               .append("'").toString();
    
  }
  
  public static String getFileName (String pathName) {
    String trimmedName = pathName.trim();
    int lastSlashIdx = trimmedName.lastIndexOf('/');
    if (-1 == lastSlashIdx)
      return trimmedName;
    if (lastSlashIdx >= trimmedName.length()-1)
      return "";
    else 
      return trimmedName.substring(lastSlashIdx+1);     
  }
  
  public static String getPath (String fileName) {
    String trimmedName = fileName.trim();
    int lastSlashIdx = trimmedName.lastIndexOf('/');
    if (-1 == lastSlashIdx)
      return "";
    else 
      return trimmedName.substring(0, lastSlashIdx+1);    
  }
  
  public static String where(String where, int equals) {
    
    return new StringBuilder(where).append("='")
                               .append(String.valueOf(equals))
                               .append("'").toString();    
  }

  public static String getNameOnly (String pathName) {
    String filename = getFileName(pathName);
    int index = pathName.indexOf('.');
    return filename.substring(0, index);
  }
  
  public static Bundle convertToBundle (SongInfo songInfo) {
    Bundle bundle = new Bundle();
    bundle.putInt(Map.SONG_WEB_ID, songInfo.getWebId());
    bundle.putString(Map.NAKED_FILENAME, songInfo.getFilename());
    bundle.putString(SongsCP.link_to_new_work_license, songInfo.getLinkToNewWorkLicense());
    bundle.putString(SongsCP.name_of_new_work_license, songInfo.getNameOfNewWorkLicense());
    bundle.putString(SongsCP.new_work_title, songInfo.getNewWorkTitle());
    bundle.putString(SongsCP.orig_artist, songInfo.getOrigArtist());
    bundle.putString(SongsCP.orig_downloaded_at_link, songInfo.getOrigDownloadedAtLink());
    bundle.putString(SongsCP.orig_downloaded_at_name, songInfo.getOrigDownloadedAtName());
    bundle.putString(SongsCP.orig_english_changes_made, songInfo.getOrigEnglishChangesMade());
    bundle.putString(SongsCP.orig_english_title, songInfo.getOrigEnglishTitle());
    bundle.putString(SongsCP.orig_english_uses_sample_from_1, songInfo.getOrigEnglishUsesSampleFrom1());
    bundle.putString(SongsCP.orig_license_link, songInfo.getOrigLicenseLink());
    bundle.putString(SongsCP.orig_license_name, songInfo.getOrigLicenseName());   
    bundle.putString(SongsCP.orig_spanish_changes_made, songInfo.getOrigSpanishChangesMade());    
    bundle.putString(SongsCP.orig_spanish_uses_sample_from_1, songInfo.getOrigSpanishUsesSampleFrom1()); 
    bundle.putString(SongsCP.orig_uses_sample_from_link_1, songInfo.getOrigUsesSampleFromLink1());   
    bundle.putString(SongsCP.orig_uses_sample_from_link_name_1, songInfo.getOrigUsesSampleFromLinkName1());    
    bundle.putString(SongsCP.song_type, songInfo.getSongType());   
    bundle.putString(SongsCP.zip_size, songInfo.getZipSize());    
    return bundle;
  }
}
