package com.eebolf.CantandoIngles.start;

import com.eebolf.CantandoIngles.collectors.WordsExplanationsAndTime;

import java.util.ArrayList;


public abstract class LyricHolderComplete implements LyricHolderCompleteInterface{
  protected ArrayList<WordsExplanationsAndTime> lyrics = new ArrayList<WordsExplanationsAndTime>();

  public int getNextTimePing(int currentTime) {
    if (isAtOrPastEndOfSong(currentTime)) {
      return getLastWordsExplanationsAndTime().getTime();
    }
    else {
      for (int currIndex = 0; currIndex<lyrics.size(); currIndex++) {
        int nextIndex = currIndex +1;
        int nextTime = lyrics.get(nextIndex).getTime();
        if (nextTime > currentTime) {
          return lyrics.get(nextIndex).getTime();
        }
      }
      return getLastWordsExplanationsAndTime().getTime();
    }
  }

  public boolean isAtOrPastEndOfSong(int time) {
    return (getLastWordsExplanationsAndTime().getTime() <= time);
  }

  private WordsExplanationsAndTime getWordsExplanationsAndTime(int argTime) {
    for (int currIndex = 0; currIndex<lyrics.size(); currIndex++) {
      int currentIndexTime = lyrics.get(currIndex).getTime();
      int nextIndex = currIndex +1;
      if (nextIndex >= lyrics.size()) {
        return getLastWordsExplanationsAndTime();
      }
      int nextTime = lyrics.get(nextIndex).getTime();
      if (argTime == currentIndexTime || nextTime > argTime) {
        return lyrics.get(currIndex);
      }
    }
    return getLastWordsExplanationsAndTime();
  }

  private WordsExplanationsAndTime getLastWordsExplanationsAndTime() {
    return lyrics.get(lyrics.size()-1);
  }

}

