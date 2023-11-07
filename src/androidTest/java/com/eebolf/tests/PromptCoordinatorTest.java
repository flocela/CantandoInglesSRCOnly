package com.eebolf.tests;

import android.test.suitebuilder.annotation.SmallTest;

import com.eebolf.CantandoIngles.mainactivity.PromptCoordinator;
import com.eebolf.CantandoIngles.mainactivity.PromptDownloadFirstSongDialogV4;
import com.eebolf.CantandoIngles.mainactivity.PromptDownloadInstructionsDialogV4;
import com.eebolf.CantandoIngles.mainactivity.PromptStartSongDialogV4;
import com.eebolf.CantandoIngles.start.SettingsActivity;

import junit.framework.TestCase;

import java.util.HashMap;


public class PromptCoordinatorTest extends TestCase {

  /**
   * downloadFirstSongPrompt       = true
   * showStartSongPrompt           = true
   * showDownloadInstructionPrompt = true
   * numOfRows = 0
   * Should result in PromptDownloadFirstSongDialogV4.
   */
  @SmallTest
  public void testDownloadFirstSong () {
    HashMap<String, Boolean> map = new HashMap<String, Boolean>();
    map.put(SettingsActivity.download_first_song_prompt,true);
    map.put(SettingsActivity.start_song_instructions_prompt, true);
    map.put(SettingsActivity.download_instructions_prompt, true);
    int numRows = 0;
    PromptCoordinator promptCoordinator = new PromptCoordinator(numRows, map);
    assertTrue(promptCoordinator.getCurrPromptDialog() instanceof PromptDownloadFirstSongDialogV4);
  }


  /**
   * downloadFirstSongPrompt       = false
   * showStartSongPrompt           = true
   * showDownloadInstructionPrompt = true
   * numOfRows = 0
   * Should result in PromptDownloadInstructionsDialogV4.
   */
  @SmallTest
  public void testDownloadSongOne () {
    HashMap<String, Boolean> map = new HashMap<String, Boolean>();
    map.put(SettingsActivity.download_first_song_prompt,false);
    map.put(SettingsActivity.start_song_instructions_prompt, true);
    map.put(SettingsActivity.download_instructions_prompt, true);
    int numRows = 0;
    PromptCoordinator promptCoordinator = new PromptCoordinator(numRows, map);
    assertTrue(promptCoordinator.getCurrPromptDialog() instanceof PromptDownloadInstructionsDialogV4);
  }


  /**
   * downloadFirstSongPrompt       = false
   * showStartSongPrompt           = true
   * showDownloadInstructionPrompt = true
   * numOfRows = 1
   * Should result in PromptStartSongDialogV4.
   */
  @SmallTest
  public void testStartSong () {
    HashMap<String, Boolean> map = new HashMap<String, Boolean>();
    map.put(SettingsActivity.download_first_song_prompt,false);
    map.put(SettingsActivity.start_song_instructions_prompt, true);
    map.put(SettingsActivity.download_instructions_prompt, true);
    int numRows = 1;
    PromptCoordinator promptCoordinator = new PromptCoordinator(numRows, map);
    assertTrue(promptCoordinator.getCurrPromptDialog() instanceof PromptStartSongDialogV4);
  }


  /**
   * downloadFirstSongPrompt       = false
   * showStartSongPrompt           = false
   * showDownloadInstructionPrompt = true
   * numOfRows = 0
   * Should result in PromptStartSongDialogV4.
   */
  @SmallTest
  public void testDownloadAnotherSongA () {
    HashMap<String, Boolean> map = new HashMap<String, Boolean>();
    map.put(SettingsActivity.download_first_song_prompt,false);
    map.put(SettingsActivity.start_song_instructions_prompt, false);
    map.put(SettingsActivity.download_instructions_prompt, true);
    int numRows = 0;
    PromptCoordinator promptCoordinator = new PromptCoordinator(numRows, map);
    assertTrue(promptCoordinator.getCurrPromptDialog() instanceof PromptDownloadInstructionsDialogV4);
  }

  /**
   * downloadFirstSongPrompt       = false
   * showStartSongPrompt           = false
   * showDownloadInstructionPrompt = true
   * numOfRows = 1
   * Should result in PromptStartSongDialogV4.
   */
  @SmallTest
  public void testDownloadAnotherSongB () {
    HashMap<String, Boolean> map = new HashMap<String, Boolean>();
    map.put(SettingsActivity.download_first_song_prompt,false);
    map.put(SettingsActivity.start_song_instructions_prompt, false);
    map.put(SettingsActivity.download_instructions_prompt, true);
    int numRows = 0;
    PromptCoordinator promptCoordinator = new PromptCoordinator(numRows, map);
    assertTrue(promptCoordinator.getCurrPromptDialog() instanceof PromptDownloadInstructionsDialogV4);
  }


  /**
   * downloadFirstSongPrompt       = true
   * showStartSongPrompt           = false
   * showDownloadInstructionPrompt = true
   * numOfRows = 1
   * Should result in PromptStartSongDialogV4.
   */
  @SmallTest
  public void testDownloadAnotherSongC () {
    HashMap<String, Boolean> map = new HashMap<String, Boolean>();
    map.put(SettingsActivity.download_first_song_prompt, true);
    map.put(SettingsActivity.start_song_instructions_prompt, false);
    map.put(SettingsActivity.download_instructions_prompt, true);
    int numRows = 1;
    PromptCoordinator promptCoordinator = new PromptCoordinator(numRows, map);
    assertTrue(promptCoordinator.getCurrPromptDialog() instanceof PromptDownloadInstructionsDialogV4);
  }


  /**
   * downloadFirstSongPrompt       = false
   * showStartSongPrompt           = false
   * showDownloadInstructionPrompt = false
   * numOfRows = 0
   * Should result in PromptStartSongDialogV4.
   */
  @SmallTest
  public void testNoDialog () {
    HashMap<String, Boolean> map = new HashMap<String, Boolean>();
    map.put(SettingsActivity.download_first_song_prompt, false);
    map.put(SettingsActivity.start_song_instructions_prompt, false);
    map.put(SettingsActivity.download_instructions_prompt, false);
    int numRows = 0;
    PromptCoordinator promptCoordinator = new PromptCoordinator(numRows, map);
    assertTrue(promptCoordinator.getCurrPromptDialog() == null);
  }


}
