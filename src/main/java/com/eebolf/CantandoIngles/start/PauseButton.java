package com.eebolf.CantandoIngles.start;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.eebolf.CantandoIngles.R;

// This should be a private member of LyricActivity, but activity is so full
// now that I've moved it here in a separate file.
public class PauseButton extends Button {

  public boolean isPlaying = false;

  public PauseButton(Context context) {
    super(context);
    setBackgroundResource(R.drawable.dark_blue); // always starts off as blank.
    setListener();
  }

  public PauseButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    setBackgroundResource(R.drawable.dark_blue);
    setListener();
  }

  public PauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setBackgroundResource(R.drawable.dark_blue);
    setListener();
  }

  public void setPaused (PlayerState state) {
    isPlaying = state.playing();
    setDrawable();
  }

  private void setListener() {
    setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        isPlaying = !isPlaying;
        ((LyricActivity)getContext()).
          receivingNotePauseButtonClicked(isPlaying);
        setDrawable();
      }
    });
  }

  private void setDrawable () {
    int drawable = (isPlaying)? R.drawable.pause : R.drawable.play;
    setBackgroundResource(drawable);
  }
 }
