package com.eebolf.CantandoIngles.start;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.eebolf.CantandoIngles.utils.Map;
import com.eebolf.CantandoIngles.utils.ez;

public class DownloadReceiver extends BroadcastReceiver {
  public static final String DOWNLOAD_FILTER           = "DOWNLOAD_FILTER";
  public static final String EXCEPTION_BUNDLE          = "EXCEPTION_BUNDLE";
  public static final String PROGRESS_BUNDLE           = "PROGRESS_BUNDLE";
  public static final String PROGRESS_SONG             = "PROGRESS_SONG";
  public static final String PROGRESS_PERCENTAGE       = "PROGRESS_PERCENTAGE";
  public static final String ALL_SONGS_DONE            = "ALL_SONGS_DONE";
  public Handler progressHandler;
  public Handler exceptionHandler;

  public DownloadReceiver (Handler progressHandler, Handler exceptionHandler) {
    this.progressHandler = progressHandler;
    this.exceptionHandler = exceptionHandler;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    if (null != intent.getAction() &&
      intent.getAction() == DOWNLOAD_FILTER) {
      Bundle receivedBundle = intent.getExtras();
      if (receivedBundle.containsKey(PROGRESS_BUNDLE)) {
        Bundle handlerBundle = ez.booleanBundle(PROGRESS_BUNDLE, true);
        if (50 == receivedBundle.getInt(PROGRESS_PERCENTAGE)) {
          handlerBundle.putString(Map.TITLE, "Progreso de la Descarga");
          String songName = receivedBundle.getString(PROGRESS_SONG);
          handlerBundle.putString(Map.TITLE, "50% Descargada:");
          handlerBundle.putString(Map.MESSAGE,
            "La canción " + songName +  " esta cincuenta por " + "ciento decargada.");
        }
        else if (100  == receivedBundle.getInt(PROGRESS_PERCENTAGE)) {
          handlerBundle.putString(Map.TITLE, "100% Descargada:");
          String songName = receivedBundle.getString(PROGRESS_SONG);
          handlerBundle.putString(Map.MESSAGE,
            "La canción " + songName +  " esta lista.");
          handlerBundle.putInt(Map.TOAST_LENGTH, Toast.LENGTH_SHORT);
        }
        Message message = progressHandler.obtainMessage();
        message.setData(handlerBundle);
        progressHandler.sendMessage(message);
      }
      else if (receivedBundle.containsKey(EXCEPTION_BUNDLE)) {
        Message message = exceptionHandler.obtainMessage();
        message.setData(receivedBundle);
        exceptionHandler.sendMessage(message);
      }
      else if (receivedBundle.containsKey(ALL_SONGS_DONE)) {
        Message message = progressHandler.obtainMessage();
        message.setData(receivedBundle);
        progressHandler.sendMessage(message);
      }
    }
  }
}
