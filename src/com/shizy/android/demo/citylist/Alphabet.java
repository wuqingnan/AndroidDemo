package com.shizy.android.demo.citylist;

public class Alphabet implements ICityListItem {

	private String mLetter;
	
	public String getLetter() {
		return mLetter;
	}

	public void setLetter(String letter) {
		mLetter = letter;
	}

	@Override
	public int getType() {
		return TYPE_ALPHABET;
	}

	@Override
	public String getTitle() {
		return getLetter();
	}

}
