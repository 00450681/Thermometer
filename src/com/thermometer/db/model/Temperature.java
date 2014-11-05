package com.thermometer.db.model;

public class Temperature {

	private String deviceID;
	private String openID;
	private String time;
	private int temperature;
	
	public static final String TABLE = "temperature";
	public static final String DEVICE_ID = "device_id";
	public static final String OPEN_ID = "open_id";
	public static final String TIME = "time";
	public static final String TEMPERATURE = "temp";
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getOpenID() {
		return openID;
	}
	public void setOpenID(String openID) {
		this.openID = openID;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
	
}
