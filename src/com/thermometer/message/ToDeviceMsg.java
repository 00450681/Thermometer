package com.thermometer.message;

import com.thermometer.servlet.WXServlet;
import com.thermometer.utility.HttpUtil;

public class ToDeviceMsg extends Message{
	private String accessToken = WXServlet.ACCESS_TOKEN;
	private String deviceType;
	private String deviceId;
	private String openId;
	private String content;
	
	public static String ACCESS_TOKEN = "\"access_token\"";
	public static String DEVICE_TYPE = "\"device_type\"";
	public static String DEVICE_ID = "\"device_id\"";
	public static String OPEN_ID = "\"open_id\"";
	public static String CONTENT = "\"content\"";





	public String getDeviceType() {
		return deviceType;
	}


	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}


	public String getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	public String getOpenId() {
		return openId;
	}


	public void setOpenId(String openId) {
		this.openId = openId;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	@Override
	public boolean sendMessage() {
		// TODO Auto-generated method stub
		String data = "{ " + DEVICE_TYPE + ": \"" + deviceType +
				"\", " + DEVICE_ID + ": \"" + deviceId + "\", " + 
				OPEN_ID + ": \"" + openId + "\", " + CONTENT + 
				": \"" + content + "\"}" ;
		url = "https://api.weixin.qq.com/device/transmsg?access_token=" + accessToken;
		HttpUtil.sendHttpsPOST(url, data);
		return false;
	}
}
