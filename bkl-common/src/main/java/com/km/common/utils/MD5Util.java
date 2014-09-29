package com.km.common.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class MD5Util {
	public static String md5(String string) {
		if (string == null) {
			return null;
		}
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(string.getBytes(Charset.forName("UTF8")));
			final byte[] resultByte = messageDigest.digest();
			final String result = new String(Hex.encodeHex(resultByte));
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public static void main(String[] args) throws Exception {
		String str = md5("123456");
		System.out.println(str);
	}
}
