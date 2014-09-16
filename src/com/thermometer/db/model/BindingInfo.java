package com.thermometer.db.model;

public class BindingInfo {
	private String deviceID;
	private String openID;
	private String displayOnWeChat;
	
	public static final String TABLE = "binding_info";
	public static final String DEVICE_ID = "device_id";
	public static final String OPEN_ID = "open_id";
	public static final String DISPLAY_ON_WECHAT = "display_on_wechat";
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
	public String getDisplayOnWeChat() {
		return displayOnWeChat;
	}
	public void setDisplayOnWeChat(String displayOnWeChat) {
		this.displayOnWeChat = displayOnWeChat;
	}
	
	
}
