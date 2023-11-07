package com.eebolf.CantandoIngles.mainactivity;


public interface MainListFragmentListener {

	public boolean noticeListItemClick (String musicName, String lyricsFile);
  public void noticeLoadFinished();
	public void noticeQueryIconClicked (Integer id);
	
}
