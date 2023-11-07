package com.eebolf.CantandoIngles.listeners;


public interface WorkProgressListener {
	public void workProgressReportAsync (String tag, boolean cancelled, int percentDone, String info);
	//public void exceptionOccuredAsync (Exception e);
	
}
