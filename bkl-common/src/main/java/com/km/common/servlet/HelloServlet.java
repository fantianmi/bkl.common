package com.km.common.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.km.common.utils.ServletUtil;

public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 7280237333692634683L;
	public static final Log LOG = LogFactory.getLog(HelloServlet.class);

	public HelloServlet() {
		
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
		
			String input = ServletUtil.readObject(
					request.getInputStream(), "utf-8", String.class);
			input = request.getServletContext().getRealPath("test");
			
			
			String output = "hello, " + input;
			ServletUtil.writeObjectReply(output, response);
		} catch (Throwable tt) {
			throw new IOException(tt);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
