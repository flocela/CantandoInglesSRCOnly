package com.eebolf.CantandoIngles.start;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

public class TouchableProgressBar extends ProgressBar implements Progressable {

  public TouchableProgressBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  public TouchableProgressBar(Context context) {
    super(context);
  }


  public TouchableProgressBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void drawableStateChanged() {
    // TODO Auto-generated method stub
    super.drawableStateChanged();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (!isEnabled()) {
      return false;
    }

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        setSelected(true);
        setPressed(true);
        break;
      case MotionEvent.ACTION_MOVE:
        setProgress((int) (getMax() * event.getX() / getWidth()));
        break;
      case MotionEvent.ACTION_UP:
        setSelected(false);
        setPressed(false);
        break;
      case MotionEvent.ACTION_CANCEL:
        break;
    }
    return true;
  }

  @Override
  public synchronized void setProgress(int progress) {
    if (Math.abs(this.getProgress() - progress) < 500) {
      return;
    }

    if (progress >= 0)
      super.setProgress(progress);
    else
      super.setProgress(0);
    ((LyricActivity)this.getContext()).moveDisplayOnly((progress));
  }

  @Override
  public void setProgressPercentage(float percentage) {
    int progress = (int) (percentage * ((float)getMax()));
    if (progress >= 0)
      super.setProgress(progress);
    else
      super.setProgress(0);
  }
                                             

}
