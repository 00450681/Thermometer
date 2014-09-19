package com.thermometer.message;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;

import com.thermometer.message.model.Oauth2Response;
import com.thermometer.utility.HttpUtil;
import com.thermometer.utility.JSONUtil;

public class Oauth2GetAccessTokenMsg extends Message {

	String code;
	Oauth2Response response;
	public Oauth2GetAccessTokenMsg() {
		
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
		url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx4c80d3e38ff364d9&secret=0d3f85782bc21f70c38c2c74e4b66d00&code="
				+ code + "&grant_type=authorization_code";
	}
	public Oauth2Response getResponse() {
		return response;
	}
	public void setResponse(Oauth2Response response) {
		this.response = response;
	}
	
	@Override
	public boolean sendMessage() {
		// TODO Auto-generated method stub
		String jsonString = HttpUtil.sendHttpsGET(url);
		
		if (jsonString != null) {
			response = (Oauth2Response) JSONUtil.getObjectFromJSONStr(jsonString, Oauth2Response.class);
			return true;
		}
		return false;
	}
	

}
