package com.eebolf.CantandoIngles.listeners;


import com.eebolf.CantandoIngles.start.PlayerState;

public interface LyricDisplayer {

  // update display (move to word corresponding currTime and hi-lite) if
  // state is synched. If not synched then do nothing.
  void updateLyrics (int currTime, PlayerState state);

  // Will move regardless of state, as doesn't even
  // a PlayerState object.
  void moveToLyric (int currTime);
  void setUp();

  interface Listener {
    void longClickAtTime(int row);
    void moveDisplayOnly(int time);
  }
}
