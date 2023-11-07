package com.eebolf.CantandoIngles.workers;

import com.eebolf.CantandoIngles.listeners.WorkProgressListener;
import com.eebolf.CantandoIngles.start.TracedException;
import com.eebolf.CantandoIngles.utils.Map;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ZipFileDownloaderWorker extends Worker {
	
	private static final int DOWNLOAD_BUFFER_SIZE = 32768;

	private String serverWithId;
	private String outputFilename;
	private String newTitle;
	private WorkProgressListener workProgressListener;
			
	public ZipFileDownloaderWorker (String serverWithId, String outputFilename, String newTitle, WorkProgressListener workProgressListener) {
		this.serverWithId = serverWithId;
		this.outputFilename = outputFilename;	
		this.workProgressListener = workProgressListener;
		this.newTitle = newTitle;
	}
	
	@Override
	public void work () {
	  try {
			URL url = new URL(serverWithId);
			//URL url = new URL("https://cantandoinglesbucket.s3.amazonaws.com/m4a/act_cool_loveshadow.zip");
			//System.out.println("serverWithId: " + serverWithId);
			FileOutputStream fileStream;
      try {
        fileStream = new FileOutputStream(outputFilename);
      } catch (FileNotFoundException e) {
        TracedException tracedException = new TracedException(e, "ZipFileDownloaderWorker.work();new FileOutputStream"+outputFilename);
        throw tracedException;
      }
			
			byte[] data = new byte[DOWNLOAD_BUFFER_SIZE];
			int bytesRead = 0;
			
			URLConnection connection;
      connection = url.openConnection();
      connection.setUseCaches(false);
			BufferedInputStream inStream = new BufferedInputStream(connection.getInputStream());
      BufferedOutputStream outStream = new BufferedOutputStream(fileStream, DOWNLOAD_BUFFER_SIZE);
			int count = 0;
			while((bytesRead = inStream.read(data, 0, data.length)) >= 0)
      {
				if (true == this.isCancelRequested) {
					this.isCancelCompleted = true;
					break;					
				}
        outStream.write(data, 0, bytesRead);
        count++;
				if (200 == count) {
					this.workProgressListener.workProgressReportAsync(Map.ZIP_FILE_DOWNLOADER, false, 10, newTitle);
				}
        if (600 == count) {
					/*if (true)
						throw new IOException("fake IO Exception");
					Log.i("ATAG", "shouldn't get here");*/
          this.workProgressListener.workProgressReportAsync(Map.ZIP_FILE_DOWNLOADER, false, 25,  newTitle);
        }
				if (1125 == count) {
					this.workProgressListener.workProgressReportAsync(Map.ZIP_FILE_DOWNLOADER, false, 50,  newTitle);
				}
				else if (2500 == count) {
					this.workProgressListener.workProgressReportAsync(Map.ZIP_FILE_DOWNLOADER, false, 75,  newTitle);
				}
      }
			this.workProgressListener.workProgressReportAsync(Map.ZIP_FILE_DOWNLOADER, false, 90,  newTitle);
			outStream.close();
      fileStream.close();
      inStream.close();
	  }
	  catch(Exception e){
	    TracedException tracedException =
				new TracedException (e, "ZipFileDownload.work(), " +
				"serverWithId: " + serverWithId +", " + "outputFilename: " + outputFilename);
	    throw tracedException;
	  }
  }
}
