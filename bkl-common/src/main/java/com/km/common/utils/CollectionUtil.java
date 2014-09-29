package com.km.common.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {
	public static <T> T[] mergeArray(T t, T[] arrays) {
		List<T> newArrays = new ArrayList<T>();
		if (t != null) {
			newArrays.add(t);
		}
		if (arrays != null) {
			for (T e : arrays) {
				newArrays.add(e);
			}
		}
		return (T[])newArrays.toArray();
	}
}
