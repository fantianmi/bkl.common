package com.km.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;

public class CharsetFilter implements Filter {

	private static String CHARSET = "UTF-8";

	@Override
	public void init(FilterConfig config) throws ServletException {
		CHARSET = StringUtils.defaultString(config.getInitParameter("charset"), "UTF-8");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(CHARSET);
		response.setCharacterEncoding(CHARSET);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
