package com.eebolf.CantandoIngles.listeners;

import android.os.Bundle;

public interface DialogDoneListener {

	public void notifyDialogDone (int requestCode, 
			                          boolean isCancelled, 
			                          Bundle returnedBundle);
	
}
