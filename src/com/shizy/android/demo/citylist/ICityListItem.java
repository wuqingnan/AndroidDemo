package com.shizy.android.demo.citylist;

public interface ICityListItem {
	
	public int TYPE_CITY = 0;
	public int TYPE_INDEXER = 1;

	public int getType();
	
	public String getTitle();
	
}
