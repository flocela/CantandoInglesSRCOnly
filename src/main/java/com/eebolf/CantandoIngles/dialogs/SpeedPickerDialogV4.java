package com.eebolf.CantandoIngles.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.utils.Map;


public class SpeedPickerDialogV4 extends RetrieverDialogV4 {

  public static String SCROLL_SPEED = "SCROLL_SPEED";
  TextView    speedView;
  ImageButton slowerButton;
  ImageButton fasterButton;
  Integer     speed;

	/**
	 * Requires:
   * SCROLL_SPEED integer stating current scroll preference of user for
   * WordListFragment's scrolling.
	 * 
	 * Returns: bundle with same keys that were provided and new current preferences.
   * Check notifyDialogDone for second argument "isCancelled".
   * If isCancelled = false, then user confirmed.
	 * If isCancelled = true, then user did not confirm.
	 */
	public static SpeedPickerDialogV4 newInstance (Bundle startingBundle) {
		SpeedPickerDialogV4 cD = new SpeedPickerDialogV4();
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
    speed = Integer.valueOf(bundle.getInt(SCROLL_SPEED));
  }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View v = inflater.inflate(R.layout.dialog_speed_pref, container, false);

    speedView    = (TextView)v.findViewById(R.id.scroll_speed);
    speedView.setText("" + speed);
    slowerButton = (ImageButton)v.findViewById(R.id.btn_slow_down);
    slowerButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Integer curr = Integer.parseInt(speedView.getText().toString());
        if (1 < curr) {
          int newValue = curr - 1;
          speedView.setText("" + newValue);
        }
      }
    });
    fasterButton = (ImageButton)v.findViewById(R.id.btn_speed_up);
    fasterButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Integer curr = Integer.parseInt(speedView.getText().toString());
        if (20 > curr) {
          int newValue = curr + 1;
          speedView.setText("" + newValue);
        }
      }
    });

		Button okButton = (Button)v.findViewById(R.id.btn_ok);

		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
        setDialogDoneListener();
        Bundle bundle = new Bundle();
        bundle.putString(SCROLL_SPEED, speedView.getText().toString());
				listener.notifyDialogDone(requestCode, false, bundle);
				SpeedPickerDialogV4.this.dismiss();
			}
		});

		Button cancelButton = (Button)v.findViewById(R.id.btn_cancel);

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setDialogDoneListener();
				listener.notifyDialogDone(requestCode, true, SpeedPickerDialogV4.this.getArguments());
				SpeedPickerDialogV4.this.dismiss();
			}
		});

		return v;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		setDialogDoneListener();
		listener.notifyDialogDone(requestCode, true, SpeedPickerDialogV4.this.getArguments());
	}

  @Override
  public void onSaveInstanceState (Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(Map.REQUEST_CODE, this.requestCode);
    outState.putInt(SCROLL_SPEED, Integer.parseInt(speedView.getText().toString()));
  }

}
