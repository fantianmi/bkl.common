package com.km.common.utils;

public class TimeUtil {
	static java.text.SimpleDateFormat dateTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static java.text.SimpleDateFormat dateTimeNotSplitFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
	static java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
	static java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
	
	public static String getNowDateTime() {
		java.util.Date today=new java.util.Date();   
		return dateTimeFormat.format(today);
	}
	
	public static String getNowDateTime4NotSplit() {
		java.util.Date today=new java.util.Date();   
		return dateTimeNotSplitFormat.format(today);
	}
	
	public static String getNowDate() {
		java.util.Date today=new java.util.Date();   
		return dateFormat.format(today);
	}
	
	public static long getUnixTime() {
		return (long)System.currentTimeMillis()/1000;
	}
	
	public static String fromUnixTime(int ctime) {
		java.util.Date today=new java.util.Date((long)ctime * 1000);   
		return dateTimeFormat.format(today);
	}
	
	public static String fromUnixTime(long ctime) {
		java.util.Date today=new java.util.Date(ctime * 1000);   
		return dateTimeFormat.format(today);
	}
	
	public static String fromUnixTime2Time(long ctime) {
		java.util.Date today=new java.util.Date(ctime * 1000);   
		return timeFormat.format(today);
	}
	
	public static void main(String[] args) {
		System.out.print(getNowDateTime());
	}
}
