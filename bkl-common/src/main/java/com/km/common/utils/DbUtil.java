package com.km.common.utils;

import java.io.File;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;

import com.km.common.config.Config;
import com.km.common.vo.Condition;
import com.km.common.vo.DataSource;
import com.km.common.vo.MySQLColumn;
import com.km.common.vo.Page;
import com.km.common.vo.PageReply;


public class DbUtil {
	private Connection conn;
	private JdbcConn jdbcConn;
	
	
	
	public DbUtil() throws SQLException {
		//test
		DataSource ds = Config.getDataSource();
		jdbcConn = new JdbcConn(ds.getUrl(),ds.getUsername(), ds.getPassword());
		conn = jdbcConn.openConnection();
	}
	
	public DbUtil(Connection conn) {
		this.conn = conn;
	}
	
	
	public long saveObject(String tableName, Object obj) {
		return saveRecord(tableName, ObjectUtil.object2Map(obj));
	}
	
	public long updateObject(String tableName, Object obj,Condition[] conditions) {
		return updateRecord(tableName, ObjectUtil.object2Map(obj),conditions);
	}
	
	public <T> T queryObject(String tableName, Class<T> clazz, Condition[] conditions, String[] orderColumns) {
		String whereSql = generalWheresSql(conditions);
		String orderSql = generalOrders(orderColumns);
		Object[] params = generalParams(conditions);
		String sql = String.format("SELECT * FROM %s %s %s limit 1", tableName, whereSql, orderSql);
		return queryObject(sql, clazz, params);
	}
	
	public <T> PageReply<T> queryPageObject(String tableName, Page page, Class<T> clazz, Condition[] conditions, String [] orderColumns) {
		String whereSql = generalWheresSql(conditions);
		String orderSql = generalOrders(orderColumns);
		Object[] params = generalParams(conditions);
		
		String getDataListSql = String.format("SELECT * from %s %s  %s", tableName,whereSql, orderSql);
		
		
		return queryPageObject(getDataListSql, page ,clazz,params);
	}
	
