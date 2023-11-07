package com.eebolf.CantandoIngles.todowithlists;


import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.BaseColumns;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.eebolf.CantandoIngles.R;
import com.eebolf.CantandoIngles.SongsCP;
import com.eebolf.CantandoIngles.listeners.WorkProgressListener;
import com.eebolf.CantandoIngles.start.DownloadReceiver;
import com.eebolf.CantandoIngles.start.TracedException;
import com.eebolf.CantandoIngles.start.UriDeterminer;
import com.eebolf.CantandoIngles.utils.Map;
import com.eebolf.CantandoIngles.utils.ez;
import com.eebolf.CantandoIngles.workers.SongDeleter;
import com.eebolf.CantandoIngles.workers.SongImporter;
import com.eebolf.CantandoIngles.workers.UnzipperWorker;
import com.eebolf.CantandoIngles.workers.ZipFileDownloaderWorker;

import java.io.File;
import java.io.IOException;

public class DownloadSongService extends Service implements WorkProgressListener {

  private Looper mServiceLooper;
  private ServiceHandler mServiceHandler;
  private Uri tableUri = Uri.parse((SongsCP.CP_URI) + SongsCP.song_table);
  private ZipFileDownloaderWorker downloader;

  public static final String half_way_done_message            = "Cincuenta por ciento hecho.";
  public static final String three_quarters_way_done_message  = "Setenta y cinco por ciento hecho.";
  public static final String one_quarter_way_done_message     = "Vienticinco por ciento hecho.";
  public static final String nine_tenths_done_message         = "Noventa por ciento hecho.";
  public static final String one_thenth_way_done_message      = "Diez por ciento hecho.";
  public static final String done_downloading_song            = "La cancio\u0301n esta lista.";

