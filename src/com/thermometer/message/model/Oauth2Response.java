package com.thermometer.message.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.thermometer.utility.JSONProtocal;

public class Oauth2Response implements JSONProtocal{
	private String access_token;
	private String expires_in;
	private String refresh_token;
	private String openid;
	private String scope;
	
	private String errcode;
	private String errmsg;
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	/*@Override
	public static Object instantiateFromJSONString(String jsonString) {
		return null;
	}*/
	@Override
	public String toJSONString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("\"");
		sb.append("access_token:" + access_token);
		sb.append("\",");
		
		sb.append("\"");
		sb.append("expires_in:" + expires_in);
		sb.append("\",");
		
		sb.append("\"");
		sb.append("refresh_token:" + refresh_token);
		sb.append("\",");
		
		sb.append("\"");
		sb.append("openid:" + openid);
		sb.append("\",");
		
		sb.append("\"");
		sb.append("scope:" + scope);
		sb.append("\"");
		return sb.toString();
		//return super.toJSONString();
	}
	@Override
	public Object instantiateFromJSONString(String jsonString) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		try {
			access_token = (String) jsonObject.get("access_token");
			expires_in = Integer.toString((Integer) jsonObject.get("expires_in"));
			refresh_token = (String) jsonObject.get("refresh_token");
			openid = (String) jsonObject.get("openid");
			scope = (String) jsonObject.get("scope");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			try {
				Integer errcodeInt = (Integer) jsonObject.get("errcode");
				errcode = Integer.toString(errcodeInt);
				errmsg = (String) jsonObject.get("errmsg");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
			e.printStackTrace();
		}
		return this;
	}
	
	
}
