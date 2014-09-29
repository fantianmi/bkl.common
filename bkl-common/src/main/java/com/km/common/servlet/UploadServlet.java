package com.km.common.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.km.common.utils.IOUtils;
import com.km.common.utils.ServletUtil;
import com.km.common.vo.RetCode;
import com.km.common.vo.UploadReply;

public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 7280237333692634683L;
	public static final Log LOG = LogFactory.getLog(UploadServlet.class);
	
	private String[] blackFileSuffixList = {"css", "js"};
	private String[] whiteSuffixList = null;
	protected String relativeUploadDir = "";
	
	public UploadServlet() {

	}
	
	public void init(ServletConfig servletConfig) throws ServletException{
	    this.relativeUploadDir = servletConfig.getInitParameter("uploaddir");
	    if (this.relativeUploadDir == null || "".equals(relativeUploadDir)) {
	    	relativeUploadDir = "uploads";
	    }
	    
	    String whiteImageSuffixArrayString = servletConfig.getInitParameter("white_suffix_array");
	    if (whiteImageSuffixArrayString != null) {
	    	whiteSuffixList = whiteImageSuffixArrayString.split(",");
	    }
	 }
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String relativeUploadDir = getUploadDir(request, response);
		
		String absoluteUploadDir = request.getServletContext().getRealPath(relativeUploadDir);
		if (!new File(absoluteUploadDir).isDirectory()) {
			IOUtils.createDirRecursive(new File(absoluteUploadDir));
		}
		
		
		request.setCharacterEncoding("utf-8"); // ���ñ���
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(absoluteUploadDir)); // ���û�����������õĻ����ϴ���� �ļ� ��ռ�úܶ��ڴ�
		factory.setSizeThreshold(1024 * 1024); // ���� ����Ĵ�С�����ϴ��ļ�����������û���ʱ��ֱ�ӷŵ���ʱ�洢��
		ServletFileUpload upload = new ServletFileUpload(factory); // ��ˮƽ��API�ļ��ϴ�����
		List<UploadReply> uploadReplys = new ArrayList<UploadReply>();
		
		try {
			// �����ϴ�����ļ�
			List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
			
			for (FileItem item : list) {
				String name = item.getFieldName();// ��ȡ�?����������
				if (item.isFormField()) {// ����ȡ�� �?��Ϣ����ͨ�� �ı� ��Ϣ
					String value = item.getString();
					request.setAttribute(name, value);
					continue;
				}
				// �Դ���ķ� �򵥵��ַ���д��� ������˵�����Ƶ� ͼƬ����Ӱ��Щ
				String requestFilename = getRequestFileName(item);
				request.setAttribute(name, requestFilename);
				if ("".equals(requestFilename)) {
					continue;
				}
				UploadReply uploadReply = null;
				if (!isAccess(item)) {
					uploadReply = new UploadReply();
					uploadReply.setRequestFileName(requestFilename);
					uploadReply.setCode(RetCode.FORBID.code());
					uploadReply.setMsg(RetCode.FORBID.msg());
				} else {
					uploadReply = doUploadFile(request, response, item);
				}
				uploadReplys.add(uploadReply);
			}
			ServletUtil.writeOkCommonReply(uploadReplys, response);
		} catch (Exception e) {
			ServletUtil.handleThrowable(e, response);
		}

	}

	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	public String getRequestFileName(FileItem item) {
		String value = item.getName();
		int start = value.lastIndexOf("\\");
		String requestFileName = value.substring(start + 1);
		return requestFileName;
	}
	
	// for override
	protected String getUploadDir(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return relativeUploadDir;
	}
	
	protected String getUploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return relativeUploadDir;
	}
	
	// for override
	protected boolean isAccess(FileItem item) {
		String requestFilename = getRequestFileName(item);
		String suffix = IOUtils.getSuffixFileName(requestFilename);
		if (suffix != null) {
			suffix = suffix.trim();
			suffix = suffix.toLowerCase();
		}
		for (String blackFileSuffix : blackFileSuffixList) {
			if(blackFileSuffix.toLowerCase().equals(suffix)) {
				return false;
			}
		}
		if (whiteSuffixList == null) {
			return true;
		}
		for (String whiteSuffix : whiteSuffixList) {
			if(whiteSuffix.toLowerCase().equals(suffix)) {
				return true;
			}
		}
		return false;
	}
	
	// for override
	protected UploadReply doUploadFile(HttpServletRequest request,HttpServletResponse response,FileItem item) throws Exception {
		UploadReply uploadReply = new UploadReply();
		String relativeUploadDir = getUploadDir(request, response);
		
		String absoluteUploadDir = request.getServletContext().getRealPath(relativeUploadDir);
		String uploadDirPath = absoluteUploadDir;
		
		String requestFilename = getRequestFileName(item);
		
		String suffix = IOUtils.getSuffixFileName(requestFilename);
		File uploadFile = null;
		
		do {
			String preffixFileName = UUID.randomUUID().toString();
			uploadFile = new File(uploadDirPath, preffixFileName + "." + suffix);
		} while (uploadFile.exists());
		
		item.write(uploadFile);
		
		uploadReply.setCode(RetCode.OK.code());
		uploadReply.setMsg(RetCode.OK.msg());
		
		uploadReply.setRequestFileName(requestFilename);
		uploadReply.setUploadFileURL(relativeUploadDir + "/" + uploadFile.getName());
		uploadReply.setFileSize(item.getSize());
		
		postUploadFile(request,response,uploadReply);
		return uploadReply;
	}
	
	protected void postUploadFile(HttpServletRequest request,HttpServletResponse response,UploadReply uploadReply) throws Exception {
		
	}
	
	
	
	
	
}
