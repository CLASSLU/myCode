package kd.idp.common.database;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import kd.idp.common.CommonTools;

import oracle.jdbc.driver.OraclePreparedStatement;
import KD.IDP.Service.StringConvert;
import KD.IDP.Service.DataBase.DBColumn;
import KD.IDP.Service.DataBase.DBTable;
import KD.IDP.Service.DataBase.DataFilters;
import KD.IDP.basic.CallSuport;
import KD.IDP.basic.TimePack;


/**
 * <p>Title: ���ݿ��������</p>
 * <p>Description: </p>
 * <p>�ṩ���ݿ�����Ļ������񣬰���������Oracle���ݿ�����ӡ�ִ��select��update��create��drop��merge
 * ��SQL���Ĳ������ṩResultSet����ת��ΪVector����</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: �ƶ���˾</p>
 * @author ���
 * @version 1.0 Beta2
 */

public class DBService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5027406407097613125L;

	public static final int ORACLE = 0;

	public static final int SYBASE = 1;

	protected Connection m_connection;

	protected Statement m_statement;

	protected int flag;

	protected String url, username, password;

	protected String dbType = null;
	
	/**
	 * 2011-04-19 LS
	 * ����idp.conf��debugģʽ,ֱ���޸�Ϊ yes
	 */
	static String debug = "yes";

	//  PooledConnection dbpool;

	public DBService() {

	}

	/**
	 *DBService���캯����������Oracle���ݿ��Oci����
	 * @param url
	 * ���ݿ�Ӧ�÷����������磺e351����������Oracle�ͻ������ã���ӦIP��ַ���˿ںż����ݿ������
	 * @param username
	 * Oracle���ݿ��û���
	 * @param password
	 * Oracle���ݿ��Ӧusername������
	 */
	public DBService(String url, String username, String password) {
		flag = 1;
		this.url = "jdbc:oracle:oci8:@" + url;
		this.username = username;
		this.password = password;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			url = "jdbc:oracle:oci8:@" + url;
			CallSuport.println("url:", url, debug);
			CallSuport.println("username", username, debug);
			m_connection = DriverManager.getConnection(url, username, password);
			m_statement = m_connection.createStatement();
			dbType = "oracle";
		} catch (ClassNotFoundException e) {
			CallSuport.println(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e1) {
			CallSuport.println(e1.getErrorCode() + ":" + e1.getMessage());
			e1.printStackTrace();

		} catch (InstantiationException e) {
			CallSuport.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			CallSuport.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * DBService���캯����������DM���ݿ������
	 * 
	 * @param url
	 *            ���ݿ�Ӧ�÷����������磺jdbc:dm://192.168.100.95/PSIDPDM
	 * @param username
	 *            DM���ݿ��û���
	 * @param password
	 *            DM���ݿ��Ӧusername������
	 * @param dbType
	 *            1:oracle,2:����
	 */
	public DBService(String url, String username, String password, int dbType) {
		if (dbType == 1) {
			new DBService(url, username, password);
		} else {
			flag = 1;
			this.url = url;
			this.username = username;
			this.password = password;

			try {
				Class.forName("dm.jdbc.driver.DmDriver").newInstance();
				CallSuport.println("url:", url, debug);
				CallSuport.println("username", username, debug);
				m_connection = DriverManager.getConnection(url, username,
						password);
				m_statement = m_connection.createStatement();
				this.dbType = "dm";
			} catch (ClassNotFoundException e) {
				CallSuport.println(e.getMessage());
				e.printStackTrace();
			} catch (SQLException e1) {
				CallSuport.println(e1.getErrorCode() + ":" + e1.getMessage());
				e1.printStackTrace();

			} catch (InstantiationException e) {
				CallSuport.println(e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				CallSuport.println(e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * DBService���캯����������SyBase���ݿ��jdbc����
	 * @param ip
	 * Sybase���ݿ�ip��ַ
	 * @param port
	 * Sybase���ݿ�˿ں�
	 * @param username
	 * Sybase���ݿ��û���
	 * @param password
	 * ��Ӧusername������
	 */
	public DBService(String ip, String port, String username, String password) {
		flag = 4;
		try {
			Class.forName("com.sybase.jdbc2.jdbc.SybDriver").newInstance();
			String url = "jdbc:sybase:Tds:" + ip + ":" + port;
			m_connection = DriverManager.getConnection(url, username, password);
			m_statement = m_connection.createStatement();
			dbType = "sybase";
		} catch (ClassNotFoundException e) {
			CallSuport.println(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			e.printStackTrace();

		} catch (InstantiationException e) {
			CallSuport.println(e.getMessage());

			e.printStackTrace();

		} catch (IllegalAccessException e) {
			CallSuport.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * DBService���캯����ͨ��ODBC���������ݿ��jdbc����
	 * @param datasource
	 * ODBC���ݿ���
	 */
	public DBService(String datasource) {
		flag = 4;
		Class c;
		try {
			c = Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			DriverManager.registerDriver((Driver) c.newInstance());
			String[] dbinfo = datasource.split(";");
			String url = "jdbc:odbc:" + dbinfo[0];
			m_connection = DriverManager.getConnection(url, dbinfo[1],
					dbinfo[2]);
			m_statement = m_connection.createStatement();
			dbType = "odbc";
		} catch (ClassNotFoundException e) {
			CallSuport.println(e.getMessage());
		} catch (SQLException e1) {
			CallSuport.println(e1.getErrorCode() + ":" + e1.getMessage());

		} catch (InstantiationException e) {
			CallSuport.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			CallSuport.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * DBService���캯����������Oracle���ݿ��thinģʽ����
	 * @param sid
	 *  ���ݿ�Ӧ�÷�����
	 * @param ip
	 * ���ݿ������IP��ַ
	 * @param port
	 * ���ݿ�������˿ں�
	 * @param username
	 * Oracle���ݿ��û���
	 * @param password
	 * Oracle���ݿ��Ӧusername������
	 */
	public DBService(String sid, String ip, String port, String username,
			String password) {
		flag = 2;
		this.url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid;
		this.username = username;
		this.password = password;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid;
			m_connection = DriverManager.getConnection(url, username, password);
			m_statement = m_connection.createStatement();
			dbType = "oracle";
		} catch (ClassNotFoundException e) {
			CallSuport.println(e.getMessage());
		} catch (SQLException e1) {
			CallSuport.println(e1.getErrorCode() + ":" + e1.getMessage());

		} catch (InstantiationException e2) {
			CallSuport.println(e2.getMessage() + e2.getCause());
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			CallSuport.println(e2.getMessage() + e2.getCause());
			e2.printStackTrace();
		}

	}

	/**
	 * DBService���캯�������������ݿ��jdbc����
	 * @param conn
	 * ���ݿ�����
	 */
	public DBService(Connection conn) {
		flag = 3;
		this.m_connection = conn;
	}

	/**
	 * �������ݿ���Ϣ����DBServiceʵ��������Oralce,Sybase,SqlServer,odbc
	 * @param 

	public void init(String url, String username, String password) {
		flag = 1;
		this.url = "jdbc:oracle:oci8:@" + url;
		this.username = username;
		this.password = password;
		try {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			} catch (InstantiationException e2) {
				CallSuport.println(e2.getMessage() + e2.getCause());
				e2.printStackTrace();
			} catch (IllegalAccessException e2) {
				CallSuport.println(e2.getMessage() + e2.getCause());
				e2.printStackTrace();
			}
			url = "jdbc:oracle:oci8:@" + url;
			m_connection = DriverManager.getConnection(url, username, password);
			m_statement = m_connection.createStatement();
		} catch (ClassNotFoundException e) {
			CallSuport.println(e.getMessage());
		} catch (SQLException e1) {
			CallSuport.println(e1.getErrorCode() + ":" + e1.getMessage());

		}
	}

	public void init(String sid, String ip, String port, String username,
			String password) {
		flag = 2;
		this.url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid;
		this.username = username;
		this.password = password;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid;
			m_connection = DriverManager.getConnection(url, username, password);
			m_statement = m_connection.createStatement();
		} catch (ClassNotFoundException e) {
			CallSuport.println(e.getMessage());
		} catch (SQLException e1) {
			CallSuport.println(e1.getErrorCode() + ":" + e1.getMessage());
		} catch (InstantiationException e2) {
			CallSuport.println(e2.getMessage() + e2.getCause());
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			CallSuport.println(e2.getMessage() + e2.getCause());
			e2.printStackTrace();
		}

	}

	/**
	 * �������ӵ�Connetctionʵ��
	 * @return
	 * �������ӵ�ʵ��
	 */
	public Connection getConnection() {
		return m_connection;
	}

	/**
	 * ����Oracle Oci���ݿ�����
	 * @param url
	 * ������
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public Connection createConnection(String url, String username,
			String password) throws SQLException {
		url = "jdbc:oracle:oci8:@" + url;
		return DriverManager.getConnection(url, username, password);
	}

	/**
	 * ����Oracle thin���ݿ�����
	 * @param servname
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public Connection createConnection(String servname, String ip, String port,
			String username, String password) throws SQLException {
		String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + servname;
		return DriverManager.getConnection(url, username, password);
	}

	//	public Co
	/**
	 * �������ӵ�Statementʵ��
	 * @return
	 * �������ӵ�Statementʵ��
	 */
	public Statement getStatement() {
		if (m_statement == null) {
			m_statement = createStatement();
		}
		return m_statement;
	}

	/**
	 * �½�Statementʵ��
	 * @return
	 * �½���Statemengʵ��
	 */
	public Statement createStatement() {
		try {
			return m_connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	public void setStatement(Statement statement) {
		try {
			if (m_statement != null) {
				m_statement.close();
			}
			m_statement = statement;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���������SQL���ִ�в���������ִ�н����֧��Select��ѯ���
	 * @param sql
	 * �����SQL���
	 * @return
	 * ����ִ�н����ResultSet���ͣ�ִ�в��ɹ�����null��
	 */
	public ResultSet executeQuery(String sql) {
		ResultSet resultset = null;
		try {
			Statement stmt = getStatement();
			resultset = stmt.executeQuery(sql);

		} catch (SQLException e) {
			printSqlException(e);
		}
		return resultset;
	}

	public ResultSet executeQueryAll(String sql) {
		String fromwhere = sql.split("from")[1];
		ResultSet result = null;
		String column = "";
		try {
			ResultSet desc = getStatement().executeQuery(sql);
			int columnCount = desc.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				column += desc.getMetaData().getColumnName(i) + ",";
			}
			desc.close();
			column += "rowid";
			result = executeQuery("select " + column + " from " + fromwhere);
		} catch (SQLException e) {
			printSqlException(e);
			return null;
		}
		return result;
	}

	/**
	 * ���������SQL���ִ�в���������ִ�н����֧��Select��ѯ���
	 * @param sql
	 * �����SQL���
	 * @return
	 * ����ִ�н����Vector���ͣ�ִ�в��ɹ�����null��
	 */
	public Vector executeQueryV(String sql) {
		try {
			ResultSet resultset = getStatement().executeQuery(sql);
			Vector result = getRSData(resultset);
			resultset.close();
			return result;
		} catch (SQLException e) {
			printSqlException(e);
			return null;
		}
	}

	/**
	 * ���������SQL���ִ�в���������ִ�н����֧��Select��ѯ���
	 * @param sql
	 * �����SQL���
	 * @return
	 * ����ִ�н����ArrayList���ͣ�ִ�в��ɹ�����null��
	 */
	public ArrayList executeQueryA(String sql) {
		Statement stmt = null;
		ResultSet resultset = null;
		ArrayList result = null;
		try {
			stmt = getStatement();
			resultset = stmt.executeQuery(sql);
			result = getRSDataA(resultset);

		} catch (SQLException e) {
			printSqlException(e);

		} finally {
			if (resultset != null) {
				try {
					resultset.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 2006-08-31
	 * ͨ����ѯ����ñ���
	 * @param sql String
	 * @return ArrayList
	 */
	public ArrayList getQueryTable(String sql) {
		ArrayList temp = new ArrayList();
		String tableStr = "";
		sql = sql.toUpperCase();
		int fromIndex = sql.indexOf("FROM");
		int whereIndex = sql.indexOf("WHERE");
		int orgerIndex = sql.indexOf("ORDER");

		if (fromIndex != -1 && whereIndex != -1) {
			tableStr = sql.substring(fromIndex + 4, whereIndex);
		} else if (fromIndex != -1 && orgerIndex != -1) {
			tableStr = sql.substring(fromIndex + 4, orgerIndex);
		} else {
			tableStr = sql.substring(fromIndex + 4);
		}
		tableStr = tableStr.trim();
		String[] tableArray = tableStr.split(",");
		for (int i = 0; i < tableArray.length; i++) {
			String schema_table_alias = tableArray[i].trim();

			String schema_table = schema_table_alias.split(" ")[0].trim();

			String schema = "";
			String table = "";
			String alias = "";

			if (schema_table.split("[.]").length > 1) {
				schema = schema_table.split("[.]")[0];
				table = schema_table.split("[.]")[1];

			} else {
				table = schema_table;
			}

			if (schema_table_alias.split(" ").length > 1) {
				alias = schema_table_alias.split(" ")[1].trim();
			}

			schema = StringConvert.removeColon(schema);
			table = StringConvert.removeColon(table);
			alias = StringConvert.removeColon(alias);
			DBTable _dbtable = new DBTable();
			_dbtable.setSchema(schema);
			_dbtable.setTable(table);
			_dbtable.setAlias(alias);
			temp.add(_dbtable);
		}
		return temp;
	}

	/**
	 * 2006-08-31
	 * ͨ����ѯ���������
	 * @param sql String
	 * @return ArrayList
	 */
	public ArrayList getQueryCol(String sql) {
		ArrayList temp = new ArrayList();
		String columnStr = "";
		sql = sql.toUpperCase();
		int selectIndex = sql.indexOf("SELECT");
		int fromIndex = sql.indexOf("FROM");
		if (fromIndex != -1 && selectIndex != -1) {
			columnStr = sql.substring(selectIndex + 6, fromIndex);
		}
		columnStr = columnStr.trim();

		String[] columnArray = columnStr.split(",");
		for (int i = 0; i < columnArray.length; i++) {
			String table_column = columnArray[i].trim();
			String table = "";
			String column = "";
			if (table_column.split("[.]").length > 1) {
				table = table_column.split("[.]")[0];
				column = table_column.split("[.]")[1];
			} else {
				column = table_column;
			}
			table = StringConvert.removeColon(table);
			column = StringConvert.removeColon(column);

			DBColumn _dbcolumn = new DBColumn();
			_dbcolumn.setTable(table);
			_dbcolumn.setColumn(column);
			temp.add(_dbcolumn);
		}
		return temp;
	}

	/**
	 * 2006-08-31
	 * ȥ������
	 * @param tableList ArrayList
	 * @param columnList ArrayList
	 */
	public void removeColumnAlias(ArrayList tableList, ArrayList columnList) {

		for (int i = 0; i < columnList.size(); i++) {
			DBColumn _dbcolumn = (DBColumn) columnList.get(i);
			String table_perhaps_alias = _dbcolumn.getTable();
			if (!table_perhaps_alias.equals("")) {
				for (int j = 0; j < tableList.size(); j++) {
					DBTable _dbtable = (DBTable) tableList.get(j);
					String alias = _dbtable.getAlias();
					if (table_perhaps_alias.equals(alias)) {
						_dbcolumn.setSchema(_dbtable.getSchema());
						_dbcolumn.setTable(_dbtable.getTable());
					}
				}
			}
		}

	}

	/**
	 * ͨ��������ѯ���ݿ�Ԫ���ݻ������
	 * @param tableList ArrayList
	 * @return Hashtable
	 */
	public Hashtable getQueryColumns(ArrayList tableList) {
		Hashtable dbcolumn = new Hashtable();
		try {
			DatabaseMetaData metaData = getConnection().getMetaData();

			for (int i = 0; i < tableList.size(); i++) {

				ArrayList tableColumnNames = new ArrayList();
				DBTable dbTable = (DBTable) tableList.get(i);
				String schema = dbTable.getSchema().trim().toUpperCase();
				String table = dbTable.getTable().trim().toUpperCase();
				ResultSet columnMeta = metaData.getColumns("", schema, table,
						"%");
				while (columnMeta.next()) {
					Object columnNameObj = columnMeta.getObject(4);
					tableColumnNames
							.add(columnNameObj.toString().toUpperCase());

				}
				dbcolumn.put(schema + "." + table, tableColumnNames);
			}
			return dbcolumn;
		} catch (SQLException e) {
			printSqlException(e);
			return dbcolumn;
		}

	}

	/**
	 * 2006-08-23 ʹ�ù�������ѯ���
	 * @param sql String  ��ѯ���
	 * @param filterList ArrayList  �������б� ÿһ��Ԫ��ΪDataFilter
	 * @return ArrayList
	 */
	public ArrayList executeQueryA(String sql, DataFilters filters) {

		long start = System.currentTimeMillis();

		ArrayList returnArray = new ArrayList();
		ArrayList tableList = this.getQueryTable(sql);
		ArrayList columnList = this.getQueryCol(sql);
		removeColumnAlias(tableList, columnList);
		filters.checkFiltersStep1(tableList, columnList);
		if (!filters.isAllValid()) {
			Hashtable dbcolumn = getQueryColumns(tableList);
			filters.checkFiltersStep2(dbcolumn);
			sql = filters.getSqlIncludeFilters(sql);
		}

		long pre_query = System.currentTimeMillis();
		//System.err.println("pre_query:" + (pre_query - start) + " ms ");

		ArrayList result = executeQueryA(sql);

		long query = System.currentTimeMillis();
		//System.err.println("query:" + (query - pre_query) + " ms ");

		for (int i = 0; i < result.size(); i++) {
			ArrayList record = (ArrayList) result.get(i);
			//��������
			if (filters.include(record)) {
				ArrayList filterRecord = new ArrayList();
				for (int j = 0; j < columnList.size(); j++) {
					filterRecord.add(record.get(j));
				}
				returnArray.add(filterRecord);
			}
		}

		long end = System.currentTimeMillis();
		//System.err.println("after:" + (end - query) + " ms ");

		return returnArray;
	}

	/**
	 *ִ��Oracle���ݿ��еĴ洢����
	 * @param name
	 * �洢���̵�����
	 * @param para
	 *���������Vector�͡����ݴ洢���̵Ķ������ΰ�������������Ϣ��û����Ϊnull��in:"1"��out:"2"��in/out:"3"
	 * <p>���磬�洢���� <i>procedure</i> ( <i>para1</i> in number,<i>para2</i> out varchar2, <i>para3</i> in out number)</p>
	 * <blockquote><p>Vector <i>para</i> = new Vector();</p></blockquote>
	 * <blockquote><p>Vector <i>para1</i> = new Vector();   <i>para1</i>.add("1");   <i>para1</i>.add ( new Integer(<i>number</i>));</p></blockquote>
	 * <blockquote><p><i>para</i>.add ( <i>para1</i> );</p></blockquote>
	 * <blockquote><p>Vector <i>para2</i> = new Vector();   <i>para2</i>.add("2");   <i>para2</i>.add ( new Integer( Types.<i>VARCHAR</i>));</p></blockquote>
	 * <blockquote><p><i>para</i>.add ( <i>para2</i>);</p></blockquote>
	 * <blockquote><p>Vector <i>para3</i> = new Vector();   <i>para3</i>.add("3");   <i>para3</i>.add ( new Integer(<i>number</i>)); <i>para3</i>.add ( new Integer( Types.<i>NUMBER</i>));</p></blockquote>
	 * <blockquote><p<i>para</i>.add ( <i>para3</i>);</p></blockquote>
	 * <blockquote><p>db.callProcedure ( "<i>procedure</i>", <i>para</i>);</p></blockquote>
	 * @see java.sql.Types
	 * @see java.sql.CallableStatement
	 * @return
	 * ��������out����������Vector�С�
	 */
	public Vector callProcedure(String name, Vector para) {
		CallableStatement cs = null;
		Vector result = new Vector();
		String call;
		Vector outFlag = new Vector();
		try {
			if (para == null) {
				call = "{call " + name + "}";
				cs = m_connection.prepareCall(call);
			} else {
				call = "{call " + name + "(?";
				for (int i = 1; i < para.size(); i++) {
					call = call + ",?";
				}
				call = call + ")}";
				cs = m_connection.prepareCall(call);
				for (int i = 1; i <= para.size(); i++) {
					Vector p = (Vector) para.elementAt(i - 1);
					if (p.elementAt(0).toString().equals("1")) {
						cs.setObject(i, p.elementAt(1));
					} else if (p.elementAt(0).toString().equals("2")) {
						cs.registerOutParameter(i, ((Integer) p.elementAt(1))
								.intValue());
						outFlag.add(new Integer(i));
					} else if (p.elementAt(0).toString().equals("3")) {
						cs.setObject(i, p.elementAt(1));
						cs.registerOutParameter(i, ((Integer) p.elementAt(2))
								.intValue());
						outFlag.add(new Integer(i));
					} else {
						CallSuport.println("������������ϸ�ʽҪ��");
						return null;
					}
				}
			}
			cs.execute();
			if (outFlag != null) {
				for (int i = 1; i <= outFlag.size(); i++) {
					result.add(cs
							.getObject(((Integer) outFlag.elementAt(i - 1))
									.intValue()));
				}
			}
		} catch (SQLException e) {
			printSqlException(e);
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 *ִ��Oracle���ݿ��еĴ洢����
	 * @param name
	 * �洢���̵�����
	 * @param para
	 *���������Object[][]�͡����ݴ洢���̵Ķ������ΰ�������������Ϣ��û����Ϊnull��in:"1"��out:"2"��in/out:"3"
	 * <p>���磬�洢���� <i>procedure</i> ( <i>para1</i> in number,<i>para2</i> out varchar2, <i>para3</i> in out number)</p>
	 * <blockquote><p>Object <i>para</i> = { {"1", <i>number</i> }, {"2", new Integer( Types.<i>VARCHAR</i> )}, </blockquote></p>
	 * <blockquote><p>{"3", new Integer(<i>number</i>), new Integer( Types.<i>NUMBER</i> )},};</blockquote></p>
	 * <blockquote><p>db.callProcedure ( "<i>procedure</i>", <i>para</i>);</p></blockquote>
	 * @see java.sql.Types
	 * @see java.sql.CallableStatement
	 * @return
	 * ��������out����������Vector�С�
	 */
	public Vector callProcedure(String name, Object[][] para) {
		CallableStatement cs = null;
		Vector result = new Vector();
		String call;
		Vector outFlag = new Vector();
		try {
			if (para == null) {
				call = "{call " + name + "}";
				cs = m_connection.prepareCall(call);
			} else {
				call = "{call " + name + "(?";
				for (int i = 1; i < para.length; i++) {
					call = call + ",?";
				}
				call = call + ")}";
				cs = m_connection.prepareCall(call);
				for (int i = 1; i <= para.length; i++) {
					if (para[i - 1][0].equals("1")) {
						cs.setObject(i, para[i - 1][1]);
					} else if (para[i - 1][0].equals("2")) {
						cs.registerOutParameter(i, ((Integer) para[i - 1][1])
								.intValue());
						outFlag.add(new Integer(i));
					} else if (para[i - 1][0].equals("3")) {
						cs.setObject(i, para[i - 1][1]);
						cs.registerOutParameter(i, ((Integer) para[i - 1][2])
								.intValue());
						outFlag.add(new Integer(i));
					} else {
						CallSuport.println("������������ϸ�ʽҪ��");
						return null;
					}
				}
			}
			cs.execute();
			if (outFlag != null) {
				for (int i = 1; i <= outFlag.size(); i++) {
					result.add(cs
							.getObject(((Integer) outFlag.elementAt(i - 1))
									.intValue()));
				}
			}
		} catch (SQLException e) {
			printSqlException(e);
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * ִ��Oracle���ݿ��еĺ���
	 * @param name
	 * �洢���̵�����
	 * @param type
	 * ����ֵ���ͣ�������
	 * @param para
	 * ���������Vector�͡����ݴ洢���̵Ķ������ΰ�������������Ϣ��û����Ϊnull��in:"1"��out:"2"��in/out:"3"
	 * ʹ�÷�����callProcedure���ƣ�
	 * @see java.sql.Types
	 * @see java.sql.CallableStatement
	 * @see #callProcedure
	 * @return
	 * ��������out����������Vector�У����е�һ��Ϊ����ֵ��
	 */
	public Vector callFunction(String name, int type, Vector para) {
		CallableStatement cs = null;
		Vector result = new Vector();
		String call;
		Vector outFlag = new Vector();
		try {
			if (para == null) {
				call = "{ ? = call " + name + "}";
				cs = m_connection.prepareCall(call);
			} else {
				call = "{ ? = call " + name + "(?";
				for (int i = 1; i < para.size(); i++) {
					call = call + ",?";
				}
				call = call + ")}";
				cs = m_connection.prepareCall(call);
				cs.registerOutParameter(1, type);
				for (int i = 1; i <= para.size(); i++) {
					Vector p = (Vector) para.elementAt(i - 1);
					if (p.elementAt(0).toString().equals("1")) {
						cs.setObject(i + 1, p.elementAt(1));
					} else if (p.elementAt(0).toString().equals("2")) {
						cs.registerOutParameter(i + 1, ((Integer) p
								.elementAt(1)).intValue());
						outFlag.add(new Integer(i + 1));
					} else if (p.elementAt(0).toString().equals("3")) {
						cs.setObject(i + 1, p.elementAt(1));
						cs.registerOutParameter(i + 1, ((Integer) p
								.elementAt(2)).intValue());
						outFlag.add(new Integer(i + 1));
					} else {
						CallSuport.println("������������ϸ�ʽҪ��");
						return null;
					}
				}
			}
			cs.execute();
			result.add(cs.getObject(1));
			if (outFlag != null) {
				for (int i = 0; i < outFlag.size(); i++) {
					result.add(cs.getObject(((Integer) outFlag.elementAt(i))
							.intValue()));
				}
			}
		} catch (SQLException e) {
			printSqlException(e);
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 *ִ��Oracle���ݿ��еĺ���
	 * @param name
	 * �洢���̵�����
	 * @param type
	 * ����ֵ���ͣ�������
	 * @param para
	 * ���������Object[][]�͡����ݴ洢���̵Ķ������ΰ�������������Ϣ��û����Ϊnull��in:"1"��out:"2"��in/out:"3"
	 * ʹ�÷�����callProcedure���ƣ�
	 * @see java.sql.Types
	 * @see java.sql.CallableStatement
	 * @see #callProcedure(String name,Object[][] para)
	 * @return
	 * ��������out����������Vector�У����е�һ��Ϊ����ֵ��
	 */
	public Vector callFunction(String name, int type, Object[][] para) {
		CallableStatement cs = null;
		Vector result = new Vector();
		String call;
		Vector outFlag = new Vector();
		try {
			if (para == null) {
				call = "{ ? = call " + name + "}";
				cs = m_connection.prepareCall(call);
			} else {
				call = "{ ? = call " + name + "(?";
				for (int i = 1; i < para.length; i++) {
					call = call + ",?";
				}
				call = call + ")}";
				cs = m_connection.prepareCall(call);
				cs.registerOutParameter(1, type);
				for (int i = 1; i <= para.length; i++) {
					if (para[i - 1][0].equals("1")) {
						cs.setObject(i + 1, para[i - 1][1]);
					} else if (para[i - 1][0].equals("2")) {
						cs.registerOutParameter(i + 1,
								((Integer) para[i - 1][1]).intValue());
						outFlag.add(new Integer(i + 1));
					} else if (para[i - 1][0].equals("3")) {
						cs.setObject(i + 1, para[i - 1][1]);
						cs.registerOutParameter(i + 1,
								((Integer) para[i - 1][2]).intValue());
						outFlag.add(new Integer(i + 1));
					} else {
						CallSuport.println("������������ϸ�ʽҪ��");
						return null;
					}
				}
			}
			cs.execute();
			result.add(cs.getObject(1));
			if (outFlag != null) {
				for (int i = 0; i < outFlag.size(); i++) {
					result.add(cs.getObject(((Integer) outFlag.elementAt(i))
							.intValue()));
				}
			}
		} catch (SQLException e) {
			printSqlException(e);
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * ���������SQL���ִ�в���������ִ�н����֧��update��create��drop��insert���޸����
	 * @param sql
	 * �����SQL���
	 * @return
	 * ����ִ�н����int���ͣ���ʾ���β�����Ӱ������������ִ�в��ɹ��򷵻�-1��
	 */
	public int executeUpdate(String sql) {
//		CallSuport.printFunction(this.getClass() + ".executeUpdate()", debug);
		try {
			CallSuport.println("�ж������Ƿ�Ϊ��", getStatement() + "", debug);
			int result = getStatement().executeUpdate(sql);
			return result;
		} catch (SQLException e) {
			printSqlException(e);
			return -1;
		}
	}

	/**
	 * ��ResultSetת��ΪVector
	 * @param rs
	 * �����ResultSet���ʵ��
	 * @return
	 * ���ؾ���ת����Vector
	 */
	public static Vector getRSData(ResultSet rs) {
		Vector rsVector = new Vector();
		int columnCount;
		try {
			columnCount = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				Vector row = new Vector();

				for (int i = 1; i <= columnCount; i++) {
					row.add(rs.getObject(i));
				}
				rsVector.add(row);
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}

		return rsVector;
	}

	/**
	 * ��ResultSetת��ΪArrayList
	 * @param rs
	 * �����ResultSet���ʵ��
	 * @return
	 * ���ؾ���ת����ArrayList
	 */
	public static ArrayList getRSDataA(ResultSet rs) {
		ArrayList rsArrayList = new ArrayList();
		int columnCount;
		try {
			columnCount = rs.getMetaData().getColumnCount();
			//		rs.beforeFirst();
			while (rs.next()) {
				ArrayList row = new ArrayList();
				for (int i = 1; i <= columnCount; i++) {
					if (rs.getObject(i) != null
							&& (rs.getObject(i) instanceof String)) {
						row.add(rs.getObject(i).toString().trim());
					} else {
						row.add(rs.getObject(i));
					}
				}
				rsArrayList.add(row);
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			e.printStackTrace();
			return null;
		}

		return rsArrayList;
	}

	/**
	 * ���ݰ���ROWID�Ľ����rs�õ�����ROWID��Vector
	 * @param rs
	 * ����ROWID�Ľ����
	 * @return
	 * ����ROWID��Vector
	 */
	public static Vector getRSDataAll(ResultSet rs) {
		Vector rsVector = new Vector();
		int columnCount;
		try {
			columnCount = rs.getMetaData().getColumnCount();
			//    rs.beforeFirst();
			while (rs.next()) {
				Vector row = new Vector();

				for (int i = 1; i <= columnCount - 1; i++) {
					row.add(rs.getObject(i));
				}
				row.add(((oracle.sql.ROWID) rs.getObject(columnCount))
						.stringValue());
				rsVector.add(row);
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}

		return rsVector;
	}

	/**
	 * ���ݰ���ROWID�Ľ����rs�õ�����ROWID��ArrayList
	 * @param rs
	 * ����ROWID�Ľ����
	 * @return
	 * ����ROWID��ArrayList
	 */
	public static ArrayList getRSDataAllA(ResultSet rs) {
		ArrayList rsArrayList = new ArrayList();
		int columnCount;
		try {
			columnCount = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				ArrayList row = new ArrayList();

				for (int i = 1; i <= columnCount - 1; i++) {
					row.add(rs.getObject(i));
				}
				row.add(((oracle.sql.ROWID) rs.getObject(columnCount))
						.stringValue());
				rsArrayList.add(row);
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}

		return rsArrayList;
	}

	/**
	 * ����JDBC��Oracle���ӵĶ���m_connetion���Զ��ύ��ʽ
	 * @param autoCommit
	 * �Զ��ύ��ʽ��trueΪ�Զ��ύ��falseΪ���Զ��ύ
	 */
	public void setAutoCommit(boolean autoCommit) {
		try {
			m_connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
		}
	}

	public void commit() {
		try {
			m_connection.commit();
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
		}
	}

	public void rollback() {
		try {
			m_connection.rollback();
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * ִ�г������£�����ͬʱִ�ж���SQL��䣨������select��䣩
	 * @param sql
	 * ��������SQL��䣨������select��䣩��Vector����Vector��ÿ��Ԫ��Ϊһ��SQL��䡣
	 * @param autoCommit
	 * autoCommit�������ó������������ύ��ʽ��trueΪ�Զ��ύ��falseΪ���Զ��ύ��
	 * �Զ��ύģʽ��������������е�ĳ��ִ��ʧ��ʱ�������������ǰ������SQL����ִ��Ч����
	 * ���Ϊ���Զ��ύ���򽫳������������������������� ĳ�����ִ��ʧ�ܣ����˻ص����������������ִ��ǰ��״̬
	 * @return
	 * ����ֵΪ�������飬��ʾÿ�������Ӱ���������
	 */
	public int[] batchExecute(Vector sql, boolean autoCommit) {
		int[] result = null;
		try {
			boolean originCommit = m_connection.getAutoCommit();
			m_connection.setAutoCommit(autoCommit);
			for (int i = 0; i < sql.size(); i++) {
				m_statement.addBatch(sql.elementAt(i).toString());
			}
			result = m_statement.executeBatch();
			if (autoCommit == false) {
				m_connection.commit();
			}
			m_connection.setAutoCommit(originCommit);
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
		}
		return result;
	}

	/**
	 * �ر�����
	 * @return
	 * ����ִ�н�����ɹ�����1�����ɹ����أ�1��
	 */
	public int closeConn() {

		try {
			if (m_statement != null) {
				m_statement.close();
			}
			if (m_connection != null && (!m_connection.isClosed())) {
				m_connection.close();
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return -1;
		}
		return 1;
	}

	public int closeStatement() {
		try {
			if (m_statement != null) {
				m_statement.close();
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return -1;
		}
		return 1;
	}

	/**
	 *�õ������ResultSet�������
	 * @param rs
	 * �����
	 * @return
	 * �������������Vector�͡�
	 */
	public static Vector getRSColumnName(ResultSet rs) {
		Vector columnName = new Vector();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnName.add(rs.getMetaData().getColumnName(i));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnName;
	}

	/**
	 *�õ������ResultSet�������
	 * @param rs
	 * �����
	 * @return
	 * �������������ArrayList�͡�
	 */
	public static ArrayList getRSColumnNameA(ResultSet rs) {
		ArrayList columnName = new ArrayList();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String cName = rs.getMetaData().getColumnName(i);
				cName = cName.toUpperCase();
				columnName.add(cName);
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnName;
	}

	/**
	 *�õ������ResultSet�������
	 * @param rs
	 * �����
	 * @return
	 * �������������Vector�͡�
	 */
	public Vector getRSColumnChName(ResultSet rs, String table) {
		Vector columnName = getRSColumnName(rs);
		Vector columnChName = new Vector();

		try {
			int columnCount = columnName.size();
			for (int i = 0; i < columnCount; i++) {
				String enName = columnName.elementAt(i).toString();
				String chName;
				ResultSet rs1 = getStatement().executeQuery(
						"select COLUMN_DESC1 from idp.mgr_column where COLUMN_NAME=\'"
								+ enName + "\' and TABLE_NAME=\'"
								+ table.toUpperCase() + "\'");
				if (rs1.next() != true) {
					chName = enName;
				} else {
					chName = rs1.getString(1);
				}
				columnChName.add(chName);
				rs1.close();
			}
		} catch (SQLException e) {
			printSqlException(e);
			return null;
		}
		return columnChName;
	}

	/**
	 * �õ���������е�SQL��������
	 * @param rs
	 * �����
	 * @return
	 * ���������������ͣ�Integer�ࣩ��Vector�������Ӧ��ϵ�ο�java.sql.Types��
	 * @see java.sql.Types
	 * @see java.sql.ResultSetMetaData#getColumnType
	 */
	public static Vector getRSColumnType(ResultSet rs) {
		Vector columnType = new Vector();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnType.add(new Integer(rs.getMetaData().getColumnType(i)));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnType;
	}

	/**
	 * �õ���������е�SQL��������
	 * @param rs
	 * �����
	 * @return
	 * ���������������ͣ�Integer�ࣩ��ArrayList�������Ӧ��ϵ�ο�java.sql.Types��
	 * @see java.sql.Types
	 * @see java.sql.ResultSetMetaData#getColumnType
	 */
	public static ArrayList getRSColumnTypeA(ResultSet rs) {
		ArrayList columnType = new ArrayList();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnType.add(new Integer(rs.getMetaData().getColumnType(i)));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnType;
	}

	/**
	 * �õ���������е�SQL����������
	 * @param rs
	 * �����
	 * @return
	 * ��������SQL������������String����Vector��
	 * @see java.sql.Types
	 * @see java.sql.ResultSetMetaData#getColumnTypeName
	 */
	public static Vector getRSColumnTypeName(ResultSet rs) {
		Vector columnTypeName = new Vector();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnTypeName.add(rs.getMetaData().getColumnTypeName(i));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnTypeName;
	}

	/**
	 * �õ���������е�SQL����������
	 * @param rs
	 * �����
	 * @return
	 * ��������SQL������������String����ArrayList��
	 * @see java.sql.Types
	 * @see java.sql.ResultSetMetaData#getColumnTypeName
	 */
	public static ArrayList getRSColumnTypeNameA(ResultSet rs) {
		ArrayList columnTypeName = new ArrayList();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnTypeName.add(rs.getMetaData().getColumnTypeName(i));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnTypeName;
	}

	/**
	 * �õ���������е������ʾ����
	 * @param rs
	 * �����
	 * @return
	 * �������������ʾ���ȣ�Integer�ࣩ��Vector��
	 * @see java.sql.ResultSetMetaData#getColumnDisplaySize
	 */
	public static Vector getRSColumnLength(ResultSet rs) {
		Vector columnLength = new Vector();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnLength.add(new Integer(rs.getMetaData()
						.getColumnDisplaySize(i)));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnLength;
	}

	/**
	 * �õ���������е������ʾ����
	 * @param rs
	 * �����
	 * @return
	 * �������������ʾ���ȣ�Integer�ࣩ��ArrayList��
	 * @see java.sql.ResultSetMetaData#getColumnDisplaySize
	 */
	public static ArrayList getRSColumnLengthA(ResultSet rs) {
		ArrayList columnLength = new ArrayList();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnLength.add(new Integer(rs.getMetaData()
						.getColumnDisplaySize(i)));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnLength;
	}

	/**
	 * �õ���������е�λ������Ϊ�ַ�������Ϊ�ַ�������
	 * @param rs
	 * �����
	 * @return
	 * ��������λ����С��Integer�ࣩ��Vector��
	 * @see java.sql.ResultSetMetaData#getPrecision
	 */
	public static Vector getRSColumnPrecision(ResultSet rs) {
		Vector columnPrecision = new Vector();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnPrecision.add(new Integer(rs.getMetaData()
						.getPrecision(i)));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnPrecision;
	}

	/**
	 * �õ���������е�λ������Ϊ�ַ�������Ϊ�ַ�������
	 * @param rs
	 * �����
	 * @return
	 * ��������λ����С��Integer�ࣩ��ArrayList��
	 * @see java.sql.ResultSetMetaData#getPrecision
	 */
	public static ArrayList getRSColumnPrecisionA(ResultSet rs) {
		ArrayList columnPrecision = new ArrayList();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnPrecision.add(new Integer(rs.getMetaData()
						.getPrecision(i)));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnPrecision;
	}

	/**
	 * �õ���������е��Ƿ��Ϊ�գ�
	 * @param rs
	 * �����
	 * @return
	 * ���������Ƿ��Ϊ����Ϣ��Integer�ࣩ��Vector����Ϊ��0����Ϊ��1����֪��2��
	 * @see java.sql.ResultSetMetaData#isNullable
	 */
	public static Vector getRSColumnIsNullable(ResultSet rs) {
		Vector columnPrecision = new Vector();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnPrecision
						.add(new Integer(rs.getMetaData().isNullable(i)));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnPrecision;
	}

	/**
	 * �õ���������е��Ƿ��Ϊ�գ�
	 * @param rs
	 * �����
	 * @return
	 * ���������Ƿ��Ϊ����Ϣ��Integer�ࣩ��ArrayList����Ϊ��0����Ϊ��1����֪��2��
	 * @see java.sql.ResultSetMetaData#isNullable
	 */
	public static ArrayList getRSColumnIsNullableA(ResultSet rs) {
		ArrayList columnPrecision = new ArrayList();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnPrecision
						.add(new Integer(rs.getMetaData().isNullable(i)));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnPrecision;
	}

	/**
	 * �õ���������е�С��λ����Ϊ�ַ�������Ϊ0
	 * @param rs
	 * �����
	 * @return
	 * ��������С��λ��Integer�ࣩ��Vector��
	 * @see java.sql.ResultSetMetaData#getScale
	 */
	public static Vector getRSColumnScale(ResultSet rs) {
		Vector columnScale = new Vector();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnScale.add(new Integer(rs.getMetaData().getScale(i)));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnScale;
	}

	/**
	 * �õ���������е�С��λ����Ϊ�ַ�������Ϊ0
	 * @param rs
	 * �����
	 * @return
	 * ��������С��λ��Integer�ࣩ��ArrayList��
	 * @see java.sql.ResultSetMetaData#getScale
	 */
	public static ArrayList getRSColumnScaleA(ResultSet rs) {
		ArrayList columnScale = new ArrayList();
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnScale.add(new Integer(rs.getMetaData().getScale(i)));
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
			return null;
		}
		return columnScale;
	}

	/**
	 * ����Oracle�洢���̣�������
	 * @param procedure
	 * �����洢���̣���������SQL���
	 * <p>����:</p>
	 * <blockquote><p>     procedure =</p>
	 <p>   "CREATE OR REPLACE PROCEDURE myprocin(x VARCHAR) IS "</p>
	 <p>  + "BEGIN "</p>
	 <p> + "INSERT INTO oracle_table VALUES(x); "</p>
	 <p>  + "END;";</p></blockquote>
	 */
	public void createProcedure(String procedure) {
		try {
			getStatement().executeUpdate(procedure);
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + e.getMessage());
		}
	}

	public boolean isTableExist(String table) {
		ArrayList list = executeQueryA("select count(*) from user_tables where table_name=\'"
				+ table.toUpperCase() + "\'");
		if (Integer.parseInt(((ArrayList) (list.get(0))).get(0).toString()) == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �õ��ƶ���tablename���ֶ������ֶ�����
	 * @param conn
	 * ���ݿ�����
	 * @param tablename
	 * ����
	 * @return
	 * ������ֶ���Ϊkey�����ֶ�����Ϊ���ݵ�Hashtable
	 */
	public Hashtable getColumnInfo(Connection conn, String tablename) {
		Hashtable info = new Hashtable();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " + tablename
					+ " where 1=2");
			ResultSetMetaData meta = rs.getMetaData();
			int column = meta.getColumnCount();
			for (int i = 1; i <= column; i++) {
				info.put(meta.getColumnName(i).toUpperCase(), ""
						+ meta.getColumnType(i));
			}
			rs.close();
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + ":" + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e1) {
					CallSuport.println(e1.getErrorCode() + ":"
							+ e1.getMessage());
				}
			}
		}
		return info;
	}

	public ArrayList getColumnInfo(String tablename) {
		Statement stmt = null;
		ArrayList columnInfo = new ArrayList(2);
		String colTypeName;
		DBServiceColumnType colType = new DBServiceColumnType();
//		
		
		try {
			stmt = m_connection.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " + tablename
					+ " where 1=2");
			ResultSetMetaData meta = rs.getMetaData();
			int column = meta.getColumnCount();
			String[] columnName = new String[column];
			int[] columnType = new int[column];
			for (int i = 0; i < column; i++) {
				columnName[i] = "\"" + meta.getColumnName(i + 1).toUpperCase()
						+ "\"";

//				columnType[i] = meta.getColumnType(i + 1);
				colTypeName = meta.getColumnTypeName(i + 1);
				columnType[i] = colType.getColumnType(colTypeName);
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
				} catch (SQLException e1) {
					CallSuport.println(e1.getErrorCode() + ":"
							+ e1.getMessage());
				}
			}
		}
		return columnInfo;
	}

	//2005-11-02 �ֺ�����ӷ���
	/**
	 * @param sqlStr String  select ���
	 * @return ArrayList
	 */
	public ArrayList getColumnInfoFromSQL(String sqlStr) {
		Statement stmt = null;
		ArrayList columnInfo = new ArrayList(2);
		try {
			stmt = m_connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlStr);
			ResultSetMetaData meta = rs.getMetaData();
			int column = meta.getColumnCount();
			String[] columnName = new String[column];
			int[] columnType = new int[column];
			for (int i = 0; i < column; i++) {
				columnName[i] = meta.getColumnName(i + 1);
				columnType[i] = meta.getColumnType(i + 1);
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
				} catch (SQLException e1) {
					CallSuport.println(e1.getErrorCode() + ":"
							+ e1.getMessage());
				}
			}
		}
		return columnInfo;
	}

	public PreparedStatement createPrepareIns(String tableName,
			String[] columnName) throws SQLException {

		PreparedStatement preparedstatement;

		//�趨PrepareStatement�����ݡ�
		StringBuffer stringbuffer = new StringBuffer("insert into " + tableName
				+ " (");
		int i;
		for (i = 0; i < columnName.length; i++) {
			if (i != 0) {
				stringbuffer.append(",");
			}
			//2006-08-07
			if (dbType!=null&&dbType.equals("sqlserver")) {
				columnName[i] = "\"" + columnName[i].toUpperCase() + "\"";
			}
			stringbuffer.append(columnName[i]);
		}
		stringbuffer.append(") values (");
		for (int j = 0; j < i; j++) {
			if (j > 0) {
				stringbuffer.append(",");
			}
			stringbuffer.append("?");
		}
		stringbuffer.append(")");
		preparedstatement = m_connection.prepareStatement(stringbuffer
				.toString());
        //System.out.println(stringbuffer);
		return preparedstatement;
	}
	
    public PreparedStatement createPrepareIns(
            String tableName,
            String[] columnName,
            String autoAscColumnName,
            String sequenceSchema,
            String sequenceName) throws SQLException {

        PreparedStatement preparedstatement;

        //�趨PrepareStatement�����ݡ�
        StringBuffer stringbuffer =
                new StringBuffer("insert into " + tableName + " (");
        int i;

        stringbuffer.append("\"" + autoAscColumnName + "\"");

        for (i = 0; i < columnName.length; i++) {

            stringbuffer.append(",");

            //2006-08-07 ��Ӻ���֧��
            columnName[i] = "\"" + columnName[i].toUpperCase() + "\"";
            stringbuffer.append(columnName[i]);
        }

        stringbuffer.append(") values (");

        stringbuffer.append(sequenceSchema + "." + "\"" + sequenceName +
                            "\".nextval");

        for (int j = 0; j < i; j++) {

            stringbuffer.append(",");

            stringbuffer.append("?");
        }
        stringbuffer.append(")");
       // System.out.println(stringbuffer.toString());
        preparedstatement =m_connection.prepareStatement(stringbuffer.toString());

        return preparedstatement;
    }

	public PreparedStatement createPrepareUpd(String tableName,
			String[] columnName, String[] keyword) throws SQLException {
		PreparedStatement preparedstatement;
		Hashtable key = getHashKey(keyword);
		int setflag = 0;
		//�趨PrepareStatement�����ݡ�
		StringBuffer stringbuffer = new StringBuffer("update " + tableName
				+ " set ");
		int i;

		for (i = 0; i < columnName.length; i++) {
			if (key.get(columnName[i].toUpperCase()) == null) {
				if (setflag != 0) {
					stringbuffer.append(",");
				}
				stringbuffer.append(columnName[i].toUpperCase()
						+ "=?");
				setflag++;
			}
		}

		stringbuffer.append(" where ");
		for (int j = 0; j < keyword.length; j++) {
			if (j > 0) {
				stringbuffer.append(" and ");
			}
			stringbuffer.append("\"" + keyword[j].toUpperCase() + "\"" + "=?");
		}
		        CallSuport.println("update sql",stringbuffer.toString(),debug);
		    
		preparedstatement = m_connection.prepareStatement(stringbuffer.toString());

		return preparedstatement;
	}
	
    /**
     * 2007-01-25 LS
     * @param tableName String
     * @param columnName String[]
     * @param keyword String[]
     * @return PreparedStatement
     * @throws SQLException
     */
    public PreparedStatement createPrepareUpd(String tableName,String[] columnName,String[] keyword,String oldKeyData) throws SQLException {
        PreparedStatement preparedstatement;
        Hashtable key = new Hashtable();
        for (int i = 0; i < keyword.length; i++) {
            key.put(keyword[i].toUpperCase(), "1");
        }
        int setflag = 0, whereflag = 0;
        //�趨PrepareStatement�����ݡ�
        StringBuffer stringbuffer =
                new StringBuffer("update " + tableName + " set ");
        int i;

        for (i = 0; i < columnName.length; i++) {
//          if (key.get(columnName[i].toUpperCase()) == null) {
                if (setflag != 0) {
                    stringbuffer.append(",");
                }
                //2006-08-07
                //stringbuffer.append(columnName[i] + "=?");
                stringbuffer.append("\"" + columnName[i].toUpperCase() + "\"" +
                                    "=?");
                setflag++;
 //           }
        }

        stringbuffer.append(" where ");
        for (int j = 0; j < keyword.length; j++) {
            if (j > 0) {
                stringbuffer.append(" and ");
            }
            //2006-08-07
            //stringbuffer.append(keyword[j] + "=?");
            stringbuffer.append("\"" + keyword[j].toUpperCase() + "\"" + "=?");
        }
       // System.out.println(stringbuffer.toString());
        preparedstatement =
                m_connection.prepareStatement(stringbuffer.toString());

        return preparedstatement;
    }
//}

	public PreparedStatement createPrepareDel(String tableName, String[] keyword)
			throws SQLException {
//		CallSuport.printFunction(this.getClass() + ".PreparedStatement()",
//				debug);
		PreparedStatement preparedstatement;
		StringBuffer stringbuffer = new StringBuffer("delete from  "
				+ tableName);
		stringbuffer.append(" where ");
		for (int j = 0; j < keyword.length; j++) {
			if (j > 0) {
				stringbuffer.append(" and ");
			}
			stringbuffer.append(keyword[j] + "=?");
		}
		CallSuport.println("�趨PrepareStatement������", stringbuffer.toString(),
				debug);
		preparedstatement = m_connection.prepareStatement(stringbuffer
				.toString());
        //System.out.println(stringbuffer);
		return preparedstatement;
	}

	private String getColumnStr(String columnStr,String split) {
		columnStr = columnStr.replaceAll(split, "\""+split+"\"");
		columnStr = "\"" + columnStr + "\"";
		return columnStr;
	}

	/**
	 * Txt�ļ�������������ݿ�
	 * @param tableName
	 * @param columnList
	 * @param dataList
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */

	public int executePrepareIns(String tableName, String columnStr,
			ArrayList dataList) throws SQLException, ParseException {
		return executePrepareIns(tableName, columnStr, dataList, ",");
	}

	public int executePrepareIns(String tableName, String columnStr,
			ArrayList dataList, String split) throws SQLException,
			ParseException {
		ArrayList columns = new ArrayList();
		columns.add(columnStr);
		return prepareIns(tableName, columns, dataList, split,true);
	}
	
	/**
	 * 2008-02-22 LS ����
	 * @param tableName
	 * @param colNames
	 * @param dataList
	 * @param autoAscColumnName
	 * @param sequenceSchema
	 * @param sequenceName
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 * @throws Exception
	 */
	public int executePrepareIns(
            String tableName,
            Vector colNames,
            Vector dataList,
            String autoAscColumnName,
            String sequenceSchema,
            String sequenceName
            ) throws SQLException, ParseException,Exception {
		return prepareIns(tableName,colNames,dataList,
	            autoAscColumnName,sequenceSchema,
	            sequenceName, ",",true);
		//����־λ false  ��Ϊ false;
		
	}

	/**
	 * xml�ļ����ͽ��������
	 * @param tableName
	 * @param columnList
	 * @param dataList
	 * @return
	 * @throws ParseException 
	 * @throws SQLException 
	 * @throws SQLException
	 * @throws ParseException
	 */

	public int executePrepareIns(String tableName, ArrayList columnList,
			ArrayList dataList) throws SQLException, ParseException {
		return executePrepareIns(tableName, columnList, dataList, ",");
	}

	public int executePrepareIns(String tableName, ArrayList columnList,
			ArrayList dataList, String split) throws SQLException,
			ParseException {
		return prepareIns(tableName, columnList, dataList, split,true);
	}
	
	public int executePrepareIns(
            String tableName,
            Vector colNames,
            Vector dataList) throws SQLException, ParseException {
		ArrayList columnList=CallSuport.vectorToArray(colNames);
		ArrayList data=CallSuport.vectorToArray(dataList);
		return executePrepareIns(tableName, columnList,data);
	}
	public int executePrepareIns(
            String tableName,
            String colNames,
            Vector dataList) throws SQLException, ParseException {
//		ArrayList columnList=CallSuport.vectorToArray(colNames);
		ArrayList data=CallSuport.vectorToArray(dataList);
		return executePrepareIns(tableName, colNames,data);
	}

	/**
	 * XML�ļ�������Update���ݿ�
	 * @param tableName
	 * @param columnList
	 * @param dataList
	 * @param keyword
	 * @return
	 * @throws ParseException 
	 * @throws SQLException 
	 * @throws SQLException
	 * @throws ParseException
	 */
	public int executePrepareUpd(String tableName, ArrayList columnList,
			ArrayList dataList, String[] keyword) throws SQLException,
			ParseException {
		return executePrepareUpd(tableName, columnList, dataList, keyword, ",");
	}

	public int executePrepareUpd(String tableName, ArrayList columnList,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException, ParseException {
		return prepareUpdate(tableName, columnList, dataList, keyword, split);
	}
	
	
	 public int executePrepareUpd(
	            String tableName,
	            Vector columnList,
	            Vector dataList,
	            String[] keyword) throws SQLException, ParseException {
		ArrayList columns=CallSuport.vectorToArray(columnList);
		ArrayList data=CallSuport.vectorToArray(dataList);
		 return executePrepareUpd(tableName, columns,data,keyword);
	 }

	   /**
	     * 2007-01-25 Lyy  ���£���������¼�¼��
	     * 2008-02-21 LS   ���� : ��colTypeȡֵʱ����������
	     * @param tableName String
	     * @param columnList Vector
	     * @param dataList Vector
	     * @param keyword String[]
	     * @return int
	     * @throws SQLException
	     * @throws ParseException
	     */

	    public int executePrepareUpd(String tableName,Vector columnList,Vector dataList,String[] keyword,Vector oldKeyDataList) throws SQLException, ParseException,Exception {
	        int k = 0;
	        PreparedStatement prepareStmt = null;
	        Hashtable key = new Hashtable();
	        for (int i = 0; i < keyword.length; i++) {
	            key.put(keyword[i].toUpperCase(), "1");
	        }
	        ArrayList columnInfo = getColumnInfo(tableName);
	        String[] columnName = (String[]) columnInfo.get(0);
	        int[] columnType = (int[]) columnInfo.get(1);
	        Hashtable colType=getColumnTypeHash(columnInfo);
	        //2005-11-16
	        //prepareStmt = createPrepareUpd(tableName, columnName, keyword);

	        String[] column = new String[columnList.size()];
	        //test

	        for (int cloIdx = 0; cloIdx < columnList.size(); cloIdx++) {
	            column[cloIdx] = (String) columnList.get(cloIdx);
	            //test

	        }

	        // 2005��11��18 ���²��������������͵�ӳ��
	        int[] columnTypeReOrder = new int[column.length];
	        for (int m = 0; m < column.length; m++) {
	            String columnName1 = column[m];
	            for (int n = 0; n < columnName.length; n++) {
	                String columnName2 = columnName[n];
	                columnName2=CallSuport.replaceLine(columnName2, "\"", "");
	                if (columnName1.equals(columnName2)) {
	                    int colunType = columnType[n];
	                    columnTypeReOrder[m] = colunType;

	                }
	            }
	        }
	        //2007-01-25 LS
	        prepareStmt = createPrepareUpd(tableName, column, keyword,"");

	        int length = dataList.size();

	        //2005-11-16
	        //int size = columnName.length;
	        int size = column.length;

	        m_connection.setAutoCommit(false);

	        for (int i = 0; i < length; i++) {
	            int setflag = 0, whereflag = size ;

	            Vector rowVec = (Vector) dataList.get(i);
	            Vector keyrowVec = (Vector) oldKeyDataList.get(i);
	            String[] row = new String[rowVec.size()];
	            String[] keyrow = new String[keyrowVec.size()];
	            for (int rowIdx = 0; rowIdx < rowVec.size(); rowIdx++) {
	                row[rowIdx] = (String) rowVec.get(rowIdx);
	            }
	            for (int rowIdx = 0; rowIdx < keyrowVec.size(); rowIdx++) {
	               keyrow [rowIdx] = (String)keyrowVec.get(rowIdx);
	            }


	            Hashtable hashrow = strToHash(row, column);
	            //�õ����ֵ����ݺ���Ϣ�������ǲ��ԵĻ���
	            Hashtable hashkeyrow = strToHash(keyrow, keyword);
	            
	            for (int j = 0; j < size; j++) {
	                String value = (String) hashrow.get(column[j]);
	                
	                
	                //2008-02-21 LS   ���� : ��colTypeȡֵʱ����������
	                //int col_type=Integer.parseInt(colType.get(column[j]).toString());
	                int col_type=Integer.parseInt(colType.get("\""+column[j]+"\"").toString());
	                
	                //test
	                String keyvalue  = (String)  hashkeyrow.get(keyword[0]);
	                int index;
	                int index1=0;
	                index = setflag + 1;
	               int  keyindex=0;
	                setflag++;
	                if (key.get(column[j].toUpperCase()) != null) {
	                    keyindex = whereflag - size;
	                    //test
	                    keyvalue = (String) hashkeyrow.get(keyword[keyindex]);
	                    index1 = whereflag + 1;
	                    whereflag++;
	               }



	                if (value == null || value.length() == 0) {
	                	
	                    prepareStmt.setNull(index, col_type);
	                    if ( index1!=0){
	                            prepareStmt.setNull(index1, col_type);   }
	                } else {
	                    switch (col_type) {
	                    case 91:

	                        if (value.length() == 16) {
	                            value += ":00";
	                            prepareStmt.setTimestamp(
	                                    index,
	                                    createTimestamp(value.substring(0, 19)));

	                        }

	                        else if (value.length() == 10) {
	                            value += " 00:00:00";
	                            prepareStmt.setTimestamp(
	                                    index,
	                                    createTimestamp(value.substring(0, 19)));

	                        }

	                        else if (value.length() == 7) {
	                            value += "-01 00:00:00";
	                            prepareStmt.setTimestamp(
	                                    index,
	                                    createTimestamp(value.substring(0, 19)));

	                        } else if (value.length() == 4) {
	                            value += "-01-01 00:00:00";
	                            prepareStmt.setTimestamp(
	                                    index,
	                                    createTimestamp(value.substring(0, 19)));

	                        }
	                        else if (value.length() == 19) {
	                            //value += "-01-01 00:00:00";
	                            prepareStmt.setTimestamp(
	                                    index,
	                                    createTimestamp(value.substring(0, 19)));

	                        }

	                        else {
	                            //value = "1970-01-01 00:00:00";
	                            prepareStmt.setNull(
	                                    index,col_type);
	                        }


	                      if ( index1!=0){


	                        if (keyvalue.length() == 16) {
	                            keyvalue += ":00";
	                            prepareStmt.setTimestamp(
	                                    index1,
	                                    createTimestamp(keyvalue.substring(0, 19)));

	                        }

	                        else if (keyvalue.length() == 10) {
	                            keyvalue += " 00:00:00";
	                            prepareStmt.setTimestamp(
	                                    index1,
	                                    createTimestamp(keyvalue.substring(0, 19)));

	                        }

	                        else if (keyvalue.length() == 7) {
	                            keyvalue += "-01 00:00:00";
	                            prepareStmt.setTimestamp(
	                                    index1,
	                                    createTimestamp(keyvalue.substring(0, 19)));

	                        } else if (keyvalue.length() == 4) {
	                            keyvalue += "-01-01 00:00:00";
	                            prepareStmt.setTimestamp(
	                                    index1,
	                                    createTimestamp(keyvalue.substring(0, 19)));

	                        }else if (keyvalue.length() == 19) {
	                           // keyvalue += "-01-01 00:00:00";
	                            prepareStmt.setTimestamp(
	                                    index1,
	                                    createTimestamp(keyvalue.substring(0, 19)));

	                        }

	                        else {
	                            //value = "1970-01-01 00:00:00";
	                            prepareStmt.setNull(
	                                    index1,col_type);
	                        }


	                      }

//	              prepareStmt.setTimestamp(
//	                  index,
//	                  createTimestamp(value.substring(0, 19)));
	                        break;
	                    case 93:
	                        prepareStmt.setTimestamp(
	                                index,
	                                createTimestamp(value.substring(0, 19)));
	                        if (index1!=0){
	                            prepareStmt.setTimestamp(
	                                  index1,
	                                  createTimestamp(keyvalue.substring(0, 19)));

	                        }
	                        break;
	                    case 2:
	                        prepareStmt.setBigDecimal(
	                                index,
	                                new BigDecimal(value));

	                        if (index1!=0){
	                            prepareStmt.setBigDecimal(
	                             index1,
	                             new BigDecimal(keyvalue));
	 }

	                        //				( (OraclePreparedStatement) prepareStmt).setBigDecimal(index,
	                        //					new BigDecimal(value));
	                        break;
	                    default:

	                        //							System.out.println(index);
	                        //							System.out.println(value);
	                        //							System.out.println(columnName[j]);
	                        if (index1!=0){
	                                prepareStmt.setString(index1, keyvalue)  ;
	                            }

	                          prepareStmt.setString(index, value);

	                        break;
	                    }
	                }
	             // System.out.println("int:"+index+";data:"+value);
	             //   System.out.println("int1:"+index1+"data:"+value);

	            }

	            prepareStmt.executeUpdate();
//				if (i % 20 == 0) {
//					k += ((OraclePreparedStatement) prepareStmt).sendBatch();
//				}
	            //			((OraclePreparedStatement) prepareStmt).sendBatch();
	            hashrow.clear();
	        }
//			k += ((OraclePreparedStatement) prepareStmt).sendBatch();
	        m_connection.commit();
	        prepareStmt.close();
	        return k;
	    }

	/**
	 * Txt�ļ�������Update���ݿ�
	 * @param tableName
	 * @param columnList
	 * @param dataList
	 * @param keyword
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public int executePrepareUpd(String tableName, String columnList,
			ArrayList dataList, String[] keyword) throws SQLException,
			ParseException {
		return executePrepareUpd(tableName, columnList, dataList, keyword, ",");
	}
	public int executePrepareUpd(String tableName, String columnList,
			Vector dataV, String[] keyword) throws SQLException,
			ParseException {
		ArrayList dataList=CallSuport.vectorToArray(dataV);
		return executePrepareUpd(tableName, columnList, dataList, keyword, ",");
	}

	public int executePrepareUpd(String tableName, String columnStr,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException, ParseException {
		ArrayList columns = new ArrayList();
		columns.add(columnStr);
		return prepareUpdate(tableName, columns, dataList, keyword, split);
	}

	/**
	 * Txt�ļ�������Delete���ݿ�
	 * @param tableName
	 * @param columnList
	 * @param dataList
	 * @param keyword
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public int executePrepareDel(String tableName, String columnStr,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException, ParseException {
		ArrayList columnList = new ArrayList();
		columnList.add(columnStr);
		return prepareDel(tableName, columnList, dataList, keyword, split);
	}

	public int executePrepareDel(String tableName, String columnList,
			ArrayList dataList, String[] keyword) throws SQLException,
			ParseException {
		return executePrepareDel(tableName, columnList, dataList, keyword, ",");
	}
	
	

	/**
	 * XML�ļ�������Update���ݿ�
	 * @param tableName
	 * @param columnList
	 * @param dataList
	 * @param keyword
	 * @return
	 * @throws ParseException 
	 * @throws SQLException 
	 * @throws SQLException
	 * @throws ParseException
	 */
	public int executePrepareDel(String tableName, ArrayList columnList,
			ArrayList dataList, String[] keyword) throws SQLException,
			ParseException {
		return executePrepareDel(tableName, columnList, dataList, keyword, ",");
	}

	public int executePrepareDel(String tableName, ArrayList columnList,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException, ParseException {
		return prepareDel(tableName, columnList, dataList, keyword, split);
	}

	public int executePrepareDel(
            String tableName,
            Vector columnVec,
            Vector dataVec,
            String[] keyword) throws SQLException, ParseException {
		ArrayList columns=CallSuport.vectorToArray(columnVec);
		ArrayList data=CallSuport.vectorToArray(dataVec);
		return executePrepareDel(tableName, columns,
				data,keyword);
	}
	
	

	
	/**
	 * txt�ļ����ͽ�����merge���(������update����������insert)
	 * @param tableName
	 * ����
	 * @param columnList
	 * �ֶ���
	 * @param dataList
	 * ����
	 * @param keyword
	 * �ؼ���
	 * @return
	 * ����ִ�е�����
	 * @throws SQLException
	 * @throws ParseException
	 */
	public int executePrepareMerge(String tableName, String columnStr,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException, ParseException {
		ArrayList columnList = new ArrayList();
		columnList.add(columnStr);
		return prepareMerge(tableName, columnList, dataList, keyword, split);
	}

	public int executePrepareMerge(String tableName, String columnList,
			ArrayList dataList, String[] keyword) throws SQLException,
			ParseException {
		return executePrepareMerge(tableName, columnList, dataList, keyword,
				",");
	}

	/**
	 * xml�ļ����ͽ�����merge���(������update����������insert)
	 * @param tableName
	 * ����
	 * @param columnList
	 * �ֶ���
	 * @param dataList
	 * ����
	 * @param keyword
	 * �ؼ���
	 * @return
	 * ����ִ�е�����
	 * @throws SQLException
	 * @throws ParseException
	 */
	public int executePrepareMerge(String tableName, ArrayList columnList,
			ArrayList dataList, String[] keyword) throws SQLException,
			ParseException {
		return prepareMerge(tableName, columnList, dataList, keyword, ",");
	}

	public int executePrepareMerge(String tableName, ArrayList columnList,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException, ParseException {
		return prepareMerge(tableName, columnList, dataList, keyword, split);
	}

	private int prepareIns(String tableName, ArrayList moreColumnList,
			ArrayList dataList, String split,boolean isCommit) throws SQLException,
			ParseException {
		int k = 0;
		PreparedStatement prepareStmt = null;
		//�õ��ֶ���Ϣ
		ArrayList columnInfo = getColumnInfo(tableName);
		String[] columnName = (String[]) columnInfo.get(0);
		
		/**
		 * linhaifeng 208-04-21
		 */
		//System.out.println("moreColumnList:"+moreColumnList);
		String columnStr=(String)moreColumnList.get(0);
		String [] columnName1=columnStr.split(",");
		for(int i=0;i<columnName1.length;i++){
			columnName1[i]="\""+columnName1[i]+"\"";
		}
		
		
		
		
		
		
		//prepareStmt = createPrepareIns(tableName, columnName);
//		k = execPrepare(prepareStmt, moreColumnList, columnInfo, dataList,
//				split,isCommit,null);
		prepareStmt = createPrepareIns(tableName, columnName1);
		
		k = execPrepare(prepareStmt, moreColumnList, columnInfo, dataList,
				split,isCommit,null);
		
		return k;
	}
	
	
	/**
	 * 2008-02-21 LS gengxin
	 * @param tableName
	 * @param colNames
	 * @param dataList
	 * @param autoAscColumnName
	 * @param sequenceSchema
	 * @param sequenceName
	 * @param split
	 * @param isCommit
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	private int prepareIns(String tableName,
            Vector colNames,
            Vector dataList,
            String autoAscColumnName,
            String sequenceSchema,
            String sequenceName, String split,boolean isCommit) throws SQLException,
			ParseException {
		int k = 0;
		PreparedStatement prepareStmt = null;
		//�õ��ֶ���Ϣ
		ArrayList columnInfo = getColumnInfo(tableName);
		
		
		
		ArrayList moreColumnList=CallSuport.vectorToArray(colNames);
		ArrayList data=CallSuport.vectorToArray(dataList);
		String[] column=CallSuport.getStrFromArrayList(moreColumnList);
	
//		String[] columnName = (String[]) columnInfo.get(0);
		prepareStmt = createPrepareIns(tableName, column,autoAscColumnName, sequenceSchema,sequenceName);
		
		
		//////System.out.println("#########   moreColumnList 1 is:"+moreColumnList);
        String tbCols = moreColumnList.toString();
        tbCols = CallSuport.replaceLine(tbCols,"[","");
        tbCols = CallSuport.replaceLine(tbCols,"]","");
        tbCols = CallSuport.replaceLine(tbCols," ","");
        ///System.out.println("#########   tbCols is:"+tbCols);
        moreColumnList = new ArrayList();
        moreColumnList.add(tbCols);
        ///System.out.println("#########   moreColumnList 2 is:"+moreColumnList);
        
        
		k = execPrepare(prepareStmt, moreColumnList, columnInfo, data,split,isCommit,null);
		
		return k;
	}

	/**
	 * 2008-02-21 LS ������    ������ʱȡ�Ĳ���columnInfo
	 * @param tableName
	 * @param moreColumnList
	 * @param dataList
	 * @param keyword
	 * @param split
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	private int prepareUpdate(String tableName, ArrayList moreColumnList,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException, ParseException {
//		CallSuport.printFunction(this.getClass() + ".prepareUpdate()", debug);
		int k = 0;
		PreparedStatement prepareStmt = null;
		ArrayList columnInfo = getColumnInfo(tableName);
		
		
		
		ArrayList  newColumnList = CommonDB.getArrayList(moreColumnList);
		String[] columnNamett = (String[]) columnInfo.get(0);
		  int allCol = columnNamett.length;
		  int partCol = newColumnList.size();
		  
		  
		if(newColumnList.size()!=columnNamett.length){
			//2008-02-21 LS �����²�����ʱȡ�Ĳ���columnInfo
			columnInfo = CommonDB.getPartColumnInfo(columnInfo,newColumnList);
		}	
			
			
			
		String[] columnName = (String[]) columnInfo.get(0);
		Hashtable key =getHashKey(keyword);
		prepareStmt = createPrepareUpd(tableName, columnName, keyword);
		k=execPrepare(prepareStmt, moreColumnList, columnInfo, dataList,split,true,key);
		
		return k;
	}

	private int prepareDel(String tableName, ArrayList moreColumnList,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException, ParseException {
		int k = 0;
		Hashtable key=getHashKey(keyword);
		PreparedStatement prepareStmt = null;
		ArrayList columnInfo = getColumnInfo(tableName);
		prepareStmt = createPrepareDel(tableName, keyword);
		k=execPrepare(prepareStmt, moreColumnList, columnInfo, dataList,
				split,true,key);
		return k;
	}
	
	
	
	

	private int prepareMerge(String tableName, ArrayList moreColumnList,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException, ParseException {
		String tableName_temp = tableName + "_meg";
		if (tableName_temp.length() >= 30) {
			tableName_temp = tableName.substring(0, 25) + "_meg";
		}
		Statement stmt;

		stmt = m_connection.createStatement();
		int k = 0;
		
		//���� 20061009

		//        createMergeTable(stmt, tableName_temp, tableName, keyword);
		stmt.executeUpdate("truncate table " + tableName_temp);
		//		�õ��ֶ���Ϣ
		k=prepareIns(tableName_temp, moreColumnList,dataList, split,true);
		ArrayList columnInfo = getColumnInfo(tableName);
		String[] columnName = (String[]) columnInfo.get(0);
		int size = columnName.length;

		String on_string = "";
		String set_string = "";
		String insert_string = "";
		String values_string = "";
		for (int i = 0; i < keyword.length; i++) {
			on_string = on_string + "a." + keyword[i] + "=b." + keyword[i]
					+ " and ";
		}
		on_string = on_string.substring(0, on_string.length() - 5);
		Hashtable key =getHashKey(keyword);
		for (int j = 0; j < size; j++) {
			if (key.get(columnName[j].toUpperCase()) == null) {
				set_string = set_string + "a." + columnName[j] + "=b."
						+ columnName[j] + ",";
			}
			insert_string = insert_string + "a." + columnName[j] + ",";
			values_string = values_string + "b." + columnName[j] + ",";
		}
		insert_string = insert_string.substring(0, insert_string.length() - 1);
		values_string = values_string.substring(0, values_string.length() - 1);
		set_string = set_string.substring(0, set_string.length() - 1);
		String sql = "merge into " + tableName + " a using (select * from "
				+ tableName_temp + ") b on (" + on_string
				+ ") when matched then update set " + set_string
				+ " when not matched then insert (" + insert_string
				+ ") values(" + values_string + ")";
		CallSuport.println("mergeSql is", sql, debug);
		k = stmt.executeUpdate(sql);
		m_connection.commit();
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e1) {
				CallSuport.println(e1.getErrorCode() + ":" + e1.getMessage());
			}
		}
		return k;

	}
	
	
	
	private Hashtable getColumnTypeHash(ArrayList columnInfo){
		String[] columnName = (String[]) columnInfo.get(0);
		int[] columnType = (int[]) columnInfo.get(1);
		Hashtable columnHash=new Hashtable();
		for(int i=0;i<columnName.length;i++){
			columnHash.put(columnName[i], columnType[i]+"");
		}
		return columnHash;
	}

	private int execPrepare(PreparedStatement prepareStmt,
			ArrayList moreColumnList, ArrayList columnInfo, ArrayList dataList,
			String split,boolean isCommit,Hashtable key) throws SQLException, ParseException {
		int k = 0;
		int length = dataList.size();
		boolean isOracleStatement=(prepareStmt instanceof OraclePreparedStatement);
		m_connection.setAutoCommit(false);
		////System.out.println("###  isOracleStatement  is:"+isOracleStatement);
		if(isOracleStatement){
		   ((OraclePreparedStatement) prepareStmt).setExecuteBatch(300);
		}
		Hashtable colType=getColumnTypeHash(columnInfo);
		
		boolean equalLength = (moreColumnList.size() == length);
	

		for (int i = 0; i < length; i++) {
			String columnStr = getColumnStr(equalLength, moreColumnList,split, i);
			
			String[] row =null;
			if(dataList.get(i) instanceof ArrayList){
				
				row = getRow((ArrayList)dataList.get(i));
				
			}else{
				
			    row = getRow((String) dataList.get(i), split);
			    
			}
			String[] column = (columnStr).split(split);

		
			Hashtable hashrow = strToHash(row, column);
			
			if(key!=null){
				
			    prepareStmt = putPrepareStmt(prepareStmt,hashrow,column, colType,key);
			    
			}else{
				
				prepareStmt = putPrepareStmt(prepareStmt,hashrow,column, colType);	
				
			}
			prepareStmt.executeUpdate();
			if(isOracleStatement){
			if (i % 20 == 0) {
				k += ((OraclePreparedStatement) prepareStmt).sendBatch();
			}
			}
			hashrow.clear();
		}
		if(isOracleStatement){
		   k += ((OraclePreparedStatement) prepareStmt).sendBatch();
		}
		if(isCommit){
		   m_connection.commit();
		}
		prepareStmt.close();
		return k;
	}
	
	public int executeBatchIns(String tableName, ArrayList columnList,
			ArrayList dataList) throws SQLException {
		return executeBatchIns(tableName, columnList, dataList, ",");
	}

	public int executeBatchIns(String tableName, ArrayList columnList,
			ArrayList dataList, String split) throws SQLException {
		int result = 0;
		int length = columnList.size();
		Hashtable info = getColumnInfo(m_connection, tableName);
		m_connection.setAutoCommit(false);
		Statement stmt = m_connection.createStatement();
		for (int i = 0; i < length; i++) {
			StringBuffer sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append(" (");
			sql.append((String) columnList.get(i));
			sql.append(") values(");
			String columnStr = (String) columnList.get(i);
			String[] row = getRow((String) dataList.get(i), split);
			String[] column = (columnStr).split(split);
			int size = column.length;
			for (int j = 0; j < size; j++) {
				if (j != 0) {
					sql.append(",");
				}
				//helei 2006-02-10 �ع�
				appendSqlOracle(sql, info, row[j], column[j]);
			}
			sql.append(")");
			stmt.addBatch(sql.toString());
			if (i % 100 == 0) {
				result += stmt.executeBatch().length;
			}
		}
		info.clear();
		result += stmt.executeBatch().length;
		m_connection.commit();
		stmt.close();
		return result;
	}

	public int executeInsSyb(String tableName, ArrayList columnList,
			ArrayList dataList) throws SQLException, ParseException {
		return executeInsSyb(tableName, columnList, dataList, ",");
	}

	public int executeInsSyb(String tableName, ArrayList columnList,
			ArrayList dataList, String split) throws SQLException,
			ParseException {
		int result = 0;
		int length = columnList.size();
		Hashtable info = getColumnInfo(m_connection, tableName);
		Statement stmt = m_connection.createStatement();
		for (int i = 0; i < length; i++) {
			StringBuffer sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append(" (");
			sql.append((String) columnList.get(i));
			sql.append(") values(");
			String columnStr = (String) columnList.get(i);
			String[] row = getRow((String) dataList.get(i), split);
			String[] column = (columnStr).split(split);
			int size = column.length;
			for (int j = 0; j < size; j++) {
				String value = CallSuport.revertKeyFromIdentifier(row[j]);
				if (j != 0) {
					sql.append(",");
				}
				if (info.get(column[j].toUpperCase()).equals("91")) {
					sql.append("\'" + value.substring(0, 19) + "\'");
				} else if (info.get(column[j].toUpperCase()).equals("93")) {
					sql.append("\'" + value + "\'");
				} else if (info.get(column[j].toUpperCase()).equals("12")) {
					sql.append("\'" + value + "\'");
				} else if (info.get(column[j].toUpperCase()).equals("1")) {
					sql.append("\'" + value + "\'");
				} else if (info.get(column[j].toUpperCase()).equals("-7")) {
					if (value.equals("false")) {
						sql.append("0");
					} else {
						sql.append("1");
					}
				} else {
					sql.append(value);
				}
			}
			sql.append(")");
			result += stmt.executeUpdate(sql.toString());
		}
		info.clear();
		stmt.close();
		return result;
	}

	public StringBuffer appendSqlOracle(StringBuffer sql, Hashtable info,
			String row, String column) {
		int type = Integer.parseInt(info.get(column).toString());
		switch (type) {
		case 91:
			sql.append("to_date(\'" + row.substring(0, 19)
					+ "\',\'yyyy-mm-dd hh24:mi:ss\')");
			break;
		case 93:
			sql.append("to_date(\'" + row.substring(0, 19)
					+ "\',\'yyyy-mm-dd hh24:mi:ss\')");
			break;
		case 12:
			sql.append("\'" + row + "\'");
			break;
		case 1:
			sql.append("\'" + row + "\'");
			break;
		default:
			sql.append(row);
			break;

		}
		return sql;
	}

	public int executeBatchUpd(String tableName, ArrayList columnList,
			ArrayList dataList, String[] keyword) throws SQLException {
		return executeBatchUpd(tableName, columnList, dataList, keyword, ",");
	}

	public int executeBatchUpd(String tableName, ArrayList columnList,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException {
		int result = 0;
		int length = columnList.size();
		Hashtable info = getColumnInfo(m_connection, tableName);
		m_connection.setAutoCommit(false);
		Statement stmt = m_connection.createStatement();
		Hashtable key = getHashKey(keyword);
		for (int i = 0; i < length; i++) {
			int setflag = 0, whereflag = 0;
			StringBuffer sql = new StringBuffer();
			StringBuffer setsql = new StringBuffer();
			StringBuffer wheresql = new StringBuffer();
			sql.append("update " + tableName);
			String columnStr = (String) columnList.get(i);
			String[] row = getRow((String) dataList.get(i), split);
			String[] column = (columnStr).split(split);
			int size = column.length;
			for (int j = 0; j < size; j++) {
				if (key.get(column[j].toUpperCase()) != null) {
					if (whereflag != 0) {
						wheresql.append(" and ");
					}
					//helei 20060210
					wheresql.append(column[j] + "=");
					wheresql = appendSqlOracle(wheresql, info, row[j],
							column[j]);
					whereflag++;
				} else {
					if (setflag != 0) {
						setsql.append(",");
					}
					setsql.append(column[j] + "=");
					//helei 20060210
					setsql = appendSqlOracle(setsql, info, row[j], column[j]);
					setflag++;
				}
			}
			if (setflag != 0) {
				sql.append(" set ");
				sql.append(setsql);
				if (whereflag != 0) {
					sql.append(" where ");
					sql.append(wheresql);
				}
				stmt.addBatch(sql.toString());
			}
			if (i % 100 == 0) {
				result += stmt.executeBatch().length;
			}
		}
		info.clear();
		key.clear();
		result += stmt.executeBatch().length;
		m_connection.commit();
		stmt.close();
		return result;
	}

	public int executeUpdSyb(String tableName, ArrayList columnList,
			ArrayList dataList, String[] keyword) throws SQLException,
			ParseException {
		return executeUpdSyb(tableName, columnList, dataList, keyword, ",");
	}

	public int executeUpdSyb(String tableName, ArrayList columnList,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException, ParseException {
		int result = 0;
		int length = columnList.size();
		Hashtable info = getColumnInfo(m_connection, tableName);
		Statement stmt = m_connection.createStatement();
		Hashtable key = new Hashtable(keyword.length);
		for (int i = 0; i < keyword.length; i++) {
			key.put(keyword[i].toUpperCase(), "1");
		}
		for (int i = 0; i < length; i++) {
			int setflag = 0, whereflag = 0;
			StringBuffer sql = new StringBuffer();
			StringBuffer setsql = new StringBuffer();
			StringBuffer wheresql = new StringBuffer();
			sql.append("update " + tableName);
			String columnStr = (String) columnList.get(i);
			String[] row = getRow((String) dataList.get(i), split);
			String[] column = (columnStr).split(split);
			int size = column.length;
			for (int j = 0; j < size; j++) {
				String value = CallSuport.revertKeyFromIdentifier(row[j]);
				if (key.get(column[j].toUpperCase()) != null) {
					if (whereflag != 0) {
						wheresql.append(" and ");
					}
					wheresql.append(column[j] + "=");
					if (info.get(column[j].toUpperCase()).equals("91")) {
						wheresql.append("\'" + value.substring(0, 19) + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("93")) {
						wheresql.append("\'" + value + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("12")) {
						wheresql.append("\'" + value + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("1")) {
						wheresql.append("\'" + value + "\'");
					} else {
						wheresql.append(value);
					}
					whereflag++;
				} else {
					if (setflag != 0) {
						setsql.append(",");
					}
					setsql.append(column[j] + "=");
					if (info.get(column[j].toUpperCase()).equals("91")) {
						setsql.append("\'" + value.substring(0, 19) + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("93")) {
						setsql.append("\'" + value + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("12")) {
						setsql.append("\'" + value + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("1")) {
						setsql.append("\'" + value + "\'");
					} else {
						setsql.append(value);
					}
					setflag++;
				}
			}
			if (setflag != 0) {
				sql.append(" set ");
				sql.append(setsql);
				if (whereflag != 0) {
					sql.append(" where ");
					sql.append(wheresql);
				}
				result += stmt.executeUpdate(sql.toString());
			}

		}
		info.clear();
		key.clear();
		stmt.close();
		return result;
	}

	public int executeInsSyb(String tableName, String columnList,
			ArrayList dataList) throws SQLException, ParseException {
		return executeInsSyb(tableName, columnList, dataList, ",");
	}

	public int executeInsSyb(String tableName, String columnList,
			ArrayList dataList, String split) throws SQLException,
			ParseException {
		int result = 0;
		int length = dataList.size();
		Hashtable info = getColumnInfo(m_connection, tableName);
		Statement stmt = m_connection.createStatement();
		String[] column = columnList.split(split);
		for (int i = 0; i < length; i++) {
			StringBuffer sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append(" (");
			sql.append(columnList);
			sql.append(") values(");
			String dataStr = (String) dataList.get(i);
			dataStr = CallSuport.replaceLine(dataStr, ",null,", ",'null',");
			dataStr = CallSuport.replaceLine(dataStr, ",,", ",null,");
			String[] row = dataStr.split(split, 200);

			int size = column.length;
			for (int j = 0; j < size; j++) {
				String value = CallSuport.revertKeyFromIdentifier(row[j]);
				if (j != 0) {
					sql.append(",");
				}
				if (!value.equals("") && !value.equals("'null'")) {
					if (info.get(column[j].toUpperCase()).equals("91")) {
						sql.append("\'" + value.substring(0, 19) + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("93")) {
						sql.append("\'" + value + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("12")) {
						sql.append("\'" + value + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("1")) {
						sql.append("\'" + value + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("-7")) {
						if (value.equals("false")) {
							sql.append("0");
						} else {
							sql.append("1");
						}
					} else {
						sql.append(value);
					}
				} else if (value.equals("")) {
					sql.append("null");
				} else if (value.equals("'null'")) {
					sql.append("null");
				}
			}
			sql.append(")");
			//				stmt.addBatch(sql.toString());
			//				if(i%100==99){
			//					result+=stmt.executeBatch().length;
			//				}
			result += stmt.executeUpdate(sql.toString());
		}
		m_connection.commit();
		//			result+=stmt.executeBatch().length;
		info.clear();
		stmt.close();
		return result;
	}

	public int executeUpdSyb(String tableName, String columnList,
			ArrayList dataList, String[] keyword) throws SQLException,
			ParseException {
		return executeUpdSyb(tableName, columnList, dataList, keyword, ",");
	}

	public int executeUpdSyb(String tableName, String columnList,
			ArrayList dataList, String[] keyword, String split)
			throws SQLException, ParseException {
//		CallSuport.printFunction(this.getClass() + ".executeUpdSyb()", debug);
		int result = 0;
		int length = dataList.size();
		Hashtable info = getColumnInfo(m_connection, tableName);
		Statement stmt = m_connection.createStatement();
		Hashtable key = new Hashtable(keyword.length);
		for (int i = 0; i < keyword.length; i++) {
			key.put(keyword[i].toUpperCase(), "1");
		}
		String[] column = columnList.split(split);
		for (int i = 0; i < length; i++) {
			int setflag = 0, whereflag = 0;
			StringBuffer sql = new StringBuffer();
			StringBuffer setsql = new StringBuffer();
			StringBuffer wheresql = new StringBuffer();
			sql.append("update " + tableName);
			//			String[] row = ((String) dataList.get(i)).split(",",200);
			String[] row = StringConvert.strSplit((String) dataList.get(i),
					split);
			int size = column.length;
			for (int j = 0; j < size; j++) {
				String value = CallSuport.revertKeyFromIdentifier(row[j]);
				if (key.get(column[j].toUpperCase()) != null) {
					if (whereflag != 0) {
						wheresql.append(" and ");
					}
					wheresql.append(column[j] + "=");
					if (info.get(column[j].toUpperCase()).equals("91")) {
						wheresql.append("\'" + value.substring(0, 19) + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("93")) {
						wheresql.append("\'" + value + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("12")) {
						wheresql.append("\'" + value + "\'");
					} else if (info.get(column[j].toUpperCase()).equals("1")) {
						wheresql.append("\'" + value + "\'");
					} else {
						wheresql.append(value);
					}
					whereflag++;
				} else {
					if (value != null && (!value.trim().equals(""))) {
						if (setflag != 0) {
							setsql.append(",");
						}
						setsql.append(column[j] + "=");
						if (info.get(column[j].toUpperCase()).equals("91")) {
							setsql.append("\'" + value.substring(0, 19) + "\'");
						} else if (info.get(column[j].toUpperCase()).equals(
								"93")) {
							setsql.append("\'" + value + "\'");
						} else if (info.get(column[j].toUpperCase()).equals(
								"12")) {
							setsql.append("\'" + value + "\'");
						} else if (info.get(column[j].toUpperCase())
								.equals("1")) {
							setsql.append("\'" + value + "\'");
						} else {
							setsql.append(value);
						}
						setflag++;
					}
				}
			}
			if (setflag != 0) {
				sql.append(" set ");
				sql.append(setsql);
				if (whereflag != 0) {
					sql.append(" where ");
					sql.append(wheresql);
				}
				if (i == 0) {

					CallSuport.println("sql", sql.toString(), debug);
				}
				result += stmt.executeUpdate(sql.toString());
			}

		}
		m_connection.commit();
		info.clear();
		key.clear();
		stmt.close();
		return result;
	}

	protected Timestamp createTimestamp(String value) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = df.parse(value);
		return new Timestamp(date.getTime());
	}

	/**
	 * �ֺ������
	 * @param value String yyyy-MM-dd hh:mm:ss
	 * @throws ParseException exception
	 * @return Date
	 */
	/*private java.sql.Date createDate(String value) throws ParseException {
	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	 Date date = df.parse(value);
	 Timestamp stamp = new Timestamp(date.getTime());
	 java.sql.Date date1 = new java.sql.Date(stamp.getTime());
	 return date1;
	 }*/

	public Hashtable strToHash(String[] row, Object[] column) {
		Hashtable hash = new Hashtable();
		int length = row.length;
		for (int i = 0; i < length; i++) {
			if (row[i] != null) {
				String columnStr = ((String) column[i]).toUpperCase();
				hash.put(columnStr, row[i]);
			}
		}
		return hash;
	}

	/**
	 * ����ָ�������ݿ��Ƿ����С�
	 * @param ip
	 * ָ�����ݿ��IP��ַ
	 * @param port
	 * ָ�����ݿ�Ķ˿ں�
	 * @return
	 * ���ݿ������򷵻�true������false
	 */
	public static boolean isDBOpen(String ip, int port) {
		Socket database = new Socket();
		try {
			database.connect(new InetSocketAddress(ip, port), 100);
			return true;
		} catch (UnknownHostException e) {
			CallSuport.println(e.getMessage());
			e.printStackTrace();
			return false;
		} catch (ConnectException e) {
			CallSuport.println(e.getMessage() + e.getCause() + "2");
			return false;
		} catch (SocketTimeoutException e) {
			CallSuport.println(e.getMessage() + e.getCause() + "1");
			return false;
		} catch (IOException e) {
			CallSuport.println(e.getMessage());
			return false;
		} finally {
			try {
				if (database != null) {
					database.close();
				}
			} catch (IOException e1) {
				CallSuport.println(e1.getMessage());
				e1.printStackTrace();
			}
		}
	}

	/**
	 * ����ָ�������ݿ��Ƿ����С�
	 * @param ip
	 * ָ�����ݿ��ip��ַ
	 * @return
	 * ���ݿ������򷵻�true������false
	 */
	public static boolean isDBOpen(String ip) {
		return isDBOpen(ip, 1521);
	}

	/**
	 * ����ָ�����������û����������Ӧ�����ݿ����ӡ�
	 * @param servicename
	 * ��������ָOracle�ͻ��������õķ�����
	 * @param username
	 * �û���
	 * @param password
	 * ����
	 * @return
	 * ����ɹ��򷵻�true
	 * @throws SQLException
	 */
	public static boolean testDB(String servicename, String username,
			String password) throws SQLException {
		Connection connection = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		} catch (InstantiationException e2) {
			CallSuport.println(e2.getMessage() + e2.getCause());
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			CallSuport.println(e2.getMessage() + e2.getCause());
			e2.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "jdbc:oracle:oci8:@" + servicename;
		connection = DriverManager.getConnection(url, username, password);
		connection.close();
		return true;
	}

	/**
	 * ����ָ����ַ���˿ڡ�SID���û����������Ӧ�����ݿ����ӡ�
	 * @param sid
	 * ���ݿ�SID
	 * @param ip
	 * ���ݿ��ַ
	 * @param port
	 * ���ݿ�˿�
	 * @param username
	 * �û���
	 * @param password
	 * ����
	 * @return
	 * ����ɹ��򷵻�true
	 * @throws SQLException
	 */
	public static boolean testDB(String sid, String ip, String port,
			String username, String password) {
		boolean result = false;
		Connection connection = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid;
			connection = DriverManager.getConnection(url, username, password);
			connection.close();
			result = true;
		} catch (InstantiationException e2) {
			CallSuport.println(e2.getMessage() + e2.getCause());
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			CallSuport.println(e2.getMessage() + e2.getCause());
			e2.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * ����Sybase���ݿ�����
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean testDB(String ip, String port, String username,
			String password) {
		boolean result = false;
		Connection connection = null;
		try {
			Class.forName("com.sybase.jdbc2.jdbc.SybDriver").newInstance();
			String url = "jdbc:sybase:Tds:" + ip + ":" + port;
			connection = DriverManager.getConnection(url, username, password);
			connection.close();
			result = true;
		} catch (InstantiationException e2) {
			CallSuport.println(e2.getMessage() + e2.getCause());
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			CallSuport.println(e2.getMessage() + e2.getCause());
			e2.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	
	//���� 20061009
	//    public void createMergeTable(Statement stmt, String tableName_temp,
	//                                 String tableName, String[] key) throws
	//            SQLException {
	//        if (!MetaData.isContainsTable(DBService.this, null, tableName_temp)) {
	//            stmt.executeUpdate(
	//                    "create table "
	//                    + tableName_temp
	//                    + " as select * from "
	//                    + tableName
	//                    + " where 1=2");
	//            if (key != null) {
	//                String keyStr = CallSuport.getStringFromGroup(key, ",");
	//                String indexSql = "create index pk_" + tableName_temp + " on " +
	//                                  tableName_temp + "(" + keyStr + ")";
	//                stmt.executeUpdate(indexSql);
	//            }
	//        } else {
	//            stmt.executeUpdate("truncate table " + tableName_temp);
	//        }
	//
	//    }

	public void reconnect() {
		try {
			if (flag == 1 || flag == 2) {
				m_connection = DriverManager.getConnection(url, username,
						password);
				m_statement = m_connection.createStatement();
			}
		} catch (SQLException e) {
			CallSuport.println(e.getErrorCode() + e.getMessage());
			e.printStackTrace();
		}
	}

	public String getUsername() {
		return username;
	}

	/*
	 psidp group ���� blob�ļ�����jdbc���뵼���ķ��� helei 2007-02-01
	 *
	 *
	 */
	public ArrayList getValues(DBService db, String table, String[] columns,
			String[] values) {
		String columnStr = CallSuport.getStringFromGroup(columns, ",");
		String sql = "select " + columnStr + " from " + table + " where 1=2";
		ResultSet set = db.executeQuery(sql);
		ArrayList type = DBService.getRSColumnTypeNameA(set);
		ArrayList valueList = CallSuport.getArrayListFromStr(values);
		for (int i = 0; i < type.size(); i++) {
			String typeStr = type.get(i).toString().trim().toUpperCase();
			String value = valueList.get(i).toString();
			if (typeStr.equals("varchar2".toUpperCase())) {
				if (value.indexOf("'") != -1) {
					value = value.replaceAll("'", "''");
				}
				valueList.set(i, "'" + value + "'");
			}
			if (typeStr.equals("date".toUpperCase())) {
				if (value.equals("sysdate")) {
					valueList.set(i, "to_date('"
							+ TimePack.getTime(System.currentTimeMillis())
							+ "','yyyy-mm-dd hh24:mi:ss')");
				} else {
					valueList.set(i, "to_date('" + value
							+ "','yyyy-mm-dd hh24:mi:ss')");

				}

			}
		}
		return valueList;
	}


	/**
	 * 2007-03-12 �ֺ���
	 * ִ�д洢����,�����쳣�׳�,
	 * @param name String
	 * @param para Vector
	 * @return Vector
	 */
	public Vector callProcedureFeedback(String name, Vector para)
			throws SQLException {
		CallableStatement cs = null;
		Vector result = new Vector();
		String call;
		Vector outFlag = new Vector();

		if (para == null) {
			call = "{call " + name + "}";
			cs = m_connection.prepareCall(call);
		} else {
			call = "{call " + name + "(?";
			for (int i = 1; i < para.size(); i++) {
				call = call + ",?";
			}
			call = call + ")}";
			cs = m_connection.prepareCall(call);
			for (int i = 1; i <= para.size(); i++) {
				Vector p = (Vector) para.elementAt(i - 1);
				if (p.elementAt(0).toString().equals("1")) {
					cs.setObject(i, p.elementAt(1));
				} else if (p.elementAt(0).toString().equals("2")) {
					cs.registerOutParameter(i, ((Integer) p.elementAt(1))
							.intValue());
					outFlag.add(new Integer(i));
				} else if (p.elementAt(0).toString().equals("3")) {
					cs.setObject(i, p.elementAt(1));
					cs.registerOutParameter(i, ((Integer) p.elementAt(2))
							.intValue());
					outFlag.add(new Integer(i));
				} else {
					CallSuport.println("������������ϸ�ʽҪ��");
					return null;
				}
			}
		}
		cs.execute();
		if (outFlag != null) {
			for (int i = 1; i <= outFlag.size(); i++) {
				result.add(cs.getObject(((Integer) outFlag.elementAt(i - 1))
						.intValue()));
			}
		}

		return result;
	}

	//���� 2007-04-14�汾
	protected PreparedStatement putTimeStamp(PreparedStatement prepareStmt,
			String value, int length, int index) throws ParseException,
			SQLException {
		switch (length) {
		case 16:
			value += ":00";
			prepareStmt.setTimestamp(index, createTimestamp(value.substring(0,
					19)));
			break;
		case 10:
			value += " 00:00:00";
			prepareStmt.setTimestamp(index, createTimestamp(value.substring(0,
					19)));

			break;
		case 7:
			value += "-01 00:00:00";
			prepareStmt.setTimestamp(index, createTimestamp(value.substring(0,
					19)));
			break;
		case 4:
			value += "-01-01 00:00:00";
			prepareStmt.setTimestamp(index, createTimestamp(value.substring(0,
					19)));

			break;
		default:
			prepareStmt.setTimestamp(index, createTimestamp(value.substring(0,
					19)));
			break;

		}
		return prepareStmt;
	}

	protected PreparedStatement putValue(PreparedStatement prepareStmt,
			String value, int columnType, int index)
			throws ParseException, SQLException {
		if (value != null && value.trim().equals("null")) {
			value = null;
		}
		if (value != null && value.trim().equals("'null'")) {
			value = "null";
		}
		if (value == null || value.trim().length() == 0) {
			prepareStmt.setNull(index, columnType);
		} else {
			value = CallSuport.revertKeyFromIdentifier(value);
			switch (columnType) {
			case 91:
				int length = value.length();
				prepareStmt = putTimeStamp(prepareStmt, value, length, index);

				break;
			case 93:
				prepareStmt.setTimestamp(index, createTimestamp(value
						.substring(0, 19)));
				break;
			case 2:
				prepareStmt.setBigDecimal(index, new BigDecimal(value));
				break;
			default:
				prepareStmt.setString(index, value);
				break;
			}
		}
		return prepareStmt;
	}
	
	protected PreparedStatement putPrepareStmt(PreparedStatement prepareStmt,Hashtable hashrow, String[] columnName,
			Hashtable columnType) throws SQLException, ParseException {
		int length=columnName.length;
		for (int j = 0; j < length; j++) {
				String value = (String) hashrow.get(columnName[j]);
				//System.out.println("columnType$$$:"+columnType);
				//System.out.println("columnName[j]:"+columnName[j]);
				int colType=Integer.parseInt(columnType.get(columnName[j]).toString());
				prepareStmt = putValue(prepareStmt, value, colType, j+1);
//				System.out.println(columnName[j]+"="+value+"\t����="+columnType[j]);
				
		}
		return prepareStmt;
	}
	protected PreparedStatement putPrepareStmt(PreparedStatement prepareStmt,Hashtable hashrow, String[] columnName,
			Hashtable columnType,Hashtable key) throws SQLException, ParseException {
		int length=columnName.length;
		int setflag = 0, whereflag = length - key.size();
		for (int j = 0; j < length; j++) {
			int index;
            if (key.get(columnName[j].toUpperCase()) == null) {
                index = setflag + 1;
                setflag++;
            } else {
                index = whereflag + 1;
                whereflag++;
            }
				String value = (String) hashrow.get(columnName[j]);
				//System.out.println("columnType.get(columnName["+j+"]) is "+columnType.get(columnName[j]));
				int colType=Integer.parseInt(columnType.get(columnName[j]).toString());
				prepareStmt = putValue(prepareStmt, value, colType, index);
				
//				System.out.println(columnName[j]+"="+value+"\t����="+columnType[j]);
				
		}
		return prepareStmt;
	}
	
	private void printSqlException(SQLException e){
		CallSuport.println(e.getMessage());
		e.printStackTrace();
		if (e.getErrorCode() == 3114 || e.getErrorCode() == 17002
				|| e.getErrorCode() == 17008 || e.getErrorCode() == 604
				|| e.getErrorCode() == 1000) {
			int answer = JOptionPane.showConfirmDialog(null,
					"���ݿ�����ʧ�ܣ��������������Ƿ�������\n�����������ݿ⣿", "���ݿ�����ʧ�ܣ�",
					JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if (answer == JOptionPane.YES_OPTION) {
				reconnect();
			}
		}
	}


	public static void main(String[] args) {
//		DBService db = CallSuport.getDB("./idp.conf");
		String time = TimePack.getTime(System.currentTimeMillis());

		//    try {
		//      db.insertBlobOra("idptest.blob_test", new String[] {"app_name",
		//                       "file_name",
		//                       "ddate", "file_name2"},
		//                       new String[] {"aa",
		//                       "C:/Documents and Settings/Administrator/����/test/aa.svg",
		//                       "sysdate",
		//                       "C:/Documents and Settings/Administrator/����/test/bb.svg"},
		//                       new String[] {"app_name", "ddate"});
		//      db.updateBlobOra("idptest.blob_test", new String[] {"app_name",
		//                       "file_name",
		//                       "ddate", "file_name2"},
		//                       new String[] {"aa",
		//                       "C:/Documents and Settings/Administrator/����/test/aa1.svg",
		//                       "sysdate",
		//                       "C:/Documents and Settings/Administrator/����/test/b1.svg"},
		//                       new String[] {"app_name"});
		//      db.getBlobFileFromTableOra("idptest.blob_test",
		//                                 new String[] {"file_name"},
		//                                 new String[] {"app_name"},
		//                                 new String[] {"aa"},
		//                                 new String[] {"G:/�½��ļ���/aa1.svg"});
		//      System.out.println("close db");
		//
		//    }
		//    catch (IOException ex) {
		//      ex.printStackTrace();
		//    }
		//    catch (SQLException ex) {
		//      ex.printStackTrace();
		//    }
//		db.closeStatement();
//		db.closeConn();
		//    db.insertBlob("helei_test");
	}

	public String getDBType() {
		return dbType;
	}

	protected String[] getRow(String dataStr, String split) {
		dataStr = CallSuport.replaceLine(dataStr, split+"null"+split, split+"'null'"+split);
		dataStr = CallSuport.replaceLine(dataStr, split+split, split+"null"+split);
		String[] row = (dataStr).split(split);
		return row;
	}
	protected String[] getRow(ArrayList dataStr) {
		String[] row = CallSuport.getStrFromArrayList(dataStr);
		return row;
	}

	private String getColumnStr(boolean equalLength, ArrayList moreColumnList,String split,
			int i) {
		String columnStr = null;
		if (equalLength) {
			columnStr = (String) moreColumnList.get(i);
		} else {
			columnStr = (String) moreColumnList.get(0);
		}
		columnStr = getColumnStr(columnStr,split);
		return columnStr;
	}
	
	private Hashtable getHashKey(String[] keyword){
		Hashtable key = new Hashtable();
		for (int i = 0; i < keyword.length; i++) {
			key.put("\""+keyword[i].toUpperCase()+"\"", "1");
		}
		return key;
	}
	
	

	
	
	/**
	 * ���������SQL���,��ʼ��¼����¼���ȣ�ִ�в���������ִ�н����֧��Select��ѯ���
	 * @param sql 
	 * �����SQL���
	 * @param start
	 * ��ʼ��¼����0��ʼ
	 * @param size
	 * ��¼����
	 * @return
	 * ����ִ�н����ArrayList����
	 * ÿ�м�¼ΪArrayList
	 * ���һ�м�¼ΪInteger �ܼ�¼����
	 */
	public ArrayList executeQueryA(String sql,int start,int size) {
		//System.out.println("start:"+start);
		//System.out.println("size:"+size);
		Statement stmt = null;
		ResultSet resultset = null;
		ArrayList result = new ArrayList();
		try {
			
			long beforeQuery=System.currentTimeMillis();
			//System.out.println("beforeQuery:"+System.currentTimeMillis());
	          
			stmt = this.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//( (OracleStatement)stmt ).setRowPrefetch (250);
			//stmt.setFetchSize(20);
			//System.out.println(stmt);
			resultset = stmt.executeQuery(sql);
			
			long afterQuery=System.currentTimeMillis();
			//System.out.println("��ѯ ��ʱ:"+(afterQuery-beforeQuery));
			
			int columnCount = resultset.getMetaData().getColumnCount();
			
			if(start>0){
			  resultset.absolute(start);
			}
			
			long afterAbsolute=System.currentTimeMillis();
			//System.out.println("���� ��ʱ:"+(afterAbsolute-afterQuery));
			
			int count=0;
			while(resultset.next()&&count<size){
				ArrayList row = new ArrayList();
				
				for (int i = 1; i <= columnCount; i++) {
					
					//if(resultset.getObject(i) == null){
					//	row.add("");
					//}
//					else if (resultset.getObject(i) != null&&resultset.getObject(i) instanceof Date) {
//						row.add(resultset.getObject(i).toString().substring(0, 10));
//					} 
//					else{
//					row.add(CommonTools.getGBK(resultset.getObject(i).toString()));
						row.add(resultset.getObject(i));
//					}
				}
				result.add(row);
				count++;
				
			}
			
			long afterPut=System.currentTimeMillis();
//			System.out.println("���� ��ʱ:"+(afterPut-afterAbsolute));
			resultset.last();
			
			result.add(new Integer(resultset.getRow()));
			
			
			
			resultset.close();

		} catch (SQLException e) {
			//printSqlException(e);
			e.printStackTrace();

		} finally {
			if (resultset != null) {
				try {
					resultset.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
	
	
	
	public int executePrepareDel(
            String tableName,
            Vector data,
            String[] keyword) throws SQLException, ParseException {
		int k = 0;
	
		
		ArrayList columnInfo = getColumnInfo(tableName);
		
		PreparedStatement prepareStmt = createPrepareDel(tableName, keyword);
		
		Hashtable colType=getColumnTypeHash(columnInfo);
		
		for(int i=0;i<keyword.length;i++){
			keyword[i]="\""+keyword[i]+"\"";
		}
		
		for(int i=0;i<data.size();i++){
		  Vector row=(Vector)data.get(i);
		  Hashtable hashrow =new Hashtable();
		  for(int j=0;j<row.size();j++){
			  String value=row.get(j).toString();
			  hashrow.put(keyword[j], value);
		  }
		  prepareStmt = putPrepareStmt(prepareStmt,hashrow,keyword, colType);
		}
		prepareStmt.executeUpdate();
//		k=execPrepare(prepareStmt, moreColumnList, columnInfo, dataList,
//				split,true,key);
		return k;
	}
	
	
	
	public static final int ORACLE_TYPE_VARCHAR2=1;  
	public static final int ORACLE_TYPE_NUMBER_FLOAT=2;
	public static final int ORACLE_TYPE_LONG=8;
	public static final int ORACLE_TYPE_VARCHAR=9;
	public static final int ORACLE_TYPE_DATE=12;
	public static final int ORACLE_TYPE_RAW=23;
	public static final int ORACLE_TYPE_LONGRAW=24;
	public static final int ORACLE_TYPE_ROWID=69;
	public static final int ORACLE_TYPE_CLOB=112;
	public static final int ORACLE_TYPE_BLOB=113;
	public static final int ORACLE_TYPE_BFILE=114;
	public static final int ORACLE_TYPE_CFILE=115;
	public static final int ORACLE_TYPE_UROWID=208;
	public static final int ORACLE_TYPE_UNDEFINED=-1;

	

}
