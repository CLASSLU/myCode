package kd.idp.common.database;

import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2003</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DBConnPool {

  static private Context ctxDmis;
  /**
   * 存放DataSource的Hashtable
   */
  static private Hashtable htDataSource = new Hashtable();

  static {
    try {
      ctxDmis = new InitialContext();
      if (ctxDmis == null) {
        throw new Exception("Error: - No Context");
      }

      
      
      NamingEnumeration nameEnum=ctxDmis.list("java:comp/env/jdbc");
      while(nameEnum.hasMore()){
    	  Object nextObj=nameEnum.next();
    	  javax.naming.NameClassPair nextPair=(javax.naming.NameClassPair)nextObj;
    	  String pairName= nextPair.getName();
    	  DataSource dsSx = (DataSource) ctxDmis.lookup("java:comp/env/jdbc/"+pairName);
    	  if (dsSx != null) {
    	        htDataSource.put(pairName, dsSx);
    	  }
    	
      }
      
      
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * 构造函数
   */
  public DBConnPool() {
  }

  /**
   * 按数据库名称得到DataSource
   * @param dsName 数据库名称
   * @return DataSource
   */
  static public DataSource getDataSource(String dsName) {

    DataSource dsReturn = null;
    try {
                     //System.out.println("htDataSource 2007-05-09 ls is:"+htDataSource);
      dsReturn = (DataSource) htDataSource.get(dsName);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      return dsReturn;
    }

  }

  /**
   * 显示dbcp的状态信息
   */
  static public void showDBCPState() {
    try {
      Hashtable heDBCPState = getDBCPState();
      for (Enumeration en = heDBCPState.keys(); en.hasMoreElements(); ) {
        String dsName = (String) en.nextElement();
        Hashtable heTemp = (Hashtable) heDBCPState.get(dsName);
        System.out.println("——————DataSource " + dsName + ":————————");
        System.out.println("——driverClass: " +
                           (String) heTemp.get("driverClass"));
        System.out.println("——maxActive: " + (String) heTemp.get("maxActive"));
        System.out.println("——maxIdle: " + (String) heTemp.get("maxIdle"));
        System.out.println("——maxWait: " + (String) heTemp.get("maxWait"));
        System.out.println("——currentActive: " +
                           (String) heTemp.get("currentActive"));
        System.out.println("——currentIdle: " +
                           (String) heTemp.get("currentIdle"));
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * 显示连接属性
   */
  static public void showConnectionProp() {
    try {
      DriverPropertyInfo dpi[];
      dpi = DriverManager.getDriver(
          "jdbc:sybase:Tds:10.20.10.18:5000/dmis_dict?charset=eucgb").
          getPropertyInfo(
              "jdbc:sybase:Tds:10.20.10.18:5000/dmis_dict?charset=eucgb", null);
      for (int i = 0; i < dpi.length; i++) {
        System.out.println(dpi[i].name + ":" + dpi[i].value + "\n");
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * 得到数据库连接池的状态类表
   * @return HashtableExp
   */
  public static Hashtable getDBCPState() {

    Hashtable heReturn = new Hashtable();
    try {
      for (Enumeration en = htDataSource.keys(); en.hasMoreElements(); ) {
        String dsName = (String) en.nextElement();
        
        BasicDataSource bds = (BasicDataSource) htDataSource.get(dsName);
        
        Hashtable heTemp = new Hashtable();
        heTemp.put("driverClass", String.valueOf(bds.getDriverClassName()));
        heTemp.put("maxActive", String.valueOf(bds.getMaxActive()));
        heTemp.put("maxIdle", String.valueOf(bds.getMaxIdle()));
        heTemp.put("maxWait", String.valueOf(bds.getMaxWait()));
        heTemp.put("currentActive", String.valueOf(bds.getNumActive()));
        heTemp.put("currentIdle", String.valueOf(bds.getNumIdle()));
        heReturn.put(dsName, heTemp);
        
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      return heReturn;
    }
  }

}
