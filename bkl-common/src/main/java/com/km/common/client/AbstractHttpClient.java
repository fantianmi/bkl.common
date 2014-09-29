/**
 * Oct 16, 2012
 */
package com.km.common.client;

/**
 * @author lin
 * 
 */
public abstract class AbstractHttpClient {

    public static final String CONTENT_TYPE = "text/plain";
    public static final String CONTENT_CHARSET = "utf-8";
    public static final int DEF_CONN_TIMEOUT = 10000;
    public static final int DEF_READ_TIMEOUT = 20000;

    protected volatile int connTimeout = DEF_CONN_TIMEOUT;
    protected volatile int readTimeout = DEF_READ_TIMEOUT;

    protected volatile boolean keepAlive = true;
    protected volatile boolean ssl = false;

    protected AbstractHttpClient(int connTimeout, int readTimeout) {
        this.connTimeout = connTimeout;
        this.readTimeout = readTimeout;
    }

    protected AbstractHttpClient() {
    }

    public int getConnectTimeout() {
        return connTimeout;
    }

    public void setConnectTimeout(int connTime) {
        this.connTimeout = connTime;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTime) {
        this.readTimeout = readTime;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public static String createUrl(String head, String host, int port, String path) {
        return String.format("%s://%s:%d/%s", head, host, port, path == null ? "" : path);
    }

}
