package com.eebolf.CantandoIngles.todowithlists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.collectors.WordsExplanationsAndTime;

import java.util.ArrayList;

public class WordListArrayAdapter extends ArrayAdapter<WordsExplanationsAndTime> {
  private ArrayList<WordsExplanationsAndTime> list = null;
  private final Context context;

  public WordListArrayAdapter(Context context, ArrayList<WordsExplanationsAndTime> list) {
    super(context, R.layout.lyrics_list_row, list);
    this.context = context;
    this.list = list;
  }

  @Override
  public View getView (int position, View convertView, ViewGroup parent) {
    WordsExplanationsAndTime currentWords = list.get(position);
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      LyricRow row = (LyricRow)inflater.inflate(R.layout.lyrics_list_row, parent, false);
      row.initializeRow(currentWords);
      return row;
    }
    else {
      LyricRow row = (LyricRow)convertView;
      row.resetRow(currentWords);
      return row;
    }
  }

  @Override
  public boolean areAllItemsEnabled() {
    return true;
  }

  @Override
  public boolean isEnabled(int position) {
    return true;
  }

  public int getPosition (int time) {
    for (int currIndex=0; currIndex<list.size(); currIndex++) {
      int currentIndexTime = list.get(currIndex).getTime();
      int nextIndex = currIndex + 1;
      if (nextIndex >= list.size()) {
        return currentIndexTime;
      }
      int nextTime = list.get(nextIndex).getTime();
      if (time == currentIndexTime || nextTime > time) {
        return currIndex;
      }
    }
    return list.size()-1;
  }
}
