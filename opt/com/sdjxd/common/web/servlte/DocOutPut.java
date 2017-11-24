package com.sdjxd.common.web.servlte;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sdjxd.common.export.Word;
import com.sdjxd.pms.platform.form.model.FileBean;
import com.sdjxd.pms.platform.form.service.FormInstance;

public class DocOutPut extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7285857015622431290L;

	private String contentType = "application/x-msdownload";
	private String enc = "utf-8";
	private String fileRoot = "";

	/**
	 * 初始化contentType，enc，fileRoot
	 */
	public void init(ServletConfig config) throws ServletException {
		String tempStr = config.getInitParameter("contentType");
		if (tempStr != null && !tempStr.equals("")) {
			contentType = tempStr;
		}
		tempStr = config.getInitParameter("enc");
		if (tempStr != null && !tempStr.equals("")) {
			enc = tempStr;
		}
		tempStr = config.getInitParameter("fileRoot");
		if (tempStr != null && !tempStr.equals("")) {
			fileRoot = tempStr;
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String patternId = request.getParameter("p");
		String sheetId = request.getParameter("s");
		try {
			FormInstance formInstance = FormInstance.load(sheetId, patternId);
			String filename = formInstance.getPattern().getName();
			filename = URLEncoder.encode(filename, "UTF-8");// IE浏览器
			// response.setContentType(contentType);
			response.setContentType("application/x-msdownload");
			response.addHeader("Content-Disposition", "attachment; filename=\""
					+ filename + ".doc\"");

			String strISExportFC = request.getParameter("ISEXPORTFC");
			String strExportPicIDs = request.getParameter("alReadyExportPicID");

			if (strISExportFC.compareTo("FC") == 0 && strExportPicIDs == null) {
				strExportPicIDs = "NONE";
			}

			if (Word.getWordFileId(patternId) != null) {
				FileBean fileInfo = Word.getWordPatternFile(Word
						.getWordFileId(patternId));

				/* 创建输入流 */
				InputStream inStream = fileInfo.getFileStream();
				byte[] buf = new byte[4096];
				/* 创建输出流 */
				ServletOutputStream servletOS = response.getOutputStream();
				Word.wordPattern2Word(inStream, formInstance, servletOS,
						strExportPicIDs);
				int readLength;
				while (((readLength = inStream.read(buf)) != -1)) {
					servletOS.write(buf, 0, readLength);
				}

				inStream.close();
				servletOS.flush();
				servletOS.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

}