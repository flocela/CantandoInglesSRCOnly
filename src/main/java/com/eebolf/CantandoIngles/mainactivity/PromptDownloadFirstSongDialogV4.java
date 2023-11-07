package com.eebolf.CantandoIngles.mainactivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.dialogs.SimpleDialogV4;
import com.eebolf.CantandoIngles.start.SettingsActivity;

public class PromptDownloadFirstSongDialogV4 extends SimpleDialogV4 {

	/**
	 * Requires: nothing.
	 * 
	 * Returns: same bundle that was provided. Check notifyDialogDone for second
	 * argument "isCancelled". If isCancelled = false, then user confirmed download.
	 * If isCancelled = true, then user did not confirm download.
	 */
	public static PromptDownloadFirstSongDialogV4 newInstance () {
		PromptDownloadFirstSongDialogV4 cD = new PromptDownloadFirstSongDialogV4();
		return cD;
	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.dialog_download_first_song, container, false); 
		
		ImageButton okButton = (ImageButton)v.findViewById(R.id.btn_ok);		

		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
          ((MainActivity)getActivity()).requestShowDownloadList();
        }
        setDownloadFirstSongPromptPreferenceToFalse();
				PromptDownloadFirstSongDialogV4.this.dismiss();
			}
		});

		Button cancelButton = (Button)v.findViewById(R.id.btn_cancel);

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
        setDownloadFirstSongPromptPreferenceToFalse();
				PromptDownloadFirstSongDialogV4.this.dismiss();
			}
		});

		return v;
	}

  @Override
  public void onStart () {
    super.onStart();
    setDownloadFirstSongPromptPreferenceToFalse();
  }

  private void setDownloadFirstSongPromptPreferenceToFalse () {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    SharedPreferences.Editor prefEditor = sharedPref.edit();
    prefEditor.putBoolean(SettingsActivity.download_first_song_prompt, false);
    prefEditor.commit();
  }
	
	@Override
	public void onCancel(DialogInterface dialog) {
	}
	
}
