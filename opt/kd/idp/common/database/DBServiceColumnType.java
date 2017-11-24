package kd.idp.common.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import KD.IDP.basic.CallSuport;


public class DBServiceColumnType {
	static String ip = "192.168.100.223";
	static String sid = "psidp";
	static int port = 1521;
	static String username = "idp_web";
	static String password = "idp_web";
	static String table   = "IDP_WEB.存储单元管理表";
	
	public int getColumnType(String  columnTypeStr){
		 
		int columnNum = 0 ; 
			if (columnTypeStr.equalsIgnoreCase("BINARY")) { columnNum = 1; } 
			else if (columnTypeStr.equalsIgnoreCase("CHAR")) { columnNum = 1; } 
			else if (columnTypeStr.equalsIgnoreCase("DATE")) { columnNum = 91; }
			else if (columnTypeStr.equalsIgnoreCase("DOUBLE")) { columnNum = 8; }
			else if (columnTypeStr.equalsIgnoreCase("FLOAT")) { columnNum = 6; }
			else if (columnTypeStr.equalsIgnoreCase("INTEGER")) { columnNum = 4; } 
			else if (columnTypeStr.equalsIgnoreCase("LONG")) { columnNum = 2;  }
			else if (columnTypeStr.equalsIgnoreCase("LONGVARCHAR")) { columnNum = 2;  } 
			else if (columnTypeStr.equalsIgnoreCase("NUMBER")) { columnNum = 2; } 
			else if (columnTypeStr.equalsIgnoreCase("NUMERIC")) { columnNum = 2; }
			else if (columnTypeStr.equalsIgnoreCase("REAL")) { columnNum = 2; }
			else if (columnTypeStr.equalsIgnoreCase("TIME")) { columnNum = 92; }
			else if (columnTypeStr.equalsIgnoreCase("TIMESTAMP")) { columnNum = 93; }
			else if (columnTypeStr.equalsIgnoreCase("VARCHAR")) { columnNum = 12; }
			else if (columnTypeStr.equalsIgnoreCase("VARCHAR2")) { columnNum = 12; }
		   
		return columnNum;
	}
	
	public ArrayList getColumnTypeStr(String tablename){
		Statement stmt = null;
		ArrayList columnInfo = new ArrayList(2);
		 
		Connection connection = null; 
		int colTypeNum = 0; 
		String colTypeName = ""; 
		
		try {
			
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid;
			 connection = DriverManager.getConnection(url, username, password);
		  
		DatabaseMetaData dbMetaData = connection.getMetaData();
		
		String dbType = dbMetaData.getDatabaseProductName();
		System.out.println("dbtype is: "+dbMetaData.getDatabaseProductName());
		
		stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("select * from " + tablename	+ " where 1=2");
		ResultSetMetaData meta = rs.getMetaData();
		int column = meta.getColumnCount();
		String[] columnName = new String[column];
		int[] columnType = new int[column];
		for (int i = 0; i < column; i++) {
			columnName[i] = "\"" + meta.getColumnName(i + 1).toUpperCase()
					+ "\"";

			colTypeName = meta.getColumnTypeName(i + 1);
			if (colTypeName.equalsIgnoreCase("BINARY")) { colTypeNum = 1; } 
			else if (colTypeName.equalsIgnoreCase("CHAR")) { colTypeNum = 1; } 
			else if (colTypeName.equalsIgnoreCase("DATE")) { colTypeNum = 91; }
			else if (colTypeName.equalsIgnoreCase("DOUBLE")) { colTypeNum = 8; }
			else if (colTypeName.equalsIgnoreCase("FLOAT")) { colTypeNum = 6; }
			else if (colTypeName.equalsIgnoreCase("INTEGER")) { colTypeNum = 4; } 
			else if (colTypeName.equalsIgnoreCase("LONG")) { colTypeNum = 2;  }
			else if (colTypeName.equalsIgnoreCase("LONGVARCHAR")) { colTypeNum = 2;  } 
			else if (colTypeName.equalsIgnoreCase("NUMBER")) { colTypeNum = 2; } 
			else if (colTypeName.equalsIgnoreCase("NUMERIC")) { colTypeNum = 2; }
			else if (colTypeName.equalsIgnoreCase("REAL")) { colTypeNum = 2; }
			else if (colTypeName.equalsIgnoreCase("TIME")) { colTypeNum = 91; }
			else if (colTypeName.equalsIgnoreCase("TIMESTAMP")) { colTypeNum = 91; }
			else if (colTypeName.equalsIgnoreCase("VARCHAR")) { colTypeNum = 12; }
			else if (colTypeName.equalsIgnoreCase("VARCHAR2")) { colTypeNum = 12; }
			 
			 columnType[i] = colTypeNum;
			System.out.println(columnName[i] +":"+ columnType[i]);
		}
		rs.close();
		columnInfo.add(columnName);
		columnInfo.add(columnType);
		
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
					connection.close();
				} catch (SQLException e1) {
					CallSuport.println(e1.getErrorCode() + ":"
							+ e1.getMessage());
				}
			}
		}
		return columnInfo;
	}
	
	
	public static void main(String[] args){
		
		
		try{
		DBServiceColumnType newType = new DBServiceColumnType();
		newType.getColumnTypeStr(table);
		
		
		 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}
}
