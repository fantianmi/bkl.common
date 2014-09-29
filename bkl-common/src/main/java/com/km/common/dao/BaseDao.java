package com.km.common.dao;

import java.sql.Connection;

public class BaseDao {
	protected Connection conn;

	public BaseDao() {
		
	}
	
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
