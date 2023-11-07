package com.eebolf.CantandoIngles.collectors;

public class WordsExplanationsAndTime {
  private String englishWord;
  private String englishCorrected;
  private String spanishDirect;
  private String spanishMeaning;
  private int time; //in milliseconds from start of song.


  public WordsExplanationsAndTime (int time, String english, String spanishDirect) {
    this.time = time;
    this.englishWord = english;
    this.spanishDirect = spanishDirect;
  }

  public WordsExplanationsAndTime (int time, String english, String spanishDirect, String spanishMeaning) {
    this.englishWord = english;
    this.spanishDirect = spanishDirect;
    this.spanishMeaning = spanishMeaning;
    this.time = time;
  }

  public WordsExplanationsAndTime (int time,
                                   String english,
                                   String englishCorrected,
                                   String spanishDirect,
                                   String spanishMeaning) {
    this.englishWord = english;
    this.spanishDirect = spanishDirect;
    this.spanishMeaning = spanishMeaning;
    this.englishCorrected = englishCorrected;
    this.time = time;
  }

  public void setEnglishCorrected (String englishCorrected) {
    this.englishCorrected = englishCorrected;
  }
  
  public String getSpanishDirect () {
    return spanishDirect;
  }

  public String getSpanishMeaning () { return spanishMeaning; }
  
  public String getEnglishWord () {
    return englishWord;
  }

  public String getEnglishCorrected () { return englishCorrected;}
  
  public int getTime () {
    return time;
  }

  public boolean hasSpanishMeaning () {
    if (spanishMeaning == null) {
      return false;
    }
    else if (spanishMeaning != null && spanishMeaning.isEmpty()) {
      return false;
    }
    return true;
  }

  public boolean hasEnglishCorrected () {
    if (englishCorrected == null) {
      return false;
    }
    else if (englishCorrected != null && englishCorrected.isEmpty()) {
      return false;
    }
    return true;
  }
}
