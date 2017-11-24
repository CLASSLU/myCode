package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import com.spring.dbservice.DBTemplate;
import com.spring.mapper.HashMapStrRowMapper;

public class FtpTool {

	private String oldCode = null;
	private String newCode = null;

	public FtpTool() {

	}

	public FtpTool(String oldCode, String newCode) {
		this.oldCode = oldCode;
		this.newCode = newCode;
	}

	/**
	 * 向FTP服务器上传文件
	 * 
	 * @param url
	 *            FTP服务器IP地址
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return
	 */
	public boolean uploadFile(String url, int port, String username,
			String password, String path, String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			ftp.setFileType(2);
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			String remotePath = getTurnCode(path);
			ftp.changeWorkingDirectory(remotePath);
			ftp.storeFile(getTurnCode(filename), input);
			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	/**
	 * 从FTP服务器下载文件
	 * 
	 * @param url
	 *            FTP服务器IP地址
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public boolean downFile(String url, int port, String username,
			String password, String remotePath, String fileName,
			String localPath) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			remotePath = getTurnCode(remotePath);
			ftp.changeWorkingDirectory(remotePath);
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					// 生成文件名 可以换
					File localFile = new File(localPath + "/" + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					String name = getTurnCode(ff.getName());
					ftp.retrieveFile(name, is);
					is.close();
				}
			}
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	public boolean moveFile(String url, int port, String username,
			String password, String fileName, String oldPath, String newPath) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(url, port);
			ftp.login(username, password);
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.out.println("FTP 链接错误！！");
				return result;
			}
			String olddir = getTurnCode(oldPath);
			String newdir = getTurnCode(newPath);

			ftp.changeWorkingDirectory(olddir);
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (!ff.isFile()) {
					continue;
				}
				if (ff.getName().equals(fileName)) {
					String remoteFile = getTurnCode(fileName);
					result = ftp.rename(olddir + remoteFile, newdir
							+ remoteFile);
					System.out.println(ff.getName() + " move " + result);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		System.out.println(result);
		return result;
	}

	public boolean deleteFile(String url, int port, String username,
			String password, String path, String fileName) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(url, port);
			ftp.login(username, password);
			int reply = ftp.getReplyCode();
			String remotePath = getTurnCode(path);
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.out.println("FTP 链接错误！！");
				return result;
			}
			ftp.changeWorkingDirectory(remotePath);
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (!ff.isFile()) {
					continue;
				}
				if (ff.getName().equals(fileName)) {
					String remoteFile = getTurnCode(fileName);
					result = ftp.deleteFile(remotePath + remoteFile);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}

	/**
	 * 向FTP服务器上传文件
	 * 
	 * @param url
	 *            FTP服务器IP地址
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return
	 */
	public boolean uploadFile(String url, int port, String username,
			String password, String path, String filename, File file) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			int reply;
			ftp.connect(url, port);
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			ftp.setFileType(2);
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			String remotePath = getTurnCode(path);
			ftp.changeWorkingDirectory(remotePath);
			ftp.storeFile(getTurnCode(filename), input);
			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	public String getOldCode() {
		return oldCode;
	}

	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}

	public String getNewCode() {
		return newCode;
	}

	public void setNewCode(String newCode) {
		this.newCode = newCode;
	}

	public String getTurnCode(String str) {
		try {
			if (oldCode != null && newCode != null) {
				return new String(str.getBytes(oldCode), newCode);
			} else {
				return str;
			}
		} catch (Exception e) {
			System.out.println(this + " 转码错误！！");
			return str;
		}
	}

	public static Properties getRConfigMethod(String path){
	    Properties props = new Properties();
	    try {
	      InputStream is = FtpTool.class.getResourceAsStream(path);
	      props.load(is);
	      if (is != null) {
	        is.close();
	      }
	      Object[] all = props.stringPropertyNames().toArray();
	      for (Object o : all) {
	        String s = (String)o;
	        String val = props.getProperty(s);
	        props.setProperty(s, val);
	      }
	      return props;
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    return props;
	  }
	
	/**
	 * 从FTP服务器下载文件
	 * 
	 * @param url
	 *            FTP服务器IP地址
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @params startWith
	 *            要下载的文件名 以什么开头
	 * @params endWith
	 *            要下载的文件名 以什么结束
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public boolean downFile(String url, int port, String username,
			String password, String remotePath, String startWith,
			String endWith,String localPath) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			remotePath = getTurnCode(remotePath);
			ftp.changeWorkingDirectory(remotePath);
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().startsWith(startWith)&&ff.getName().endsWith(endWith)) {
					// 生成文件名 可以换
					File localFile = new File(localPath + "/" + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					String name = getTurnCode(ff.getName());
					ftp.retrieveFile(name, is);
					is.close();
				}
			}
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}
	
	public Map<String,String> ftpService(){
		Map<String,String> result = new HashMap<String, String>();
		String sql = "select * from PSIDP.IDP_JBOSS.DMAIL_小邮件 where 是否启用 ='1' ";
		List<Map<String, String>> list = DBTemplate.getInstance().getResultRowMapperList(sql,new HashMapStrRowMapper());
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(list.get(0).get("FTP地址"), Integer.parseInt(list.get(0).get("FTP端口")));
			ftp.login(list.get(0).get("用户名"), list.get(0).get("密码"));
			result.put("FTP地址", list.get(0).get("FTP地址"));
			result.put("FTP端口", list.get(0).get("FTP端口"));
			result.put("用户名", list.get(0).get("用户名"));
			result.put("密码", list.get(0).get("密码"));
		}catch (IOException e) {
			result.put("FTP地址", list.get(1).get("FTP地址"));
			result.put("FTP端口", list.get(1).get("FTP端口"));
			result.put("用户名", list.get(1).get("用户名"));
			result.put("密码", list.get(1).get("密码"));
		}finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		FtpTool ftptool = new FtpTool();
		ftptool.ftpService();
	}

}
