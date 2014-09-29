package com.km.common.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.km.common.utils.CollectionUtil;
import com.km.common.utils.DbUtil;
import com.km.common.vo.Condition;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;

public class GeneralDaoImpl<T> extends BaseDao implements GeneralDao<T> {

	private Class<T> entityClass;

	public GeneralDaoImpl(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public GeneralDaoImpl(Class<T> entityClass, Connection conn) {
		this.entityClass = entityClass;
		setConn(conn);
	}

	public <T> String getTableName() {
		TableAonn anno = entityClass.getAnnotation(TableAonn.class);
		return anno.tableName();
	}

	public <T> String getTableName(Class<T> entityClass) {
		TableAonn anno = entityClass.getAnnotation(TableAonn.class);
		return anno.tableName();
	}

	public Long save(T entity) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.saveObject(getTableName(entity.getClass()), entity);
	}
	public Long update(T entity) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.updateObject(getTableName(entity.getClass()), entity,new Condition[]{});
	}
	public Long update(T entity,long id) {
		DbUtil dbUtil = new DbUtil(conn);
		Condition condition = DbUtil.generalEqualWhere("id", id);
		return dbUtil.updateObject(getTableName(entity.getClass()), entity,new Condition[]{condition});
	}
	public Long update(T entity,Condition[] conditions) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.updateObject(getTableName(entity.getClass()), entity,conditions);
	}
	
	public List<T> findList() {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryListObject(getTableName(entityClass), entityClass, new Condition[] {}, new String[] {});
	}

	public List<T> findList(String orderColumn) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryListObject(getTableName(entityClass), entityClass, new Condition[] {},
				new String[] { orderColumn });
	}

	public List<T> findList(Condition[] conditions, String[] orderColumn) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryListObject(getTableName(entityClass), entityClass, conditions, orderColumn);
	}

	public List<T> findList(Condition[] conditions, String[] orderColumn, int limit) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryListObject(getTableName(entityClass), entityClass, conditions, orderColumn, limit);
	}

	public <T> List<T> findListBySQL(String sql, Object... params) {
		DbUtil dbUtil = new DbUtil(conn);
		return (List<T>) dbUtil.queryListObject(sql, entityClass, params);
	}

	public T find(long id) {
		DbUtil dbUtil = new DbUtil(conn);
		Condition condition = DbUtil.generalEqualWhere("id", id);
		return dbUtil.queryObject(getTableName(entityClass), entityClass, new Condition[] { condition },
				new String[] {});
	}

	public T find(long id, String orderColumn) {
		DbUtil dbUtil = new DbUtil(conn);
		Condition idcondition = DbUtil.generalEqualWhere("id", id);
		return dbUtil.queryObject(getTableName(entityClass), entityClass, new Condition[] { idcondition },
				new String[] {});
	}

	public T find(long id, Condition condition) {
		DbUtil dbUtil = new DbUtil(conn);
		Condition idcondition = DbUtil.generalEqualWhere("id", id);
		return dbUtil.queryObject(getTableName(entityClass), entityClass, new Condition[] { idcondition, condition },
				new String[] {});
	}

	public T find(Condition[] conditions, String[] orderColumns) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryObject(getTableName(entityClass), entityClass, conditions, orderColumns);
	}

	public T find(String column, Object value) {
		DbUtil dbUtil = new DbUtil(conn);
		Condition condition = DbUtil.generalEqualWhere(column, value);
		return dbUtil.queryObject(getTableName(entityClass), entityClass, new Condition[] { condition },
				new String[] {});
	}

	public <T> T find(String sql, Object... params) {
		DbUtil dbUtil = new DbUtil(conn);
		return (T) dbUtil.queryListObject(sql, entityClass, params);
	}

	public PageReply<T> getPage(Page page) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryPageObject(getTableName(entityClass), page, entityClass, null, null);
	}

	public PageReply<T> getPage(Page page, Condition[] conditions,String[] orderColumns, Map searchMap) {
		DbUtil dbUtil = new DbUtil(conn);
		
		return dbUtil.getPage(getTableName(entityClass), entityClass, page, conditions, orderColumns, searchMap);
	}

	@Override
	public PageReply<T> getPage(Page page, Map searchMap) {
		return getPage(page, new Condition[]{},new String[]{}, searchMap);
	}
	
	public PageReply<T> getPage(Page page, Condition[] conditions, String[] orderColumns) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryPageObject(getTableName(entityClass), page, entityClass, conditions, orderColumns);
	}
	public  PageReply<T> getPage(String tableSql, Page page, Map searchMap) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryPageObject(tableSql,entityClass, page, searchMap);
	}
	public PageReply<T> getPage(Page page, String tableSql) {
		DbUtil dbUtil = new DbUtil(conn);
		tableSql = String.format("(%s)a", tableSql);
		return dbUtil.queryPageObject(tableSql, page, entityClass, new Condition[] {}, new String[] {});
	}

	public long delete(long id) {
		DbUtil dbUtil = new DbUtil(conn);
		Condition condition = DbUtil.generalEqualWhere("id", id);
		return dbUtil.delete(getTableName(entityClass), new Condition[] { condition });
	}

	public long delete(long id, Condition condition) {
		DbUtil dbUtil = new DbUtil(conn);
		Condition idcondition = DbUtil.generalEqualWhere("id", id);
		return dbUtil.delete(getTableName(entityClass), new Condition[] { idcondition, condition });
	}

	public long delete(long id, Condition[] conditions) {
		DbUtil dbUtil = new DbUtil(conn);
		Condition idcondition = DbUtil.generalEqualWhere("id", id);
		conditions = CollectionUtil.mergeArray(idcondition, conditions);
		return dbUtil.delete(getTableName(entityClass), conditions);
	}

	public long delete(Condition[] conditions) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.delete(getTableName(entityClass), conditions);
	}

	public List<T> getAll(String sql, Object... params) {
		DbUtil dbUtil = new DbUtil(conn);
		return (List<T>) dbUtil.queryListObject(sql, entityClass, params);
	}

	public Object queryFirstColumnValue(String sql, Object... params) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryFirstColumnValue(sql, params);
	}

	public Long queryLong(String sql, Object... params) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryLong(sql, params);
	}

	public Integer queryIngeger(String sql, Object... params) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryIngeger(sql, params);
	}

	public String queryString(String sql, Object... params) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.queryString(sql, params);
	}

	public double queryDouble(String sql, Object... params) {
		DbUtil dbUtil = new DbUtil(conn);
		Double value = dbUtil.queryDouble(sql, params);
		if (value == null) {
			return 0;
		}
		return value;
	}

	public long exec(String sql, Object... params) {
		DbUtil dbUtil = new DbUtil(conn);
		return dbUtil.exec(sql, params);
	}

	public void lockTable(String... table) {
		DbUtil dbUtil = new DbUtil(conn);
		dbUtil.lockTable(table);
	}

	public void lockTableClass(Class<T>... entityClazzs) {
		if (entityClazzs == null || entityClazzs.length == 0) {
			return;
		}
		String[] tableNames = new String[entityClazzs.length];
		for (int i = 0; i < entityClazzs.length; i++) {
			tableNames[i] = getTableName(entityClazzs[i]);
		}
		DbUtil dbUtil = new DbUtil(conn);
		dbUtil.lockTable(tableNames);
	}

	public void unLockTable() {
		DbUtil dbUtil = new DbUtil(conn);
		dbUtil.unLockTable();
	}

	public void beginTransaction() {
		DbUtil dbUtil = new DbUtil(conn);
		dbUtil.beginTransaction();
	}

	public void rollback() {
		DbUtil dbUtil = new DbUtil(conn);
		dbUtil.rollback();
	}

	public void commit() {
		DbUtil dbUtil = new DbUtil(conn);
		dbUtil.commit();
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

}
