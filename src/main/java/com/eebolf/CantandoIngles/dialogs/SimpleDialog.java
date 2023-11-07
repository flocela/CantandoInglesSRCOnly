package com.eebolf.CantandoIngles.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * SimpleDialogV4 means there is no title,
 * the theme is not specified
 * Also note, no requestCode is saved.
 */
public abstract class SimpleDialog extends DialogFragment {
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);	
		this.setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());		
	}
	
	@Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);return dialog;
		
	}
	
	
}