  @Override
  public void workProgressReportAsync(String tag, boolean cancelled, int percentDone, String info) {
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    mBuilder.setContentTitle(info);
    if (Map.ZIP_FILE_DOWNLOADER.equals(tag)) {
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
        Bundle bundle = ez.booleanBundle(DownloadReceiver.PROGRESS_BUNDLE, true);
        bundle.putInt(DownloadReceiver.PROGRESS_PERCENTAGE, 50);
        bundle.putString(DownloadReceiver.PROGRESS_SONG, info);
        Intent intent = new Intent(DownloadReceiver.DOWNLOAD_FILTER);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
      }
      else if (75 <= percentDone && 90 > percentDone){
        mBuilder.setContentText(three_quarters_way_done_message);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_75);
      }
      else if (90 <= percentDone && 100 > percentDone) {
        mBuilder.setContentText(nine_tenths_done_message);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_90);
      }
    }
    if (Map.UNZIP.equals(tag)) {
      if (100 == percentDone) {
        mBuilder.setContentText(done_downloading_song);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_100);
        Bundle bundle = ez.booleanBundle(DownloadReceiver.PROGRESS_BUNDLE, true);
        bundle.putInt(DownloadReceiver.PROGRESS_PERCENTAGE, 100);
        bundle.putString(DownloadReceiver.PROGRESS_SONG, info);
        Intent intent = new Intent(DownloadReceiver.DOWNLOAD_FILTER);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
      }
    }
    NotificationManager mNotificationManager =
      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.notify(Map.NOTIFICATION_ID, mBuilder.build());
  }

  private final class ServiceHandler extends Handler {

    public ServiceHandler (Looper looper) {
      super(looper);
    }

    @Override
    public void handleMessage (Message msg) {
      Bundle bundle = msg.getData();
      String newWorkTitle = bundle.getString(SongsCP.new_work_title);
      ContentResolver cR = getContentResolver();
      int rowID = getIDFromTitle(cR, tableUri, newWorkTitle);
      if (rowID != -1) {
        SongDeleter songDeleter = new SongDeleter(DownloadSongService.this, rowID);
        songDeleter.work();
      }
      SongImporter songImporter = new SongImporter(bundle);
      Uri uri = cR.insert(tableUri, songImporter.getSongValues());
      int rowsAffected = Integer.parseInt(UriDeterminer.getRowKey(uri));
      if (rowsAffected == 0) {
        broadcastException(new TracedException(new Exception("contentResolver returned" +
          " zero rows affected."),"DownloadSongService. "));
        return;
      }
      File dir = DownloadSongService.this.getDir("songs", Context.MODE_PRIVATE);
      String outputFilename = dir.getAbsolutePath() + "/" +
        bundle.getString(Map.NAKED_FILENAME) + ".zip";
      downloader =
        new ZipFileDownloaderWorker(bundle.getString(Map.SERVER_WITH_ZIP_WEB_ID),
                                    outputFilename,
                                    bundle.getString(SongsCP.new_work_title),
                                    DownloadSongService.this);
      try {
        downloader.work();
        UnzipperWorker unzipper = new UnzipperWorker(outputFilename);
        unzipper.work();
        String musicFilename = unzipper.getMusicFileName();
        if (!musicFilename.equals(bundle.getString(musicFilename))) {
          ContentValues revisedFileName = new ContentValues();
          revisedFileName.put(SongsCP.music_file, musicFilename);
          int revisingRow = Integer.parseInt(UriDeterminer.getRowKey(uri));
          cR.update(tableUri, revisedFileName, ez.where(BaseColumns._ID, revisingRow), null);
        }
        workProgressReportAsync(Map.UNZIP, false, 100,
          bundle.getString(SongsCP.new_work_title));
      }
      catch (Exception e) {
        broadcastException(e);
        return;
      }

      stopSelf(msg.arg1);
    }

    private Integer getIDFromTitle (ContentResolver cR, Uri table, String newWorkTitle) {
      Cursor cursor = cR.query(table,
                               new String[]{BaseColumns._ID},
                               ez.where(SongsCP.new_work_title, newWorkTitle),
                              null,
                              null);
      if (cursor != null && 0 < cursor.getCount()) {
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
      }
      return -1;
    }

  }

  @Override
  public void onCreate () {
    HandlerThread thread = new HandlerThread("ServiceStartArguments",
      Process.THREAD_PRIORITY_BACKGROUND);
    thread.start();
    mServiceLooper = thread.getLooper();
    mServiceHandler = new ServiceHandler(mServiceLooper);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Bundle bundle = intent.getExtras();
    Message msg = mServiceHandler.obtainMessage();
    msg.arg1 = startId;
    msg.setData(bundle);
    mServiceHandler.sendMessage(msg);
    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy () {
    super.onDestroy();
    Bundle bundle = ez.booleanBundle(DownloadReceiver.ALL_SONGS_DONE, true);
    Intent intent = new Intent(DownloadReceiver.DOWNLOAD_FILTER);
    intent.putExtras(bundle);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void broadcastException (Exception exception) {
    Bundle bundle = ez.booleanBundle(DownloadReceiver.EXCEPTION_BUNDLE, true);
    bundle.putString(Map.TITLE, "Un error ocurio\u0301.");
    if (exception instanceof TracedException) {
      TracedException tracedException = (TracedException)exception;
      bundle.putString(Map.TITLE, "Un error descargando la cancio\u0301n de la red.");
      if (tracedException.getOrigException() instanceof IOException) {
        String notification = "Hay posibilidad que no estas connectado a la red.\nO "
          + "www.appsbyflo\\cantando_ingles no esta funcionando orita.\n";
        bundle.putString(Map.MESSAGE, notification + "/n" + tracedException.getMessage());
      }
      else {
        bundle.putString(Map.MESSAGE, tracedException.getMessage());
      }
    }
    else {
      bundle.putString(Map.TITLE, "Un error ocurio\u0301.");
      bundle.putString(Map.MESSAGE, exception.getMessage());
    }
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    mBuilder.setContentTitle("Error descargando la cancio\u0301n de la red.");
    mBuilder.setSmallIcon(R.drawable.ic_launcher_error_c);
    NotificationManager mNotificationManager =
      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.notify(Map.NOTIFICATION_ID, mBuilder.build());

    Intent intent = new Intent(DownloadReceiver.DOWNLOAD_FILTER);
    intent.putExtras(bundle);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }
}
