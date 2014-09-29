package com.km.common.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TableUUID {
	private static ConcurrentHashMap<String,Object> table2LockObject = new  ConcurrentHashMap<String,Object>();
	private static ConcurrentHashMap<String,AtomicLong> table2UUID = new  ConcurrentHashMap<String,AtomicLong>();
	private static Object lockObject = new Object();
	
	public static long getUUID(String tableName,DbUtil dbUtil) {
		AtomicLong uuid = table2UUID.get(tableName);
		if (uuid == null) {
			synchronized (lockObject) {
				String get_maxid_sql = "select max(id) from " + tableName;
				Long id = dbUtil.queryLong(get_maxid_sql);
				if (id == null || id == 0L) {
					id = 1L;
				} 
				table2UUID.putIfAbsent(tableName, new AtomicLong(id));
				uuid = table2UUID.get(tableName);
			}
			
		}
		return uuid.incrementAndGet();
		
	}
	
	public static void main(String[] args) {
		AtomicLong uuid = new AtomicLong(10);
		System.out.println(uuid.incrementAndGet());
	}
}
