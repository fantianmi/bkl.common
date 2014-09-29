package com.km.common.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.km.common.utils.ServletUtil;

public class CommonServlet extends HttpServlet {
	private static final long serialVersionUID = 7280237333692634683L;
	public static final Log LOG = LogFactory.getLog(CommonServlet.class);

	public CommonServlet() {
		
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String methodName = getMethod(request);
			if (methodName.equals("") || methodName.equals("doPost")  || methodName.equals("doGet")) {
				doDefault(request, response);
				return;
			}
			
			Method[] methods = this.getClass().getDeclaredMethods();
			
			String destMethodName = methodName;
			Class<?>[] destMethodParameterTypes = {HttpServletRequest.class,HttpServletResponse.class};
			
			Method destMethod = null;
			for (Method method: methods) {
				if (!method.getName().equals(destMethodName)) {
					continue;
				}
				Class<?>[] methodParamTypes = method.getParameterTypes();
				
				if (methodParamTypes.length != destMethodParameterTypes.length) {
					continue;
				}
				
				boolean methodparamTypesMatch = true;
				for (int i = 0; i<methodParamTypes.length; i ++) {
					if (!methodParamTypes[i].getName().equals(destMethodParameterTypes[i].getName())) {
						methodparamTypesMatch = false;
						break;
					}
				}
				
				if (methodparamTypesMatch) {
					destMethod = method;
					break;
				}
			}
			
			if (destMethod != null) {
				try {
				destMethod.invoke(this, request,response);
				} catch (Throwable tt) {
					ServletUtil.handleThrowable(tt,response);
				}
			} else {
				response.getWriter().print("invalid request path : " + request.getRequestURL());
			}
			
		//	ServletUtil.writeReply(output, response);
		} catch (Throwable tt) {
			throw new IOException(tt);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doDefault(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().print("do default : " + request.getRequestURL());
	}
	protected void add(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String output = "common:add";
		ServletUtil.writeObjectReply(output, response);
	}
	
	
	
	private String getMethod(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		if (requestURI.endsWith("/")) {
			requestURI = requestURI.substring(0, requestURI.length() - 1);
		}
		String lastPath = requestURI.substring(requestURI.lastIndexOf("/") + 1);
		String servletPath = request.getServletPath();
		
		lastPath = requestURI.substring(requestURI.indexOf(servletPath) + servletPath.length());
		lastPath = lastPath.trim();
		if (lastPath.trim() == "") {
			return "";
		}
		
		if (lastPath.startsWith("/")) {
			lastPath = lastPath.substring(1);
		}
		
		
		String method = lastPath;
		if (method.indexOf("/") != -1) {
			method = method.substring(0,method.indexOf("/")-1);
		}
				
		
		return method;
	}
}
