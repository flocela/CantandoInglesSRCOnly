package com.eebolf.CantandoIngles.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class EZActivity extends FragmentActivity {

  public String ezGetString(int stringID) {
    return getResources().getString(stringID);
  }

  public void addFragment (Fragment fragment, String tag) {
    FragmentManager fm = getSupportFragmentManager();
    Fragment existing = fm.findFragmentByTag(tag);
    if (existing == null) {
      FragmentTransaction ft = fm.beginTransaction();
      ft.add(fragment, tag);
      ft.commit();
    }
  }


  public Fragment findFragment (String tag) {
    return getSupportFragmentManager().findFragmentByTag(tag);
  }

  public void removeFragment (Fragment fragment) {
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.remove(fragment);
    ft.commitAllowingStateLoss();
  }
}
