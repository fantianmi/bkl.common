package com.km.common.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.km.common.utils.ServletUtil;
import com.km.common.vo.CommonReply;
import com.km.common.vo.RetCode;

public class ApiFilter implements Filter {
	private String sessionName = "";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res =(HttpServletResponse)response;
		
		String username = (String)req.getSession(true).getAttribute(sessionName);
		if (username != null) {
			filterChain.doFilter(request, response);
			return;
		}
		CommonReply commonReply = new CommonReply();
    	commonReply.setRet(RetCode.SESSION_TIMEOUT.code());
    	commonReply.setMsg(RetCode.SESSION_TIMEOUT.msg());
    	ServletUtil.writeObjectReply(commonReply, res);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		sessionName = filterConfig.getInitParameter("session_name");
		if (StringUtils.isEmpty(sessionName)) {
			sessionName = "username";
		}
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
