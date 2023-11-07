package com.eebolf.CantandoIngles.dialogs;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;

public class MainActivityInstructionsDialogV4 extends SimpleDialogV4 {

	public static MainActivityInstructionsDialogV4 newInstance (Bundle startingBundle) {
		MainActivityInstructionsDialogV4 cD = new MainActivityInstructionsDialogV4();
		cD.setArguments(startingBundle);
		return cD;
	}
	
	private static String inEnglishBoolean = "IN_ENGLISH_BOOLEAN";	
	private boolean inEnglish = false;	
	private Button okButton;
  private View spanishText;
  private View englishText;
  private View englishTitle;
  private View spanishTitle;
  private Button languageButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	  
	  if (null != savedInstanceState && savedInstanceState.containsKey(inEnglishBoolean)) {
	    inEnglish = savedInstanceState.getBoolean(inEnglishBoolean);
	  }

    ImageSpan downloadImageSpan = getImageSpan(R.drawable.ic_download_in_text);
    ImageSpan xImageSpan = getImageSpan(R.drawable.ic_delete_in_text);
    ImageSpan songInfoImageSpan = getImageSpan(R.drawable.ic_info_in_text);
    ImageSpan threeDotsImageSpan = getImageSpan(R.drawable.ic_menu_three_dots_in_text);
    ImageSpan lyricDisplayImageSpan = getImageSpan(R.drawable.ic_switch_view_in_text);

		View v = inflater.inflate(R.layout.dialog_main_activity_instructions, container, false);
    englishText = v.findViewById(R.id.english_explanation);
    spanishText = v.findViewById(R.id.spanish_explanation);

    englishTitle = v.findViewById(R.id.en_help_title);
    spanishTitle = v.findViewById(R.id.es_help_title);

    TextView enDownloadTextView = (TextView)v.findViewById(R.id.en_download_instructions);
    SpannableString enDownloadSpannableString = new SpannableString("To download a song press     .");
    enDownloadSpannableString.setSpan(downloadImageSpan, 24, 29, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    enDownloadTextView.setText(enDownloadSpannableString);

    TextView  enDeleteSongTextView = (TextView)v.findViewById(R.id.en_delete_song_instructions);
    SpannableString enDeleteSongSpannableString = new SpannableString("To delete a song press       , or see the top menu.");
    enDeleteSongSpannableString.setSpan(xImageSpan, 22, 29, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    enDeleteSongTextView.setText(enDeleteSongSpannableString);

    TextView enSongInfoTextView = (TextView)v.findViewById(R.id.en_song_info_instructions);
    SpannableString enSongInfoSpannableString = new SpannableString("To see more information about a song press       .");
    enSongInfoSpannableString.setSpan(songInfoImageSpan, 42, 49, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    enSongInfoTextView.setText(enSongInfoSpannableString);

    TextView enLyricDisplayImageSpan = (TextView)v.findViewById(R.id.en_display_lyrics_differently_instructions);
    SpannableString enLyricDisplaySpannableString = new SpannableString("To change how the lyrics are displayed press       .  or see Preferences in the top menu.");
    enLyricDisplaySpannableString.setSpan(lyricDisplayImageSpan, 46, 50, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    enLyricDisplayImageSpan.setText(enLyricDisplaySpannableString);

    TextView esDownloadTextView = (TextView)v.findViewById(R.id.es_download_instructions);
    SpannableString esDownloadSpannableString = new SpannableString("Para descargar una canción oprima   .");
    esDownloadSpannableString.setSpan(downloadImageSpan, 33, 35, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    esDownloadTextView.setText(esDownloadSpannableString);

    TextView  esDeleteSongTextView = (TextView)v.findViewById(R.id.es_delete_song_instructions);
    SpannableString esDeleteSongSpannableString = new SpannableString("Para borrar una canción oprima    , o vea el menú superior.");
    esDeleteSongSpannableString.setSpan(xImageSpan, 30, 34, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    esDeleteSongTextView.setText(esDeleteSongSpannableString);

    TextView esSongInfoTextView = (TextView)v.findViewById(R.id.es_song_info_instructions);
    SpannableString esSongInfoSpannableString = new SpannableString("Para ver más información sobre una canción oprima      .");
    esSongInfoSpannableString.setSpan(songInfoImageSpan, 49, 55, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    esSongInfoTextView.setText(esSongInfoSpannableString);

    TextView esLyricDisplayImageTextView = (TextView)v.findViewById(R.id.es_display_lyrics_differently_instructions);
    SpannableString esLyricDisplaySpannableString = new SpannableString("Para cambiar cómo las letras estan presentadas oprima       , o vea Preferencias en el menú superior.");
    esLyricDisplaySpannableString.setSpan(lyricDisplayImageSpan, 54, 60, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    esLyricDisplayImageTextView.setText(esLyricDisplaySpannableString);

		okButton = (Button)v.findViewById(R.id.btn_ok);
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MainActivityInstructionsDialogV4.this.dismiss();
			}
		});
		
		languageButton = (Button)v.findViewById(R.id.btn_language);

		languageButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        inEnglish = !inEnglish;
        setLanguage();
      }
    });

    setLanguage();
		return v;
	}

  private ImageSpan getImageSpan (int imageResource) {
    Drawable drawable = getResources().getDrawable(imageResource);
    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    return new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
  }

  private void setLanguage () {
    if (inEnglish) {
      languageButton.setText("Español");
      okButton.setText("Close");
      spanishText.setVisibility(View.GONE);
      englishText.setVisibility(View.VISIBLE);
      spanishTitle.setVisibility(View.GONE);
      englishTitle.setVisibility(View.VISIBLE);
    }
    else {
      languageButton.setText("English");
      okButton.setText(getString(R.string.terminado));
      spanishText.setVisibility(View.VISIBLE);
      englishText.setVisibility(View.GONE);
      spanishTitle.setVisibility(View.VISIBLE);
      englishTitle.setVisibility(View.GONE);
    }
  }
	
	public void onSaveInstanceState(Bundle outState) {
	  super.onSaveInstanceState(outState);
	  outState.putBoolean(inEnglishBoolean, this.inEnglish);
	}
}
