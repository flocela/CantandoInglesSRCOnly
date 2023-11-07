package com.eebolf.CantandoIngles.start;

public class TracedException extends RuntimeException  {
  private Exception realException;
  private String localDescription;
  
  public TracedException (Exception e, String localDescription) {
  	this.realException = e;
  	this.localDescription = localDescription;
  }
  
  public Exception getOrigException () {
  	return this.realException;
  }
  
  public String getLocalDescription () {
  	return this.localDescription;
  }
  
  @Override
  public String getMessage () {
    return localDescription + "  " +realException.getMessage();
  }  
  
}
