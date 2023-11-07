package com.eebolf.CantandoIngles.todowithlists;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.collectors.WordsExplanationsAndTime;
import com.eebolf.CantandoIngles.listeners.LyricDisplayer;
import com.eebolf.CantandoIngles.start.LyricActivity;

public class LyricRow extends LinearLayout {

  private View spParent;
  private TextView spanishTextView;
  private View spMeaningTier;
  private View spMeaningParent;
  private TextView spMeaningTextView;

  private View enParent;
  private TextView englishTextView;
  private View enCorrectedTier;
  private View enCorrectedParent;
  private TextView englishCorrectedTextView;

  public LyricRow (Context context) {
    super(context);
  }

  public LyricRow (Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public LyricRow (Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs);
  }

  private void setAttributes () {
    this.spParent = this.findViewById(R.id.spanish_direct_parent);
    this.spanishTextView = (TextView)this.findViewById(R.id.spanish_direct);
    this.spMeaningTier = this.findViewById(R.id.meaning_tier);
    this.spMeaningParent = this.findViewById(R.id.spanish_meaning_parent);
    this.spMeaningTextView = (TextView)this.findViewById(R.id.spanish_meaning);

    this.enParent = findViewById(R.id.english_word_parent);
    this.englishTextView = (TextView) this.findViewById(R.id.english_word);
    this.enCorrectedTier = this.findViewById(R.id.corrected_english_tier);
    this.enCorrectedParent = findViewById(R.id.english_corrected_parent);
    this.englishCorrectedTextView = (TextView)this.findViewById(R.id.english_corrected_text);
  }

  public void initializeRow(WordsExplanationsAndTime words) {
    setAttributes();
    setEnglishTiers(words);
    setSpanishTiers(words);
    spMeaningTier.setVisibility(View.GONE);
    enCorrectedTier.setVisibility(View.GONE);
  }

  public void resetRow (WordsExplanationsAndTime words) {
    setEnglishTiers(words);
    setSpanishTiers(words);
  }

  private void setEnglishTiers (WordsExplanationsAndTime words) {
    setEnglishTextView(words);
    setEnglishBackground(words);
    setCorrectedEnglishText(words);
    spMeaningTier.setVisibility(View.GONE);
    enCorrectedTier.setVisibility(View.GONE);
  }

  private void setEnglishTextView(final WordsExplanationsAndTime words) {
    englishTextView.setText(words.getEnglishWord());
    englishTextView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (words.hasEnglishCorrected() && enCorrectedTier.getVisibility() == View.GONE) {
          enCorrectedTier.setVisibility(View.VISIBLE);
          spMeaningTier.setVisibility(View.VISIBLE);
        }
        else if (words.hasEnglishCorrected() && enCorrectedTier.getVisibility() == View.VISIBLE) {
          enCorrectedTier.setVisibility(View.GONE);
          spMeaningTier.setVisibility(View.GONE);
        }
      }
    });
    englishTextView.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        if (LyricRow.this.getContext() instanceof LyricActivity) {
          ((LyricDisplayer.Listener) LyricRow.this.getContext()).
            longClickAtTime(words.getTime());
        }
        return true;
      }
    });
  }

  private void setEnglishBackground(WordsExplanationsAndTime words) {
    if (words.hasEnglishCorrected()) {
      setBGBlueLightning(enParent);
      setBGBlueBorder(enCorrectedParent);
    }
    else {
      setBGFilledBlue(enParent);
    }
  }

  private void setCorrectedEnglishText (WordsExplanationsAndTime words) {
    if (words.hasEnglishCorrected()) {
      englishCorrectedTextView.setText(words.getEnglishCorrected());
    }
    else {
      englishCorrectedTextView.setText("");
    }
  }

  private void setSpanishTiers (WordsExplanationsAndTime words) {
    setSpanishTextView(words);
    setSpanishBackgrounds(words);
    setSpanishMeaningText(words);
  }

  private void setSpanishTextView(final WordsExplanationsAndTime words) {
    spanishTextView.setText(words.getSpanishDirect());
    spanishTextView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (words.hasSpanishMeaning() && spMeaningTier.getVisibility() == View.GONE) {
            spMeaningTier.setVisibility(View.VISIBLE);
            enCorrectedTier.setVisibility(View.VISIBLE);
        }
        else if (words.hasSpanishMeaning() &&
                 spMeaningTier.getVisibility() == View.VISIBLE) {
          spMeaningTier.setVisibility(View.GONE);
          enCorrectedTier.setVisibility(View.GONE);
        }
      }
    });
    spanishTextView.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        if (LyricRow.this.getContext() instanceof LyricActivity) {
          ((LyricDisplayer.Listener) LyricRow.this.getContext()).
            longClickAtTime(words.getTime());
        }
        return true;
      }
    });
  }

  private void setSpanishBackgrounds(WordsExplanationsAndTime words) {
    if (words.hasSpanishMeaning()) {
      setBGBluePlus(spParent);
      setBGBlueBorder(spMeaningParent);
    }
    else {
      setBGFilledBlue(spParent);
    }
  }

  private void setSpanishMeaningText (WordsExplanationsAndTime words) {
    if (words.hasSpanishMeaning()) {
      spMeaningTextView.setText(words.getSpanishMeaning());
    }
    else {
      spMeaningTextView.setText("");
    }
  }

  public void setToLightBlue () {
    setEnglishToLightBlue();
    setSpanishToLightBlue();
  }

  private void setEnglishToLightBlue () {
    if (englishCorrectedTextView.getText().toString().isEmpty()) {
      setBGFilledBlue(enParent);
    }
    else {
      setBGBlueLightning(enParent);
      setBGBlueBorder(enCorrectedParent);
    }
  }

  private void setSpanishToLightBlue () {
    if (spMeaningTextView.getText().toString().isEmpty()) {
      setBGFilledBlue(spParent);
    }
    else {
      setBGBluePlus(spParent);
      setBGBlueBorder(spMeaningParent);
    }
  }

  public void setToLightPink () {
    this.setEnglishToLightPink();
    this.setSpanishToLightPink();
  }

  private void setEnglishToLightPink () {
    if (englishCorrectedTextView.getText().toString().isEmpty()) {
      setBGFilledPink(enParent);
    }
    else {
      setBGPinkLightning(enParent);
      setBGPinkBorder(enCorrectedParent);
    }
  }

  private void setSpanishToLightPink () {
    if (spMeaningTextView.getText().toString().isEmpty()) {
      setBGFilledPink(spParent);
    }
    else {
      setBGPinkPlus(spParent);
      setBGPinkBorder(spMeaningParent);
    }
  }

  private void setBGBlueLightning(View view) {
    view.setBackgroundResource(R.drawable.light_blue_lightning_bg);
  }

  private void setBGBluePlus(View view) {
    view.setBackgroundResource(R.drawable.light_blue_plus_bg);
  }

  private void setBGFilledBlue(View view) {
    view.setBackgroundResource(R.drawable.light_blue_filled_bg);
  }

  private void setBGBlueBorder(View view) {
    view.setBackgroundResource(R.drawable.light_blue_border_bg);
  }

  private void setBGPinkLightning(View view) {
    view.setBackgroundResource(R.drawable.light_pink_lightning_bg);
  }

  private void setBGPinkPlus(View view) {
    view.setBackgroundResource(R.drawable.light_pink_plus_bg);
  }

  private void setBGFilledPink(View view) {
    view.setBackgroundResource(R.drawable.light_pink_filled_bg);
  }

  private void setBGPinkBorder(View view) {
    view.setBackgroundResource(R.drawable.light_pink_border_bg);
  }

}
