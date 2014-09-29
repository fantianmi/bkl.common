package com.km.common.vo;

public class Condition {
	private String sql;
	private Object value;
	
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public static class EqualCondition extends Condition {
		public EqualCondition(String column, Object value) {
			String sql = String.format("%s=?", column);
			setSql(sql);
			setValue(value);
		}
	}
	
	public static class UnEqualCondition extends Condition {
		public UnEqualCondition(String column, Object value) {
			String sql = String.format("%s!=?", column);
			setSql(sql);
			setValue(value);
		}
	}
	public static class LargerCondition extends Condition {
		public LargerCondition(String column, Object value) {
			String sql = String.format("%s>?", column);
			setSql(sql);
			setValue(value);
		}
	}
}
