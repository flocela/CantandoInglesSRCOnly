package com.eebolf.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.eebolf.CantandoIngles.todowithlists.DeleterSongsListActivity;

public class DeleteAllSongTest extends ActivityInstrumentationTestCase2<DeleterSongsListActivity> {

  private DeleterSongsListActivity listActivity;

  public DeleteAllSongTest() {
    super(DeleterSongsListActivity.class);
  }

  @Override
  protected void setUp () throws Exception {
    super.setUp();
    setActivityInitialTouchMode(false);
    listActivity = getActivity();
  }

  @Override
  protected void tearDown () throws Exception {
    super.tearDown();
  }

  @SmallTest
  public void testShowExistingSongs () throws Exception{
    int initialCount = listActivity.getListView().getCount();
    assertTrue(initialCount > 0);

    listActivity.clearSongs();
    Thread.currentThread().sleep(2000);
    int count = listActivity.getListView().getCount();
    assertEquals(0, count);
  }

}
