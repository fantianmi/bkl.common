package com.km.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtil {

	public static String encode(String passwd) {
		return DigestUtils.shaHex(passwd);
	}
	
}
