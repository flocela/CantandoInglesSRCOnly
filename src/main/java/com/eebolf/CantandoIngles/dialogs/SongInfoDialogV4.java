package com.eebolf.CantandoIngles.dialogs;

import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.SongsCP;

public class SongInfoDialogV4 extends SimpleDialogV4 {

	public static SongInfoDialogV4 newInstance (Bundle startingBundle) {
		SongInfoDialogV4 cD = new SongInfoDialogV4();
		cD.setArguments(startingBundle);
		return cD;
	}
	
	private static String inEnglishBoolean = "IN_ENGLISH_BOOLEAN";
	private TextView newWorkTextView;
	private TextView paragraphDescriptionTextView;
	private TextView originalSongTextView;
	private TextView originalArtistTextView;
	private TextView origSpanishUsesSamples1TextView;
	private TextView origDownloadedAtLinkTextView;
	private TextView origLicenseLinkTextView;
	private TextView origSpanishChangesMadeTextView;
	private TextView zipSizeTextView;
	private boolean inEnglish = false;	
	private Button okButton;
	
	
	private void setSongArtistTextView(String artistName) {
	  if (inEnglish) {
	    String artistLabel= "artist: " + artistName;
      Spannable origArtistSpan = new SpannableString(artistLabel);  
      origArtistSpan.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      SongInfoDialogV4.this.originalArtistTextView.setText(origArtistSpan);
	  }
	  else {
	    String artistLabel= "artista: " + artistName;
      Spannable origArtistSpan = new SpannableString(artistLabel);  
      origArtistSpan.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      originalArtistTextView.setText(origArtistSpan);
	  }
	}
	
	private void setDownloadSize(String zipSize) {
	  if (inEnglish) {    
      String zipSizeLabel= "translation + synchronization: " + zipSize + "MB";
      Spannable zipSizeSpan = new SpannableString(zipSizeLabel);
      zipSizeTextView.setText(zipSizeSpan);
    }
    else {
      String zipSizeLabel= "traducción + sincronización: " + zipSize + "MB";
      Spannable zipSizeSpan = new SpannableString(zipSizeLabel);
      zipSizeTextView.setText(zipSizeSpan);
    }
	}
	
