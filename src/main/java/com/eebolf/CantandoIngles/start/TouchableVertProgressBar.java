package com.eebolf.CantandoIngles.start;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

public class TouchableVertProgressBar extends ProgressBar {

  public TouchableVertProgressBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  public TouchableVertProgressBar(Context context) {
    super(context);
  }

  boolean firstTouchHappened = false;

  @Override
  public boolean onTouchEvent(MotionEvent event){ 
          
      int action = MotionEventCompat.getActionMasked(event);
          
      switch(action) {
          case (MotionEvent.ACTION_DOWN) :
              firstTouchHappened = true;
              return true;
          case (MotionEvent.ACTION_UP) :
              if (firstTouchHappened) {
                float ratio = (event.getX()/this.getWidth());
                if (this.getContext() instanceof LyricActivity) {
                  ProgressBarChangedListener playWords = (ProgressBarChangedListener)this.getContext();
                  playWords.updateProgressBarChangedByUser(ratio);
                }                
              }
              return true;
          case (MotionEvent.ACTION_OUTSIDE) :
              firstTouchHappened = false;
              return true;      
          default : 
              return super.onTouchEvent(event);
      }
  }
  
  public interface ProgressBarChangedListener {
    public void updateProgressBarChangedByUser(float ratioNow);
  }
                                             

}
