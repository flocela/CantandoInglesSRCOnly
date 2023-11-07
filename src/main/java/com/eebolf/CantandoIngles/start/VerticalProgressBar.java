package com.eebolf.CantandoIngles.start;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

public class VerticalProgressBar extends ProgressBar implements Progressable {
  private int x, y, z, w;

  @Override
  protected void drawableStateChanged() {
    // TODO Auto-generated method stub
    super.drawableStateChanged();
  }

  public VerticalProgressBar(Context context) {
    super(context);
  }

  public VerticalProgressBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public VerticalProgressBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(h, w, oldh, oldw);
    this.x = w;
    this.y = h;
    this.z = oldw;
    this.w = oldh;
  }

  @Override
  protected synchronized void onMeasure(int widthMeasureSpec,
                                        int heightMeasureSpec) {
    super.onMeasure(heightMeasureSpec, widthMeasureSpec);
    setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
  }

  protected void onDraw(Canvas c) {
    c.rotate(90);
    c.translate(0, - getWidth());
    super.onDraw(c);
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
        setProgress((int) (getMax() * event.getY() / getHeight()));
        onSizeChanged(getWidth(), getHeight(), 0, 0);
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
    onSizeChanged(x, y, z, w);
    ((LyricActivity)this.getContext()).moveDisplayOnly((progress));
  }

  @Override
  public void setProgressPercentage(float percentage) {
    int progress = (int) (percentage * ((float)getMax()));
    if (progress >= 0)
      super.setProgress(progress);
    else
      super.setProgress(0);
    onSizeChanged(x, y, z, w);
  }
}
