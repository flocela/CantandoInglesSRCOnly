package com.eebolf.CantandoIngles.start;

// Moving committing of LyricHolderFragment here. LyricDealer has a reference to
// Activity so can get the retained fragment LyricHolder and get the LyricDevice.
// This does not help with dependencies as a LyricActivity is required in the constructor,
// but helps encapsulate creating and holding the lyric device here making LyricActivity
// smaller.

import android.os.Bundle;

import com.eebolf.CantandoIngles.collectors.WordsExplanationsAndTime;
import com.eebolf.CantandoIngles.utils.ez;

import java.util.ArrayList;

// Constructor has to be called in onCreate so that I know that the LyricHolderFragment
// is committed without data loss i.e. before onSavedInstanceState is called in the
// activity.
public class LyricDealer {
  private LyricActivity       activity;
  private String              lyricFilename;
  private static final String LYRIC_CREATOR_TAG = "LYRIC_CREATOR_TAG";

  public LyricDealer (LyricActivity activity, String lyricFilename) {
    this.activity      = activity;
    this.lyricFilename = lyricFilename;
    commitLyricHolderFragment();
  }

  public LyricDevice getLyricDevice () {
    LyricHolderFragment lyricHolderFr = findLyricHolderFr();
    if (null == lyricHolderFr)
      commitLyricHolderFragment();
    else
      return lyricHolderFr.getLyricDevice();
    return null;
  }

  public ArrayList<WordsExplanationsAndTime> getLyrics () {
    if (null == getLyricDevice())
      return null;
    else
      return getLyricDevice().getLyrics();
  }

  private void commitLyricHolderFragment () {
    LyricHolderFragment lyricHolderFr = findLyricHolderFr();
    if (null == lyricHolderFr) {
      Bundle bundle = ez.stringBundle(LyricActivity.LYRIC_FILENAME, lyricFilename);
      activity.addFragment(LyricHolderFragment.newInstance(bundle), LYRIC_CREATOR_TAG);
    }
  }

  private LyricHolderFragment findLyricHolderFr () {
    return (LyricHolderFragment) activity.findFragment(LYRIC_CREATOR_TAG);
  }
}
