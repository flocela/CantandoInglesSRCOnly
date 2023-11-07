package com.eebolf.CantandoIngles.start;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.eebolf.CantandoIngles.R;

// This should be a private member of LyricActivity, but activity is so full
// now that I've moved it here in a separate file.
// Moves song back to beginning, time 0.
public class BeginningButton extends Button {

  public BeginningButton(Context context) {
    super(context);
    setBackgroundResource(R.drawable.dark_blue); // always starts off as blank.
    setListener();
  }

  public BeginningButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    setBackgroundResource(R.drawable.dark_blue);
    setListener();
  }

  public BeginningButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setBackgroundResource(R.drawable.dark_blue);
    setListener();
  }

  public void initialize () {
    setDrawable();
  }

  private void setListener() {
    setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        ((LyricActivity)getContext()).
          receivingNoteBeginningButtonClicked();
        setDrawable();
      }
    });
  }

  private void setDrawable () {
    setBackgroundResource(R.drawable.back_to_start);
  }
 }
