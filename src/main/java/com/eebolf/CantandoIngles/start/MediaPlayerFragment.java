package com.eebolf.CantandoIngles.start;


import android.media.MediaPlayer;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.IOException;

public class MediaPlayerFragment extends Fragment
                                 implements OnSeekCompleteListener,
                                            MediaPlayer.OnErrorListener,
                                            MediaPlayer.OnCompletionListener{

  public final static String FOR_TIMING        = "FOR_TIMING";
  public final static String START_POS         = "START_POS";
  public final static String START_IMMEDIATELY = "START_IMMEDIATELY";
  private String      musicFilename;
  private LyricDevice lyricDevice;
  private MediaPlayer mediaPlayer;
  private boolean     forTiming = false;
  private boolean     hasFinished = false;
  private boolean     mReady = false;
  private boolean     mQuiting = false;
  private boolean     wantToStart = false;
  private int         startingPostion = 0;

  /**
   * Activity commiting this fragment must be a LyricActivity because
   * this fragment calls getLyricDevice(). The method getLyricDevice() has to
   * be able return a working LyricDevice by onActivityCreated().
   * Bundle needs to have the following keys:
   * LyricActivity.MUSIC_FILENAME.
   * START_POS, or position of zero will be used.
   * If using for timing, then also add key FOR_TIMING.
   */
  public static MediaPlayerFragment newInstance(Bundle bundle) {
    MediaPlayerFragment mediaPlayerFragment= new MediaPlayerFragment();
    mediaPlayerFragment.setArguments(bundle);    
    return mediaPlayerFragment;
  }

  final Thread mThread = new Thread() {
    @Override
    public void run() {
      synchronized (this) {
        while (!mReady && !mQuiting) {
          try {
            wait();
          } catch (InterruptedException e) {}
        }
        if (fragmentBeingDestroyed())
          return;
        try {
          prepareMediaPlayer();
        }
        catch(Exception ex) {
          notifyActivityOfException(ex, "MediaPlayerFragment.lM. prepareMediaPlayer() " +
            musicFilename);
          return;
        }
      }
      notifyActivityIfReady();
      int playerTime;
      while(true){
        if (fragmentBeingDestroyed())
          return;
        enterPausedStateIfPaused();
        notifyActivityIfReady();
        playerTime = getPlayerTime();
        int previousPlayerTime;
        int sleepTime;
        int nextPing;
        synchronized (this) {
          if (fragmentBeingDestroyed())
            return;
          previousPlayerTime = playerTime;
          sleepTime = getSleepTime(playerTime);
          nextPing = previousPlayerTime + sleepTime;
          if (sleepTime > 0) {
            try {
              this.wait(sleepTime);
            }catch (InterruptedException e) {}
          }
        }
        playerTime = getPlayerTime();
        // mediaPlayer's getCurrentTime sometimes returns previousPlayerTime, so wait 20ms.
        synchronized (this) {
          if (fragmentBeingDestroyed())
            return;
          if (mediaPlayer.isPlaying() && nextPing > playerTime &&
              playerTime >= previousPlayerTime) {
            try {
              wait(20);
            }catch(InterruptedException e){}
          }
        }
      }    
    }

    private int getSleepTime (int playerTime) {
      int nextPingTime = lyricDevice.getNextTimePing(playerTime);
      return nextPingTime - playerTime;
    }

    private void enterPausedStateIfPaused() {
      // wait begins again if mediaPlayer is not playing.
      // so it is safe to call this twice in a row.
      synchronized(this) {
        if (!mQuiting && mediaPlayer != null && !mediaPlayer.isPlaying()) {
          try {
            this.wait();
          } catch (InterruptedException e) {}
        }
      }
    }

    private boolean fragmentBeingDestroyed() {
      synchronized(this) {
        if (mQuiting) {
          return true;
        }
        return false;
      }
    }
  }; // mThread Ends

  public void requestPause () {
    synchronized(this.mThread) {
      if (mReady) {
        if (mediaPlayer.isPlaying()) {
          mediaPlayer.pause();
        }
      }
      mThread.notify();
    }
  }

  public void requestPlay () {
    synchronized(this.mThread) {
      if (mReady) {
        if (null == mediaPlayer) {
          try {
            mediaPlayer.prepare();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        else if (null != mediaPlayer) {
          mediaPlayer.start();
        }
      }
      mThread.notify();
    }
  }

  public void requestTimeSet(int time) {
    if (null != mediaPlayer) {
      mediaPlayer.seekTo(time);
    }
  }

  public void setUp() {
    if (lyricDevice == null) {
      lyricDevice = ((LyricActivity)getActivity()).getLyricDevice();
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    mThread.start();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (musicFilename == null) {
      Bundle args = getArguments();
      musicFilename = args.getString(LyricActivity.MUSIC_FILENAME);
      lyricDevice = ((LyricActivity)getActivity()).getLyricDevice();
      startingPostion = args.getInt(START_POS);
      wantToStart = args.getBoolean(START_IMMEDIATELY, true);
      if (args.containsKey(FOR_TIMING))
        this.forTiming = args.getBoolean(FOR_TIMING);
    }
    synchronized (mThread) {
      mReady = true;
      mQuiting = false;
      mThread.notify();
    }
  }

  @Override
  public void onSeekComplete(MediaPlayer arg0) {
    synchronized(this.mThread) {
      if (!mQuiting && mediaPlayer.isPlaying()) {
        this.mThread.notify(); //wake up sleeping thread, activity will be notified in due course.
      }
      else if (!mQuiting && !mediaPlayer.isPlaying() && wantToStart) {
        requestPlay();
        wantToStart = false;
      }
      else if (!mQuiting && !mediaPlayer.isPlaying() && !wantToStart){
        notifyActivityIfReady();// don't wake up paused thread, but do notify activity.
      }
    }
  }

  public boolean onError(MediaPlayer mp, int what, int extra) {
    return true;
  }

  @Override
  public void onCompletion(MediaPlayer mP) {
    ((LyricActivity)getActivity()).onCompletion(null);
    hasFinished = true;
  }

  public int getPlayerTime() {
    synchronized (this.mThread) {
      if(!mQuiting && null != mediaPlayer)
        return mediaPlayer.getCurrentPosition();
      else
        return -1;
    }
  }

  @Override
  public void onStart () {
    super.onStart();
  }

  @Override
  public void onResume () {
    super.onResume();
    if (this.hasFinished) {
      ((LyricActivity)getActivity()).onCompletion(null);
    }
  }

  @Override
  public void onPause () {
    super.onPause();
  }

  @Override
  public void onStop () {
    super.onStop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    synchronized (mThread) {
      mQuiting = true;
      mReady = false;
      mThread.notify();
    }
    if (null != mediaPlayer) {
      mediaPlayer.stop();
      mediaPlayer.release();
      mediaPlayer = null;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    synchronized (mThread) {
      mReady = false;
    }
  }

  private void notifyActivityIfReady() {
    synchronized (this.mThread) {
      if (mReady) {
        LyricActivity LyricActivity =
          (LyricActivity)MediaPlayerFragment.this.getActivity();
        if (null != lyricDevice) {
          if (getPlayerTime() < lyricDevice.getLastTime() && getPlayerTime() != -1) {
            LyricActivity.notifyTimeForNewWord();
          }
        }
        else {
          setUp();
        }
      }
    }
  }

  private void notifyActivityOfException (Exception ex, String localDescription) {
    synchronized(this) {
      if (mReady){
        TracedException tr = new TracedException(ex,localDescription);
        ((LyricActivity)getActivity()).exceptionOccuredAsync(tr);
      }
    }
  }

  private void prepareMediaPlayer() {
    if (forTiming) { //TIMING
      //type in filename here.
      /*
      mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(),
        R.raw.under_the_christmas_tree_keytronic); // put .mp3 file in res/raw/dream_a_little_dream.mp3.
       */
    }
    else {
      if (null == mediaPlayer) {
        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(),
          Uri.parse(musicFilename));
      }
    }
    mediaPlayer.setOnErrorListener(MediaPlayerFragment.this);
    mediaPlayer.setOnCompletionListener(MediaPlayerFragment.this);
    mediaPlayer.setOnSeekCompleteListener(MediaPlayerFragment.this);
    mediaPlayer.setLooping(false);
    mediaPlayer.seekTo(startingPostion);
  }
}