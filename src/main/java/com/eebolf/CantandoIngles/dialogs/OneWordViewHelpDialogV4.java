package com.eebolf.CantandoIngles.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.eebolf.CantandoIngles.R;

public class OneWordViewHelpDialogV4 extends SimpleDialogV4 {

	public static OneWordViewHelpDialogV4 newInstance (Bundle startingBundle) {
		OneWordViewHelpDialogV4 cD = new OneWordViewHelpDialogV4();
		cD.setArguments(startingBundle);
		return cD;
	}
	
	private static String inEnglishBoolean = "IN_ENGLISH_BOOLEAN";	
	private boolean inEnglish = false;	
	private Button okButton;
  private View spanishText;
  private View englishText;
  private Button languageButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	  
	  if (null != savedInstanceState && savedInstanceState.containsKey(inEnglishBoolean)) {
	    inEnglish = savedInstanceState.getBoolean(inEnglishBoolean);
	  }
	  
		View v = inflater.inflate(R.layout.dialog_one_word_view_help, container, false);
    englishText = v.findViewById(R.id.english_explanation);
    spanishText = v.findViewById(R.id.spanish_explanation);

		okButton = (Button)v.findViewById(R.id.btn_ok);
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				OneWordViewHelpDialogV4.this.dismiss();
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
    }
    else {
      languageButton.setText("English");
      okButton.setText(getString(R.string.terminado));
      spanishText.setVisibility(View.VISIBLE);
      englishText.setVisibility(View.GONE);
    }
  }
	
	public void onSaveInstanceState(Bundle outState) {
	  super.onSaveInstanceState(outState);
	  outState.putBoolean(inEnglishBoolean, this.inEnglish);
	}
}
