package com.eebolf.CantandoIngles.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.eebolf.CantandoIngles.R;

public class LongClickHelpDialogV4 extends SimpleDialogV4 {

	public static LongClickHelpDialogV4 newInstance (Bundle startingBundle) {
		LongClickHelpDialogV4 cD = new LongClickHelpDialogV4();
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
	  
		View v = inflater.inflate(R.layout.dialog_long_click, container, false);
    englishText = v.findViewById(R.id.english_explanation);
    spanishText = v.findViewById(R.id.spanish_explanation);
		englishTitle = v.findViewById(R.id.help_title_english);
		spanishTitle = v.findViewById(R.id.help_title_spanish);

		okButton = (Button)v.findViewById(R.id.btn_ok);
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				LongClickHelpDialogV4.this.dismiss();
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
