package com.eebolf.CantandoIngles.start;

import com.eebolf.CantandoIngles.collectors.WordsExplanationsAndTime;

import java.util.ArrayList;

public interface LyricDeviceInterface {
  public String getSpanishWord(int time);
  public String getEnglishWord(int time);
  public boolean isAtOrPastEndOfSong(int time);  
  public int getNextTimePing(int currentTime);
  public int getLastTime();
  public PastPresentFutureWords getPastPresentFutureWords(int currentTime);
  public void setUpLyrics();
  public ArrayList<WordsExplanationsAndTime> getLyrics();
}
