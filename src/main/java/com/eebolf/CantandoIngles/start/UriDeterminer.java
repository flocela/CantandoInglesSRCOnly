package com.eebolf.CantandoIngles.start;
import android.net.Uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriDeterminer {
	
  public static final Pattern p
  = Pattern.compile("((content://com\\.eebolf\\.CantandoIngles\\.SongsCP/song_table)(/(([^/])+)))");

  public static String getRowKey (Uri uri) {
    Matcher m = p.matcher(uri.toString());
    m.matches();
    try {
      return m.group(4);
    }
    catch (IllegalStateException e) {
      return null;
    }	
  }
}
