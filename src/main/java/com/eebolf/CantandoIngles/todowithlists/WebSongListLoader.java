package com.eebolf.CantandoIngles.todowithlists;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Xml;

import com.eebolf.CantandoIngles.collectors.SongInfo;
import com.eebolf.CantandoIngles.start.TracedException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebSongListLoader extends AsyncTaskLoader<List<SongInfo>> {
	
	final InterestingConfigChanges mLastConfig = new InterestingConfigChanges();
  private String urlString;
  private String errorString;  
  private List<SongInfo> songInfos;
  private TracedException tracedException = null;
  
  public WebSongListLoader (Context context, String urlString) {
  	super(context); 	
  	this.urlString = urlString;
  }
  
  @Override 
  public List<SongInfo> loadInBackground() {
  	URL url;
  	BufferedInputStream inStream = null;
  	List<SongInfo> entries = null;
  	HttpURLConnection conn = null;
  	try {
  		url = new URL(urlString);
  		conn = (HttpURLConnection) url.openConnection();  		
  		conn.setReadTimeout(10000 /* milliseconds */);
      conn.setConnectTimeout(15000 /* milliseconds */);
      conn.setDoInput(true);
      conn.setRequestProperty("Accept","text/xml");
			conn.connect();
			/*if (true)
			  throw new IOException("fake IO Exception");
			Log.i("ATAG", "WebSongListLoader shouldn't get here");*/
  		inStream = new BufferedInputStream(conn.getInputStream());
  		SetXmlParser xmlParser = new SetXmlParser();
  		entries = xmlParser.parse(inStream);
  		inStream.close();
  		return entries; 
  	}
		catch (Exception e) {
			tracedException = new TracedException(e, "WebSongListLoader ");
			return entries;
		}
  	finally {
  		if (null != inStream) {
  			try {
					inStream.close();
					return entries;
				} catch (IOException e) {
				  tracedException = new TracedException (e, "WebSongListLoader .inStream.close(); ");
		      return entries;
				}
  		} 
  	}
  }
  
  public TracedException getTracedException () {
    return tracedException;
  }

  @Override 
  public void deliverResult(List<SongInfo> preparedSetes) {  	
  	if (isStarted()) {
  		// If the Loader is currently started, we can immediately
  		// deliver its results.
  		super.deliverResult(preparedSetes);
  	}
  }
  
  protected void onReleaseResources(List<SongInfo> apps) {}
    
  @Override 
  protected void onStartLoading() { // TODO does readySetzes really need to reload on a config change?
  	if (songInfos != null) {
  		deliverResult(songInfos);
  	}

  	// Has something interesting in the configuration changed since we
  	// last built the app list?
  	boolean configChange = mLastConfig.applyNewConfig(getContext().getResources());

  	if (takeContentChanged() || songInfos == null || configChange) {
  		// If the data has changed since the last time it was loaded
  		// or is not currently available, start a load.
  		forceLoad();
  	}
  }
  
  // TODO this is probably not necessary.
  public static class InterestingConfigChanges {
  	final Configuration mLastConfiguration = new Configuration();
  	int mLastDensity;

  	boolean applyNewConfig(Resources res) {
  		int configChanges = mLastConfiguration.updateFrom(res.getConfiguration());
  		boolean densityChanged = mLastDensity != res.getDisplayMetrics().densityDpi;
  		if (densityChanged || (configChanges&(ActivityInfo.CONFIG_LOCALE
  				|ActivityInfo.CONFIG_UI_MODE|ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
  			mLastDensity = res.getDisplayMetrics().densityDpi;
  			return true;
  		}
  		return false;
  	}
  }
  
  @Override 
  protected void onStopLoading() {
  	// Attempt to cancel the current load task if possible.
  	cancelLoad();
  }
  
  @Override 
  public void onCanceled(List<SongInfo> apps) {
  	super.onCanceled(apps);
  	// At this point we can release the resources associated with 'apps'
  	// if needed.
  	onReleaseResources(apps);
  } 
  
  @Override 
  protected void onReset() {
  	super.onReset();

  	// Ensure the loader is stopped
  	onStopLoading();

  	// At this point we can release the resources associated with 'apps'
  	// if needed.
  	if (songInfos!= null) {
  		onReleaseResources(songInfos);
  		songInfos = null;
  	}
  }
  
  
  private class SetXmlParser {
		// TODO We don't use namespaces
		public List<SongInfo> parse(InputStream in) throws XmlPullParserException, IOException {
			try {
				XmlPullParser parser = Xml.newPullParser();
				parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
				parser.setInput(in, null);
				parser.next();
				return readFeed(parser);
			} finally {
				in.close();
			}
		}

		private String getStringFromTag(XmlPullParser parser, String tag) 
		    throws XmlPullParserException, IOException {
      while (parser.getEventType() != XmlPullParser.START_TAG) {
        parser.next();
      }
      if (parser.getName().equals(tag)) {
        parser.next();
        return parser.getText();
      }
      return null;
    }
    
    private Integer getIntegerFromTag(XmlPullParser parser, String tag) 
        throws XmlPullParserException, IOException {
      while (parser.getEventType() != XmlPullParser.START_TAG) {
        parser.next();
      }
      if (parser.getName().equals(tag)) {
        parser.next();
        return Integer.parseInt(parser.getText());
      }
      return null;
    }
		
		private List<SongInfo> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
			List<SongInfo> entries = new ArrayList<SongInfo>();
			
			do {	

			  if (parser.getEventType() == XmlPullParser.START_TAG) {
			    Integer webId = null;
			    String filename = null;
			    String linkToNewWorkLicense = null;
			    String nameOfNewWorkLicense = null;
			    String newWorkTitle = null;
			    String origArtist = null;
			    String origDownloadedAtLink = null;
			    String origDownloadedAtName = null;
			    String origEnglishChangesMade = null;
			    String origEnglishTitle = null;
			    String origEnglishUsesSampleFrom1 = null;
			    String origLicenseLink = null;
			    String origLicenseName = null;
			    String origSpanishChangesMade = null;
			    String origSpanishUsesSampleFrom1 = null;
			    String origUsesSampleFromLink1 = null;
			    String origUsesSampleFromLinkName1 = null;
			    Integer songType = null;
			    String zipSize = null;
			    

			    if (parser.getName().equals("filename")) {
            parser.next();
            filename = parser.getText();
            webId = getIntegerFromTag(parser, "id");
            linkToNewWorkLicense = getStringFromTag(parser, "link-to-new-work-license");
            nameOfNewWorkLicense = getStringFromTag(parser, "name-of-new-work-license");
            newWorkTitle = getStringFromTag(parser, "new-work-title");
            origArtist = getStringFromTag(parser, "orig-artist");
            origDownloadedAtLink = getStringFromTag(parser, "orig-downloaded-at-link");
            origDownloadedAtName = getStringFromTag(parser, "orig-downloaded-at-name");
            origEnglishChangesMade = getStringFromTag(parser, "orig-english-changes-made");
            origEnglishTitle = getStringFromTag(parser, "orig-english-title");
            origEnglishUsesSampleFrom1 = getStringFromTag(parser, "orig-english-uses-sample-from-1");
            origLicenseLink = getStringFromTag(parser, "orig-license-link");
            origLicenseName = getStringFromTag(parser, "orig-license-name");
            origSpanishChangesMade = getStringFromTag(parser, "orig-spanish-changes-made");
            origSpanishUsesSampleFrom1 = getStringFromTag(parser, "orig-spanish-uses-sample-from-1");
            origUsesSampleFromLink1 = getStringFromTag(parser, "orig-uses-sample-from-link-1");
            origUsesSampleFromLinkName1 = getStringFromTag(parser, "orig-uses-sample-from-link-name-1");
            songType = getIntegerFromTag(parser, "song-type");
            zipSize = getStringFromTag(parser, "zip-size");
            
            SongInfo songInfo = new SongInfo(webId);
            songInfo.setFilename(filename);
            songInfo.setLinkToNewWorkLicense(linkToNewWorkLicense);
            songInfo.setNameOfNewWorkLicense(nameOfNewWorkLicense);
            songInfo.setNewWorkTitle(newWorkTitle);
            songInfo.setOrigArtist(origArtist);
            songInfo.setOrigDownloadedAtLink(origDownloadedAtLink);
            songInfo.setOrigDownloadedAtName(origDownloadedAtName);
            songInfo.setOrigEnglishChangesMade(origEnglishChangesMade);
            songInfo.setOrigEnglishTitle(origEnglishTitle);
            songInfo.setOrigEnglishUsesSampleFrom1(origEnglishUsesSampleFrom1);
            songInfo.setOrigLicenseLink(origLicenseLink);
            songInfo.setOrigLicenseName(origLicenseName);
            songInfo.setOrigSpanishChangesMade(origSpanishChangesMade);
            songInfo.setOrigSpanishUsesSampleFrom1(origSpanishUsesSampleFrom1);
            songInfo.setOrigUsesSampleFromLink1(origUsesSampleFromLink1);
            songInfo.setOrigUsesSampleFromLinkName1(origUsesSampleFromLinkName1);
            songInfo.setSongType(""+songType);
            songInfo.setZipSize(zipSize);
			      entries.add(songInfo);
			    }			
			  }
				parser.next();				
			}while(parser.getEventType() != XmlPullParser.END_DOCUMENT);
						
			return entries;
		}
			
	}
  
}
