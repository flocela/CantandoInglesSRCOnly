package com.eebolf.CantandoIngles.workers;

import com.eebolf.CantandoIngles.start.TracedException;
import com.eebolf.CantandoIngles.utils.ez;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class UnzipperWorker extends Worker {

	private String mZipFileName; 
	private String mLocationForFiles;
	private String musicFileName;

	/* UnzipperWorker will overwrite any files that may already be in the songs directory
		new UnzipperWorker("/data/user/0/com.eebolf.CantandoIngles/app_songs/the_first_noel_admiral_bob.zip")
	*/
	public UnzipperWorker (String zipFileFullName) {
		mZipFileName = zipFileFullName;
		mLocationForFiles = ez.getPath(mZipFileName);
		// mLocationForFiles  /data/user/0/com.eebolf.CantandoIngles/app_songs/
	}
	
	@Override
	public void work() {
		
		try {
			FileInputStream fin = new FileInputStream(mZipFileName); 
			ZipInputStream zin = new ZipInputStream(fin); 
			ZipEntry ze = null; 
			
			while ((ze = zin.getNextEntry()) != null) {  
				if(ze.isDirectory()) {
					File tempFile = new File(mLocationForFiles + ze.getName());
					tempFile.mkdirs();
				} 
				else {
					if (ze.getName().endsWith("mp4") ||
						  ze.getName().endsWith("mp3") ||
						  ze.getName().endsWith("m4a")) {
						musicFileName = ze.getName();
					}
					String path = mLocationForFiles + ze.getName();
					OutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
					int bufferSize = 16384;
					byte[] buffer = new byte[bufferSize];
					int len = 0;
					while ((len = zin.read(buffer)) != -1) {
						stream.write(buffer, 0, len);
						if (true == this.isCancelRequested()) {
							if(stream != null) {
								stream.close();
							}							
							zin.closeEntry();
							if (zin != null) {
								zin.close();
							}
							this.isCancelCompleted= true;
							return;
						}
					}
					if(stream != null) {
						stream.close();
					}
					zin.closeEntry();
				}				
			}
			if (zin != null) {
				zin.close();
			}
		}
		catch(Exception e) {
			StringBuilder errorBuilder = new StringBuilder("UnzipperWorker ");
			errorBuilder.append("mZipFile: ").append(mZipFileName).append(" ");
			errorBuilder.append("mLocationForFiles: ").append(mLocationForFiles).append("\n");
			TracedException tracedException = new TracedException(e, "UnzipperWorker.work();"+mZipFileName+";"+mLocationForFiles);
			throw tracedException;
		}
		File zipFile = new File(mZipFileName);
		if (zipFile.exists()) {
		  zipFile.delete();
		}
	}

  public String getDirLocationOfUnzippedFiles() {
  	return mLocationForFiles;
  }  

  public String getMusicFileName () {
		return this.musicFileName;
	}
}
