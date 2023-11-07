package com.eebolf.CantandoIngles.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.utils.Map;




public class ConfirmationDialog extends RetrieverDialog {

	/**
	 * Requires: 
	 * Map.PROMPT- what the user needs to confirm.
	 * 
	 * Uses but doesn't require:
	 * Map.TITLE- if not title given, then uses "Confirmation Required" 
	 * 
	 * Returns: same bundle that was provided. Check notifyDialogDone for second
	 * argument "isCancelled". If isCancelled = false, then user confirmed.
	 * If isCancelled = true, then user did not confirm.
	 */
	public static ConfirmationDialog newInstance (Bundle startingBundle) {
		ConfirmationDialog cD = new ConfirmationDialog();
		cD.setArguments(startingBundle);
		return cD;
	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_confirmation, container, false);

		TextView titleTextView = (TextView)v.findViewById(R.id.title);
		String titleString = getArguments().getString(Map.TITLE);
		if (null != titleString && !titleString.isEmpty()) {
			titleTextView.setText(titleString);
		}
		else {
			titleTextView.setText("Se Requiere Confirmacio\u0301n");
		}
		

		TextView promptTextView = (TextView)v.findViewById(R.id.confirmation_message);
		promptTextView.setText(getArguments().getString(Map.PROMPT));
		Button okButton = (Button)v.findViewById(R.id.btn_ok);		

		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setDialogDoneListener();
				listener.notifyDialogDone(requestCode, false, ConfirmationDialog.this.getArguments());
				ConfirmationDialog.this.dismiss();
			}
		});

		Button cancelButton = (Button)v.findViewById(R.id.btn_cancel);

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setDialogDoneListener();
				listener.notifyDialogDone(requestCode, true, ConfirmationDialog.this.getArguments());
				ConfirmationDialog.this.dismiss();
			}
		});

		return v;
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		setDialogDoneListener();
		listener.notifyDialogDone(requestCode, true, ConfirmationDialog.this.getArguments());
	}
	
}
