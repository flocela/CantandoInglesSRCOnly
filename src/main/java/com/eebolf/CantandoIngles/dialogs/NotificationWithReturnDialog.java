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

public class NotificationWithReturnDialog extends RetrieverDialog {

	/**
	 * Requires: 
	 * Map.MESSAGE- the notification text.
	 * 
	 * Uses but doesn't require:
	 * Map.TITLE- if not title given, then uses "Notification" 
	 * 
	 * Returns: same bundle that was provided. Check notifyDialogDone for second
	 * argument "isCancelled". If isCancelled = false, then user confirmed.
	 * If isCancelled = true, then user did not confirm.
	 */
	public static NotificationWithReturnDialog newInstance (Bundle startingBundle) {
		NotificationWithReturnDialog cD = new NotificationWithReturnDialog();
		cD.setArguments(startingBundle);
		return cD;
	}	
	
	public static NotificationWithReturnDialog newInstance (String title, String message) {
	  Bundle startingBundle = new Bundle();
	  startingBundle.putString(Map.TITLE, title);
	  startingBundle.putString(Map.MESSAGE, message);
    NotificationWithReturnDialog cD = new NotificationWithReturnDialog();
    cD.setArguments(startingBundle);
    return cD;
  } 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.dialog_notification_with_return, container, false);

		TextView titleTextView = (TextView)v.findViewById(R.id.title);
		String titleString = getArguments().getString(Map.TITLE);
		if (null != titleString && !titleString.isEmpty()) {
			titleTextView.setText(titleString);
		}
		else {
			titleTextView.setText("Notificatio\u0301;n");
		}
		

		TextView notificationTextView = (TextView)v.findViewById(R.id.notification_message);
		notificationTextView.setText(getArguments().getString(Map.MESSAGE));
		Button okButton = (Button)v.findViewById(R.id.btn_ok);		

		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setDialogDoneListener();
				listener.notifyDialogDone(requestCode, false, NotificationWithReturnDialog.this.getArguments());
				NotificationWithReturnDialog.this.dismiss();
			}
		});

		return v;
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		setDialogDoneListener();
		listener.notifyDialogDone(requestCode, true, NotificationWithReturnDialog.this.getArguments());
	}
	
}