	public <T> PageReply<T> getPage(String tableName, Class<T> clazz,Page page, Condition[] conditions,String[] orderColumns, Map searchMap) {
		String whereSql = generalWheresSql(conditions);
		String orderSql = generalOrders(orderColumns);
		Object[] params = generalParams(conditions);
		List<Object> paramsList = new ArrayList<Object>();
		if (params != null && params.length > 0) {
			for (Object param : params) {
				paramsList.add(param);
			}
		}
		String searchSql = "";
		
		if (searchMap != null && searchMap.size() > 0) {
			if (conditions != null && conditions.length > 0) {
				searchSql += " and";
			}
			searchSql += " (";
			Iterator it = searchMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				searchSql += key +" like ? ";
				if (it.hasNext()) {
					searchSql +=" or ";
				}
				paramsList.add(searchMap.get(key).toString() + "%");
			}
			searchSql +=") ";
		}
		if (StringUtils.isBlank(whereSql) && searchMap != null && searchMap.size() > 0) {
			searchSql = " where " + searchSql;
		}
		String getDataListSql = String.format("SELECT * from %s %s %s  %s", tableName,whereSql,searchSql, orderSql);
		return queryPageObject(getDataListSql, page, clazz, paramsList.toArray());
	}
	
	public <T> PageReply<T> queryPageObject(String tableSql, Class<T> clazz,Page page, Map searchMap) {
		
		List<Object> paramsList = new ArrayList<Object>();
		
		String searchSql = "";
		
		if (searchMap != null && searchMap.size() > 0) {
			searchSql += "where ";
			searchSql += " (";
			Iterator it = searchMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				searchSql += key +" like ? ";
				if (it.hasNext()) {
					searchSql +=" or ";
				}
				paramsList.add(searchMap.get(key).toString() + "%");
			}
			searchSql +=") ";
		}
		
		String sql = String.format("SELECT * from (%s)a %s", tableSql, searchSql);
		return queryPageObject(sql, page ,clazz,paramsList.toArray());
	}
	
	public <T> PageReply<T> queryPageObject(String tableSql, Page page, Class<T> clazz, Object[] params) {
	
		String countSql = String.format("SELECT count(*) FROM (%s)a", tableSql);
		System.out.println(countSql);
		String getDataListSql = String.format("SELECT * from (%s)a", tableSql);
		System.out.println(getDataListSql);
		Long count = queryLong(countSql,params);
		long pagesize = page.getPagesize();
		long totalpage = count/pagesize;
		long pagenum = page.getPagenum();
		
		if (count%pagesize != 0) {
			totalpage += 1;
		}
		if (pagenum > totalpage) {
			pagenum = totalpage;
		}
		if (pagenum < 1) {
			pagenum = 1;
		}
		
		if (pagesize < 1) {
			pagesize = 1;
		}
		
		
		long offset = (pagenum - 1) * pagesize;
		getDataListSql += " limit " + offset + ", " + pagesize;
		boolean hasNextPage = (pagenum != totalpage);
		
		System.out.println(getDataListSql);

		List<T> pagedatas = queryListObject(getDataListSql, clazz, params);
		if (pagedatas == null) {
			pagedatas = new ArrayList<T>();
		}
		PageReply<T> pageReply = new PageReply<T>();
		pageReply.setTotalcount(count);
		pageReply.setTotalpage(totalpage);
		pageReply.setPagesize(pagesize);
		pageReply.setPagenum(pagenum);
		pageReply.setHasNextPage(hasNextPage);
		T[] ts = (T[])Array.newInstance(clazz, 0);
		T[]pagedataArray = pagedatas.toArray(ts);
		if (pagedataArray == null) {
			pagedataArray = ts; 
		}
		pageReply.setPagedatas(pagedataArray);
		return pageReply;
	}
	
	
	public long saveRecord(String tableName, Map<String, Object> record) {
		List<String> insertColumns = new ArrayList<String>();
		List<String> existTableColumns = getTableColumns(tableName);

		for (String column : record.keySet()) {
			if (existTableColumns.contains(column) && record.get(column) != null)
				insertColumns.add(column);
		}

		if (insertColumns.size() == 0) {
			return 0;
		}
		if (existTableColumns.contains("id") && !insertColumns.contains("id")) {
			insertColumns.add("id");
		}

		String insertColumnNames = "", insertColumnValues = "", keyUpdates = "";
		for (int i = 0; i < insertColumns.size(); i++) {
			if (i == 0) {
				insertColumnNames = "(";
				insertColumnValues = "(";
			}

			String columnName = insertColumns.get(i);
			insertColumnNames += columnName;
			insertColumnValues += "?";
			keyUpdates += columnName + "=" + "values(" + columnName + ")";

			if (i != (insertColumns.size() - 1)) {
				insertColumnNames += ",";
				insertColumnValues += ",";
				keyUpdates += ",";
			}

			if (i == (insertColumns.size() - 1)) {
				insertColumnNames += ")";
				insertColumnValues += ")";
			}
		}

		String insert_sql = String.format(
				"INSERT INTO `%s` %s values %s ON DUPLICATE KEY UPDATE %s",
				tableName, insertColumnNames, insertColumnValues, keyUpdates);

		Long id = null;
		if (record.containsKey("id")) {
			if (record.get("id") != null) {
				id = Long.parseLong(record.get("id").toString());
			}
		}
		if (id == null || id == -1 || id == 0) {
			if (existTableColumns.contains("id")) {
				id = TableUUID.getUUID(tableName, this);
				record.put("id", id);
			}
		}
		Object[] params = new Object[insertColumns.size()];
		for (int i = 0; i < insertColumns.size(); i++) {
			String columnName = insertColumns.get(i);
			params[i] = record.get(columnName);
		}

		long ret = exec(insert_sql, params);
		if (id == null) {
			return ret;
		}
		return id;
	}
	
	
	public long updateRecord(String tableName, Map<String, Object> record,Condition[] conditions) {
		if (record.get("id") == null) {
			return 0;//必须包含一个id条件
		}
		
		List<String> updateColumns = new ArrayList<String>();
		List<String> existTableColumns = getTableColumns(tableName);
		
		List<Condition> conditionList = new ArrayList<Condition>();
		if (conditions != null && conditions.length > 0) {
			for (Condition condition : conditions) {
				conditionList.add(condition);
			}
		}
		
		
		
		for (String column : record.keySet()) {
			if ("id".equals(column) && record.get(column) != null) {
				conditionList.add(generalEqualWhere("id", record.get("id")));
				continue;
			}
			if (existTableColumns.contains(column) && record.get(column) != null)
				updateColumns.add(column);
		}

		if (updateColumns.size() == 0) {
			return 0;
		}

		String updateSql = "";
		for (int i = 0; i < updateColumns.size(); i++) {
			String columnName = updateColumns.get(i);
			updateSql += String.format("%s=?",columnName);

			if (i != (updateColumns.size() - 1)) {
				updateSql += ",";
			}
		}
		conditions = conditionList.toArray(new Condition[]{new Condition()});
		String whereSql = generalWheresSql(conditions);
		Object[] whereParams = generalParams(conditions);

		
		String update_sql = String.format(
				"UPDATE `%s` set %s %s",
				tableName, updateSql, whereSql);



		List<Object> paramsList = new ArrayList<Object>();
		for (int i = 0; i < updateColumns.size(); i++) {
			String columnName = updateColumns.get(i);
			Object updateParam = record.get(columnName);
			paramsList.add(updateParam);
		}
		if (whereParams != null && whereParams.length > 0) {
			for (Object param : whereParams) {
				paramsList.add(param);
			}
		}

		long ret = exec(update_sql, paramsList.toArray());
		return ret;
	}
	
	
	public <T> List<T> queryListObject(String tableName, Class<T> clazz, Condition[] conditions, String[] orderColumns) {
		String whereSql = generalWheresSql(conditions);
		String orderSql = generalOrders(orderColumns);
		Object[] params = generalParams(conditions);
		
		String sql = "SELECT * FROM " + tableName + whereSql + orderSql;
		return queryListObject(sql, clazz, params);
	}
	public <T> List<T> queryListObject(String tableName, Class<T> clazz, Condition[] conditions, String[] orderColumns, int limit) {
		String whereSql = generalWheresSql(conditions);
		String orderSql = generalOrders(orderColumns);
		Object[] params = generalParams(conditions);
		
		String sql = "SELECT * FROM " + tableName + whereSql + orderSql + " limit " + limit;
		return queryListObject(sql, clazz, params);
	}
	
	public long delete(String tableName, Condition[] conditions) {
		
		String whereSql = generalWheresSql(conditions);
		Object[] params = generalParams(conditions);
		
		String sql = String.format("DELETE FROM %s %s", tableName, whereSql);
		return exec(sql, params);
	}
	
	public <T> List<T> queryListObject(String sql, Class<T> clazz, Object... params) {
		QueryRunner qr = new QueryRunner();
		
		System.out.println(sql);
		try {
			List<T> pset = (List<T>) qr.query(conn, sql,
					new BeanListHandler<T>(clazz),params);
			return pset;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> T queryObject(String sql, Class<T> clazz, Object... params) {
		QueryRunner qr = new QueryRunner();
		System.out.println(sql);
		try {
			T t = (T) qr.query(conn, sql,new BeanHandler<T>(clazz),params);
			return t;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Object queryFirstColumnValue(String sql, Object... params) {
		QueryRunner qr = new QueryRunner();
		
		try {
			Object tmpValue = qr.query(conn, sql, new ScalarHandler(1),params);
			return tmpValue;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Long queryLong(String sql, Object... params) {
		Object ret = queryFirstColumnValue(sql,params);
		if (ret == null) {
			return null;
		}
		return Long.parseLong(ret.toString());
	}
	
	public Integer queryIngeger(String sql, Object... params) {
		Object ret = queryFirstColumnValue(sql,params);
		if (ret == null) {
			return null;
		}
		return Integer.parseInt(ret.toString());
	}
	
	public String queryString(String sql, Object... params) {
		Object ret = queryFirstColumnValue(sql,params);
		if (ret == null) {
			return null;
		}
		return ret.toString();
	}
	
	public Double queryDouble(String sql, Object... params) {
		Object ret = queryFirstColumnValue(sql,params);
		if (ret == null) {
			return null;
		}
		return Double.parseDouble(ret.toString());
	}
	
	public long exec(String sql,Object... params) {
		QueryRunner qr = new QueryRunner();
		System.out.println(sql);
		try {
			return qr.update(conn, sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void lockTable(String... table) {
		String tableSql = "";
		if (table != null) {
			for (int i = 0;i < table.length; i++) {
				tableSql += " " + table[i] + " write";
				if (i != table.length - 1) {
					tableSql += ","; 
				}
			}
		}
		
		if (tableSql.trim().equals("")) {
			return;
		}
		
/*		String sql = String.format("set autocommit=0");
		exec(sql);*/
		String sql = String.format("lock tables %s", tableSql);
		exec(sql);
	}
	
	public void unLockTable() {
		String sql = String.format("unlock tables");
		exec(sql);
	}
	
	public void beginTransaction() {
		 try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void rollback() {
		 try {
			conn.rollback();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void commit() {
		 try {
			conn.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void generalEntity() throws Exception {
		String sql = "show tables";
		ResultSet rs = jdbcConn.executeSql(sql);
		while (rs.next()) {
			String tableName = rs.getString(1);
			String csql = "desc " + tableName;
			ResultSet crs = jdbcConn.executeSql(csql);
			String classString = "@TableAonn(tableName = \"" + tableName + "\")\n";
		    classString += "public class " + tableName + " {\n";
			while (crs.next()) {
				String column = crs.getString(1);
				String type = crs.getString(2);
				String javaType = "String";
				if (type.startsWith("int")) {
					javaType = "int";
				}
				if (type.startsWith("bigint")) {
					javaType = "long";
				}
				if (type.startsWith("decimal")) {
					javaType = "float";
				}
				classString += "\t" + "private " + javaType + " " + column
						+ ";" + "\n";

			}

			classString += "}";
			System.out.println(classString);
			IOUtils.echo(new File("target/" + tableName + ".java") , classString, "UTF-8", true);
			crs.close();
		}
		rs.close();
	}
	
	private  boolean containsColumn(String tableName, String column) throws Exception {
		List<String> pset =  getTableColumns(tableName);
		
		return pset.contains(column);
	}
	
	private List<String> getTableColumns(String tableName)  {
		QueryRunner qr = new QueryRunner();
		String sql = "desc `" + tableName + "`;";
		List<MySQLColumn> mysqlColumns;
		List<String> columns = new ArrayList<String>();	
			try {
				mysqlColumns = (List<MySQLColumn>) qr.query(conn, sql,
						new BeanListHandler<MySQLColumn>(MySQLColumn.class));
				for (MySQLColumn mysqlColumn : mysqlColumns) {
					columns.add(mysqlColumn.getField());
				}
				return columns;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
	}
	
	private String generalWheresSql(Condition[] wheres) {
		String whereSql = "";
		if (wheres != null) {
			for (int i = 0;i < wheres.length; i++) {
				if (i == 0) {
					whereSql += " where"; 
				}
				whereSql += " " + wheres[i].getSql();
				if (i != wheres.length - 1) {
					whereSql += " and"; 
				}
			}
		}
		return whereSql;
	}

	private Object[] generalParams(Condition[] conditions) {
		ArrayList<Object> params = new ArrayList<Object>();
		if (conditions != null) {
			for (int i = 0;i < conditions.length; i++) {
				params.add(conditions[i].getValue());
			}
		}
		return params.toArray();
	}
	
	public static Condition generalEqualWhere(String column, Object value) {
		return new Condition.EqualCondition(column, value);
	}
	public static Condition generalUnEqualWhere(String column, Object value) {
		return new Condition.UnEqualCondition(column, value);
	}
	public static Condition generalLargerWhere(String column, Object value) {
		return new Condition.LargerCondition(column, value);
	}
	
	private String generalOrders(String[] orderColumns) {
		String orderSql = "";
		if (orderColumns != null) {
			for (int i = 0;i < orderColumns.length; i++) {
				if (i == 0) {
					orderSql += " order by"; 
				}
				orderSql += " " + orderColumns[i];
				if (i != orderColumns.length - 1) {
					orderSql += ","; 
				}
			}
		}
		return orderSql;
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		DbUtil dbUtil = new DbUtil();
		dbUtil.generalEntity();
	}
	
}

