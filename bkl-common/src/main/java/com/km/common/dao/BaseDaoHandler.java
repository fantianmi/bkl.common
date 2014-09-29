package com.km.common.dao;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import com.km.common.utils.JdbcConn;
import com.km.common.vo.DataSource;

public class BaseDaoHandler implements InvocationHandler{
      //要代理的原始对象
      private DataSource ds;
      private BaseDao baseDao;
      
      public static final ThreadLocal<Connection> tLocalconn = new ThreadLocal<Connection>();
      
      public BaseDaoHandler(BaseDao baseDao, DataSource ds) {
          super();
          this.baseDao = baseDao;
          this.ds = ds;
      }
  
      /**
       * 在代理实例上处理方法调用并返回结果
       * 
       * @param proxy 代理类
       * @param method 被代理的方法
       * @param args 该方法的参数数组
       */
      
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          Object result = null;
          JdbcConn jdbcConn = new JdbcConn(ds.getUrl(),ds.getUsername(), ds.getPassword());
          try {
        	  baseDao.setConn(jdbcConn.openConnection());
        	  result=method.invoke(baseDao, args);
          } finally {
        	  jdbcConn.closeConnection();
          }
          return result;
      }
      
      public Object invoke_v2(Object proxy, Method method, Object[] args) throws Throwable {
          Object result = null;
          try {
        	  baseDao.setConn(getConnection());
        	  result=method.invoke(baseDao, args);
          } finally {
        	  
          }
          return result;
      }
      
      private Connection getConnection() throws SQLException {
    	  Connection connection = (Connection) tLocalconn.get(); 
    	  if (connection ==null) { 
    		  JdbcConn jdbcConn = new JdbcConn(ds.getUrl(),ds.getUsername(), ds.getPassword());
	    	  connection = jdbcConn.openConnection(); 
	    	  tLocalconn.set(connection); 
    	  }
    	  return connection;
      }
      private void doBefore(){
          System.out.println("before method invoke");
      }
      
      private void doAfter(){
          System.out.println("after method invoke");
      }
      
  }