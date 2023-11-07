package com.eebolf.CantandoIngles.mainactivity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.eebolf.CantandoIngles.start.SettingsActivity;

import java.util.Map;

/**
 * Given a list view and SharedPreferences, will
 * decide which prompt should be showing right now. Bases prompt
 * on number of items in view and which prompts have already been
 * confirmed (based on SharedPreferences).
 * This class is very closely associated with MainActivity because
 * PromptDownloadFirstSongDialogV4 calls requestShowDownloadList() on
 * MainActivity.
 * 
 * Prompts that show based on the main list view are
 * 1. Download your first song instruction.
 * 2. Start playing a song instruction.
 * 3. Download another song instruction.
 * 4. Download a song instruction.
 * 
 */
public class PromptCoordinator {

  public int numOfRows = 0;
  public Map preferences;

  // TODO make this protected. Only public now, so that tests can access it.
  public PromptCoordinator (int numRows, final Map preferences) {
    this.numOfRows = numRows;
    this.preferences = preferences;
  }

  // TODO make this protected. Only public now, so that tests can access it.
  public DialogFragment getCurrPromptDialog () {
    Boolean downloadFirstSongPrompt = (Boolean)preferences.get(SettingsActivity.download_first_song_prompt);
    Boolean startSongsInstructions = (Boolean)preferences.get(SettingsActivity.start_song_instructions_prompt);
    Boolean showDownloadInstructions = (Boolean)preferences.get(SettingsActivity.download_instructions_prompt);

    if (downloadFirstSongPrompt && 0 == numOfRows) {
      return PromptDownloadFirstSongDialogV4.newInstance();
    }
    else if (downloadFirstSongPrompt == false && startSongsInstructions && 1==numOfRows) {
      Bundle bundle = new Bundle();
      bundle.putString(PromptStartSongDialogV4.INSTRUCTIONS_SPANISH, "Oprima la cancio\u0301n para empezar!");
      bundle.putString(PromptStartSongDialogV4.INSTRUCTIONS_ENGLISH, "Select the song to start!");
      bundle.putInt(MainActivity.REQUEST_CODE, MainActivity.DIALOG_CONFIRM_FIRST_SONG_DOWNLOAD);
      return PromptStartSongDialogV4.newInstance(bundle);
    }
    else if (showDownloadInstructions && numOfRows==0) {
      Bundle bundle = new Bundle();
      bundle.putString(PromptDownloadInstructionsDialogV4.INSTRUCTIONS_SPANISH, "descargar una cancio\u0301n.");
      return PromptDownloadInstructionsDialogV4.newInstance(bundle);
    }
    else if (showDownloadInstructions && numOfRows>0){
      Bundle bundle = new Bundle();
      bundle.putString(PromptDownloadInstructionsDialogV4.INSTRUCTIONS_SPANISH, "descargar mas cancio\u0301nes.");
      return PromptDownloadInstructionsDialogV4.newInstance(bundle);
    }
    return null;
  }
  
  
}
