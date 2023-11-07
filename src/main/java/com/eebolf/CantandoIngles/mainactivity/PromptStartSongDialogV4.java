package com.eebolf.CantandoIngles.mainactivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.dialogs.SimpleDialogV4;
import com.eebolf.CantandoIngles.start.SettingsActivity;
import com.eebolf.CantandoIngles.utils.Map;


public class PromptStartSongDialogV4 extends SimpleDialogV4 {

  public static String INSTRUCTIONS_SPANISH = "instructions_spanish";
  public static String INSTRUCTIONS_ENGLISH = "instructions_english";

  public static PromptStartSongDialogV4 newInstance (Bundle startingBundle) {
    PromptStartSongDialogV4 pD = new PromptStartSongDialogV4();
    pD.setArguments(startingBundle);
    return pD;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.dialog_general_instructions, container, false);

    TextView instructionsEnglishTextView = (TextView)v.findViewById(R.id.instructions_english);
    TextView instructionsSpanishTextView = (TextView)v.findViewById(R.id.instructions_spanish);

    Bundle args = this.getArguments();
    instructionsEnglishTextView.setText(args.getString(Map.INSTRUCTIONS_ENGLISH));
    instructionsSpanishTextView.setText(args.getString(Map.INSTRUCTIONS_SPANISH));

    Button okButton = (Button)v.findViewById(R.id.btn_ok);

    okButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        setPromptStartSongPreferenceToFalse();
        dismiss();
      }
    });

    Button bienButton = (Button)v.findViewById(R.id.btn_bien);

    bienButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        setPromptStartSongPreferenceToFalse();
        dismiss();
      }
    });

    return v;
  }

  @Override
  public void onStart () {
    super.onStart();
  }

  private void setPromptStartSongPreferenceToFalse () {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    SharedPreferences.Editor prefEditor = sharedPref.edit();
    prefEditor.putBoolean(SettingsActivity.start_song_instructions_prompt, false);
    prefEditor.commit();
  }
}
