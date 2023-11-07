package com.eebolf.CantandoIngles.start;

public class PastPresentFutureWords {
  
  private String pastEnglish;
  private String pastEnglishCorrected;
  private String pastSpanish;
  private String pastSpanishMeaning;
  private String currEnglish;
  private String currEnglishCorrected;
  private String currSpanish;
  private String currSpanishMeaning;
  private String futureEnglish;
  private String futureEnglishCorrected;
  private String futureSpanish;
  private String futureSpanishMeaning;
  
  public PastPresentFutureWords () {}
  
  public PastPresentFutureWords setPastSpanishEnglish (String spanish,
                                                       String spanishMeaning,
                                                       String english,
                                                       String englishCorrected) {
    this.pastEnglish = english;
    this.pastEnglishCorrected = englishCorrected;
    this.pastSpanish = spanish;
    this.pastSpanishMeaning = spanishMeaning;
    return this;
  }
  
  public PastPresentFutureWords setCurrSpanishEnglish (String spanish,
                                                       String spanishMeaning,
                                                       String english,
                                                       String englishCorrected) {
    this.currEnglish = english;
    this.currEnglishCorrected = englishCorrected;
    this.currSpanish = spanish;
    this.currSpanishMeaning = spanishMeaning;
    return this;
  }
  
  public PastPresentFutureWords setFutureSpanishEnglish (String spanish,
                                                         String spanishMeaning,
                                                         String english,
                                                         String englishCorrected
                                                         ) {
    this.futureEnglish = english;
    this.futureEnglishCorrected = englishCorrected;
    this.futureSpanish = spanish;
    this.futureSpanishMeaning = spanishMeaning;
    return this;
  }
  
  public String getPastEnglish () {
    return pastEnglish;
  }

  public String getPastEnglishCorrected () {return pastEnglishCorrected;}

  public String getPastSpanish () {
    return pastSpanish;
  }

  public String getPastSpanishMeaning () {return pastSpanishMeaning;}
  
  public String getCurrEnglish () {
    return currEnglish;
  }

  public String getCurrEnglishCorrected () {return currEnglishCorrected;}
  
  public String getCurrSpanish() {
    return currSpanish;
  }

  public String getCurrSpanishMeaning () {return currSpanishMeaning;}
  
  public String getFutureEnglish () {
    return futureEnglish;
  }

  public String getFutureEnglishCorrected () {return futureEnglishCorrected;}
  
  public String getFutureSpanish () {
    return futureSpanish;
  }

  public String getFutureSpanishMeaning () {return futureSpanishMeaning;}
}
