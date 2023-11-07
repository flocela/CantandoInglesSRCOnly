package com.eebolf.CantandoIngles.start;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.eebolf.CantandoIngles.collectors.WordsExplanationsAndTime;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// Whole purpose is to keep a reference to the LyricDevice through
// configuration changes.
public class LyricHolderFragment extends Fragment {
  private boolean      mReady   = false;
  private boolean      mQuiting = false;
  private LyricDevice  lyricDevice = null;
  private String       fullFileName = null;

  /** Bundle requires LyricActivity.LYRIC_FILENAME with full file name
   *  Makes a LyricDevice and keeps it through the Activity's configuration
   *  changes.
   */
  public static LyricHolderFragment newInstance(Bundle bundle) {
    LyricHolderFragment lyricHolderFragment = new LyricHolderFragment();
    lyricHolderFragment.setArguments(bundle);
    return lyricHolderFragment;
  }

  final Thread mThread = new Thread () {
    @Override
    public void run () {
      createLyricHolder();
      synchronized (this) {
        while (!mReady && !mQuiting) {
          try {
            this.wait();
          }
          catch (InterruptedException e) {}
        }
        if (mQuiting) {
          return;
        }
        ((Listener)getActivity()).receivingNotificationLyricHolderReady();
      }
    }
  };

  public LyricDevice getLyricDevice() {
    return lyricDevice;
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    Bundle args = getArguments();
    fullFileName = args.getString(LyricActivity.LYRIC_FILENAME);
    mThread.start();
  }

  @Override
  public void onActivityCreated (Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    synchronized (mThread) {
      mReady = true;
      mThread.notify();
    }
    if (null != lyricDevice)
      ((Listener)getActivity()).receivingNotificationLyricHolderReady();
  }

  @Override
  public void onDestroy () {
    super.onDestroy();
    synchronized (mThread) {
      mReady = false;
      mQuiting = true;
      mThread.notify();
    }
  }

  @Override
  public void onDetach () {
    super.onDetach();
    synchronized (mThread) {
      mReady = false;
      mThread.notify();
    }
  }

  private void createLyricHolder() {
    // Put lyric file in main/assets.
    // is is used for TIMING. Comment out try block when deploying.
    InputStream is = null;
    /*
    try {
      is = getContext().getAssets().open(fullFileName);
    } catch (IOException e) {
      e.printStackTrace();
    }
    */

    File lyricsFile = new File(fullFileName);
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;

    try {
      docBuilder = docBuilderFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      TracedException tr =
        new TracedException(e, "CommonLyricDevice.setupLyricViews.newDocumentBuilder();"
          + fullFileName);
      throw tr;
    }
    Document doc = null;
    try {
      // use if deploying.
      doc = docBuilder.parse(lyricsFile);

      // use if for TIMING
      //doc = docBuilder.parse(is);

    } catch (Exception e) {
      TracedException tr =
        new TracedException(e, "CommonLyricDevice.setupLyricViews.parse;"
          + fullFileName);
      throw tr;
    }

    ArrayList<WordsExplanationsAndTime> lyrics = new ArrayList<>();
    NodeList listOfLyrics = doc.getElementsByTagName("LYRIC");
    int numOfLyrics = listOfLyrics.getLength();
    if (mQuiting)
      return;
    for (int lyric = 0; lyric < numOfLyrics; lyric++) {
      Node lyricNode = listOfLyrics.item(lyric);
      if (lyricNode.getNodeType() == Node.ELEMENT_NODE) {
        Element lyricElement = (Element) lyricNode;
        String time = getTextFrom(lyricElement, "TIME");
        String english = getTextFrom(lyricElement, "ENGLISH");
        String englishCorrected = getTextFrom(lyricElement, "ENGLISH_CORRECTED");
        String spanish = getTextFrom(lyricElement, "SPANISH");
        String spanishMeaning = getTextFrom(lyricElement, "SPANISH_MEANING");
        lyrics.add(new WordsExplanationsAndTime(Integer.parseInt(time),
          english,
          englishCorrected,
          spanish,
          spanishMeaning));
      }
    }
    lyricDevice = new CommonLyricDevice(lyrics);
  }

  private String getTextFrom (Element element, String xmlTagString) {
    NodeList nodeList = element.getElementsByTagName(xmlTagString);
    if ( 0 < nodeList.getLength() ) {
      Element subElement = (Element)nodeList.item(0);
      NodeList subNodeList = subElement.getChildNodes();
      Node tagNode = subNodeList.item(0);
      if (null != tagNode) {
        return tagNode.getNodeValue().trim();
      }
    }
    return "";
  }

  interface Listener {
    void receivingNotificationLyricHolderReady();
  }

}
