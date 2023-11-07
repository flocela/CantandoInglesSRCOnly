package com.eebolf.CantandoIngles.start;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.collectors.WordsExplanationsAndTime;
import com.eebolf.CantandoIngles.dialogs.LongClickHelpDialogV4;
import com.eebolf.CantandoIngles.dialogs.NotificationDialogV4;
import com.eebolf.CantandoIngles.dialogs.OneWordViewHelpDialogV4;
import com.eebolf.CantandoIngles.dialogs.SpeedPickerDialogV4;
import com.eebolf.CantandoIngles.dialogs.WordListHelpDialogV4;
import com.eebolf.CantandoIngles.listeners.DialogDoneListener;
import com.eebolf.CantandoIngles.listeners.LyricDisplayer;
import com.eebolf.CantandoIngles.listeners.WordTimeListener;
import com.eebolf.CantandoIngles.utils.EZActivity;
import com.eebolf.CantandoIngles.utils.Map;
import com.eebolf.CantandoIngles.utils.ez;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class LyricActivity extends EZActivity
                           implements WordTimeListener,
                                      MediaPlayer.OnCompletionListener,
                                      LyricHolderFragment.Listener,
                                      LyricDisplayer.Listener,
                                      AudioManager.OnAudioFocusChangeListener,
                                      DialogDoneListener {
  public static final String MUSIC_FILENAME       = "MUSIC_FILENAME";
  public static final String LYRIC_FILENAME       = "LYRIC_FILENAME";
  public static final String LYRIC_VIEW_PREF      = "LYRIC_VIEW_PREF";
  public static final String LYRIC_LIST_VIEW      = "LYRIC_LIST_VIEW";
  public static final String LYRIC_ONE_WORD_VIEW  = "LYRIC_ONE_WORD_VIEW";
  public static final String MAKING_LYRIC_TIMES   = "MAKING_LYRIC_TIMES";
  private final String STATE                      = "STATE";
  private static final String HELP_DIALOG_TAG     = "HELP_DIALOG_TAG";
  private static final String HELP_LONG_CLICK_TAG = "HELP_LONG_CLICK_TAG";
  private static final String SPEED_DIALOG_TAG    = "SPEED_DIALOG_TAG";

  private String      lyricsFilename;
  private String      musicFilename;
  private String      viewPref;
  private int         audioFocus = AudioManager.AUDIOFOCUS_LOSS;
  private PlayerState state;

  private LyricDealer   lyricDealer;
  private DisplayDealer displayDealer;
  private MusicDealer   musicDealer;
  private boolean       makingLyricTimes = false;

  private final ExceptionHandler exceptionHandler = new ExceptionHandler();
  private final MessageHandler messageHandler = new MessageHandler(this);

  /* startActivity putExtras are: LYRIC_FILENAME,
                                  MUSIC_FILE_NAME,
                                  LYRIC_VIEW_PREF */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lyric_activity);

    // TESTING: Use word_music_coordinator when testing LyricActivity logic. Shows all buttons.
    //setContentView(R.layout.word_music_coordinator);

    // TIMING: Uncomment when setting times for lyrics and is not being deployed.
    // TIMING: Comment out when being deployed.
    // TIMING: Also see LyricHolderFragment for using filename in assets.
    /*
    try {
      ApplicationInfo ai = getPackageManager().
        getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
      Bundle bundle = ai.metaData;
      if (bundle != null && bundle.getBoolean(MAKING_LYRIC_TIMES)) {
        makingLyricTimes = bundle.getBoolean(MAKING_LYRIC_TIMES);
        lyricsFilename   = bundle.getString(LYRIC_FILENAME);
        musicFilename    = bundle.getString(MUSIC_FILENAME);
        viewPref         = bundle.getString(LYRIC_VIEW_PREF);
        state            = new PlayerState();
      }
    } catch (PackageManager.NameNotFoundException e) {
    } catch (NullPointerException e) {
    }
    */
    //end comment out.

    if (false == makingLyricTimes) {
      Bundle args = (null == savedInstanceState)? getIntent().getExtras() : 
        savedInstanceState;
      lyricsFilename = args.getString(LYRIC_FILENAME);
      musicFilename  = args.getString(MUSIC_FILENAME);
      viewPref       = args.getString(LYRIC_VIEW_PREF);
      state = (args.containsKey(STATE))? (PlayerState)args.getParcelable(STATE) : state;
      if (null == state)
        state = new PlayerState();
    }
    lyricDealer   = new LyricDealer(this, lyricsFilename);
    musicDealer   = new MusicDealer(this, musicFilename, makingLyricTimes);
    displayDealer = new DisplayDealer(this, viewPref);
    updateButtons();
  }

  private void setupComponents() {
    // called from MessageHandler when lyricHolderCreator
    // is done creating lyrics, so lyricDevice is ready and so is array of lyrics.
    displayDealer.initializeViews();
    musicDealer = new MusicDealer(this, musicFilename, makingLyricTimes);
    musicDealer.setUp();
    updateButtons();
    attachAudioFocus();
  }

  @Override
  public void receivingNotificationLyricHolderReady () {
    Message message = messageHandler.obtainMessage();
    Bundle bundle = ez.booleanBundle(MessageHandler.holder_ready, true);
    message.setData(bundle);
    this.messageHandler.sendMessage(message);
  }

  public LyricDevice getLyricDevice () {
    return lyricDealer.getLyricDevice();
  }

  public ArrayList<WordsExplanationsAndTime> getLyrics () {
    if (lyricDealer == null)
      return null;
    return lyricDealer.getLyrics();
  }

  /** BUTTON CALLBACKS / BUTTON CALLBACKS / BUTTON CALLBACKS / BUTTON CALLBACKS */
  public void receivingNoteSynchButtonClicked(boolean isSynched) {
    if (isSynched) {
      state.setToSynched();
      state.position = musicDealer.getMusicPosition(state);
      displayDealer.changeOfStateRequiresAction(state);
    }
    else
      state.unSynch();
    // case where originally was stopped or paused and unsynched, synched
    // was pressed. So displayDealer was updated.
    // Then have to change state back to unsynched since player is not playing.
    if (!state.playing() && state.synched)
      state.synched = false;
    updateButtons();
  }

  public void receivingNoteStopButtonClicked() {
      stop();
  }

  public void receivingNotePauseButtonClicked(boolean toPlay) {
    if (toPlay) {
      if (audioFocus == AudioManager.AUDIOFOCUS_GAIN)
        play();
      else {
        PauseButton pauseButton = (PauseButton) findViewById(R.id.pause_start_button);
        pauseButton.setPaused(state);
        Message message = this.messageHandler.obtainMessage();
        message.setData(ez.booleanBundle(MessageHandler.audio_focus_missing, true));
        this.messageHandler.sendMessage(message);
      }
    }
    else
      pause();
  }

  public void receivingNoteBeginningButtonClicked() {
    state.position = 0;
    musicDealer.moveToTime(state);
    displayDealer.moveToPosition(state.position);
  }

  public void moveDisplayOnly (int time) {
    displayDealer.moveToPosition(time);
  }

  @Override
  public void longClickAtTime (int time) {
    state.position = time;
    musicDealer.moveToTime(state);
    if (!state.stopped) {
      state.synched = true;
      play();
    }
    displayDealer.moveToPosition(time);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.playwords_activity_actions, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_help_word_list:
        pause();
        WordListHelpDialogV4 wordListHelpDialog =
          WordListHelpDialogV4.newInstance(new Bundle());
        showDialogFragment(wordListHelpDialog,
          HELP_DIALOG_TAG,
          Map.DIALOG_HELP_LYRIC_DISPLAYER);
        return true;
      case R.id.action_help_one_word_view:
        pause();
        OneWordViewHelpDialogV4 oneWordViewHelpDialog =
          OneWordViewHelpDialogV4.newInstance(new Bundle());
        showDialogFragment(oneWordViewHelpDialog,
          HELP_DIALOG_TAG,
          Map.DIALOG_HELP_LYRIC_DISPLAYER);
        return true;
      case R.id.action_scroll_word_list:
        pause();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int pref = Integer.valueOf(sharedPref.getString("scroll_speed", "11"));
        Bundle bundle = new Bundle();
        bundle.putInt(SpeedPickerDialogV4.SCROLL_SPEED, pref);
        SpeedPickerDialogV4 speedDialog = SpeedPickerDialogV4.newInstance(bundle);
        showDialogFragment(speedDialog, SPEED_DIALOG_TAG, Map.DIALOG_SCROLL_SPEED);
        return true;
      case R.id.action_long_click_expl:
        pause();
        LongClickHelpDialogV4 longClickDialog =
          LongClickHelpDialogV4.newInstance(new Bundle());
        showDialogFragment(longClickDialog,
          HELP_LONG_CLICK_TAG, Map.DIALOG_HELP_LONG_CLICK);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onAudioFocusChange(int ii) {
    audioFocus = ii;
    if (ii == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
      pause();
    else if (ii == AudioManager.AUDIOFOCUS_LOSS)
      stop();
    else if (ii == AudioManager.AUDIOFOCUS_GAIN && state.paused)
      play();
  }

  @Override
  public void notifyTimeForNewWord() {
    Message message = this.messageHandler.obtainMessage();
    message.setData(ez.booleanBundle(MessageHandler.new_lyric, true));
    this.messageHandler.sendMessage(message);
  }

  // only call from UI, as it updates the UI.
  public void updateFromSong() {
    musicDealer.getMusicPosition(state);
    // 50 is fudge because Android MediaPlayer position timer may be behind.
    displayDealer.updateDisplay(musicDealer.getMusicPosition(state) + 50, state);
  }

  @Override
  public void notifyDialogDone(int requestCode, boolean isCancelled, Bundle returnedBundle) {
    switch (requestCode) {
      case Map.DIALOG_SCROLL_SPEED: {
        if (!isCancelled) {
          String newPref = returnedBundle.getString(SpeedPickerDialogV4.SCROLL_SPEED);
          SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
          SharedPreferences.Editor prefEditor = sharedPref.edit();
          prefEditor.putString("scroll_speed", newPref);
          prefEditor.commit();
        }
      }
    }
  }

  private class MessageHandler extends Handler {
    private final WeakReference<LyricActivity> playwordsWeakRef;
    public static final String new_lyric           = "NEW_LYRIC";
    public static final String holder_ready        = "HOLDER_READY";
    public static final String audio_focus_missing = "AUDIO_FOCUS_MISSING";
    public static final String song_complete       = "SONG_COMPLETE";

    public MessageHandler(LyricActivity playwords) {
      playwordsWeakRef = new WeakReference<LyricActivity>(playwords);
    }

    @Override
    public void handleMessage(Message msg) {
      LyricActivity playwords = playwordsWeakRef.get();
      Bundle data = msg.getData();
      if (data.containsKey(holder_ready))
        setupComponents();
      else if (data.containsKey(audio_focus_missing)){
        LayoutInflater inflater = playwords.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout_audio_focus,
          (ViewGroup)playwords.findViewById(R.id.toast_layout_root));
        Toast toast = new Toast(playwords.getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(layout);
        toast.show();
      }
      else if (data.containsKey(new_lyric) && playwords != null) {
        if (data.getBoolean(new_lyric))
          playwords.updateFromSong();
      }
      else if (data.containsKey(song_complete)) {
        stop();
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle bundle) {
    super.onSaveInstanceState(bundle);
    bundle.putString(LYRIC_FILENAME, this.lyricsFilename);
    bundle.putString(MUSIC_FILENAME, this.musicFilename);
    bundle.putString(LYRIC_VIEW_PREF, this.viewPref);
    bundle.putParcelable(STATE, this.state);
  }

  @Override
  public void onPause() {
    super.onPause();
    if (state.paused && AudioManager.AUDIOFOCUS_GAIN == audioFocus)
      stop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (null != messageHandler) {
      this.messageHandler.removeCallbacksAndMessages(null);
    }
    if (null != exceptionHandler) {
      exceptionHandler.removeCallbacksAndMessages(null);
    }
    AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    am.abandonAudioFocus(this);
  }

  @Override
  public void onCompletion(MediaPlayer mP) {
    Message message = this.messageHandler.obtainMessage();
    message.setData(ez.booleanBundle(MessageHandler.song_complete, true));
    this.messageHandler.sendMessage(message);
  }

  public void exceptionOccuredAsync(Exception exception) {
    Message message = exceptionHandler.obtainMessage();
    Bundle bundle = new Bundle();
    bundle.putString(Map.TITLE, "Error a Ocurrido");
    bundle.putString(Map.MESSAGE, exception.getMessage());
    message.setData(bundle);
    exceptionHandler.sendMessage(message);
  }

  public String getLyricFilename () {
    return lyricsFilename;
  }

  public void addLyricFragment (Fragment fragment, String tag) {
    FragmentManager fm = getSupportFragmentManager();
    Fragment existing = fm.findFragmentByTag(tag);
    if (existing == null) {
      FragmentTransaction ft = fm.beginTransaction();
      ft.add(R.id.lyric_group_view, fragment, tag);
      ft.commit();
    }
  }

  @SuppressLint("HandlerLeak")
  private class ExceptionHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {//TODO have this dialog show.
      Bundle origBundle = msg.getData();
      FragmentManager fm = getSupportFragmentManager();
      DialogFragment notificationDialog =
        (DialogFragment) fm.findFragmentByTag(Map.EXCEPTION_NOTIFICATION_TAG);
      if (null == notificationDialog) {
        notificationDialog = NotificationDialogV4.newInstance(origBundle);
        notificationDialog.show(getSupportFragmentManager(),
          Map.EXCEPTION_NOTIFICATION_TAG);
      }
    }
  }

  private void showDialogFragment(DialogFragment fragment, String tag, int requestCode) {
    FragmentManager fm = this.getSupportFragmentManager();
    Fragment foundFragment = (Fragment) fm.findFragmentByTag(tag);
    if (null == foundFragment) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      fragment.setTargetFragment(null, requestCode);
      fragment.show(ft, tag);
    }
  }

  private void attachAudioFocus () {
    AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    int result = am.requestAudioFocus(this,
                                      AudioManager.STREAM_MUSIC,
                                      AudioManager.AUDIOFOCUS_GAIN);
    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
      audioFocus = result;
      if (state.playing())
        play();
    }
  }

  private void pause () {
    state.setToPaused();
    updateButtons();
    musicDealer.changeOfStateRequiresAction(state);
  }

  private void play () {
    state.setToPlay();
    updateButtons();
    musicDealer.changeOfStateRequiresAction(state);
  }

  private void stop () {
    state.setToStop(musicDealer.getMusicPosition(state));
    updateButtons();
    musicDealer.changeOfStateRequiresAction(state);
  }

  private void updateButtons () {
    PauseButton pauseButton = (PauseButton)findViewById(R.id.pause_start_button);
    pauseButton.setPaused(state);
    StopButton stopButton = (StopButton)findViewById(R.id.stop_button);
    stopButton.initialize();
    SynchButton synchButton = (SynchButton) findViewById(R.id.synch_button);
    synchButton.setSynched(state);
    BeginningButton beginningButton =
      (BeginningButton)findViewById(R.id.back_to_start_button);
    beginningButton.initialize();
  }
}
