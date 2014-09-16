package com.thermometer.db.model;

public class Device {
	private String deviceType;
	private String deviceID;
	private int deviceMeasureInterval;
	
	public static final String TABLE = "device";
	public static final String DEVICE_TYPE = "device_type";
	public static final String DEVICE_ID = "device_id";
	public static final String DEVICE_MEASURE_INTERVAL = "device_measure_interval";
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public int getDeviceMeasureInterval() {
		return deviceMeasureInterval;
	}
	public void setDeviceMeasureInterval(int deviceMeasureInterval) {
		this.deviceMeasureInterval = deviceMeasureInterval;
	}
	
	
			
}
