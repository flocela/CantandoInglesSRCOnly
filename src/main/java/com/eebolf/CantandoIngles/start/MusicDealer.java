package com.eebolf.CantandoIngles.start;

import android.os.Bundle;

import com.eebolf.CantandoIngles.utils.ez;

// Moving committing and use of MusicPlayerFragment here.
// This does nothelp with dependencies as requires a LyricActivity in the constructor,
// but does help encapsulate MusicPlayer stop, pause, play, commit and remove
// logic here.
public class MusicDealer {
  private LyricActivity       activity;
  private String              musicFilename;
  private boolean             forTiming;
  private static final String MEDIA_PLAYER_TAG = "MEDIA_PLAYER_TAG";

  public MusicDealer (LyricActivity activity, String musicFile, boolean forTiming) {
    this.activity = activity;
    this.musicFilename = musicFile;
    this.forTiming = forTiming;
  }

  public void changeOfStateRequiresAction (PlayerState state) {
    MediaPlayerFragment mediaPlayerFr = (MediaPlayerFragment)activity.
      findFragment(MEDIA_PLAYER_TAG);
    if (state.stopped) {
      if (mediaPlayerFr != null)
        activity.removeFragment(mediaPlayerFr);
    }
    else if (state.paused) {
      if (mediaPlayerFr != null)
        mediaPlayerFr.requestPause();
      else
        commitMediaPlayerFragment(state);
    }
    else if (!state.stopped && !state.paused) {
      if (mediaPlayerFr != null)
        mediaPlayerFr.requestPlay();
      else
        commitMediaPlayerFragment(state);
    }
  }

  public void moveToTime (PlayerState state) {
    MediaPlayerFragment mediaPlayerFr = (MediaPlayerFragment)activity.
      findFragment(MEDIA_PLAYER_TAG);
    if (mediaPlayerFr == null)
      return;
    else {
      mediaPlayerFr.requestTimeSet(state.position);
    }
  }

  public int getMusicPosition (PlayerState state) {
    MediaPlayerFragment mediaPlayerFr = (MediaPlayerFragment)
      activity.findFragment(MEDIA_PLAYER_TAG);
    if (null != mediaPlayerFr) {
      return mediaPlayerFr.getPlayerTime();
    }
    else {
      return state.position;
    }

  }

  public void setUp() {
    MediaPlayerFragment mediaPlayerFr = (MediaPlayerFragment)
      activity.findFragment(MEDIA_PLAYER_TAG);
    if (null != mediaPlayerFr) {
      mediaPlayerFr.setUp();
    }
  }

  private void commitMediaPlayerFragment (PlayerState state) {
    MediaPlayerFragment mediaPlayerFr = (MediaPlayerFragment)
      activity.findFragment(MEDIA_PLAYER_TAG);
    if (null == mediaPlayerFr) {
      Bundle bundle = ez.stringBundle(LyricActivity.MUSIC_FILENAME, musicFilename);
      bundle.putInt(MediaPlayerFragment.START_POS, state.position);
      boolean startImmediately = ( !state.stopped && !state.paused);// don't have to check
      // for stopped I wouldn't be committing the mediaPlayerFragment if the state
      // was stopped.
      bundle.putBoolean(MediaPlayerFragment.START_IMMEDIATELY, (startImmediately));
      bundle.putBoolean(MediaPlayerFragment.FOR_TIMING, forTiming);
      mediaPlayerFr = MediaPlayerFragment.newInstance(bundle);
      activity.addFragment(mediaPlayerFr, MEDIA_PLAYER_TAG);
    }
  }

}
