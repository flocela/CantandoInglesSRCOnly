package com.eebolf.CantandoIngles.listeners;


public interface DeleterItemListener {

	public boolean noticeItemInfoClick (String newWorkTitle, String englishTitle, String origArtist, String zipSize);
	
	public boolean noticeItemDeleteClick (Integer id, String newWorkTitle, String englishTitle, String OrigArtist);
	
}
