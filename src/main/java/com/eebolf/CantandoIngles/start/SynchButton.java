package com.eebolf.CantandoIngles.start;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.eebolf.CantandoIngles.R;

// This should be a private member of LyricActivity, but activity is so full
// now that I've moved it here in a separate file.
public class SynchButton extends Button {

  public boolean isSynched = false;

  public SynchButton(Context context) {
    super(context);
    setBackgroundResource(R.drawable.dark_blue); // always starts off as blank.
    setListener();
  }

  public SynchButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    setBackgroundResource(R.drawable.dark_blue);
    setListener();
  }

  public SynchButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setBackgroundResource(R.drawable.dark_blue);
    setListener();
  }

  // Does not notify anyone that synch has been set.
  public void setSynched (PlayerState state) {
    isSynched = state.synched;
    setDrawable();
  }

  public void receivingNotificationIsPlaying (boolean isPlaying) {
    // make synch and not synch button available or not.
  }

  private void setListener() {
    setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        isSynched = !isSynched;
        ((LyricActivity)getContext()).
          receivingNoteSynchButtonClicked(isSynched);
        setDrawable();
      }
    });
  }

  private void setDrawable () {
    int drawable = (isSynched)? R.drawable.unsynch : R.drawable.synch;
    setBackgroundResource(drawable);
  }
 }
