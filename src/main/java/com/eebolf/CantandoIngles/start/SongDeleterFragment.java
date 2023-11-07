package com.eebolf.CantandoIngles.start;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.BaseColumns;

import com.eebolf.CantandoIngles.workers.SongDeleter;

/**
 * Requires SongsCP row id for the song to be deleted. SongsCP.;
 **/

public class SongDeleterFragment extends Fragment {
  private boolean mReady = false;
  private boolean mQuitting = false;
  private Integer rowId = null;

  public static SongDeleterFragment newInstance(Bundle startingBundle) {
    SongDeleterFragment songDeleterFragment = new SongDeleterFragment();
    songDeleterFragment.setArguments(startingBundle);
    return songDeleterFragment;
  }

  final Thread mThread = new Thread() {
    @Override
    public void run() {
      synchronized (this) {
        while (!mReady) {
          if (mQuitting)
            return;
          try {
            wait();
          } catch (InterruptedException e) {
          }
        }
        Context context = SongDeleterFragment.this.getActivity();
        SongDeleter songDeleter = new SongDeleter(context, rowId);
        songDeleter.work();
      }
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    Bundle args = (null != savedInstanceState) ? savedInstanceState : getArguments();
    if (null != args) {
      this.rowId = args.getInt(BaseColumns._ID);
    }
    mThread.start();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    synchronized (mThread) {
      mReady = true;
      mThread.notify();
    }
  }

  @Override
  public void onDestroy() {
    synchronized (mThread) {
      mReady = false;
      mQuitting = true;
      mThread.notify();
    }
    super.onDestroy();

  }

  @Override
  public void onDetach() {
    synchronized (mThread) {
      mReady = false;
      mThread.notify();
    }
    super.onDetach();
  }
}
