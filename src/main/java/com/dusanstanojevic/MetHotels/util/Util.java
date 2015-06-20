package com.dusanstanojevic.MetHotels.util;

public class Util {
	public static final String getMD5Hash(String yourString) {
		try {
			java.security.MessageDigest md =
			java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(yourString.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
		 	return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}
}
