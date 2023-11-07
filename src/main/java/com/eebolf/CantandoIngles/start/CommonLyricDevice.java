package com.eebolf.CantandoIngles.start;

import com.eebolf.CantandoIngles.collectors.WordsExplanationsAndTime;

import java.util.ArrayList;

public class CommonLyricDevice extends LyricDevice {

  public CommonLyricDevice(ArrayList<WordsExplanationsAndTime> timedWords) {
    this.lyrics = timedWords;
  }

  @Override
  public void setUpLyrics() {
    // already set up.
  }
}
