package com.sdjxd.common.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sdjxd.pms.platform.base.Global;
import com.sdjxd.pms.platform.form.dao.PatternDao;
import com.sdjxd.pms.platform.form.model.FileBean;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.webapp.BeanFactory;
import com.sdjxd.util.Dom4Jparser;

public class Word {
	private static boolean  IS_SAVE_DB = false;
	private static Dao dao = new Dao();

	public static String getFilePath(String fileId) {
		String savepath = Global.getConfig("upload.tempDir");
		if (savepath.startsWith("/"))
			savepath = Global.getPath() + savepath;
		if (!savepath.endsWith("/"))
			savepath = savepath + "/";
		savepath = savepath.replaceAll("(\\\\)|(//)", "/");
		savepath = savepath + fileId;
		return savepath;
	}

	public static FileBean getWordPatternFile(String fileId) {

		PatternDao dao = (PatternDao) BeanFactory.getClass("PatternDao");

		FileBean fileInfo = null;
		String saveToDb = Global.getConfig("upload.saveToDB");
		if ((saveToDb != null)
				&& ((("true".equalsIgnoreCase(saveToDb)) || ("1"
						.equalsIgnoreCase(saveToDb))))) {
			IS_SAVE_DB = true;
		}
		if (IS_SAVE_DB) {
			try {
				fileInfo = dao.getDbFile(fileId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String savepath = getFilePath(fileId);
			File file = new File(savepath);
			if ((file.exists()) && (file.isFile())) {
				try {
					fileInfo = dao.getFile(fileId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				FileInputStream in;
				try {
					in = new FileInputStream(file);
					fileInfo.setFileStream(in);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			} else {
				fileInfo = new FileBean();
			}
		}
		return fileInfo;
	}
	public static String getWordFileId(String dybdid) {
		return dao.getWordFileId(dybdid);
	}
	public static void wordPattern2Word(InputStream wordPattern,FormInstance formInstance,OutputStream os, String strExportPicIDs){
		Dom4Jparser.replaceAllCellText(wordPattern, formInstance, os, strExportPicIDs);
	}
}
