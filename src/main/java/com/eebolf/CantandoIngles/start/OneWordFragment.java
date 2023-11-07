package com.eebolf.CantandoIngles.start;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.listeners.LyricDisplayer;

public class OneWordFragment extends Fragment implements LyricDisplayer {

  public OneWordView oneWordView = null;
  private LyricDeviceInterface lyricDevice;

  /**
   * Activity has to be a LyricActivity because this fragment calls
   * getLyricDevice in onActivityCreated(). Activity should be able to
   * return a working LyricDevice by the time onActivityCreated() is called.
   */
  public static OneWordFragment newInstance (String lyricsFileName) {
    OneWordFragment oneWordFragment = new OneWordFragment();
    Bundle bundle = new Bundle();
    oneWordFragment.setArguments(bundle);
    return  oneWordFragment;
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    setHasOptionsMenu(true);
  }

  @Override
  public void onActivityCreated (Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setUp();
  }

  @Override
  public View onCreateView (LayoutInflater inflater,
                            ViewGroup container,
                            Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.one_word_fragment, container, false);
    oneWordView = (OneWordView)view.findViewById(R.id.one_word_view);

    return view;
  }

  @Override
  public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.one_word_fragment_actions, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    return true;
  }

  @Override
  public void setUp() {
    if (lyricDevice == null) {
      lyricDevice = ((LyricActivity)getActivity()).getLyricDevice();
    }
    if (lyricDevice != null) {
      ProgressBar progressBar = (ProgressBar)oneWordView.findViewById(R.id.progressBar);
      progressBar.setMax(lyricDevice.getLastTime());
    }
  }

  // Note updates to currTime not to time in state.
  // Does take state's other attributes into account.
  @Override
  public void updateLyrics (int currTime, PlayerState state) {
    ProgressBar progressBar = (ProgressBar)oneWordView.findViewById(R.id.progressBar);
    if (!state.synched) {
      return;
    }
    if (lyricDevice != null) {
      PastPresentFutureWords ppfWords = lyricDevice.getPastPresentFutureWords(currTime);
      oneWordView.setEnglishPastText(ppfWords.getPastEnglish());
      oneWordView.setEnglishCurrText(ppfWords.getCurrEnglish(), currTime);
      oneWordView.setEnglishCurrCorrectedText(ppfWords.getCurrEnglishCorrected());
      oneWordView.setEnglishFutureText(ppfWords.getFutureEnglish());

      oneWordView.setSpanishPastText(ppfWords.getPastSpanish());
      oneWordView.setSpanishCurrText(ppfWords.getCurrSpanish(), currTime);
      oneWordView.setSpanishCurrMeaningTextView(ppfWords.getCurrSpanishMeaning());
      oneWordView.setSpanishFutureText(ppfWords.getFutureSpanish());
     ((Progressable)progressBar).
       setProgressPercentage(((float)currTime)/lyricDevice.getLastTime());
    }
  }

  // Does not take state into account, just moves to currTime.
  @Override
  public void moveToLyric(int currTime) {
    if (lyricDevice != null) {
      PastPresentFutureWords ppfWords = lyricDevice.getPastPresentFutureWords(currTime);
      oneWordView.setEnglishPastText(ppfWords.getPastEnglish());
      oneWordView.setEnglishCurrText(ppfWords.getCurrEnglish(), currTime);
      oneWordView.setEnglishCurrCorrectedText(ppfWords.getCurrEnglishCorrected());
      oneWordView.setEnglishFutureText(ppfWords.getFutureEnglish());

      oneWordView.setSpanishPastText(ppfWords.getPastSpanish());
      oneWordView.setSpanishCurrText(ppfWords.getCurrSpanish(), currTime);
      oneWordView.setSpanishCurrMeaningTextView(ppfWords.getCurrSpanishMeaning());
      oneWordView.setSpanishFutureText(ppfWords.getFutureSpanish());
      ProgressBar progressBar = (ProgressBar)oneWordView.findViewById(R.id.progressBar);
      ((Progressable)progressBar).
        setProgressPercentage(((float)currTime)/lyricDevice.getLastTime());
    }
  }

  public void onSaveInstanceState (Bundle outState) {
    super.onSaveInstanceState(outState);
  }

}