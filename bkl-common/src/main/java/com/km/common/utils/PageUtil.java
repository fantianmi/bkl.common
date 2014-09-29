package com.km.common.utils;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.km.common.vo.Page;
import com.km.common.vo.PageReply;

public class PageUtil {
	private Connection conn;
	public PageUtil(Connection conn) {
		this.conn = conn;
	}
	
	public <T> PageReply<T> getPageReply(String countSql, String getDataListSql, Page page,Class<T> clazz,Object[]params) {
		QueryRunner qr = new QueryRunner();
		try {
			System.out.println(countSql);
			Long count = (Long) qr.query(conn, countSql, new ScalarHandler(1));
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
			List<T> pagedatas = (List<T>) qr.query(conn, getDataListSql,
					new BeanListHandler<T>(clazz),params);
			PageReply<T> pageReply = new PageReply<T>();
			pageReply.setTotalcount(count);
			pageReply.setTotalpage(totalpage);
			pageReply.setPagesize(pagesize);
			pageReply.setPagenum(pagenum);
			pageReply.setHasNextPage(hasNextPage);
			T[] ts = (T[])Array.newInstance(clazz, 1);
			pageReply.setPagedatas(pagedatas.toArray(ts));
			return pageReply;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
