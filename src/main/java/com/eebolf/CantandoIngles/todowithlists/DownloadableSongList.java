package com.eebolf.CantandoIngles.todowithlists;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.collectors.SongInfo;
import com.eebolf.CantandoIngles.dialogs.MessageProgressDialog;
import com.eebolf.CantandoIngles.dialogs.NotificationDialog;
import com.eebolf.CantandoIngles.dialogs.NotificationWithReturnDialog;
import com.eebolf.CantandoIngles.listeners.DialogDoneListener;
import com.eebolf.CantandoIngles.listeners.WorkProgressListener;
import com.eebolf.CantandoIngles.start.DownloadReceiver;
import com.eebolf.CantandoIngles.start.TracedException;
import com.eebolf.CantandoIngles.utils.Map;
import com.eebolf.CantandoIngles.utils.ez;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class DownloadableSongList extends ListActivity 
	                                implements LoaderManager.LoaderCallbacks<List<SongInfo>>,
                                             WorkProgressListener,
                                             DialogDoneListener {
	private SongInfoListAdapter adapter;
	private MessageProgressDialog progressDialog;
	private Handler exceptionHandler = new ExceptionHandler();
	private Handler progressHandler = new ProgressHandler();
	private String songListUrl = "http://www.appsbyflo.com/cantando_ingles/songs/";
	private String songZipUrl = "http://www.appsbyflo.com/cantando_ingles/songs/get_zip_m4a/";

	private static final String PROGRESS_DIALOG_MESSAGE = "progress_dialog_message";
  private static final String SONG_BUILDER_TAG        = "song_builder";

	private static final String LIST_LOADER_TAG                 = "list_loader_tag";
	private static final String downloading_song_list_message   = "Descargando cancio\u0301nes de la red.";
	public static final String  half_way_done_message           = "Cincuenta por ciento hecho.";
	public static final String  three_quarters_way_done_message = "Setenta y cinco por ciento hecho.";
	public static final String  one_quarter_way_done_message    = "Vienticinco por ciento hecho.";
	public static final String  nine_tenths_done_message        = "Noventa por ciento hecho.";
	public static final String  one_thenth_way_done_message     = "Diez por ciento hecho.";
	public static final String  done_downloading_song           = "La cancio\u0301n esta lista.";

	private DownloadReceiver mMessageReceiver;

	@Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.downloadable_song_list);
    startPreviousProgressDialog(savedInstanceState);
    this.adapter = new SongInfoListAdapter(this);
    setListAdapter(this.adapter);

    if (serviceIsConnected()) {
      if (!progressDialogIsShowing()) {
        this.startProgressDialog(downloading_song_list_message, true);
      }
      getLoaderManager().initLoader(0, null, this);
    }
    else {
      DialogFragment noConnectionNotificationDialog = NotificationWithReturnDialog.
          newInstance("La red no esta disponible.","Esta seguro que "
                      + "la conexio\u0301n al wifi o la conexio\u0301n " +
						            "mobil esta prendida?");
      showDialogFragment(noConnectionNotificationDialog,
				                 Map.NO_INTERNET_TAG,
				                 Map.DIALOG_NOTIFICATION_ID);
		}
		initDownloadBroadcastManager();
	}

	@Override
	public void notifyDialogDone (int requestCode, 
			                          boolean isCancelled, 
			                          Bundle returnedBundle) {
		switch (requestCode) {
		  case Map.DIALOG_NOTIFICATION_ID: {
		    finish();
		  }
		}
	}

	@Override
	public Loader<List<SongInfo>> onCreateLoader(int arg0, Bundle arg1) {
	    WebSongListLoader songListLoder = new WebSongListLoader(this, songListUrl);
			return songListLoder;			
	}

	@Override
	public void onLoadFinished(Loader<List<SongInfo>> loader, 
			                       List<SongInfo> data) {	  
	  workProgressReportAsync(LIST_LOADER_TAG, false, 100, null);
		if (loader instanceof WebSongListLoader) {
		  TracedException tracedException = ((WebSongListLoader) loader).getTracedException();
		  if (null != tracedException) {
		    exceptionOccuredAsync(tracedException);
		  }
		}
		if (data != null && !data.isEmpty()) {
			Collections.sort(data, SongInfo.SongInfoComparator);
		}
		this.adapter.setData(data);
	}	

	@Override
	public void onLoaderReset(Loader<List<SongInfo>> arg0) {
		this.adapter.setData(null);		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		SongInfo songInfo = (SongInfo)this.adapter.getItem(position);
		addSong(songInfo);
	}	

	@Override
	public void workProgressReportAsync(String tag,
																			boolean isCancelled,
																			int percentDone,
																			String info) {
	  if (SONG_BUILDER_TAG == tag) {
	    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
	    mBuilder.setContentTitle(info);
			if (10 <= percentDone && 25 > percentDone) {
				mBuilder.setContentText(one_thenth_way_done_message);
				mBuilder.setSmallIcon(R.drawable.ic_launcher_10);
			}
	    else if (25 <= percentDone && 50 > percentDone) {
	      mBuilder.setContentText(one_quarter_way_done_message);
	      mBuilder.setSmallIcon(R.drawable.ic_launcher_25);
	    }
	    else if (50 <= percentDone && 75 > percentDone){
	      mBuilder.setContentText(half_way_done_message);
	      mBuilder.setSmallIcon(R.drawable.ic_launcher_50);
	    }
			else if (75 <= percentDone && 90 > percentDone){
				mBuilder.setContentText(three_quarters_way_done_message);
				mBuilder.setSmallIcon(R.drawable.ic_launcher_75);
			}
			else if (90 <= percentDone && 100 > percentDone) {
				mBuilder.setContentText(nine_tenths_done_message);
				mBuilder.setSmallIcon(R.drawable.ic_launcher_90);
			}
			else {
				mBuilder.setContentText(done_downloading_song);
				mBuilder.setSmallIcon(R.drawable.ic_launcher_101);
				Message message = progressHandler.obtainMessage();
				progressHandler.sendMessage(message);
	    }
	    NotificationManager mNotificationManager =
	        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    mNotificationManager.notify(Map.NOTIFICATION_ID, mBuilder.build());
	  }
	  else if (LIST_LOADER_TAG == tag) {
			closeDownloadSongListProgressDialogAsync();
	  }
	}
	
	@Override
	protected void onStop () {
		super.onStop();
		closeProgressDialogAsync();		
	}
	
	@Override
	public void onDestroy () {		
		super.onDestroy();
		closeProgressDialogAsync();
		if (null != progressHandler) {
		  progressHandler.removeCallbacksAndMessages(null);
	    progressHandler = null;
		}
		if (null != exceptionHandler) {
		  exceptionHandler.removeCallbacksAndMessages(null);
	    exceptionHandler = null;
		}
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	}
	
	@Override
	public void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		if (null != this.progressDialog) {
			outState.putString(PROGRESS_DIALOG_MESSAGE, progressDialog.getMessage());
		}
	}

  public void exceptionOccuredAsync(TracedException exception) {
    Message message =  exceptionHandler.obtainMessage();
    Bundle bundle = new Bundle();
    if (exception instanceof TracedException) {
      TracedException tracedException = (TracedException)exception;
      if (tracedException.getLocalDescription().contains("WebSongListLoader")) {
        bundle.putString(Map.TITLE, "Un error recibiendo la lista de cancio\u0301nes.");
      }
      else {
        bundle.putString(Map.TITLE, "Un error ocurrio\u0301.");
      }
      if (tracedException.getOrigException() instanceof IOException) {
        String notification = "Hay posibilidad que no estas connectado a la red. O "
            + "www.appsbyflo\\cantando_ingles no esta funcionando orita.\nError: ";
        bundle.putString(Map.MESSAGE, notification + tracedException.getMessage());
      }
      else {
        bundle.putString(Map.MESSAGE, tracedException.getMessage());
      }
    }
    else {
      bundle.putString(Map.TITLE, "Un error ocurio\u0301.");
      bundle.putString(Map.MESSAGE, exception.getMessage());
    }    
    message.setData(bundle);
    exceptionHandler.sendMessage(message); 
  }
  
  @SuppressLint("HandlerLeak")
  private class ExceptionHandler extends Handler {
    @Override
    public void handleMessage (Message msg) {
      Bundle origBundle = msg.getData();
      FragmentManager fm = getFragmentManager();
      DialogFragment notificationDialog = (DialogFragment)fm.
				findFragmentByTag(Map.EXCEPTION_NOTIFICATION_TAG);
      if(null == notificationDialog) {
        notificationDialog = NotificationDialog.newInstance(origBundle);
				android.app.FragmentTransaction ft = fm.beginTransaction();
				ft.add(notificationDialog, Map.EXCEPTION_NOTIFICATION_TAG);
				ft.commitAllowingStateLoss();
      }
    }
  }

	private void showDialogFragment(DialogFragment fragment, String tag, int requestCode) {
		FragmentManager fm = this.getFragmentManager();
		Fragment foundFragment = (Fragment) fm.findFragmentByTag(tag);
		if (null == foundFragment) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			fragment.setTargetFragment(null, requestCode);
			fragment.show(ft, tag);
		}
	}

	private boolean progressDialogIsShowing () {
		if (null != this.progressDialog && this.progressDialog.isShowing()) {
			return true;
		}
		return false;
	}

	private void startPreviousProgressDialog (Bundle savedInstanceState) {
		if (null != savedInstanceState) {
			if (null != savedInstanceState.getString(PROGRESS_DIALOG_MESSAGE))
				this.startProgressDialog(savedInstanceState.getString(PROGRESS_DIALOG_MESSAGE),
					                       true);
		}
	}

	private boolean serviceIsConnected () {
		ConnectivityManager cm = (ConnectivityManager) this
			.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (null != networkInfo && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	private void closeDownloadSongListProgressDialogAsync () {
		if (null != this.progressDialog && this.progressDialog.getMessage().
			equals(downloading_song_list_message)) {
			this.closeProgressDialogAsync();
		}
	}

	private void closeProgressDialogAsync() {
		if (null != this.progressDialog) {
			progressDialog.dismiss();
			this.progressDialog = null;
		}
	}

	private void startProgressDialog(String message, boolean show) {
		closeProgressDialogAsync();
		progressDialog = new MessageProgressDialog(this, message);
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				if (progressDialog.getMessage().equals(downloading_song_list_message)) {
					Intent intent = new Intent();
					setResult(RESULT_CANCELED, intent);
					finish();
					return;
				}
				if (null != progressDialog) {
					progressDialog = null;
				}
			}
		});
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}

	private void addSong(SongInfo songInfo) {
		showLongToast("Descargando: ", songInfo.getNewWorkTitle());
		Bundle bundle = ez.convertToBundle(songInfo);
		bundle. putString(Map.SERVER_WITH_ZIP_WEB_ID, songZipUrl +songInfo.getWebId());
		Intent intent = new Intent(this, DownloadSongService.class);
		intent.putExtras(bundle);
		startService(intent);
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

  @SuppressLint("HandlerLeak")
  private class ProgressHandler extends Handler {
    @Override
    public void handleMessage (Message msg) {
			Bundle origBundle = msg.getData();
			if (origBundle.containsKey(DownloadReceiver.PROGRESS_BUNDLE) && origBundle.containsKey(Map.TOAST_LENGTH))
			  showShortToast(origBundle.getString(Map.TITLE), origBundle.getString(Map.MESSAGE));
			else if (origBundle.containsKey(DownloadReceiver.PROGRESS_BUNDLE))
				showLongToast(origBundle.getString(Map.TITLE), origBundle.getString(Map.MESSAGE));
			else if (origBundle.containsKey(DownloadReceiver.ALL_SONGS_DONE))
				finish();
		}
  }

	private void initDownloadBroadcastManager() {
		mMessageReceiver = new DownloadReceiver(progressHandler, exceptionHandler);
		LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
		broadcastManager.registerReceiver(mMessageReceiver,
			new IntentFilter(DownloadReceiver.DOWNLOAD_FILTER));
	}

}