	private void setOrigDownloadedAtNameAndLink(String name, String link) {
	  if (inEnglish) {
	    origDownloadedAtLinkTextView
	      .setText(Html.fromHtml
                 ("<i>downloaded at</i>:&nbsp<a href=\""
                   + link + "\">"+name+"</a>"));
	  }
	  else {
      origDownloadedAtLinkTextView
      .setText(Html.fromHtml
               ("<i>descargada de</i>:&nbsp<a href=\""
                 + link + "\">"+name+"</a>"));
	  }	  
    origDownloadedAtLinkTextView.setClickable(true);
    origDownloadedAtLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	private void setUsesSampleFromInfo(Bundle bundle) {
	  String origUsesSampleFromLinkName = bundle.getString(SongsCP.orig_uses_sample_from_link_name_1);
	  String origUsesSampleFromLink = bundle.getString(SongsCP.orig_uses_sample_from_link_1);
	  String origEnglishUsesSample1String = bundle.getString(SongsCP.orig_english_uses_sample_from_1);
	  if (null != origEnglishUsesSample1String) {
	    if (inEnglish) {
	      origSpanishUsesSamples1TextView.setText(Html.fromHtml
	          ("<i>uses samples from</i>:&nbsp"+" "+origEnglishUsesSample1String+"<a href=\"" 
	          + origUsesSampleFromLink + "\">"+origUsesSampleFromLinkName+"</a>"));    
	    }
	    else {
	      String origSpanishUsesSample1String = bundle.getString(SongsCP.orig_spanish_uses_sample_from_1);
	      origSpanishUsesSamples1TextView.setText(Html.fromHtml
	          ("<i>usa muestra de</i>:&nbsp"+" "+origSpanishUsesSample1String+"<a href=\"" 
	              + origUsesSampleFromLink + "\">"+origUsesSampleFromLinkName+"</a>"));      
	    }
	  }
	  else {
	    origSpanishUsesSamples1TextView.setVisibility(View.GONE);
	  }
	  
	  origSpanishUsesSamples1TextView.setClickable(true);
    origSpanishUsesSamples1TextView.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	private void setLicenseInfo(Bundle bundle) {
	  String origLicenseLinkName = bundle.getString(SongsCP.orig_license_name);
    String origLicenseLink = bundle.getString(SongsCP.orig_license_link);
    String nameOfNewWorkLicense = bundle.getString(SongsCP.name_of_new_work_license);
    String linkToNewWorkLicense = bundle.getString(SongsCP.link_to_new_work_license);
    String newWorkTitle = bundle.getString(SongsCP.new_work_title);
    String origArtist = bundle.getString(SongsCP.orig_artist);
    String englishTitle = bundle.getString(SongsCP.orig_english_title);
	  if (inEnglish) {	    
      origLicenseLinkTextView.setText(
          Html.fromHtml("<i>license</i>:&nbsp<a href=\""+origLicenseLink+"\">"+origLicenseLinkName+"</a>"));
      
      paragraphDescriptionTextView.setText(Html.fromHtml(
          newWorkTitle + " is a derivative of the song \'" + englishTitle + 
          "' by " + origArtist + ", under the license " +
          "<a href=\""+origLicenseLink+"\">"+origLicenseLinkName+"</a>" +
          ". \'" + newWorkTitle + "\' is licensed under " +
          "<a href=\""+linkToNewWorkLicense+"\">"+nameOfNewWorkLicense+"</a>" +
          "by Flo Maldonado.")); 
	  }
	  else {	    
	    origLicenseLinkTextView.setText(
	        Html.fromHtml("<i>licensia</i>:&nbsp<a href=\""+origLicenseLink+"\">"+origLicenseLinkName+"</a>"));
	    paragraphDescriptionTextView.setText(Html.fromHtml(
	        newWorkTitle + " es un derivado de la canción \'" + englishTitle + 
	        "' por " + origArtist + ", usando la licencia " +
	        "<a href=\""+origLicenseLink+"\">"+origLicenseLinkName+"</a>" +
	        ". \'" + newWorkTitle + "\' esta licenciada usando " +
	        "<a href=\""+linkToNewWorkLicense+"\">"+nameOfNewWorkLicense+"</a>" +
	        "por Flo Maldonado."));	    
	  }
	  origLicenseLinkTextView.setClickable(true);
    origLicenseLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    paragraphDescriptionTextView.setClickable(true);
    paragraphDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	private void setModificationString (Bundle bundle) {
	  Spannable modifications = null;
	  if (inEnglish) {
	    String modificacionesString = "modifications: " + bundle.getString(SongsCP.orig_english_changes_made);
	    modifications = new SpannableString(modificacionesString);  
	    modifications.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	  }
	  else {	    
	    String modificacionesString = "modificaciónes: " + bundle.getString(SongsCP.orig_spanish_changes_made);
	    modifications = new SpannableString(modificacionesString);  
	    modifications.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	  }
    origSpanishChangesMadeTextView.setText(modifications);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	  
	  if (null != savedInstanceState && savedInstanceState.containsKey(inEnglishBoolean)) {
	    inEnglish = savedInstanceState.getBoolean(inEnglishBoolean);
	  }
	  
		View v = inflater.inflate(R.layout.dialog_song_info, container, false); 
		newWorkTextView = (TextView)v.findViewById(R.id.titles);
		paragraphDescriptionTextView = (TextView)v.findViewById(R.id.paragraph_description);
		originalSongTextView = (TextView)v.findViewById(R.id.original_song);
		originalArtistTextView = (TextView)v.findViewById(R.id.orig_artist);
		origSpanishUsesSamples1TextView = (TextView)v.findViewById(R.id.orig_spanish_uses_samples_1);
		origDownloadedAtLinkTextView = (TextView)v.findViewById(R.id.orig_downloaded_at_link);
		origLicenseLinkTextView = (TextView)v.findViewById(R.id.orig_license_link);
		origSpanishChangesMadeTextView = (TextView)v.findViewById(R.id.orig_spanish_changes_made);
		zipSizeTextView = (TextView)v.findViewById(R.id.zip_size);
		
		Bundle args = this.getArguments();
		String newWorkTitle = args.getString(SongsCP.new_work_title);
		newWorkTextView.setText(newWorkTitle);		
		
		String englishTitle = args.getString(SongsCP.orig_english_title);
		originalSongTextView.setText(englishTitle);
		
		String origArtist = args.getString(SongsCP.orig_artist);
		setSongArtistTextView(origArtist);
		
		String zipSize = args.getString(SongsCP.zip_size);
		setDownloadSize(zipSize);
		
		setUsesSampleFromInfo(args);		
		
		setOrigDownloadedAtNameAndLink(args.getString(SongsCP.orig_downloaded_at_name),
		                               args.getString(SongsCP.orig_downloaded_at_link));
		
		setLicenseInfo(args);	    
    
		setModificationString(args);
    
		okButton = (Button)v.findViewById(R.id.btn_ok);
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SongInfoDialogV4.this.dismiss();
			}
		});
		
		Button languageButton = (Button)v.findViewById(R.id.btn_language);    
		if (SongInfoDialogV4.this.inEnglish) {
		  languageButton.setText("Español");
		  okButton.setText("Close");
    }
    else {
      languageButton.setText("English");
      okButton.setText(getString(R.string.terminado));
    }
		languageButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        SongInfoDialogV4.this.inEnglish = !SongInfoDialogV4.this.inEnglish;
        if (SongInfoDialogV4.this.inEnglish) {
          ((Button)v).setText("Español");
          SongInfoDialogV4.this.okButton.setText("Close");
        }
        else {
          ((Button)v).setText("English");
          SongInfoDialogV4.this.okButton.setText(getString(R.string.terminado));
        }
        
        Bundle args = SongInfoDialogV4.this.getArguments();
        setSongArtistTextView(args.getString(SongsCP.orig_artist));
        setDownloadSize(args.getString(SongsCP.zip_size));
        setOrigDownloadedAtNameAndLink(args.getString(SongsCP.orig_downloaded_at_name),
            args.getString(SongsCP.orig_downloaded_at_link));
        setLicenseInfo(args);
        setUsesSampleFromInfo(args);
        setModificationString(args);
      }
    });

		return v;
	}
	
	public void onSaveInstanceState(Bundle outState) {
	  super.onSaveInstanceState(outState);
	  outState.putBoolean(inEnglishBoolean, this.inEnglish);
	}
}
