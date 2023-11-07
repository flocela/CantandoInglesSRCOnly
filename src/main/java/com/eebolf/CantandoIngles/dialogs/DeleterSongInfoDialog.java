package com.eebolf.CantandoIngles.dialogs;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.eebolf.CantandoIngles.SongsCP;
import com.eebolf.CantandoIngles.R;

public class DeleterSongInfoDialog extends SimpleDialog {

	public static DeleterSongInfoDialog newInstance (Bundle startingBundle) {
		DeleterSongInfoDialog cD = new DeleterSongInfoDialog();
		cD.setArguments(startingBundle);
		return cD;
	}
	
	private static String inEnglishBoolean = "IN_ENGLISH_BOOLEAN";
  private boolean inEnglish = false;
	
  private TextView titleTextView;
  private TextView originalSongTextView;
  private TextView origArtistTextView;
  private TextView zipSizeTextView;
  private Button okButton;
  
  
  private void setArtistInfo(String origArtist) {
    String artistLabel = null;
    if (inEnglish) {
      artistLabel= "artist: " + origArtist;
    }
    else {
      artistLabel= "artista: " + origArtist;
    }
    Spannable origArtistSpan = new SpannableString(artistLabel);  
    origArtistSpan.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    origArtistTextView.setText(origArtistSpan);
  }
  
  private void setZipSize(String zipSize) {
    String zipSizeLabel = null;
    Spannable zipSizeSpan = null;
    if (inEnglish) {
      zipSizeLabel= "translation and synchronization: " + zipSize + " MB";
      zipSizeSpan = new SpannableString(zipSizeLabel);
      zipSizeSpan.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);    
    }
    else {
      zipSizeLabel= "traducción y sincronización: " + zipSize + " MB";
      zipSizeSpan = new SpannableString(zipSizeLabel);
      zipSizeSpan.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);    
    }
    zipSizeTextView.setText(zipSizeSpan);
  }
  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	  if (null != savedInstanceState && savedInstanceState.containsKey(inEnglishBoolean)) {
	    inEnglish = savedInstanceState.getBoolean(inEnglishBoolean);
	  }
	  
		View v = inflater.inflate(R.layout.dialog_delete_song_info, container, false); 

		titleTextView = (TextView)v.findViewById(R.id.titles);
		originalSongTextView = (TextView)v.findViewById(R.id.original_song);
		origArtistTextView = (TextView)v.findViewById(R.id.orig_artist);
		zipSizeTextView = (TextView)v.findViewById(R.id.zip_size);
	
		Bundle args = this.getArguments();
		titleTextView.setText(args.getString(SongsCP.new_work_title));
		
		setArtistInfo(args.getString(SongsCP.orig_artist));   
    
		originalSongTextView.setText(args.getString(SongsCP.orig_english_title));
		okButton = (Button)v.findViewById(R.id.btn_ok);	
		
		setZipSize(args.getString(SongsCP.zip_size));

		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DeleterSongInfoDialog.this.dismiss();
			}
		});
		
		Button languageButton = (Button)v.findViewById(R.id.btn_language);    
    if (DeleterSongInfoDialog.this.inEnglish) {
      languageButton.setText("Español");
      okButton.setText("Close");
    }
    else {
      languageButton.setText("English");
      okButton.setText(getString(R.string.terminado));
    }
    languageButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        DeleterSongInfoDialog.this.inEnglish = !DeleterSongInfoDialog.this.inEnglish;
        if (DeleterSongInfoDialog.this.inEnglish) {
          ((Button)v).setText("Español");
          DeleterSongInfoDialog.this.okButton.setText("Close");
        }
        else {
          ((Button)v).setText("English");
          DeleterSongInfoDialog.this.okButton.setText(getString(R.string.terminado));
        }
        Bundle args = DeleterSongInfoDialog.this.getArguments();
        setArtistInfo(args.getString(SongsCP.orig_artist));  
        setZipSize(args.getString(SongsCP.zip_size));
      }
    });

		return v;
	}
	
	 public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putBoolean(inEnglishBoolean, this.inEnglish);
	  }
}
