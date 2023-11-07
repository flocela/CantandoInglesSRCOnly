package com.eebolf.CantandoIngles.start;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.collectors.WordsExplanationsAndTime;
import com.eebolf.CantandoIngles.listeners.LyricDisplayer;
import com.eebolf.CantandoIngles.todowithlists.LyricRow;
import com.eebolf.CantandoIngles.todowithlists.WordListArrayAdapter;

import java.util.ArrayList;

public class WordListFragment extends ListFragment
                              implements LyricDisplayer, AbsListView.OnScrollListener {

  private final String HILITED = "HILITED";

  private int hiLitedPosition = -1;

  /**
   * Activity has to be a LyricActivity because this fragment calls
   * getLyricDevice in onActivityCreated() and calls getLyrics on the activity.
   * Activity should be able to return a working LyricDevice by
   * the time onActivityCreated() is called.
   */
  public static WordListFragment newInstance () {
    WordListFragment wordListFragment = new WordListFragment();
    Bundle bundle = new Bundle();
    wordListFragment.setArguments(bundle);
    return  wordListFragment;
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
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ListView list = this.getListView();
    list.setFocusable(true);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onDetach () {
    super.onDetach();
  }

  @Override
  public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.word_list_fragment_actions, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    return inflater.inflate(R.layout.lyrics_list, container, false);
  }

  @Override
  public void onStart () {
    super.onStart();
    getListView().setOnScrollListener(this);
  }

  public void setUp () {
    LyricActivity activity = (LyricActivity)getActivity();
    ArrayList<WordsExplanationsAndTime> dataList = activity.getLyrics();
    if (null != dataList && null == getListAdapter()) {
      WordListArrayAdapter adapter =
        new WordListArrayAdapter(activity, activity.getLyrics());
      if (null != adapter) {
        setListAdapter(adapter);
      }
    }
  }

  // Note updates to currTime not to time in state.
  // Does take state's other attributes into account.
  @Override
  public void updateLyrics (int currTime, PlayerState state) {
    setCurrHiLiteRowToBlue();
    setHilitedPos(currTime);
    setCurrHiLitedRowToPink();
    if (!state.synched)
      return;
    if (hiLitedPosition == getLastVisiblePosition() - 1 ||
        hiLitedPosition == getLastVisiblePosition()) {
      int revisedSpeed = getSpeed();
      getListView().smoothScrollToPositionFromTop(hiLitedPosition, 1, revisedSpeed);// changed from 1000
    }
    else if (!isRowVisible(hiLitedPosition))
      getListView().setSelectionFromTop(hiLitedPosition, 50);
  }

  // Does not take state into account.
  // Will always move to currTime.
  @Override
  public void moveToLyric(int currTime) {
    setCurrHiLiteRowToBlue();
    setHilitedPos(currTime);
    setCurrHiLitedRowToPink();
    if (hiLitedPosition == getLastVisiblePosition() - 1 ||
      hiLitedPosition == getLastVisiblePosition()) {
      int revisedSpeed = getSpeed();
      getListView().smoothScrollToPositionFromTop(hiLitedPosition, 1, revisedSpeed);// changed from 1000
    }
    else if (!isRowVisible(hiLitedPosition))
      getListView().setSelectionFromTop(hiLitedPosition, 50);
  }

  @Override
  public void onScrollStateChanged(AbsListView absListView, int i) {}

  @Override
  public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    if (hiLitedPosition != -1 && isRowVisible(hiLitedPosition)) {
      getRowAtPosition(hiLitedPosition).setToLightPink();
    }
  }

  @Override
  public void onSaveInstanceState (Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(HILITED, hiLitedPosition);
  }

  private boolean isRowVisible (int position) {
    int firstVisible = getListView().getFirstVisiblePosition();
    int lastVisible = getListView().getLastVisiblePosition();
    if (position >= firstVisible && position <= lastVisible) return true;
    return false;
  }

  private LyricRow getRowAtPosition(int position) {
    int firstVisible = getListView().getFirstVisiblePosition();
    return (LyricRow)getListView().getChildAt(position - firstVisible);
  }

  // Note only sets to blue if row is visible.
  private void setCurrHiLiteRowToBlue () {
    if (hiLitedPosition != -1 && isRowVisible(hiLitedPosition)) {
      LyricRow toBlueRow = getRowAtPosition(hiLitedPosition);
      toBlueRow.setToLightBlue();
    }
  }

  private void setCurrHiLitedRowToPink () {
    if (hiLitedPosition != -1 && isRowVisible(hiLitedPosition)) {
      LyricRow toPinkRow = getRowAtPosition(hiLitedPosition);
      toPinkRow.setToLightPink();
    }
  }

  private void setHilitedPos(int time) {
    int position = getAdapterPosition(time);
    hiLitedPosition = position;
  }

  private int getAdapterPosition(int time) {
    return ((WordListArrayAdapter)this.getListAdapter()).getPosition(time);
  }

  private int getLastVisiblePosition () {
    return this.getListView().getLastVisiblePosition();
  }

  private int getSpeed () {
    SharedPreferences preferences =
      PreferenceManager.getDefaultSharedPreferences(this.getContext());
    int preference = Integer.valueOf(preferences.getString("scroll_speed", "11"));
    return 2000 - (100 * preference);
  }

}
