package com.eebolf.CantandoIngles.mainactivity;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.dialogs.MainActivityInstructionsDialogV4;
import com.eebolf.CantandoIngles.dialogs.NotificationDialog;
import com.eebolf.CantandoIngles.dialogs.NotificationDialogV4;
import com.eebolf.CantandoIngles.dialogs.PreferencesDialogV4;
import com.eebolf.CantandoIngles.dialogs.SongInfoDialogV4;
import com.eebolf.CantandoIngles.listeners.DialogDoneListener;
import com.eebolf.CantandoIngles.listeners.IconQueryDoneListener;
import com.eebolf.CantandoIngles.start.DownloadReceiver;
import com.eebolf.CantandoIngles.start.InfoQueryFragment;
import com.eebolf.CantandoIngles.start.LyricActivity;
import com.eebolf.CantandoIngles.todowithlists.DeleterSongsListActivity;
import com.eebolf.CantandoIngles.todowithlists.DownloadableSongList;
import com.eebolf.CantandoIngles.utils.EZActivity;
import com.eebolf.CantandoIngles.utils.Map;
import com.eebolf.CantandoIngles.utils.ez;

import java.io.File;

public class MainActivity extends EZActivity implements MainListFragmentListener,
                                                        IconQueryDoneListener,
                                                        DialogDoneListener {
  public final static String icon_query_tag = "icon_query_tag";
  public final static String icon_info_tag = "icon_info_dialog";
  public final static String notification_tag = "notification_tag";
  public final static String instruction_tag = "instruction_dialog";
  public final static String preferences_dialog_tag = "preferences_dialog_tag";
  public final static String PROMPT_DIALOG_TAG = "prompt_dialog_tag";
  public final static String REQUEST_CODE = "request_code";

  public final static int DIALOG_CONFIRM_FIRST_SONG_DOWNLOAD = 78;
  public MessageHandler messageHandler;
  public Handler progressHandler = new ProgressHandler();
  public Handler exceptionHandler = new ExceptionHandler();

  private DownloadReceiver mMessageReceiver;

  @Override
  public void onCreate(Bundle savedInstanceState) {    
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    this.messageHandler = new MessageHandler();
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
  }

  @Override
  public void onStart () {
    super.onStart();
    PromptCoordinator promptCoordinator = getPromptCoordinatorIfReady();
    if (promptCoordinator != null) {
      if (!isShowingPrompt()) {
        addFragmentRemovePrevious(promptCoordinator.getCurrPromptDialog(), PROMPT_DIALOG_TAG);
      }
    }
    initDownloadBroadcastManager();
  }

  @Override
  public void onStop () {
    super.onStop();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main_activity_actions, menu);
      return super.onCreateOptionsMenu(menu);
  } 
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
        case R.id.action_download:
            Intent downloadableSongsListIntent = new Intent(this, DownloadableSongList.class);
            startActivity(downloadableSongsListIntent);
            return true;
        case R.id.action_delete:
          Intent deletingList = new Intent(this, DeleterSongsListActivity.class);
          startActivity(deletingList);
          return true;
        case R.id.action_instructions:
          MainActivityInstructionsDialogV4 instructionsDialog = MainActivityInstructionsDialogV4.newInstance(new Bundle());
          showDialogFragment(instructionsDialog, instruction_tag, Map.DIALOG_MAIN_INSTRUCTIONS);
          return true;
        case R.id.action_preferences:
          SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
          String listPref = sharedPref.getString("pref_lyrics_view","2");
          Bundle bundle = new Bundle();
          bundle.putInt(PreferencesDialogV4.LYRIC_VIEW_TYPE,Integer.valueOf(listPref));
          PreferencesDialogV4 preferencesDialog = PreferencesDialogV4.newInstance(bundle);
          showDialogFragment(preferencesDialog, preferences_dialog_tag, Map.DIALOG_PREFERENCES_ID);
          default:
              return super.onOptionsItemSelected(item);
      }
  }

  @Override
  public void onDestroy () {
    super.onDestroy();
    if (null != messageHandler) {
      messageHandler.removeCallbacksAndMessages(null);
      messageHandler = null;
    }
    if (null != progressHandler) {
      progressHandler.removeCallbacksAndMessages(null);
      progressHandler = null;
    }
    if (null != exceptionHandler) {
      exceptionHandler.removeCallbacksAndMessages(null);
      exceptionHandler = null;
    }
  }

  @Override
  public boolean noticeListItemClick(String musicFile, String lyricsFile) {
    startPlaying(musicFile, lyricsFile);
    return false;
  }
  
  @Override
  public void noticeQueryIconClicked(Integer id) {
     Bundle bundle = ez.intBundle(Map.QUERY_ID, id);
     InfoQueryFragment iconQueryFragment = InfoQueryFragment.newInstance(bundle);
     addFragmentRemovePrevious(iconQueryFragment, icon_query_tag);
  }

  @Override
  public boolean noticeIconQueryDoneAsync(Bundle songinfoBundle) {
    songinfoBundle.putString(Map.FRAGMENT_TAG, icon_query_tag);
    Message message = this.messageHandler.obtainMessage();
    message.setData(songinfoBundle);
    messageHandler.sendMessage(message);
    return false;
  }

  @Override
  public void noticeLoadFinished() {
    Bundle bundle = new Bundle();
    bundle.putString(Map.FRAGMENT_TAG, PROMPT_DIALOG_TAG);
    Message message = this.messageHandler.obtainMessage();
    message.setData(bundle);
    messageHandler.sendMessage(message);
  }

  @SuppressLint("HandlerLeak")
  public class MessageHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
      Bundle msgBundle = msg.getData();
      String fragmentTag = msgBundle.getString(Map.FRAGMENT_TAG);
      if (fragmentTag.equals(icon_query_tag)) {
        Bundle bundle = msg.getData();
        SongInfoDialogV4 songInfoDialog = SongInfoDialogV4.newInstance(bundle);
        addFragmentRemovePreviousAllowStateLoss(songInfoDialog, icon_info_tag);
      }
      if (fragmentTag.equals(PROMPT_DIALOG_TAG)) {
        if (!isShowingPrompt()) {
          PromptCoordinator promptCoordinator = getPromptCoordinatorIfReady();
          android.support.v4.app.DialogFragment promptDialog =
            promptCoordinator.getCurrPromptDialog();
          if (!isShowingPrompt() && null != promptDialog) {
            addFragmentRemovePrevious(promptDialog, PROMPT_DIALOG_TAG);
          }
        }
      }
    }
  }

  @Override
  public void notifyDialogDone(int requestCode, boolean isCancelled, Bundle returnedBundle) {
    switch (requestCode) {
    case Map.DIALOG_PREFERENCES_ID: {
      if (!isCancelled) {
        int newPref = returnedBundle.getInt(PreferencesDialogV4.LYRIC_VIEW_TYPE);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("pref_lyrics_view", ""+newPref);
        prefEditor.commit();
      }
    }
    }
  }

  protected void requestShowDownloadList () {
    Intent downloadableSongsListIntent = new Intent(this, DownloadableSongList.class);
    startActivity(downloadableSongsListIntent);
  }

  @SuppressLint("HandlerLeak")
  private class ProgressHandler extends Handler {
    @Override
    public void handleMessage (Message msg) {
      Bundle origBundle = msg.getData();
      if (origBundle.containsKey(DownloadReceiver.PROGRESS_BUNDLE))
        if (origBundle.containsKey(Map.TOAST_LENGTH) &&
          Toast.LENGTH_SHORT == origBundle.getInt(Map.TOAST_LENGTH)) {
          showShortToast(origBundle.getString(Map.TITLE),
            origBundle.getString(Map.MESSAGE));
        }
      else {
          showLongToast(origBundle.getString(Map.TITLE), origBundle.getString(Map.MESSAGE));
        }

    }
  }

  @SuppressLint("HandlerLeak")
  private class ExceptionHandler extends Handler {
    @Override
    public void handleMessage (Message msg) {
        Bundle origBundle = msg.getData();
        android.app.FragmentManager fm = getFragmentManager();
        DialogFragment notificationDialog = (DialogFragment)fm.findFragmentByTag(Map.EXCEPTION_NOTIFICATION_TAG);
        if(null == notificationDialog) {
          notificationDialog = NotificationDialog.newInstance(origBundle);
          android.app.FragmentTransaction ft = fm.beginTransaction();
          ft.add(notificationDialog, Map.EXCEPTION_NOTIFICATION_TAG);
          ft.commitAllowingStateLoss();
        }
      }
  }

  private void showLongToast(String title, String message) {
    Toast toast = subShowToast(title, message);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.show();
  }

  private void showShortToast(String title, String message) {
    Toast toast = subShowToast(title, message);
    toast.setDuration(Toast.LENGTH_SHORT);
    toast.show();
  }

  private Toast subShowToast (String title, String message) {
    LayoutInflater inflater = getLayoutInflater();
    View layout = inflater.inflate(R.layout.toast_layout,
      (ViewGroup) findViewById(R.id.toast_layout_root));

    TextView text = (TextView) layout.findViewById(R.id.text);
    text.setText(message);
    ((TextView)layout.findViewById(R.id.text_label)).setText(title);
    Toast toast = new Toast(getApplicationContext());
    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    toast.setView(layout);
    return toast;
  }

  private PromptCoordinator getPromptCoordinatorIfReady () {
    ListView listView = getMainListView();
    if (listView != null) {
      return new PromptCoordinator(listView.getCount(),
        PreferenceManager.getDefaultSharedPreferences(this).getAll());
    }
    return null;
  }

  private ListView getMainListView () {
    android.app.FragmentManager fm = getFragmentManager();
    ListFragment listFragment = (ListFragment)fm.findFragmentById(R.id.titles);
    if (listFragment != null ) {
      return listFragment.getListView();
    }
    return null;
  }

  private void startPlaying(String musicFileName, String lyricFileName) {
    Intent playWordsIntent = new Intent(this, LyricActivity.class);
    String lyricsFileFullName = addInternalStoragePath(lyricFileName);
    String musicFileFullName = addInternalStoragePath(musicFileName);
    if ( !fileExists(musicFileFullName) || !fileExists(lyricsFileFullName) ){
      NotificationDialogV4 notificationDialog = NotificationDialogV4.newInstance(
        "Notificacio\u0301n",
        "Los archivos de la musica no existen o no estan completos. "
          + "Probablemente la descargada de la cancioin no hay terminado.\n"
          + "Espera o descarga la cancio\u0301n de la red otra vez.");
      addFragmentRemovePrevious(notificationDialog, notification_tag);
    }
    else {
      SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
      String listPref = sharedPref.getString("pref_lyrics_view","2");
      if (listPref.equals("1")) {
        playWordsIntent.putExtra(LyricActivity.LYRIC_VIEW_PREF,
          LyricActivity.LYRIC_ONE_WORD_VIEW);
      }
      else {
        playWordsIntent.putExtra(LyricActivity.LYRIC_VIEW_PREF,
          LyricActivity.LYRIC_LIST_VIEW);
      }
      playWordsIntent.putExtra(LyricActivity.LYRIC_FILENAME, lyricsFileFullName);
      playWordsIntent.putExtra(LyricActivity.MUSIC_FILENAME, musicFileFullName);
      startActivity(playWordsIntent);
    }
  }

  private boolean fileExists(String filename) {
    File file = new File(filename);
    return file.exists();
  }

  private String addInternalStoragePath(String filename) {
    String dirPath = this.getDir("songs", MODE_PRIVATE).getAbsolutePath();
    return dirPath + "/" + filename;
  }

  private void addFragmentRemovePrevious(android.support.v4.app.Fragment fragment,
                                         String tag) {
    if (fragment == null) {
      return;
    }
    android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();
    android.support.v4.app.Fragment foundFragment = fm.findFragmentByTag(tag);
    if (null != foundFragment) {
      fm.beginTransaction().remove(foundFragment).commit();
    }
    fm.beginTransaction().add(fragment, tag).commit();
  }

  private void showDialogFragment(android.support.v4.app.DialogFragment fragment,
                                  String tag, int requestCode) {
    android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();
    android.support.v4.app.Fragment foundFragment = fm.findFragmentByTag(tag);
    if (null == foundFragment) {
      android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().
        beginTransaction();
      fragment.setTargetFragment(null, requestCode);
      fragment.show(ft, tag);
    }
  }

  private boolean isShowingPrompt () {
    FragmentManager fm = getSupportFragmentManager();
    Fragment promptFrag = fm.findFragmentByTag(PROMPT_DIALOG_TAG);
    if (promptFrag != null) {
      return true;
    }
    return false;
  }

  private void addFragmentRemovePreviousAllowStateLoss
    (android.support.v4.app.Fragment fragment, String tag) {
    android.support.v4.app.FragmentManager fm = this.getSupportFragmentManager();
    android.support.v4.app.Fragment foundFragment = fm.findFragmentByTag(tag);
    if (null != foundFragment) {
      fm.beginTransaction().remove(foundFragment).commitAllowingStateLoss();
    }
    fm.beginTransaction().add(fragment, tag).commitAllowingStateLoss();
  }

  private void initDownloadBroadcastManager() {
    mMessageReceiver = new DownloadReceiver(progressHandler, exceptionHandler);
    LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
    broadcastManager.registerReceiver(mMessageReceiver,
      new IntentFilter(DownloadReceiver.DOWNLOAD_FILTER));
  }
}
