package com.thermometer.message;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Encoder;

import com.thermometer.servlet.WXServlet;
import com.thermometer.utility.HttpUtil;
import com.thermometer.utility.JavaLog;

public class ToDeviceMsg extends Message{
	private String accessToken = WXServlet.ACCESS_TOKEN;
	private String deviceType;
	private String deviceId;
	private String openId;
	private byte []content;
	
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


	public byte [] getContent() {
		return content;
	}


	public void setContent(byte[] content) {
		this.content = content;
	}


	@Override
	public boolean sendMessage() {
		// TODO Auto-generated method stub
		String data = "{ " + DEVICE_TYPE + ": \"" + deviceType +
				"\", " + DEVICE_ID + ": \"" + deviceId + "\", " + 
				OPEN_ID + ": \"" + openId + "\", " + CONTENT + 
				": \"" + new BASE64Encoder().encode(content) + "\"}" ;
		url = "https://api.weixin.qq.com/device/transmsg?access_token=" + accessToken;
		JavaLog.log4j.info("URL is " + url);
		JavaLog.log4j.info("Data is " + data);
		String response;
			try {
				response = new String(HttpUtil.sendHttpsPOST(url, data).getBytes(), "UTF-8");
				JavaLog.log4j.info("response is " + response);
				JSONObject jsonObj = new JSONObject(response);
				if (jsonObj.getInt("errcode") == 42001) {
					JavaLog.log4j.info("access token is expired...get another ");
					WXServlet.ACCESS_TOKEN = HttpUtil.getAccessToken();
					String []urlAndArg = url.split("=");
					url = urlAndArg[0] + "=" + WXServlet.ACCESS_TOKEN;
					HttpUtil.sendHttpsPOST(url, data);
					return true;
				} else {
					return true;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return true;
			}
			
		return false;
	}
}
