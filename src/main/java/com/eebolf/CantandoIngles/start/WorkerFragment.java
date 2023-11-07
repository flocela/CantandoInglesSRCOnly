package com.eebolf.CantandoIngles.start;

import com.eebolf.CantandoIngles.listeners.WorkProgressListener;

import android.app.Fragment;
import android.os.Bundle;

public abstract class WorkerFragment extends Fragment {

	protected boolean mReady = false;
	protected boolean mQuitting = false;
	protected String info = null;
	protected final Thread mThread = new Thread() {

		@Override
		public void run () {		
			synchronized(this) {
				while (!mReady) {		
					if (mQuitting) {
						return;
					}
					try {
						wait();
					} catch (InterruptedException e) {
					}
				}
			}
			
			work();
			
			if (workIsDone()) {
				synchronized(this) {				
					if (mReady && !mQuitting) 
					  ((WorkProgressListener)getActivity()).workProgressReportAsync(getTag(), false, 100, info);			
				}
			}				
		}		
	};
	
	protected abstract boolean work ();	
	protected abstract boolean workIsDone();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);		
		mThread.start();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		synchronized (mThread) {
			mReady = true;
			mThread.notify();
			if (workIsDone()) {            	 
				((WorkProgressListener)getActivity()).workProgressReportAsync(getTag(), false, 100, info);  
			}
		}
  }
	
	@Override
	public void onDestroy() {
		synchronized (mThread) {
			mReady = false;
			mQuitting = true;
			mThread.notify();            
		}
		super.onDestroy();
	}
	
	@Override
	public void onDetach() {
		synchronized (mThread) {
			mReady = false;
			mThread.notify();
		}
		super.onDetach();
	}
}
