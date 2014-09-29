package com.km.common.filter;

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

public class BackendFilter implements Filter {

	private String sessionName = "";

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String url = req.getServletPath();
		if (url.indexOf(".jsp") == -1 || url.indexOf("/backend/login.jsp") != -1) {
			chain.doFilter(request, response);
			return;
		}

		String username = (String) req.getSession(true).getAttribute(sessionName);
		if (username != null) {
			chain.doFilter(request, response);
			return;
		} else {
			resp.sendRedirect("/backend/login.jsp");
			return;
		}

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		sessionName = config.getInitParameter("session_name");
		if (StringUtils.isEmpty(sessionName)) {
			sessionName = "username";
		}
	}

}
