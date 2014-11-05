package com.thermometer.message;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.thermometer.servlet.WXServlet;
import com.thermometer.utility.HttpUtil;
import com.thermometer.utility.JavaLog;

public class ToWeChatMsg extends Message {

	public String openId, content;
	@Override
	public boolean sendMessage() {
		// TODO Auto-generated method stub
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
		url += WXServlet.ACCESS_TOKEN;//HttpUtil.getAccessToken();
		JavaLog.log4j.info("URL is " + url);
		String data = "{\"touser\":\"" + openId + "\",\"msgtype\":\"text\",\"text\":{\"content\":\"" + this.content + "\"}}";
		JavaLog.log4j.info("Data is " + data);
		String response;
		try {
			response = new String(HttpUtil.sendHttpsPOST(url, data).getBytes(), "UTF-8");
			JavaLog.log4j.info("response is " + response);
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj.getInt("errcode") == 42001 || jsonObj.getInt("errcode") == 40001) {
				JavaLog.log4j.info("access token is expired...get another ");
				WXServlet.ACCESS_TOKEN = HttpUtil.getAccessToken();
				String []urlAndArg = url.split("=");
				url = urlAndArg[0] + "=" + WXServlet.ACCESS_TOKEN;
				JavaLog.log4j.info("new URL is " + url);
				HttpUtil.sendHttpsPOST(url, data);
				return true;
			} else {
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JavaLog.log4j.info("UnsupportedEncodingException " + e.getMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JavaLog.log4j.info("JSONException " + e.getMessage());
		}
		
		return false;
	}

}
