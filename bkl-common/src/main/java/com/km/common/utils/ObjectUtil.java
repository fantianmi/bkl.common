package com.km.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class ObjectUtil {
	public static Map<String,Object>  object2Map(Object obj) {
    	Map<String, Object> map;
		try {
			map = BeanUtils.describe(obj);
			map.remove("class");
			for(String key : map.keySet()) {
	    		map.put(key, BeanUtils.getProperty(obj, key));
	    	}
	    	return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
}
