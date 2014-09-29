package com.km.common.utils;

public class StringUtil {
	public static String clearAllWhiteSpace(String str) {
		if (str == null) {
			return null;
		}
		str = str.replace(" ", "");
		str = str.replace("\t", "");
		return str;
	}
	
	public static void main(String[] args) {
		String str = "1  3  	4	4";
		
		System.out.println(clearAllWhiteSpace(str));
	}
}
