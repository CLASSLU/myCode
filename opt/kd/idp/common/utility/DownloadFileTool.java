package kd.idp.common.utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
/**
 * 根据文件传入id实现下载功能
 *
 * @author Li
 *
 */
public class DownloadFileTool{
	/**
	 * 
	 * @param response
	 * @param filePath：路径名称，不包括文件名
	 * @param fileName：文件名称
	 * @return
	 */
	public File downloadFile(HttpServletResponse response,String filePath, String fileName) throws Exception{
		File file = null;
		InputStream fis = null;
		try{
			response.setContentType("application/x-msdownload;");
			String fileNameNew;
			if(fileName!=null&&fileName.length()>5&&"F".equals(fileName.substring(0,1))){
				fileNameNew=fileName.substring(6,fileName.length());
			}else{
				fileNameNew=fileName;
			}
			
			response.setHeader("Content-Disposition", "attachment;" + "filename=" + URLEncoder.encode(fileNameNew, "UTF-8"));
			file = new File(filePath+fileName);
			if(!file.exists()){
				throw new Exception("DownloadfileNotFound!");
			}
			fis = new BufferedInputStream(new FileInputStream(file));
			FileCopyUtils.copy(fis, response.getOutputStream());
			return file;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			try{
				if(fis!= null){
					fis.close();	
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
