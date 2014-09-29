package com.km.common.vo;

public class UploadReply {
	private String requestFileName;
	
	private String uploadFileURL;
	
	private long fileSize;
	
	private int code;
	private String msg;
	
	public String getRequestFileName() {
		return requestFileName;
	}

	public void setRequestFileName(String requestFileName) {
		this.requestFileName = requestFileName;
	}

	public String getUploadFileURL() {
		return uploadFileURL;
	}

	public void setUploadFileURL(String uploadFileURL) {
		this.uploadFileURL = uploadFileURL;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
