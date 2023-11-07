package com.eebolf.CantandoIngles.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.eebolf.CantandoIngles.listeners.DialogDoneListener;
import com.eebolf.CantandoIngles.utils.Map;

/**
 * Takes care of remembering the requestcode through onSaveInstance().
 * Takes care of setting the DialogDoneListener if a TargetFragment was
 * supplied or if this dialog was started with an Activity.
 * Also specifies STYLE as STYLE_NO_TITLE
 * and theme as getTheme().
 */
public abstract class RetrieverDialogV4 extends DialogFragment {
	 protected int requestCode; 	
	 protected DialogDoneListener listener;
	 
	 protected void includeInOnSaveInstanceState(Bundle outState) {}
	 
	 @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);		
			this.setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
			
			Bundle args = (null != savedInstanceState)? savedInstanceState : getArguments(); 
			
			requestCode = this.getTargetRequestCode();		
			if (0 == requestCode) {
			   requestCode = args.getInt(Map.REQUEST_CODE);
			}			
		}
	 
	 protected void setDialogDoneListener() {		 
			Fragment fragment = getTargetFragment();
			if ( null == fragment ) {
				listener = (DialogDoneListener)RetrieverDialogV4.this.getActivity();
			}
			else {
				listener = (DialogDoneListener)fragment;
			}
	 }	
	 
	 @Override
		public void onSaveInstanceState (Bundle outState) {
			super.onSaveInstanceState(outState);
			outState.putInt(Map.REQUEST_CODE, this.requestCode);
			includeInOnSaveInstanceState(outState);
		}	 

}
