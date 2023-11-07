package com.eebolf.CantandoIngles.workers;

import com.eebolf.CantandoIngles.listeners.CancelListener;

public abstract class Worker implements CancelListener {
  protected boolean isCancelRequested = false;
  protected boolean isCancelCompleted = false;
  
	abstract public void work();
	
	@Override
	public void setCancelRequestedToTrue() {
		isCancelRequested = true;
	}
	
	public boolean isCancelCompleted() {
		return isCancelCompleted;
	}
	
	public boolean isCancelRequested() {
		return isCancelRequested;
	}
	
	
	
}
