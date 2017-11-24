package kd.idp.common.database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

public class DBManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5901811362924880127L;

	//YXY 在没有容器环境下调用db
	private static String url = "";
	private static String username = "";
	private static String password = "";
	private static int dbType = 1;
	//YXY END
	public static String dataSourceName = "psidp";

	static boolean tomcatConfig = true;

	public DBManager() {
	}

	/**
	 * 2008-09-05 LHF
	 * 
	 * @return
	 */
	public static KD.IDP.Service.DataBase.DBService getDB() {
		return getDB(tomcatConfig);
	}

	public static DBService getDBService() {
		return getDBService(tomcatConfig);
	}

	/**
	 * 
	 * @param application
	 *            boolean 是否采用tomcat连接池
	 * @return DBService
	 */

	public static DBService getDBService(boolean tomcatConfig) {

		DBService db_service = null;
		if (!tomcatConfig) {
			/**
			 * 调试用
			 */
			db_service = new DBService(getUrl(), getPassword(), getPassword(), getDbType());
			//db_service = new DBService("jdbc:dm://127.0.0.1/psidp", "SYSDBA", "SYSDBA", 2);
		} else {
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = DBConnPool.getDataSource(dataSourceName).getConnection();
				// stmt = conn.createStatement();
				//                
				// DBConnPool.showDBCPState();
				//                
			} catch (Exception e) {
				e.printStackTrace();
			}

			db_service = new DBService(conn);
			db_service.setStatement(stmt);
		}

		// db_service.addOpenCount();
		// db_service.showStatus(true);
		return db_service;

	}
	
	/**
	 * 2011-03-04 LS 
	 * @param tomcatConfig  true/false
	 * @param dataSrcName   psidp/psidp2  根据"数据源名称"获取数据库连接
	 * @return
	 */
	public static DBService getDBService(boolean tomcatConfig,String dataSrcName) {

		DBService db_service = null;
		if (!tomcatConfig) {
			/**
			 * 调试用
			 */
			db_service = new DBService("npt2", "10.35.178.104", "1521",
					"idp_web", "idp_web");
		} else {
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = DBConnPool.getDataSource(dataSrcName).getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}

			db_service = new DBService(conn);
			db_service.setStatement(stmt);
		}
		return db_service;
	}

	/**
	 * 2008-09-05 LHF
	 * 
	 * @return
	 */
	public static KD.IDP.Service.DataBase.DBService getDB(boolean tomcatConfig) {

		KD.IDP.Service.DataBase.DBService db_service = null;
		if (!tomcatConfig) {
			/**
			 * 调试用
			 */
			db_service = new KD.IDP.Service.DataBase.DBService("npt2",
					"10.35.178.104", "1521", "idp_web", "idp_web");
		} else {
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = DBConnPool.getDataSource(dataSourceName).getConnection();
				// stmt = conn.createStatement();
				//                
				// DBConnPool.showDBCPState();
				//                
			} catch (Exception e) {
				e.printStackTrace();
			}

			db_service = new KD.IDP.Service.DataBase.DBService(conn);
			db_service.setStatement(stmt);
		}

		// db_service.addOpenCount();
		// db_service.showStatus(true);
		return db_service;

	}

	public static void main(String[] args) {
		DBService db = null;
		try {
			int pageCurrent = 5000;

			int start = 20 * (pageCurrent - 1);
			long start_time = System.currentTimeMillis();

			db = DBManager.getDBService(false);
			String sql = "SELECT  \"ID\", \"SDATE\", \"DDATE\", \"CTIME\", \"DESCR\", \"ONAME_Z\", \"ONAME1\", \"ONAME2\", \"ONAME3\", \"ONAME4\", \"ONAME5\", \"ST\", \"KVLEVE\", \"LINE\", \"NUM\", \"SJLX\", \"YXDY\", \"UPDATE_FLAG\", \"Q_FLAG\", \"V00\", \"V01\", \"V02\", \"V03\", \"V04\", \"V05\", \"V06\", \"V07\", \"V08\", \"V09\", \"V10\", \"V11\", \"V12\", \"V13\", \"V14\", \"V15\", \"V16\", \"V17\", \"V18\", \"V19\", \"V20\", \"V21\", \"V22\", \"V23\" FROM \"HISDATA\".\"PDBASE\"";
			System.out.println(sql);
			ArrayList dataAndCount = db.executeQueryA(sql, start, 20);
			/**
			 * 关键字值映射
			 */

			System.out.println("total used:"
					+ (System.currentTimeMillis() - start_time));

			// this.pageTotal=(this.countTotal/this.pageSize)+1;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.closeConn();
		}

	}

	public static boolean isTomcatConfig() {
		return tomcatConfig;
	}

	public static void setTomcatConfig(boolean tomcatConfig) {
		DBManager.tomcatConfig = tomcatConfig;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		DBManager.url = url;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		DBManager.username = username;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		DBManager.password = password;
	}

	public static int getDbType() {
		return dbType;
	}

	public static void setDbType(int dbType) {
		DBManager.dbType = dbType;
	}
}
