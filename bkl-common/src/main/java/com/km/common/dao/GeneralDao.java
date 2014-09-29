package com.km.common.dao;

import java.util.List;
import java.util.Map;

import com.km.common.vo.Condition;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;

public interface GeneralDao<T> {
	//保存
	public Long save(T entity);
	//修改
	public Long update(T entity);
	//
	public Long update(T entity,long id);
	public Long update(T entity,Condition[] conditions);
	
	public  List<T>  findList();
	public  List<T>  findList(String orderColumn);
	
	public  List<T> findList(Condition[] conditions,String[] orderColumn);
	public  List<T> findList(Condition[] conditions,String[] orderColumn, int limit);
	public <T> List<T> findListBySQL(String sql, Object... params);
	
	public T find(long id);
	public T find(long id, String orderColumn);
	public T find(long id,Condition condition);
	public T find(String column, Object value);
	public  T find(Condition[] conditions,String[] orderColumn);
	public <T> T find(String sql, Object... params) ;
	
	public PageReply<T> getPage(Page page) ;
	public PageReply<T> getPage(Page page, Map searchMap) ;
	public PageReply<T> getPage(Page page, Condition[] conditions,String[] orderColumns, Map searchMap) ;
	public PageReply<T> getPage(Page page, Condition[] conditions, String[] orderColumns);
	public PageReply<T> getPage(Page page,String tableSql) ;
	public PageReply<T> getPage(String tableSql, Page page, Map searchMap);
	
	public long delete(long id);
	public long delete(long id, Condition condition);
	public long delete(long id, Condition[] conditions);
	public long delete(Condition[] conditions);
	
	
	public Object queryFirstColumnValue(String sql, Object... params);
	public Long queryLong(String sql, Object... params) ;
	public Integer queryIngeger(String sql, Object... params);
	public String queryString(String sql, Object... params);
	public double queryDouble(String sql, Object... params);
	//执行sql
	public long exec(String sql,Object... params);
	//锁住表
	public void lockTable(String... table);
	//
	public void lockTableClass(Class<T>... entityClazzs);
	//解锁table
	public void unLockTable();
	//开始事务操作，可以rollback
	public void beginTransaction() ;
	//返回
	public void rollback();
	//执行
	public void commit();
	
	public <T> String getTableName(Class<T> entityClass) ;
}
