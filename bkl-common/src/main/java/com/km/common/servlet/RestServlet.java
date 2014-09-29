package com.km.common.servlet;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.km.common.utils.ServletUtil;

/**
 * @author chaozheng
 * 
 */
public class RestServlet extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(RestServlet.class);

	private String respackage;

	@Override
	public void init() throws ServletException {
		super.init();
		respackage = getInitParameter("respackage");
	}

	/***
	 * 当请求/rest/User?method=dosomething时自动分发到UserResource.dosomething()中进行处理。
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String pathInfo = request.getPathInfo();
		final boolean checkPath = pathInfo == null || pathInfo.length() == 0 || pathInfo.equals("/");

		if (checkPath) {
			throw new ServletException("URL格式错误[" + request.getServletPath() + "/{resName}?method=mymethod].");
		}

		if (pathInfo.startsWith("/")) {
			pathInfo = pathInfo.substring(1);
		}
		String resName = "";
		if (pathInfo.indexOf("/") != -1) {
			resName = pathInfo.substring(0, pathInfo.indexOf("/"));
		} else {
			resName = pathInfo;
		}

		String resClass = respackage + "." + resName + "Resource";
		String methodParam = request.getParameter("method");
		Class resClazz = null;
		try {
			resClazz = Class.forName(resClass);
		} catch (ClassNotFoundException e) {
			LOG.error("找不到资源：" + resClass, e);
			e.printStackTrace();
		}
		Method[] methods = resClazz.getMethods();
		Method method = null;
		for (Method m : methods) {
			if (methodParam.equals(m.getName())) {
				method = m;
			}
		}
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class[] parameterTypes = method.getParameterTypes();
		Class returnType = method.getReturnType();
		List paramVlues = new ArrayList();
		int i = 0;
		for (Annotation[] annotations : parameterAnnotations) {
			Class parameterType = parameterTypes[i++];

			for (Annotation annotation : annotations) {
				String paramKey = "";
				if (annotation instanceof QueryParam) {
					QueryParam ann = (QueryParam) annotation;
					paramKey = ann.value();
				}
				if (annotation instanceof FormParam) {
					FormParam ann = (FormParam) annotation;
					paramKey = ann.value();
				}
				String paramValue = request.getParameter(paramKey);
				if ("java.lang.Long".equals(parameterType.getName())) {
					if (StringUtils.isBlank(paramValue)) {
						paramVlues.add(null);
					} else {
						try {
							paramVlues.add(Long.parseLong(paramValue));
						} catch (NumberFormatException e) {
							paramVlues.add(null);
						}
					}
				} else if ("java.lang.Integer".equals(parameterType.getName())) {
					if (StringUtils.isBlank(paramValue)) {
						paramVlues.add(null);
					} else {
						try {
							paramVlues.add(Integer.parseInt(paramValue));
						} catch (NumberFormatException e) {
							paramVlues.add(null);
						}
					}
				} else if ("java.lang.String".equals(parameterType.getName())) {
					paramVlues.add(paramValue);
				} else if ("java.lang.Double".equals(parameterType.getName())) {
					if (StringUtils.isBlank(paramValue)) {
						paramVlues.add(0d);
					} else {
						try {
							paramVlues.add(Double.parseDouble(paramValue));
						} catch (NumberFormatException e) {
							paramVlues.add(null);
						}
					}
				}
			}
		}
		Object resObj = null;
		try {
			resObj = resClazz.newInstance();
		} catch (InstantiationException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		}
		try {
			Method setReq = resClazz.getDeclaredMethod("setRequest", HttpServletRequest.class);
			Method setResp = resClazz.getDeclaredMethod("setResponse", HttpServletResponse.class);
			if (setReq != null) {
				setReq.invoke(resObj, request);
			}
			if (setResp != null) {
				setResp.invoke(resObj, response);
			}
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		try {
			Object obj = method.invoke(resObj, paramVlues.toArray());
			if (!(Void.class.isAssignableFrom(returnType) || void.class.equals(returnType))) {
				ServletUtil.writeOkCommonReply(obj, response);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
