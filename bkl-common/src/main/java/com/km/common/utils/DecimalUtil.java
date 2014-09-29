package com.km.common.utils;

import java.math.BigDecimal;

import com.km.common.config.Config;

public class DecimalUtil {
	private static int DEFAULT_SCALE = 4;
	
	static {

		try {
			if (Config.getString("config.decimal.precision.max.number") != null) {
				DEFAULT_SCALE = Integer.parseInt(Config
						.getString("config.decimal.precision.max.number"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static double formatDouble(double value) {
		BigDecimal ba = new BigDecimal(Double.toString(value));
		ba = ba.setScale(DEFAULT_SCALE,BigDecimal.ROUND_FLOOR);
		return ba.doubleValue();
	}
	
	public static void print(double value) {
		BigDecimal ba = new BigDecimal(Double.toString(value));
		System.out.println(ba);
	}
	
	public static double add(double a,double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		BigDecimal bb = new BigDecimal(Double.toString(b));
		BigDecimal bc = ba.add(bb);
		bc = bc.setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP);
		return bc.doubleValue();
	}
	public static double subtract(double a,double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		BigDecimal bb = new BigDecimal(Double.toString(b));
		BigDecimal bc = ba.subtract(bb);
		bc = bc.setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP);
		return bc.doubleValue();
	}
	
	public static double multiply(double a,double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		BigDecimal bb = new BigDecimal(Double.toString(b));
		BigDecimal bc = ba.multiply(bb);
		bc = bc.setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP);
		return bc.doubleValue();
	}
	
	public static double divide(double a,double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		BigDecimal bb = new BigDecimal(Double.toString(b));
		BigDecimal bc = ba.divide(bb,10,BigDecimal.ROUND_HALF_UP);
		bc = bc.setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP);
		return bc.doubleValue();
	}
	
	public static boolean larger(double a, double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		BigDecimal bb = new BigDecimal(Double.toString(b));
		BigDecimal bc = ba.subtract(bb);
		return bc.signum() > 0;
	}
	public static boolean equal(double a, double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		BigDecimal bb = new BigDecimal(Double.toString(b));
		BigDecimal bc = ba.subtract(bb);
		return bc.signum() == 0;
	}
	public static boolean lesser(double a, double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		BigDecimal bb = new BigDecimal(Double.toString(b));
		BigDecimal bc = ba.subtract(bb);
		return bc.signum() < 0;
	}
/*	public static double divide(double a,double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		//ba = ba.setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP);
		BigDecimal bb = new BigDecimal(Double.toString(b));
		//bb = bb.setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP);
		BigDecimal bc = ba.divide(bb);
		bc = bc.setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP);
		return bc.doubleValue();
	}*/
	
	public static double getNumberByDecimalPrecision(int precision) {
		BigDecimal value = new BigDecimal(Double.toString(1.0));
		if (precision < 0) {
			for (int i = 0; i > precision; i--) {
				value = value.multiply(new BigDecimal(Double.toString(10.0)));
			}
		} 

		if (precision > 0){
			for (int i = 0; i < precision; i++) {
				value = value.multiply(new BigDecimal(Double.toString(0.1)));
			}
		}
		//value = value.setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP);
		return value.doubleValue();
	}
	
	public static boolean isMultiple(double number, double unit) {
		
		BigDecimal bn = new BigDecimal(Double.toString(number));
		BigDecimal bu = new BigDecimal(Double.toString(unit));
		if (bn.doubleValue() <= 0 || bu.doubleValue() <= 0) {
			return false;
		}
		BigDecimal bmulti = bn.divide(bu);
		int int_multi = (int)bmulti.doubleValue();
		
		BigDecimal bn2 = bu.multiply(new BigDecimal(Double.toString(int_multi)));
		
		//System.out.println("bn :" + bn.doubleValue());
		//System.out.println("bn2 :" + bn2.doubleValue());
		return equal(bn.doubleValue(), bn2.doubleValue());
		
		
		//return false;
	}
	
	public static boolean exceedPrecision(double number, int precision) {
		double unit = getNumberByDecimalPrecision(precision);
		return !isMultiple(number, unit);
	}
	
	public static void main(String[] args) {
		double a = 0.12349;
		
		double b = 0.0000000000000000000000000000000000000000000000000000000051;
		//System.out.println((a -b) == 0);
		//print(DoubleUtil.formatDouble(a));
		/*
		print(BigDecimalUtil.add(a, b));
		print(BigDecimalUtil.subtract(a, b));
		print(BigDecimalUtil.multiply(a, b));*/
/*		System.out.println(DoubleUtil.larger(a, b));
		System.out.println(DoubleUtil.equal(a, b));
		System.out.println(DoubleUtil.lesser(a, b));
		
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(3));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(2));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(1));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(0));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(-1));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(-2));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(-3));*/
/*		System.out.println(DoubleUtil.getNumberByDecimalPrecision(3));
		System.out.println(DoubleUtil.isMultiple(0.02, 0.01));
		System.out.println(DoubleUtil.isMultiple(0.1, 0.01));
		System.out.println(DoubleUtil.isMultiple(0.12, 0.01));
		System.out.println(DoubleUtil.isMultiple(100, 0.01));
		System.out.println(DoubleUtil.isMultiple(0.0132, 0.01));*/
		
		System.out.println(exceedPrecision(1.1, 2));
		System.out.println(exceedPrecision(1.10, 2));
		System.out.println(exceedPrecision(1, 2));
		System.out.println(exceedPrecision(11, 2));
		System.out.println(exceedPrecision(11, 2));
		System.out.println(exceedPrecision(1.11, 2));
		System.out.println(exceedPrecision(1.1100, 2));
		System.out.println(exceedPrecision(1.1110, 2));
		System.out.println(exceedPrecision(1.11111111111111111111111111111111, 2));
	}
}
