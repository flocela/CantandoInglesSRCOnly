package com.eebolf.tests;


import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.mainactivity.MainActivity;
import com.eebolf.CantandoIngles.todowithlists.DownloadableSongList;

public class ActionBarDownloadTest extends ActivityInstrumentationTestCase2<MainActivity> {

  Activity mainActivity;

  public ActionBarDownloadTest() {
    super(MainActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    setActivityInitialTouchMode(false);
    this.mainActivity = getActivity();
  }

  /**
   * User clicks download button on action bar, DownloadSongList mainActivity starts.
   */
  public void testDownloadSongListStarts() throws Exception{
    ActivityMonitor dlSongListMonitor = getInstrumentation().addMonitor(DownloadableSongList.class.getName(),
                                                                        null,
                                                                        false);
    getInstrumentation().invokeMenuActionSync(mainActivity, R.id.action_download, 0);
    DownloadableSongList dlSongListActivity = (DownloadableSongList) dlSongListMonitor.waitForActivityWithTimeout(1000);
    assertNotNull(dlSongListActivity);
    Thread.currentThread().sleep(2000); //wait for songs to download from web.
    int count = dlSongListActivity.getListView().getCount();
    assertTrue(count > 5);
  }


}
