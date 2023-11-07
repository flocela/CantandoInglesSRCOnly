package com.eebolf.CantandoIngles.start;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.eebolf.CantandoIngles.R;

// I use this button for testing audio focus only. Pressing it to change
// from Transient Loss - to Gain - to PermLoss - to Gain.
// This should be a private member of LyricActivity, but activity is so full
// now that I've moved it here in a separate file.
public class AudioFocusButton extends Button {

  private int stage = 0;

  public AudioFocusButton(Context context) {
    super(context);
    setBackgroundResource(R.drawable.dark_blue); // always starts off as blank.
    setListener();
  }

  public AudioFocusButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    setBackgroundResource(R.drawable.dark_blue);
    setListener();
  }

  public AudioFocusButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setBackgroundResource(R.drawable.dark_blue);
    setListener();
  }

  private void setListener() {
    setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        if (AudioFocusButton.this.stage == 3)
          AudioFocusButton.this.stage = 0;
        else
          AudioFocusButton.this.stage++;

        if (AudioFocusButton.this.stage == 0) {
          setBackgroundResource(R.drawable.ic_perm_loss);
          ((LyricActivity)getContext()).onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS);
        }
        else if (AudioFocusButton.this.stage == 1) {
          setBackgroundResource(R.drawable.ic_gain);
          ((LyricActivity)getContext()).onAudioFocusChange(AudioManager.AUDIOFOCUS_GAIN);
        }
        else if (AudioFocusButton.this.stage == 2) {
          setBackgroundResource(R.drawable.ic_trans_loss);
          ((LyricActivity)getContext()).
            onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS_TRANSIENT);
        }
        else if (AudioFocusButton.this.stage == 3) {
          setBackgroundResource(R.drawable.ic_gain);
          ((LyricActivity)getContext()).onAudioFocusChange(AudioManager.AUDIOFOCUS_GAIN);
        }
      }
    });
  }
 }
