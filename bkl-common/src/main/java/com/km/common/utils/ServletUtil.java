package com.km.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.km.common.config.Config;
import com.km.common.vo.CommonReply;
import com.km.common.vo.CommonRequest;
import com.km.common.vo.Page;
import com.km.common.vo.RetCode;

public class ServletUtil {
	private static final ObjectMapper jsonMapper = new ObjectMapper();

	public static <T> T readObject(HttpServletRequest request, Class<T> clazz) throws IOException {
		String remoteAddr = request.getRemoteAddr();
		InputStream in = request.getInputStream();
		String charset = request.getCharacterEncoding();
		if (charset == null) {
			charset = "UTF-8";
		}
		T pro = null;
		try {
			pro = readObject(in, charset, clazz);
			return pro;
		} catch (IOException e) {
			throw e;
		}
	}

	public static <T> T readObject(InputStream in, String charset, Class<T> clazz) throws IOException,
			JsonProcessingException {
		synchronized (jsonMapper) {
			String content = readFullString(in, charset);
			return readObjectFromString(content, clazz);
		}
	}

	public static <T> T readObjectFromString(String content, Class<T> clazz) throws IOException,
			JsonProcessingException {
		if (content == null || "".equals(content.trim()))
			return null;
		synchronized (jsonMapper) {
			return jsonMapper.readValue(content, clazz);
		}
	}

	private static String readFullString(InputStream in, String charset) throws IOException {
		byte[] buf = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		int len = in.read(buf);
		while (len >= 0) {
			baos.write(buf, 0, len);
			len = in.read(buf);
		}
		buf = baos.toByteArray();
		String str = new String(buf, charset);
		return str;

	}

	public static String writeObjectReply(Object reply, HttpServletResponse response) throws IOException {
		Config.setRetCode(null);
		Config.setErrorMsg("");
		String replyStr = jsonString(reply);
		ServletUtil.httpResponse(200, replyStr, "application/json", "utf-8", response);
		return replyStr;
	}

	public static void writeJsonCallBackReply(CommonRequest commonRequest, Object reply, HttpServletResponse response)
			throws IOException {
		String replyStr = jsonString(reply);
		if ("true".equals(commonRequest.getCallback())) {
			replyStr = "JSON_CALLBACK(" + replyStr + ")";
		}
		ServletUtil.httpResponse(200, replyStr, "application/json", "utf-8", response);
	}

	public static void writeOkCommonReply(Object reply, HttpServletResponse response) throws IOException {
		CommonReply commonReply = new CommonReply();
		commonReply.setRet(RetCode.OK.code());
		commonReply.setMsg(RetCode.OK.msg());
		String errorMsg = Config.getErrorMsg();
		if (!StringUtils.isEmpty(errorMsg)) {
			commonReply.setMsg(Config.getErrorMsg());
		}
		commonReply.setData(reply);
		writeObjectReply(commonReply, response);
	}

	public static void writeCommonReply(Object reply, RetCode ret, HttpServletResponse response) throws IOException {
		CommonReply commonReply = new CommonReply();
		commonReply.setRet(ret.code());
		commonReply.setMsg(ret.msg());
		String errorMsg = Config.getErrorMsg();
		if (!StringUtils.isEmpty(errorMsg)) {
			commonReply.setMsg(Config.getErrorMsg());
			Config.setErrorMsg("");
		}
		commonReply.setData(reply);
		writeObjectReply(commonReply, response);
	}

	public static void writeCommonReplyOnSaveDb(long retid, HttpServletResponse response) throws IOException {
		if (retid > 0L) {
			ServletUtil.writeCommonReply(retid, RetCode.OK, response);
		} else {
			RetCode ret = Config.getRetCode();
			if (ret != null) {
				ServletUtil.writeCommonReply(retid, ret, response);
			} else {
				ServletUtil.writeCommonReply(retid, RetCode.DB_SAVE_FAILED, response);
			}
		}
	}

	public static void handleThrowable(Throwable t, HttpServletResponse response) {
		t.printStackTrace();

		CommonReply commonReply = new CommonReply();
		commonReply.setRet(RetCode.ERROR.code());
		commonReply.setMsg(RetCode.ERROR.msg());

		try {
			writeObjectReply(commonReply, response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String jsonString(Object obj) throws IOException, JsonProcessingException {
		synchronized (jsonMapper) {
			return jsonMapper.writeValueAsString(obj);
		}
	}

	private static void httpResponse(int code, String msg, String contentType, String charset,
			HttpServletResponse response) throws IOException {
		response.setStatus(code);
		if (contentType != null) {
			response.setContentType(contentType);
		}
		response.setCharacterEncoding(charset);
		byte[] bs = msg.getBytes(charset);
		response.setContentLength(bs.length);
		response.getOutputStream().write(bs);
		response.getOutputStream().flush();
	}

	public static <T> T readObjectServletQuery(HttpServletRequest request, Class<T> clazz) throws Exception {
		Class[] pType = new Class[] {};
		Constructor ctor = clazz.getConstructor(pType);
		Object instance = ctor.newInstance(new Object[] {});
		Map<String, Object> maps = new HashMap<String, Object>();
		// request.setCharacterEncoding("UTF-8");
		Enumeration<String> pns = request.getParameterNames();
		while (pns.hasMoreElements()) {
			String key = pns.nextElement();
			String value = request.getParameter(key);
			// value = new String(value.getBytes("iso-8859-1"),"UTF-8");
			maps.put(key, value);

		}
		BeanUtils.populate(instance, maps);
		return (T) instance;
	}

	/***
	 * 从请求中获取分页参数
	 * 
	 * @param request
	 * @return
	 */
	public static Page getPage(HttpServletRequest request) {
		Page page = new Page();
		int pagesize = 20;
		int pagenum = 1;
		String pagesizeStr = request.getParameter("pagesize");
		if (!StringUtils.isBlank(pagesizeStr)) {
			pagesize = Integer.parseInt(pagesizeStr);
		}
		String pageNumStr = request.getParameter("pagenum");
		if (!StringUtils.isBlank(pageNumStr)) {
			pagenum = Integer.parseInt(pageNumStr);
		}
		page.setPagenum(pagenum);
		page.setPagesize(pagesize);
		return page;
	}

	/***
	 * 从请求中获取模糊查询的查询条件。
	 * 
	 * @param request
	 * @return
	 */
	public static Map getSearchMap(HttpServletRequest request) {
		String searchKey = request.getParameter("searchKey");
		String searchText = request.getParameter("searchText");
		Map searchMap = new HashMap();
		if (!StringUtils.isBlank(searchKey) && !StringUtils.isBlank(searchText)) {
			String[] keyArr = StringUtils.split(searchKey, ",");
			for (String key : keyArr) {
				searchMap.put(key.trim(), searchText.trim());
			}
		}
		return searchMap;
	}

	public static void main(String[] args) throws Exception {
		String c = ServletUtil.readObjectFromString(null, String.class);

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("callback", "true");
		maps.put("callback2", "true");
		maps.put("pagesize", 10);
		maps.put("pagenum", 2);

		Page page = new Page();
		BeanUtils.populate(page, maps);

		Map<String, Object> map = BeanUtils.describe(page);
		for (String key : map.keySet()) {
			System.out.println(key + ":" + BeanUtils.getProperty(page, key));
		}

		System.out.println(page.getCallback());
		System.out.println(page.getPagesize());
		System.out.println(page.getPagenum());

	}

}
