package cn.edu.lzu.library.utils;

import java.security.MessageDigest;

public class MD5Encoder {

	public static String encode(String string) {
		try {
			byte[] hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
			StringBuilder hex = new StringBuilder(hash.length * 2);
			for (byte b : hash) {
				if ((b & 0xFF) < 0x10) {
					hex.append("0");
				}
				hex.append(Integer.toHexString(b & 0xFF));
			}
			return hex.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
