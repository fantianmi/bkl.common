package com.km.common.vo;

public class PageReply<T> {
	public long getPagesize() {
		return pagesize;
	}

	public void setPagesize(long pagesize) {
		this.pagesize = pagesize;
	}

	public long getPagenum() {
		return pagenum;
	}

	public void setPagenum(long pagenum) {
		this.pagenum = pagenum;
	}

	public long getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(long totalpage) {
		this.totalpage = totalpage;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public T[] getPagedatas() {
		return pagedatas;
	}

	public void setPagedatas(T[] pagedatas) {
		this.pagedatas = pagedatas;
	}

	public long getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(long totalcount) {
		this.totalcount = totalcount;
	}

	private long pagesize;
	
	private long pagenum;
	
	private long totalpage;
	private long totalcount;
	private boolean hasNextPage;
	
	private T[] pagedatas;

	
}
