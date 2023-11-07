package com.eebolf.CantandoIngles.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.utils.Map;


public class PreferencesDialogV4 extends RetrieverDialogV4 {

  public static String LYRIC_VIEW_TYPE = "lyric_view_type";

  RadioGroup radioGroup;
  RadioButton oneLyricButton;
  RadioButton listLyricButton;

  Integer viewPref;

	/**
	 * Requires:
   * LYRIC_VIEW_TYPE integer stating current preference of user.
	 * 
	 * Returns: bundle with same keys that were provided and new current preferences.
   * Check notifyDialogDone for second argument "isCancelled".
   * If isCancelled = false, then user confirmed.
	 * If isCancelled = true, then user did not confirm.
	 */
	public static PreferencesDialogV4 newInstance (Bundle startingBundle) {
		PreferencesDialogV4 cD = new PreferencesDialogV4();
		cD.setArguments(startingBundle);
		return cD;
	}

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      setAttributes(savedInstanceState);
    }
    else {
      setAttributes(getArguments());
    }
  }

  private void setAttributes (Bundle bundle) {
    viewPref = Integer.valueOf(bundle.getInt(LYRIC_VIEW_TYPE));
  }


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View v = inflater.inflate(R.layout.dialog_preferences, container, false);

    radioGroup = (RadioGroup)v.findViewById(R.id.one_lyric_view_group);
    oneLyricButton = (RadioButton)v.findViewById(R.id.one_lyric_view_pref);
    oneLyricButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        viewPref = 1;
      }
    });
    listLyricButton = (RadioButton)v.findViewById(R.id.list_view_lyric_pref);
    listLyricButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        viewPref = 2;
      }
    });

    if (viewPref == 1) {
      oneLyricButton.setChecked(true);
    }
    else {
      listLyricButton.setChecked(true);
    }

		Button okButton = (Button)v.findViewById(R.id.btn_ok);

		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
        setDialogDoneListener();
        Bundle bundle = new Bundle();
        bundle.putInt(LYRIC_VIEW_TYPE, viewPref);
				listener.notifyDialogDone(requestCode, false, bundle);
				PreferencesDialogV4.this.dismiss();
			}
		});

		Button cancelButton = (Button)v.findViewById(R.id.btn_cancel);

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setDialogDoneListener();
				listener.notifyDialogDone(requestCode, true, PreferencesDialogV4.this.getArguments());
				PreferencesDialogV4.this.dismiss();
			}
		});

		return v;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		setDialogDoneListener();
		listener.notifyDialogDone(requestCode, true, PreferencesDialogV4.this.getArguments());
	}

  @Override
  public void onSaveInstanceState (Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(Map.REQUEST_CODE, this.requestCode);
    outState.putInt(LYRIC_VIEW_TYPE, viewPref);
  }

}
