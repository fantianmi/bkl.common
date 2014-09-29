package com.km.common.utils;

import org.codehaus.jackson.map.ObjectMapper;

public class PrintUtil {
	public static void print(Object obj) {
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			System.out.println(jsonMapper.writeValueAsString(obj));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
