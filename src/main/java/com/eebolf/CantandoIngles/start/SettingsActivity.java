package com.eebolf.CantandoIngles.start;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivity extends Activity {
  public static String download_first_song_prompt = "download_first_song_prompt";
  public static String download_instructions_prompt = "download_instructions_prompt";
  public static String start_song_instructions_prompt = "start_song_instructions_prompt";
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      getFragmentManager().beginTransaction()
              .replace(android.R.id.content, new SettingsFragment())
              .commit();
  }
  
  public static class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.preferences);
    }
  }
}
