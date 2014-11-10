package com.shizy.android.demo.citylist;

import org.json.JSONObject;

public class City {

	private String name;
	private String provice;
	
	public City() {
		
	}
	
	public City(JSONObject object) {
		if (object != null) {
			setName(object.optString("name"));
			setProvice(object.optString("provice"));
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getProvice() {
		return provice;
	}
	
	public void setProvice(String provice) {
		this.provice = provice;
	}
	
	public String toJSONString() {
		return toJSONString().toString();
	}
	
}
