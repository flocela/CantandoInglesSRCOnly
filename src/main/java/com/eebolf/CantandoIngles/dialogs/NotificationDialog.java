package com.eebolf.CantandoIngles.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.utils.Map;

public class NotificationDialog extends SimpleDialog {

  /**
   * Expects a bundle with
   * Map.TITLE, if not then 'Notification' is used as the title.
   * Map.MESSAGE will have the main notification message.
   */
	public static NotificationDialog newInstance (Bundle startingBundle) {
		NotificationDialog cD = new NotificationDialog();
		cD.setArguments(startingBundle);
		return cD;
	}
	
	public static NotificationDialog newInstance (String title, String message) {
	  Bundle startingBundle = new Bundle();
	  startingBundle.putString(Map.TITLE, title);
	  startingBundle.putString(Map.MESSAGE, message);
    NotificationDialog cD = new NotificationDialog();
    cD.setArguments(startingBundle);
    return cD;
  }
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_notification, container, false);
		
		TextView titleTextView = (TextView)v.findViewById(R.id.title);
		TextView notificationTextView = (TextView)v.findViewById(R.id.notification_message);
	
		Bundle args = this.getArguments();
		String notificationTitle = args.getString(Map.TITLE);
		if (null == notificationTitle) {
		  notificationTitle = "Notificatio\u0301n";
		}
		titleTextView.setText(notificationTitle);
		notificationTextView.setText(args.getString(Map.MESSAGE));
		
		Button okButton = (Button)v.findViewById(R.id.btn_ok);		

		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				NotificationDialog.this.dismiss();
			}
		});

		return v;
	}
}
