package com.thermometer.db.model;

public class Client {
	
	private String openID;
	
	public static final String TABLE = "client";
	public static final String OPEN_ID = "open_id";

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}
	
}
