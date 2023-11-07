package com.eebolf.CantandoIngles.start;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;


public class OneWordView extends RelativeLayout {

  private TextView englishCurrTextView;
  private TextView englishCurrCorrectedTextView;
  private TextView spanishCurrTextView;
  private TextView spanishCurrMeaningTextView;
  private TextView englishPastTextView;
  private TextView spanishPastTextView;
  private TextView englishFutureTextView;
  private TextView spanishFutureTextView;


  public OneWordView (Context context) {
    super(context);
    setAttributes(context);
  }

  public OneWordView (Context context, AttributeSet attrs) {
    super(context, attrs);
    setAttributes(context);
  }

  public OneWordView (Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs);
    setAttributes(context);
  }

  public void setAttributes (Context context) {
    englishCurrTextView = (TextView)this.findViewById(R.id.english_word);
    englishCurrCorrectedTextView = (TextView)this.findViewById(R.id.english_word_corrected);
    spanishCurrTextView = (TextView)this.findViewById(R.id.spanish_word);
    spanishCurrMeaningTextView = (TextView)this.findViewById(R.id.spanish_word_meaning);
    englishPastTextView = (TextView)this.findViewById(R.id.english_word_past);
    spanishPastTextView = (TextView)this.findViewById(R.id.spanish_word_past);
    englishFutureTextView = (TextView)this.findViewById(R.id.english_word_future);
    spanishFutureTextView = (TextView)this.findViewById(R.id.spanish_word_future);

  }

  public void setEnglishPastText (String englishPastText) {
    englishPastTextView = (TextView)this.findViewById(R.id.english_word_past);
    englishPastTextView.setText(englishPastText);
  }

  public void setEnglishCurrText (String english, final int time) {
    englishCurrTextView = (TextView)this.findViewById(R.id.english_word);
    this.englishCurrTextView.setText(english);

    Drawable curr = englishCurrTextView.getBackground();
    Drawable blue = getResources().getDrawable(R.drawable.light_blue_rounded_2);
    if (curr != blue) {
      englishCurrTextView.
        setBackgroundResource(R.drawable.light_blue_rounded_2);
    }
    englishCurrTextView.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        ((LyricActivity)getContext()).longClickAtTime(time);
        englishCurrTextView.
          setBackgroundResource(R.drawable.light_pink_rounded_2);
        spanishCurrTextView.setBackgroundResource(R.drawable.dark_pink_rounded_2);
        return true;
      }
    });
  }

  public void setEnglishCurrCorrectedText (String english) {
    englishCurrCorrectedTextView = (TextView)this.findViewById(R.id.english_word_corrected);
    englishCurrCorrectedTextView.setText(english);
    if (english == null || (english != null && english.isEmpty())) {
      englishCurrCorrectedTextView.setVisibility(View.GONE);
    }
    else {
      englishCurrCorrectedTextView.setVisibility(View.VISIBLE);
    }
  }

  public void setEnglishFutureText (String english) {
    englishFutureTextView = (TextView)this.findViewById(R.id.english_word_future);
    englishFutureTextView.setText(english);
  }

  public void setSpanishPastText (String spanish) {
    spanishPastTextView = (TextView)this.findViewById(R.id.spanish_word_past);
    spanishPastTextView.setText(spanish);
  }

  public void setSpanishCurrText (String spanish, final int time) {
    spanishCurrTextView = (TextView)this.findViewById(R.id.spanish_word);
    spanishCurrTextView.setText(spanish);

    Drawable curr = spanishCurrTextView.getBackground();
    Drawable blue = getResources().getDrawable(R.drawable.dark_blue_rounded_2);
    if (curr != blue) {
      spanishCurrTextView.
        setBackgroundResource(R.drawable.dark_blue_rounded_2);
    }

    spanishCurrTextView.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        ((LyricActivity)getContext()).longClickAtTime(time);
        englishCurrTextView.
          setBackgroundResource(R.drawable.light_pink_rounded_2);
        spanishCurrTextView.setBackgroundResource(R.drawable.dark_pink_rounded_2);
        return true;
      }
    });
  }

  public void setSpanishCurrMeaningTextView (String spanish) {
    spanishCurrMeaningTextView = (TextView)this.findViewById(R.id.spanish_word_meaning);
    spanishCurrMeaningTextView.setText(spanish);
    if (spanish == null || (spanish != null && spanish.isEmpty())) {
      spanishCurrMeaningTextView.setVisibility(View.GONE);
    }
    else {
      spanishCurrMeaningTextView.setVisibility(View.VISIBLE);
    }
  }

  public void setSpanishFutureText (String spanish) {
    spanishFutureTextView = (TextView)this.findViewById(R.id.spanish_word_future);
    spanishFutureTextView.setText(spanish);
  }


}
