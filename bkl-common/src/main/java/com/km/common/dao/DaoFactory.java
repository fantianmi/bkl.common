package com.km.common.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.km.common.config.Config;
import com.km.common.utils.JdbcConn;
import com.km.common.vo.DataSource;

public class DaoFactory {
	public static Object createDao(Class<? extends BaseDao> baseDaoClazz) {
		try {
			Class[] pType = new Class[] {};
			Constructor ctor = baseDaoClazz.getConstructor(pType);
			BaseDao instance = (BaseDao) ctor.newInstance(new Object[] {});
			Object dao = Proxy.newProxyInstance(baseDaoClazz.getClassLoader(),
					baseDaoClazz.getInterfaces(), new BaseDaoHandler(instance,Config.getDataSource()));
			return dao;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> Object createEntityDao(Class<? extends BaseDao> baseDaoClazz,Class<T> constructorArg) {
		try {
			Class[] pType = new Class[] {constructorArg.getClass()};
			Constructor ctor = baseDaoClazz.getConstructor(pType);
			BaseDao instance = (BaseDao) ctor.newInstance(new Object[] {constructorArg});
			Object dao = Proxy.newProxyInstance(baseDaoClazz.getClassLoader(),
					baseDaoClazz.getInterfaces(), new BaseDaoHandler(instance,Config.getDataSource()));
			return dao;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static <T> GeneralDao<T> createGeneralDao(Class<T> entityClazz) {
		GeneralDao dao = (GeneralDao) DaoFactory.createEntityDao(GeneralDaoImpl.class,entityClazz);
		return (GeneralDao)dao;
	}
	
	public static <T> GeneralDao<T> createGeneralDao(Class<T> entityClazz,Connection conn) {
		return  new GeneralDaoImpl(entityClazz, conn);
	}
	
	public static Connection createConnection() {
		DataSource ds = Config.getDataSource();
		JdbcConn jdbcConn = new JdbcConn(ds.getUrl(),ds.getUsername(), ds.getPassword());
		try {
			return jdbcConn.openConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T fromResultSet(ResultSet rs, Class<T> entityClazz) {
		try {
			Class[] pType = new Class[] {};
			Constructor ctor = entityClazz.getConstructor(pType);
			Object obj =  ctor.newInstance(new Object[] {});
			Map<String,Object> column2Values = new HashMap<String,Object>();
			
			for (int i = 1; i<= rs.getMetaData().getColumnCount();i++) {
				String column = rs.getMetaData().getColumnLabel(i);
				Object value = rs.getObject(column);
				column2Values.put(column, value);
			}
			BeanUtils.populate(obj, column2Values);
			return (T)obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void closeConnection(Connection conn) {
		if (conn == null) {
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		
	}
}
