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
	 * ��FTP�������ϴ��ļ�
	 * 
	 * @param url
	 *            FTP������IP��ַ
	 * @param port
	 *            FTP�������˿�
	 * @param username
	 *            FTP��¼�˺�
	 * @param password
	 *            FTP��¼����
	 * @param path
	 *            FTP����������Ŀ¼
	 * @param filename
	 *            �ϴ���FTP�������ϵ��ļ���
	 * @param input
	 *            ������
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
	 * ��FTP�����������ļ�
	 * 
	 * @param url
	 *            FTP������IP��ַ
	 * @param port
	 *            FTP�������˿�
	 * @param username
	 *            FTP��¼�˺�
	 * @param password
	 *            FTP��¼����
	 * @param remotePath
	 *            FTP�������ϵ����·��
	 * @param fileName
	 *            Ҫ���ص��ļ���
	 * @param localPath
	 *            ���غ󱣴浽���ص�·��
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
					// �����ļ��� ���Ի�
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
				System.out.println("FTP ���Ӵ��󣡣�");
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
				System.out.println("FTP ���Ӵ��󣡣�");
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
	 * ��FTP�������ϴ��ļ�
	 * 
	 * @param url
	 *            FTP������IP��ַ
	 * @param port
	 *            FTP�������˿�
	 * @param username
	 *            FTP��¼�˺�
	 * @param password
	 *            FTP��¼����
	 * @param path
	 *            FTP����������Ŀ¼
	 * @param filename
	 *            �ϴ���FTP�������ϵ��ļ���
	 * @param input
	 *            ������
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
			System.out.println(this + " ת����󣡣�");
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
	 * ��FTP�����������ļ�
	 * 
	 * @param url
	 *            FTP������IP��ַ
	 * @param port
	 *            FTP�������˿�
	 * @param username
	 *            FTP��¼�˺�
	 * @param password
	 *            FTP��¼����
	 * @param remotePath
	 *            FTP�������ϵ����·��
	 * @params startWith
	 *            Ҫ���ص��ļ��� ��ʲô��ͷ
	 * @params endWith
	 *            Ҫ���ص��ļ��� ��ʲô����
	 * @param localPath
	 *            ���غ󱣴浽���ص�·��
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
					// �����ļ��� ���Ի�
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
		String sql = "select * from PSIDP.IDP_JBOSS.DMAIL_С�ʼ� where �Ƿ����� ='1' ";
		List<Map<String, String>> list = DBTemplate.getInstance().getResultRowMapperList(sql,new HashMapStrRowMapper());
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(list.get(0).get("FTP��ַ"), Integer.parseInt(list.get(0).get("FTP�˿�")));
			ftp.login(list.get(0).get("�û���"), list.get(0).get("����"));
			result.put("FTP��ַ", list.get(0).get("FTP��ַ"));
			result.put("FTP�˿�", list.get(0).get("FTP�˿�"));
			result.put("�û���", list.get(0).get("�û���"));
			result.put("����", list.get(0).get("����"));
		}catch (IOException e) {
			result.put("FTP��ַ", list.get(1).get("FTP��ַ"));
			result.put("FTP�˿�", list.get(1).get("FTP�˿�"));
			result.put("�û���", list.get(1).get("�û���"));
			result.put("����", list.get(1).get("����"));
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
