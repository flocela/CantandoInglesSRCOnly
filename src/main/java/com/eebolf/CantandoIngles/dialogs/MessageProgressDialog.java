package com.eebolf.CantandoIngles.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

public class MessageProgressDialog extends ProgressDialog {

  private String message;

	public MessageProgressDialog(Context context, String message) {
		super(context);
		this.message = message;
		this.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		SpannableString ss1 = new SpannableString(this.message);
		ss1.setSpan(new RelativeSizeSpan(1.25f), 0, ss1.length(), 0);
		this.setMessage(ss1);
	}
	
	public String getMessage() {
	  return message;
	}


}
