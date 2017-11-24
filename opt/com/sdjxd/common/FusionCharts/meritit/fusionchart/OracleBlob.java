package com.sdjxd.common.FusionCharts.meritit.fusionchart;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.sql.RowSet;

import org.apache.commons.codec.binary.Hex;

import com.sdjxd.pms.platform.base.Global;
import com.sdjxd.pms.platform.data.DbOper;

import oracle.sql.BLOB;
/**
 * 向oracle 读,写文件Blob
 * 
 * @author guolw
 */
public class OracleBlob {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "";
	private String user = "";
	private String pwd = "";

	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	/**
	 * 构造函数
	 * 
	 * @param String
	 *            url 数据库连接字符串
	 * @param String
	 *            user 数据库用户名
	 * @param String
	 *            pwd 数据库登录密码
	 */
	public OracleBlob(String url, String user, String pwd) {
		this.url = url;
		this.user = user;
		this.pwd = pwd;
	}
	/**默认构造函数*/
	public OracleBlob() {
	}
	
	/**
	 * 写二进制文件
	 * 
	 * @param String
	 *            ID 文件在数据表中唯一ID
	 * @param String
	 *            filePath 文件路径
	 * @param Connection conn 数据库连接，如果置为null 则函数生成conn
	 * @param InputStream inStream  需要写入到数据库的图片流
	 */
	public void writeBlob(Connection conn, InputStream inStream,
			String tableName, String ID, String fileName, String createDate) {
		boolean conFlag = false;// 当前连接是否在方法内生成
		try {
			// 具体可使用hibernate中的Conn
			if (conn == null) {
				conFlag = true;
				conn = getConnection();
			}
			boolean defaultCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			String sqlquery = "select IMAGE from " + tableName + " where ID='"
					+ ID + "' FOR UPDATE";
			ResultSet rs = stmt.executeQuery(sqlquery);
			if (rs.next()) {
				stmt.executeUpdate("UPDATE " + tableName
						+ " SET IMAGE=EMPTY_BLOB(),CREATEDATE='" + createDate + "' WHERE ID='" + ID + "'");
				rs = stmt.executeQuery(sqlquery);
				insertClob(rs, inStream);
			} else {
				StringBuffer sbf = new StringBuffer("insert into " + tableName
						+ "(ID,FILE_NAME,IMAGE,CREATEDATE) values (");
				sbf.append("'");
				sbf.append(ID);
				sbf.append("','");
				sbf.append(fileName);
				sbf.append("'");
				sbf.append(",EMPTY_BLOB()");
				sbf.append(",'");
				sbf.append(createDate);
				sbf.append("')");
				stmt.executeUpdate(sbf.toString());
				rs = stmt.executeQuery(sqlquery);
				insertClob(rs, inStream);
			}
			conn.commit();
			conn.setAutoCommit(defaultCommit);
			stmt.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getLocalizedMessage());
			}
			System.out.println(e.getLocalizedMessage());
		} finally {
			try {
				if (conn != null)
					if (conFlag) { // 如果当前连接在方法内生成，则关闭该连接
						conn.close();
					}
			} catch (SQLException e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
	}
	
	public static void setExportParam(String patternId, String cellId, String tjSDate, String tjEDate, 
			String excelName, String oldurl, String dateValue, String href, int clientWidth, int clientHeight)
	{
		HttpSession session = Global.getContext().getSession();;
		
		session.setAttribute("exportPatternId", patternId);
		session.setAttribute("exportCellID", cellId); 
		session.setAttribute("exportTjSDate", tjSDate); 
		session.setAttribute("exportTjEDate", tjEDate);
		session.setAttribute("exportExcelName", excelName); 
		session.setAttribute("exportOldUrl", oldurl); 
		session.setAttribute("exportDate", dateValue); 
		session.setAttribute("exportHref", href);
		session.setAttribute("exportCellWidth", String.valueOf(clientWidth));
		session.setAttribute("exportCellHeight", String.valueOf(clientHeight));
	}
	
	public static void setExportWordParam(String wordName, String formId, String formInstanceId, 
			String dateValue, String href, String fcCellIDs)
	{
		HttpSession session = Global.getContext().getSession();;
		
		session.setAttribute("exportWordName", wordName);
		session.setAttribute("exportFormID", formId); 
		session.setAttribute("exportInstanceID", formInstanceId); 
		session.setAttribute("exportDate", dateValue); 
		session.setAttribute("exportHref", href);
		session.setAttribute("exportCellIDs", fcCellIDs);
	}
	
	public static String getFCImageID(String datevalue) 
	{
		int nTimes = 0;
		String strFCImageID = "";
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT ID FROM ");
    	sql.append("[S].FUSIONCHART_IMAGE_TEMP WHERE CREATEDATE='");
    	sql.append(datevalue).append("'");
    	
    	try 
    	{
    		do {
    			Thread.sleep(1000);
    			
    			rs = DbOper.executeQuery(sql.toString());
    			
    			while(rs.next())
    			{
    				strFCImageID = rs.getString("ID");
    			}
    			
    			rs = null;
    			nTimes++;
    		} while(strFCImageID=="" && nTimes<=180);
		}
    	catch (SQLException e)
    	{
    		System.out.println("获取FC导出图片ID失败:" + e.getMessage());
		}
    	catch (InterruptedException e1)
    	{
    		System.out.println("时间等待异常：" + e1.getMessage());
    	}
    	
    	return strFCImageID;
	}
	
	public String getID() {
		return new String(Hex.encodeHex(org.apache.commons.id.uuid.UUID
				.randomUUID().getRawBytes()));
	}
	private void insertClob(ResultSet rs, InputStream inStream) {
		try {
			if (rs.next()) {
				BLOB blob = (BLOB) rs.getBlob("IMAGE");
				OutputStream out = blob.getBinaryOutputStream();
				int bufferSize = ((oracle.sql.BLOB) blob).getBufferSize();
				BufferedInputStream in = new BufferedInputStream(inStream,
						bufferSize);
				byte[] b = new byte[bufferSize];
				int count = in.read(b, 0, bufferSize);
				int amount = 0;
				while (count != -1) {
					out.write(b, 0, count);
					amount += count;
					count = in.read(b, 0, bufferSize);
				}
				out.close();
				out = null;
				in.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private Connection getConnection() {
		Connection con = null;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			con = DriverManager.getConnection(url, user, pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	public byte[] GetImgByteById(Connection conn, String imageID) {
		boolean conFlag = false;// 当前连接是否在方法内生成
		byte[] data = null;
		try {
			if (conn == null) {
				conn = this.getConnection();
			}
			Statement stmt = conn.createStatement();
			ResultSet myResultSet = stmt
					.executeQuery("select * from FUSIONCHART_IMAGE_TEMP"
							+ " where ID='" + imageID+"'");
			if (myResultSet.next()) {
				java.sql.Blob blob = myResultSet.getBlob("IMAGE");
				InputStream inStream = blob.getBinaryStream();
				try {
					long nLen = blob.length();
					int nSize = (int) nLen;
					data = new byte[nSize];
					inStream.read(data);
					inStream.close();
				} catch (IOException e) {
					System.out.println("获取图片数据失败,原因:" + e.getMessage());
				}
			}
			conn.commit();
			if(conFlag){
				if(conn!=null){
					conn.close();
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());

		}
		return data;

	}
}
