package com.km.common.vo;

import org.codehaus.jackson.map.ObjectMapper;

public class CommonReply {
	private int ret;
	private String msg;
	
	private Object data;

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public static void main(String[] args)  throws Exception {
		CommonReply cr = new CommonReply();
		cr.setRet(0);
		cr.setMsg("success");
		
		CommonData cd = new CommonData();
		cd.setUsername("guotianlian");
		cr.setData(cd);
		
		 ObjectMapper jsonMapper = new ObjectMapper();
		 System.out.println(jsonMapper.writeValueAsString(cr));;
		
	}
	
}
