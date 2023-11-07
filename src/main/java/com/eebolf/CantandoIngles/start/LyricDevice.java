package com.eebolf.CantandoIngles.start;

import com.eebolf.CantandoIngles.collectors.WordsExplanationsAndTime;

import java.util.ArrayList;


public abstract class LyricDevice implements LyricDeviceInterface {
  protected ArrayList<WordsExplanationsAndTime> lyrics = new ArrayList<WordsExplanationsAndTime>();

  public ArrayList<WordsExplanationsAndTime> getLyrics () { return lyrics;}

  public int getNextTimePing(int currentTime) {
    if (isAtOrPastEndOfSong(currentTime)) {
      return getLastWordsExplanationsAndTime().getTime();
    }
    else {
      for (int currIndex = 0; currIndex<lyrics.size()-1; currIndex++) {
        int nextIndex = currIndex +1;
        int nextTime = lyrics.get(nextIndex).getTime();
        if (nextTime > currentTime) {
          return nextTime;
        }
      }
      return getLastWordsExplanationsAndTime().getTime();
    }
  }
  
  public boolean isAtOrPastEndOfSong(int time) {
    return (getLastWordsExplanationsAndTime().getTime() <= time);
  }

  public int getTimeAtRowItem (int rowNum) {
    if (rowNum > -1 && rowNum < lyrics.size()) {
      return lyrics.get(rowNum).getTime();
    }
    else {
      return -1;
    }
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
    if (lyrics.size() == 0)
      return null;
    return lyrics.get(lyrics.size()-1);
  }  
  
  @Override
  public int getLastTime() {
    return getLastWordsExplanationsAndTime().getTime();
  }
  
  @Override
  public String getSpanishWord(int time) {
    return getWordsExplanationsAndTime(time).getSpanishDirect();
  }

  @Override
  public String getEnglishWord(int time) {
    return getWordsExplanationsAndTime(time).getEnglishWord();
  }
  
  @Override
  public PastPresentFutureWords getPastPresentFutureWords(int argTime) {
    for (int currIndex = 0; currIndex<lyrics.size(); currIndex++) {
      int currentIndexTime = lyrics.get(currIndex).getTime();
      int nextIndex = currIndex +1;
      if (nextIndex >= lyrics.size()) {
        WordsExplanationsAndTime lastLyrics = getLastWordsExplanationsAndTime();
        WordsExplanationsAndTime secondToLast = lyrics.get(lyrics.size()-2);
        return new PastPresentFutureWords()
                   .setPastSpanishEnglish(secondToLast.getSpanishDirect(), secondToLast.getSpanishMeaning(),
                                          secondToLast.getEnglishWord(),   secondToLast.getEnglishCorrected())
                   .setCurrSpanishEnglish(lastLyrics.getSpanishDirect(), lastLyrics.getSpanishMeaning(),
                                          lastLyrics.getEnglishWord(),   lastLyrics.getEnglishCorrected())
                   .setFutureSpanishEnglish("", "","","");
      }
      int nextIndexTime = lyrics.get(nextIndex).getTime();
      if (argTime == currentIndexTime || nextIndexTime > argTime) {
        PastPresentFutureWords ppfWords = new PastPresentFutureWords();
        if (currIndex > 0) {
          WordsExplanationsAndTime currMinus1 = lyrics.get(currIndex-1);
          ppfWords.setPastSpanishEnglish
            (currMinus1.getSpanishDirect(), currMinus1.getSpanishMeaning(),
             currMinus1.getEnglishWord(), currMinus1.getEnglishCorrected());
        }
        else {
          ppfWords.setPastSpanishEnglish("","","","");
        }
        WordsExplanationsAndTime currLyrics = lyrics.get(currIndex);
        ppfWords.setCurrSpanishEnglish(currLyrics.getSpanishDirect(), currLyrics.getSpanishMeaning(),
                                       currLyrics.getEnglishWord(), currLyrics.getEnglishCorrected());
        WordsExplanationsAndTime futureLyrics = lyrics.get(currIndex+1);
        ppfWords.setFutureSpanishEnglish(futureLyrics.getSpanishDirect(), futureLyrics.getSpanishMeaning(),
                                         futureLyrics.getEnglishWord(),   futureLyrics.getEnglishCorrected());
        return ppfWords;
      }
    } 
    WordsExplanationsAndTime lastLyrics = getLastWordsExplanationsAndTime(); //TODO this case is already taken care of.
    WordsExplanationsAndTime secondToLast = lyrics.get(lyrics.size()-2);
    return new PastPresentFutureWords()
               .setPastSpanishEnglish(secondToLast.getSpanishDirect(), secondToLast.getSpanishMeaning(),
                                      secondToLast.getEnglishWord(),   secondToLast.getEnglishCorrected())
               .setCurrSpanishEnglish(lastLyrics.getSpanishDirect(),   lastLyrics.getSpanishMeaning(),
                                      lastLyrics.getEnglishWord(),     lastLyrics.getEnglishCorrected())
               .setFutureSpanishEnglish("", "", "", "");
  }
}
