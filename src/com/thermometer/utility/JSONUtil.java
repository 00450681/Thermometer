package com.thermometer.utility;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
	public static Object getObjectFromJSONStr(String jsonString, Class clazz){
		if (JSONProtocal.class.isAssignableFrom(clazz)) {
			try {
				JSONProtocal protocal = (JSONProtocal) clazz.newInstance();
				return protocal.instantiateFromJSONString(jsonString);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
