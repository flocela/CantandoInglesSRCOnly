package com.eebolf.CantandoIngles.collectors;

import android.support.annotation.NonNull;

import java.util.Comparator;

public class SongInfo implements Comparable<SongInfo>{
  private Integer webId;
  private String filename;
  private String linkToNewWorkLicense;
  private String nameOfNewWorkLicense;
  private String newWorkTitle;
  private String origArtist;
  private String origDownloadedAtLink;
  private String origDownloadedAtName;
  private String origEnglishChangesMade;
  private String origEnglishTitle;
  private String origEnglishUsesSampleFrom1;
  private String origLicenseLink;
  private String origLicenseName;
  private String origSpanishChangesMade;
  private String origSpanishUsesSampleFrom1;
  private String origUsesSampleFromLink1;
  private String origUsesSampleFromLinkName1;
  private String songType;
  private String zipSize;

  public SongInfo (Integer webId) {
    this.webId = webId;
  }
  
  public String getFilename () {
    return this.filename;
  }
  
  public String getLinkToNewWorkLicense () {
    return this.linkToNewWorkLicense;
  }
  
  public String getNameOfNewWorkLicense () {
    return this.nameOfNewWorkLicense;
  }

  public String getNewWorkTitle () {
    return this.newWorkTitle;
  }

  public String getOrigArtist () {
    return this.origArtist;
  }

  public String getOrigDownloadedAtLink () {
    return this.origDownloadedAtLink;
  }

  public String getOrigDownloadedAtName () {
    return this.origDownloadedAtName;
  }
  
  public String getOrigEnglishChangesMade () {
    return this.origEnglishChangesMade;
  }
  
  public String getOrigEnglishTitle () {
    return this.origEnglishTitle;
  }
  
  public String getOrigEnglishUsesSampleFrom1 () {
    return this.origEnglishUsesSampleFrom1;
  }
  
  public String getOrigLicenseLink () {
    return this.origLicenseLink;
  }
  
  public String getOrigLicenseName () {
    return this.origLicenseName;
  }
  
  public String getOrigSpanishChangesMade () {
    return this.origSpanishChangesMade;
  }
  
  public String getOrigSpanishUsesSampleFrom1 () {
    return this.origSpanishUsesSampleFrom1;
  }  
  
  public String getOrigUsesSampleFromLink1 () {
    return this.origUsesSampleFromLink1;
  }
  
  public String getOrigUsesSampleFromLinkName1 () {
    return this.origUsesSampleFromLinkName1;
  }
  
  public String getSongType () {
    return this.songType;
  }

  public String getZipSize () {
    return this.zipSize;
  }
  
  public Integer getWebId () {
    return this.webId;
  }
  
  
  ///////////////////////////////////////////////////////////////
  
  
  
  public void setFilename (String filename) {
     this.filename = filename;
  }
  
  public void setLinkToNewWorkLicense (String linkToNewWorkLicense) {
     this.linkToNewWorkLicense = linkToNewWorkLicense;
  }
  
  public void setNameOfNewWorkLicense (String nameOfNewWorkLicense) {
     this.nameOfNewWorkLicense = nameOfNewWorkLicense;
  }

  public void setNewWorkTitle (String newWorkTitle) {
     this.newWorkTitle = newWorkTitle;
  }

  public void setOrigArtist (String origArtist) {
     this.origArtist = origArtist;
  }

  public void setOrigDownloadedAtLink (String origDownloadedAtLink) {
     this.origDownloadedAtLink = origDownloadedAtLink;
  }

  public void setOrigDownloadedAtName (String origDownloadedAtName) {
     this.origDownloadedAtName = origDownloadedAtName;
  }
  
  public void setOrigEnglishChangesMade (String origEnglishChangesMade) {
     this.origEnglishChangesMade = origEnglishChangesMade;
  }
  
  public void setOrigEnglishTitle (String origEnglishTitle) {
     this.origEnglishTitle = origEnglishTitle;
  }
  
  public void setOrigEnglishUsesSampleFrom1 (String origEnglishUsesSampleFrom1) {
     this.origEnglishUsesSampleFrom1 = origEnglishUsesSampleFrom1;
  }
  
  public void setOrigLicenseLink (String origLicenseLink) {
     this.origLicenseLink = origLicenseLink;
  }
  
  public void setOrigLicenseName (String origLicenseName) {
     this.origLicenseName = origLicenseName;
  }
  
  public void setOrigSpanishChangesMade (String origSpanishChangesMade) {
     this.origSpanishChangesMade = origSpanishChangesMade;
  }
  
  public void setOrigSpanishUsesSampleFrom1 (String origSpanishUsesSampleFrom1) {
     this.origSpanishUsesSampleFrom1 = origSpanishUsesSampleFrom1;
  }  
  
  public void setOrigUsesSampleFromLink1 (String origUsesSampleFromLink1) {
     this.origUsesSampleFromLink1 = origUsesSampleFromLink1;
  }
  
  public void setOrigUsesSampleFromLinkName1 (String origUsesSampleFromLinkName1) {
     this.origUsesSampleFromLinkName1 = origUsesSampleFromLinkName1;
  }
  
  public void setSongType (String songType) {
     this.songType = songType;
  }

  public void setZipSize (String zipSize) {
     this.zipSize = zipSize;
  }

  //////////////////


  @Override
  public int compareTo(@NonNull SongInfo another) {
    Integer thisType = Integer.parseInt(songType);
    Integer otherType = Integer.parseInt(another.songType);
    if (thisType < otherType)
      return -1;
    else if (thisType > otherType)
      return 1;
    else {
      return this.newWorkTitle.compareTo(another.newWorkTitle);
    }
  }

  public static Comparator<SongInfo> SongInfoComparator = new Comparator<SongInfo>() {
    @Override
    public int compare(SongInfo lhs, SongInfo rhs) {
      return lhs.compareTo(rhs);
    }
  };
}
