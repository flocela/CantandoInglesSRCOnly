package com.eebolf.tests;


import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.mainactivity.MainActivity;
import com.eebolf.CantandoIngles.todowithlists.DeleterSongsListActivity;

public class ActionBarDeleteTest extends ActivityInstrumentationTestCase2<MainActivity> {

  Activity mainActivity;

  public ActionBarDeleteTest() {
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
  public void testDownloadSongListStarts() {
    ActivityMonitor dlSongListMonitor = getInstrumentation().addMonitor(DeleterSongsListActivity.class.getName(),
                                                                        null,
                                                                        false);
    getInstrumentation().invokeMenuActionSync(mainActivity, R.id.action_delete, 0);
    DeleterSongsListActivity deleterSongListActivity = (DeleterSongsListActivity) dlSongListMonitor.waitForActivityWithTimeout(1000);
    assertNotNull(deleterSongListActivity);
    int count = deleterSongListActivity.getListView().getCount();
    assertTrue(count == 0);
  }


}
