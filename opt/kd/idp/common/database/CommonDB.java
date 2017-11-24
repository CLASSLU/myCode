package kd.idp.common.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import kd.idp.common.CommonTools;
import KD.IDP.basic.CallSuport;

/**
 * 2008-01-07 LS
 * 
 * @author Administrator
 * 
 */
public class CommonDB {

	public CommonDB() {
	}

	public static ArrayList getMetaColumnOfTable(String owner, String tableName) {
		ArrayList temp = new ArrayList();

		String sql_str = "SELECT column_name FROM sys.DBA_TAB_COLUMNS   WHERE OWNER ='"
				+ owner
				+ "'  AND TABLE_NAME ='"
				+ tableName
				+ "' order by column_id";

		temp = CommonTools.get_DB_ResultArrayList(sql_str);

		return temp;
	}

	public static ArrayList getTables(DBService db, String schema)
			throws SQLException {
		DatabaseMetaData metaData = db.getConnection().getMetaData();
		ResultSet tableResultSet = metaData.getTables(null, schema
				.toUpperCase(), null, null);
		ArrayList tables = new ArrayList();
		while (tableResultSet.next()) {
			tables.add(tableResultSet.getString(3));
		}
		return tables;
	}

	/**
	 * 2008-01-10 LS :create tableB as select * from tableA
	 * 
	 * @param oldschema
	 * @param oldtable
	 * @param newschema
	 * @param newtable
	 * @return
	 */
	public static boolean createNewTableASOldTable(String oldschema,
			String oldtable, String newschema, String newtable) {
		boolean flag = false;

		int result = -1;

		String sql_str = "create table " + newschema + ".\"" + newtable
				+ "\" as  select * from  " + oldschema + ".\"" + oldtable
				+ "\" where 1=2";
		DBService dbService = null;
		// boolean
		try {
			dbService = DBManager.getDBService();
			result = dbService.executeUpdate(sql_str);
			if (result == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			dbService.closeConn();
		}
		return flag;
	}
	
	//truncate table IDP_VERSION.DEVELOP_TABLE1_BAK
	
	
	/**
	 * 2008-06-05 LS
	 * truncate table
	 */
	public static boolean truncateTable(String schema,String table) {
		boolean flag = false;

		int result = -1;

//		String sql_str = "create table " + newschema + ".\"" + newtable
//				+ "\" as  select * from  " + oldschema + ".\"" + oldtable
//				+ "\" where 1=2";
		
		String sql_str = "truncate table " + schema + ".\"" + table+ "\" ";
		DBService dbService = null;

		
		try {
			dbService = DBManager.getDBService();
			result = dbService.executeUpdate(sql_str);
			if (result == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			dbService.closeConn();
		}
		return flag;
	}
	

	/**
	 * 2008-01-10 LS :alter one column to a table
	 * 
	 * @param _chema
	 * @param _table
	 * @param _colName
	 * @param _colProperty
	 * @return
	 */
	public static boolean alterOneColumn(String _chema, String _table,
			String _colName, String _colProperty) {
		boolean flag = false;

		int result = -1;

		String sql_str = "ALTER TABLE " + _chema + ".\"" + _table
				+ "\" ADD (\"" + _colName + "\" " + _colProperty + ")";

		DBService dbService = null;
		// boolean
		try {
			dbService = DBManager.getDBService();
			result = dbService.executeUpdate(sql_str);
			if (result == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			dbService.closeConn();
		}
		return flag;
	}

	/**
	 * 2008-02-21
	 * 
	 * @param tableNamesList
	 * @return
	 */
	public static String getDBTableColumnsString(Vector tableNamesList) {
		String namestr = null;
		String tbCols = tableNamesList.toString();
		tbCols = CallSuport.replaceLine(tbCols, "[", "");
		tbCols = CallSuport.replaceLine(tbCols, "]", "");
		tbCols = CallSuport.replaceLine(tbCols, " ", "");
		namestr = tbCols;
		return namestr;
	}

	public static ArrayList getArrayList(ArrayList temp) {
		ArrayList result = new ArrayList();

		String str = (String) temp.get(0);

		String[] str1 = str.split(",");

		for (int i = 0; i < str1.length; i++) {
			String str2 = str1[i];
			result.add(str2);
		}

		return result;
	}

	/**
	 * 2008-02-21 LS
	 * 
	 * @param schemaltable
	 * @param columnList
	 * @param dataV
	 * @param keyword
	 * @return
	 */
	/*
	public int executeUpdateW(String schemaltable, ArrayList columnList,
			Vector dataVector, String[] keyword) {
		int result = -1;

		// update tableName set a=a ,b=b where a =a

		String keywordv = keyword[0];

		String wheresql = "";

		String setsql = "";

		for (int i = 0; i < columnList.size(); i++) {
			String colname = (String) columnList.get(i);
			Object colvalue = (Object) columnList.get(i);
			String colvl = "";
			if (colvalue != null) {
				colvl = colvalue.toString();
			}

			if (colname != null && colname.equals(keywordv)) {
				wheresql = colname + " = " + colvl;
			}

			if (colname != null) {

			}

		}

		return result;

	}
	
	*/

	/**
	 * 2008-02-22 LS :根据全部 ColumnInfoList 取部分 列信息
	 * 
	 * @param allColumnInfoList
	 * @return
	 */
	public static ArrayList getPartColumnInfo(ArrayList allColumnInfoList,
			ArrayList partColumnList) {
		ArrayList temp = new ArrayList();
		String[] columnName = (String[]) allColumnInfoList.get(0);
		int[] columnType = (int[]) allColumnInfoList.get(1);

		int partsize = partColumnList.size();

		String[] partColname = new String[partsize];
		int[] partColtype = new int[partsize];

		for (int i = 0; i < partColumnList.size(); i++) {
			String partcol = (String) partColumnList.get(i);

			// /boolean iflag = isStrInStrary(partcol,columnName);
			int iposition = isStrInStrary(partcol, columnName);

			if (iposition != -1) {

				int partColnamelength = partColname.length;
				int partColtypelength = partColtype.length;

				String pcolname = columnName[iposition];
				int pcoltype = columnType[iposition];

				partColname[i] = pcolname;
				partColtype[i] = pcoltype;
			}
		}
		temp.add(partColname);
		temp.add(partColtype);
		return temp;
	}

	public static int isStrInStrary(String str, String[] ary) {
		int posi = -1;

		for (int i = 0; i < ary.length; i++) {
			String name = (String) ary[i];
			if (name != null) {
				// System.out.println("### name is:"+name);
				// /System.out.println("### str is:"+str);
				if (name.equals("\"" + str + "\"")) {
					return i;
				}
			}
		}
		return posi;
	}

	/**
	 * 调用存储过程 YXY  2008-06-02
	 * @param proName
	 */
	public static void callProcedure(String procName) {
		Vector para = new Vector();
		Vector para1 = new Vector();
		Vector para2 = new Vector();

		String ver_time = CommonTools.getCurrentDate("ymd");
		para.clear();
		para1.clear();
		para2.clear();

		para1.add("1");
		para1.add(ver_time);
		para.add(para1);

//		para2.add("1");
//		para2.add(ver_time);
//		para.add(para2);

		Vector resultVec = null;

		DBService db = null;
		db = DBManager.getDBService();

		// System.out.println("callProcedure update_app!");
		try {

			db = DBManager.getDBService();

			resultVec = db.callProcedure(procName, para);

		} catch (Exception ex1) {
			ex1.printStackTrace();
		} finally {
			db.closeConn();
		}

//		System.out.println(resultVec);
	}
	
	// public static boolean isStrInStrary(String str,String[] ary){
	// boolean flag = false;
	//		
	// for(int i=0;i<ary.length;i++){
	// String name = (String)ary[i];
	// if(name!=null){
	// System.out.println("### name is:"+name);
	// System.out.println("### str is:"+str);
	// if(name.equals("\""+str+"\"")){
	// return true;
	// }
	// }
	// }
	// return flag;
	// }

	
	/**
	 * 2008-06-05 LS  根据 方案名 表明 主键  主键值   转换  SQL
	 * @param schema
	 * @param table
	 * @param keyword
	 * @param olddatas
	 * @return
	 */
	public static String getWhereSQL(String schema,String table,String[] keyword ,Vector olddatas,String tblAlias) {
		StringBuffer bf = new StringBuffer();
		
		DBService dbService = DBManager.getDBService();
		String sctb = schema+".\""+table+"\"";
		ArrayList columnInfo  =null;
		try{
			 columnInfo = dbService.getColumnInfo(sctb);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			 dbService.closeConn();
		}
        
        String[] columnName = (String[]) columnInfo.get(0);
        int[] columnType = (int[]) columnInfo.get(1);
        
        Vector data = (Vector)olddatas.get(0);
        
        for(int j=0;j<keyword.length;j++) {
        	String keyname = (String)keyword[j];
        	keyname = "\""+keyname+"\"";
        	
        	Object obj = (Object)data.get(j);
        
        	for(int i=0;i<columnName.length;i++) {
        		
        			String colname = (String)columnName[i];
        				int type = (int)columnType[i];
        				if(keyname.toUpperCase().equals(colname.toUpperCase())) {
        					if(type==12) {
        						//varchar2
        						String  s =   " "+tblAlias+"."+colname.toUpperCase() +" "+"="+"'"+obj.toString()+"' and ";
        						bf.append(s);
        						
        					}else if(type==91) {
        						//date
        						//?????  待扩展
//        						String  s =   " "+tblAlias+"."+colname.toUpperCase() +" "+"="+"'"+obj.toString()+"' and ";
        						String  s =   " "+tblAlias+"."+colname.toUpperCase() +" "+"="+"to_date('"+obj.toString()+"','yyyy-mm-dd')  and ";
        						
        						/// TO_DATE('10/16/2006 00:00:00', 'MM/DD/YYYY HH24:MI:SS')
        						
        						bf.append(s);	
        						
        					}else if(type==2) {
        						//number 
        						String  s =   "    "+tblAlias+"."+colname.toUpperCase() +" "+"="+" "+obj.toString()+" and ";
        						
        						bf.append(s);
        					}        		
        				}
        	}
        }
        
        if(bf.length()!=0){
        	return bf.toString().substring(0, bf.toString().length()-4);
        }else{
        	return "";
        }
	}	
//	
//	/**
//	 * 2008-06-06 LS   由于    dbService.getColumnInfo();   方法 在删除时调用 取出的的 columnInfo 发生变化(字段加引号)
//	 * @param schema
//	 * @param table
//	 * @param keyword
//	 * @param olddatas
//	 * @param tblAlias
//	 * @return
//	 */
//	public static String getWhereSQL_D(String schema,String table,String[] keyword ,Vector olddatas,String tblAlias) {
//		StringBuffer bf = new StringBuffer();
//		
//		DBService dbService = DBManager.getDBService();
//		String sctb = schema+".\""+table+"\"";
//		
//        ArrayList columnInfo = dbService.getColumnInfo(sctb);     ////  ????????  问题  源
//        
//        String[] columnName = (String[]) columnInfo.get(0);
//        int[] columnType = (int[]) columnInfo.get(1);
//        
//        Vector data = (Vector)olddatas.get(0);
//        
//        for(int j=0;j<keyword.length;j++) {
//        	String keyname = (String)keyword[j];
//        	
//        	Object obj = (Object)data.get(j);
//        
//        	for(int i=0;i<columnName.length;i++) {
//        		
//        			String colname = (String)columnName[i];
//        				int type = (int)columnType[i];
//        				if(keyname.toUpperCase().equals(colname.toUpperCase())) {
//        					if(type==12) {
//        						//varchar2
//        						String  s =   " "+tblAlias+"."+colname.toUpperCase() +" "+"="+"'"+obj.toString()+"' and ";
//        						
//        						bf.append(s);
//        						
//        					}else if(type==91) {
//        						//date
//        						//?????  待扩展
//        					}else if(type==2) {
//        						//number 
//        						String  s =   "    "+tblAlias+"."+colname.toUpperCase() +" "+"="+" "+obj.toString()+" ";
//        						bf.toString();
//        					}        		
//        				}
//        	}
//        }
//		return bf.toString().substring(0, bf.toString().length()-4);
//	}	
	/**
	 * 2008-06-05 ls
	 * @param sql_str1
	 * @return
	 */
	public static int exeSQL (String  sql_str) {
		int r0 = -3;
		DBService dbservice = null;
		try {
			dbservice = DBManager.getDBService();
			CommonTools.psidpwebdebuginfor(8, "sql_str", sql_str);
			r0 = dbservice.executeUpdate(sql_str);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbservice.closeConn();
		}
		return r0;
	}
	
	public static String getDBNames(){
		String dbNames=null;
		try {
			ArrayList list=getAllSchemas();
			dbNames=CallSuport.getStringFromGroup(CallSuport.getStrFromArrayList(list), "PSIDP_SEP");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dbNames;
		
	}
	
	public static ArrayList getAllSchemas() throws SQLException {
		Connection connect = DBManager.getDBService().getConnection();
		ArrayList schemaList = null;
		if (connect != null) {
			DatabaseMetaData metaData = connect.getMetaData();
			ResultSet resultSet = metaData.getSchemas();
			schemaList = new ArrayList();
			while (resultSet.next()) {
				schemaList.add(resultSet.getString(1));
			}
			resultSet.close();
		}

		return schemaList;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
