package com.shizy.android.demo.citylist;

public interface ICityListItem {
	
	public int TYPE_CITY = 0;
	public int TYPE_ALPHABET = 1;

	public int getType();
	
	public String getTitle();
	
}
