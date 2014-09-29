/**
 * Oct 16, 2012
 */
package com.km.common.client;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author lin
 * 
 */
public class HttpObjectClient extends AbstractHttpClient implements Closeable {
	public static final String CONTENT_TYPE = "text/plain";
	public static final String CONTENT_CHARSET = "utf-8";
	public static final int DEF_CONN_TIMEOUT = 10000;
	public static final int DEF_READ_TIMEOUT = 20000;

	protected volatile int connTimeout = DEF_CONN_TIMEOUT;
	protected volatile int readTimeout = DEF_READ_TIMEOUT;

	private final HttpClient client;
	private final HttpConnectionManagerParams managerParams;

	private final Object sync = new Object();

	private static final Logger log = Logger.getLogger(HttpObjectClient.class);

	public HttpObjectClient() {
		this(DEF_CONN_TIMEOUT, DEF_READ_TIMEOUT);
	}

	public HttpObjectClient(int connTime, int readTime) {
		super(connTime, readTime);
		this.client = new HttpClient();
		managerParams = client.getHttpConnectionManager().getParams();
	}

	private PostMethod init(String url) {
		PostMethod m = new PostMethod(url);
		managerParams.setConnectionTimeout(connTimeout);
		managerParams.setSoTimeout(readTimeout);
		m.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"utf-8");
		if (keepAlive) {
			m.setRequestHeader("Connection", "keep-alive");
		} else {
			m.setRequestHeader("Connection", "close");
		}
		return m;
	}

	private String doPostString(String url, String content, PostMethod m)
			throws Exception {
		boolean sucess = false;
		try {
			m.setRequestEntity(new StringRequestEntity(content, CONTENT_TYPE,
					CONTENT_CHARSET));
			int statusCode = client.executeMethod(m);
			if (log.isDebugEnabled()) {
				Header[] header = m.getResponseHeaders();
				for (Header h : header) {
					log.debug(h.toString());
				}
			}
			String reponse = m.getResponseBodyAsString();
			if (statusCode != HttpStatus.SC_OK) {
				throw new Exception(String.format("http[%s] code=%d,msg=%s",
						url, statusCode, reponse));
			}
			sucess = true;
			return reponse;
		} catch (Exception e) {
			throw e;
		} finally {
			if (sucess) {
				m.releaseConnection();
			} else {
				close();
			}
		}
	}

	public <T> T post(String url, Object protocol, Class<T> clazz)
			throws Exception {
		ObjectMapper jsonMapper = new ObjectMapper();
		String json = jsonMapper.writeValueAsString(protocol);
		String replyStr = null;
		synchronized (sync) {
			PostMethod m = init(url);
			replyStr = doPostString(url, json, m);
		}
		T reply = null;
		if (replyStr != null) {
			try {
				reply = jsonMapper.readValue(replyStr, clazz);
			} catch (Exception e) {
				throw new Exception(String.format(
						"failed to parse %s from json=%s", clazz.getName(),
						replyStr), e);
			}
		}
		return reply;
	}

	public <T> T post(String host, int port, String path, Object protocol,
			Class<T> clazz) throws Exception {
		String url = createUrl("http", host, port, path);
		return post(url, protocol, clazz);
	}

	public <T> T postSSL(String host, int port, String path, Object protocol,
			Class<T> clazz) throws Exception {
		String url = createUrl("https", host, port, path);
		return post(url, protocol, clazz);
	}

	@Override
	public void close() throws IOException {

	}

}
