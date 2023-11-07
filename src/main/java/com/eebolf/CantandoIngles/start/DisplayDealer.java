package com.eebolf.CantandoIngles.start;


import android.support.v4.app.Fragment;

import com.eebolf.CantandoIngles.listeners.LyricDisplayer;

// Moving committing of and using displayFragment here.
// This does not help with dependencies as requires a LyricActivity in the constructor,
// but helps encapsulate display logic here making LyricActivity smaller.
public class DisplayDealer {

  private LyricActivity      activity;
  private String             pref; // list view or one word view.
  public static final String WORD_DISPLAYER_TAG  = "WORD_DISPLAYER_TAG";

  public DisplayDealer (LyricActivity activity, String pref) {
    this.activity = activity;
    this.pref     = pref;
  }

  // updates taking into account state.position.
  public void changeOfStateRequiresAction(PlayerState state) {
    LyricDisplayer displayerFr = (LyricDisplayer) activity.
      findFragment(WORD_DISPLAYER_TAG);
    if (null == displayerFr)
      commitNewDisplayer();
    else
      displayerFr.updateLyrics(state.position, state);
  }

  // updates to position, does not take state.position into account.
  // Does take state's other attributes into account.
  // (example when called is when LyricActivity gets update position
  // from MusicDealer)
  public void updateDisplay (int position, PlayerState state) {
    LyricDisplayer displayerFr = (LyricDisplayer) activity.
      findFragment(WORD_DISPLAYER_TAG);
    if (null == displayerFr)
      return;
    displayerFr.updateLyrics(position, state);
  }

  // moves to position regardless of PlayerState
  public void moveToPosition (int time) {
    LyricDisplayer displayerFr = (LyricDisplayer) activity.
      findFragment(WORD_DISPLAYER_TAG);
    if (null == displayerFr)
      commitNewDisplayer();
    else
      displayerFr.moveToLyric(time);
  }

  public void initializeViews () {
    LyricDisplayer displayerFr = (LyricDisplayer) activity.
      findFragment(WORD_DISPLAYER_TAG);
    if (displayerFr != null) {
      displayerFr.setUp();
    }
    commitNewDisplayer();
  }

  private void commitNewDisplayer () {
    LyricDisplayer displayerFr = (LyricDisplayer) activity.
      findFragment(WORD_DISPLAYER_TAG);
    if (null == displayerFr) {
      if (LyricActivity.LYRIC_LIST_VIEW.equals(pref))
        displayerFr = WordListFragment.newInstance();
      else if (LyricActivity.LYRIC_ONE_WORD_VIEW.equals(pref))
        displayerFr = OneWordFragment.newInstance(activity.getLyricFilename());
      activity.addLyricFragment((Fragment) displayerFr, WORD_DISPLAYER_TAG);
    }
  }
}
