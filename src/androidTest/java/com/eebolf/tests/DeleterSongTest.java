package com.eebolf.tests;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.eebolf.CantandoIngles.todowithlists.DeleterSongsListActivity;

public class DeleterSongTest extends ActivityUnitTestCase<DeleterSongsListActivity> {

  private DeleterSongsListActivity listActivity;

  public DeleterSongTest() {
    super(DeleterSongsListActivity.class);
  }

  @Override
  protected void setUp () throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown () throws Exception {
    super.tearDown();
  }

  @SmallTest
  public void testShowExistingSongs () {
    Intent deleterListIntent = new Intent(getInstrumentation().getTargetContext(), DeleterSongsListActivity.class);
    setActivityContext(getInstrumentation().getTargetContext());
    startActivity(deleterListIntent, null, null);
    listActivity = getActivity();
    int count = listActivity.getListView().getCount();

    assertEquals(count, 1);
  }

}
