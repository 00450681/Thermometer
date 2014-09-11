package com.thermometer.utility;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;


public class StringUtil {
	static public boolean isBlank(String str) {
		boolean ret = true;
		if (str != null && !str.equals("")) {
			ret = false;
		}
		return ret;
	}
	static public byte[] strToByteArray(String hexString) {
		int length = hexString.length();
		int count = (length / 2) + (length % 2 == 0 ? 0 : 1);
		byte []ret = new byte[count];
		for (int i = 0; i < count; i++) {
			ret[i] = singleStrToByte(hexString.substring(i * 2, (i + 1) * 2));
		}
		return ret;
	}
	static private byte singleStrToByte(String hexString) {
		byte []hexStrBytes = hexString.getBytes();
		byte bit0 = Byte.decode("0x" + new String(new byte[]{hexStrBytes[0]}));
		bit0 = (byte) (bit0 << 4);
		if (hexString.length() == 2) {
		byte bit1 = Byte.decode("0x" + new String(new byte[]{hexStrBytes[1]}));
			return (byte) (bit0 | bit1);
		}
		return bit0;
	}
}
