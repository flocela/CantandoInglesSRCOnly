package com.eebolf.CantandoIngles.mainactivity;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.dialogs.SimpleDialogV4;
import com.eebolf.CantandoIngles.start.SettingsActivity;
import com.eebolf.CantandoIngles.utils.Map;

public class PromptDownloadInstructionsDialogV4 extends SimpleDialogV4 {
  public static String INSTRUCTIONS_SPANISH = "instructions_spanish";

	public static PromptDownloadInstructionsDialogV4 newInstance (Bundle startingBundle) {
		PromptDownloadInstructionsDialogV4 cD = new PromptDownloadInstructionsDialogV4();
		cD.setArguments(startingBundle);
		return cD;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_general_instructions, container, false);
		
		Drawable drawable = getResources().getDrawable(R.drawable.ic_download_in_text);
    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    ImageSpan imgSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
    
		TextView instructionsSpanishTextView = (TextView)v.findViewById(R.id.instructions_spanish);
		SpannableString instructionsSpanishSpannableString = new SpannableString("Oprima         para");
		instructionsSpanishSpannableString.setSpan(imgSpan, 7, 14, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);	
		instructionsSpanishTextView.setText(instructionsSpanishSpannableString);		
		
		TextView instructionsEnglishTextView = (TextView)v.findViewById(R.id.instructions_english);
    SpannableString instructionsEnglishSpannableString = new SpannableString("Press         at top to download a song.");
    instructionsEnglishSpannableString.setSpan(imgSpan, 5, 14, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); 
    instructionsEnglishTextView.setText(instructionsEnglishSpannableString);	
		
		
		Bundle args = this.getArguments();		
    String instructionsLine2Spanish = args.getString(Map.INSTRUCTIONS_SPANISH);
    TextView instructionsLine2SpanishTextView = (TextView)v.findViewById(R.id.instructions_line_two_spanish);
		instructionsLine2SpanishTextView.setText(instructionsLine2Spanish);
		
    Button bienButton = (Button)v.findViewById(R.id.btn_bien);    

    bienButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        setPromptDownloadInstructionsPreferenceToFalse();
        PromptDownloadInstructionsDialogV4.this.dismiss();
      }
    });
    
		Button okButton = (Button)v.findViewById(R.id.btn_ok);		

		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
        setPromptDownloadInstructionsPreferenceToFalse();
				PromptDownloadInstructionsDialogV4.this.dismiss();
			}
		});

		return v;
	}

  @Override
  public void onStart () {
    super.onStart();
  }

  private void setPromptDownloadInstructionsPreferenceToFalse () {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    SharedPreferences.Editor prefEditor = sharedPref.edit();
    prefEditor.putBoolean(SettingsActivity.download_instructions_prompt, false);
    prefEditor.commit();
  }
}
