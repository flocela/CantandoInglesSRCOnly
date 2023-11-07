package com.eebolf.CantandoIngles.collectors;

public class WordsAndTime {
  private String englishWord;
  private String spanishWord;
  private int time; //in milliseconds from start of song.
  
  public WordsAndTime (int time, String english, String spanish) {
    this.englishWord = english;
    this.spanishWord = spanish;
    this.time = time;
  }
  
  public String getSpanishWord () {
    return spanishWord;
  }
  
  public String getEnglishWord () {
    return englishWord;
  }
  
  public int getTime () {
    return time;
  }
}
