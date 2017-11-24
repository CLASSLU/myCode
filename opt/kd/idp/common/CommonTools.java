package kd.idp.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import kd.idp.common.database.DBManager;
import kd.idp.common.database.DBService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import KD.IDP.basic.CallSuport;
import KD.IDP.basic.FilesPack;
import KD.IDP.etl.ConfigDAO.FileParserObject;
import KD.IDP.etl.fileparser.FileParser;
import KD.IDP.etl.fileparser.match.Context;
import KD.IDP.etl.fileparser.reader.FileReader;
import KD.IDP.etl.fileparser.reader.SyntaxReader;

import com.spring.dbservice.DBTemplate;

import freemarker.template.Configuration;

public class CommonTools implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5300263722081476449L;
	// public static String DEFAULT_CONFIGURATION_PATH = "";

	private static Hashtable parameters = new Hashtable();

	private static final Log logger = LogFactory.getLog(CommonTools.class);

	public final static String xmlHeader = "<?xml version=\"1.0\" encoding=\"GBK\"?>";

	private static Map<String, String> paramMap = new HashMap<String, String>();

	public CommonTools() {
	}

	// private static ConfParser _confParser = new ConfParser();

	// public static String cognos_entry = "";
	//
	// public static String psidp_entry = "";
	//
	// public static String dmis_entry = "";

	public static String os_type = "Windows";
	public static String webappName = "";

	// public static String messageModel = "";
	//
	// public static String systemName = "";
	//
	// public static String psidpchartlocation = "";
	//
	// public static String psidpportaldisplayname ="";

	// static {
	//
	// _confParser.parse();
	// cognos_entry = _confParser.getCognos_entry();
	// psidp_entry = _confParser.getPsidp_entry();
	// dmis_entry = _confParser.getDmis_entry();
	// os_type = _confParser.getOS_Type();
	// messageModel = _confParser.getMsg_model();
	// systemName = _confParser.getSys_name();
	// psidpchartlocation = _confParser.getPsidpchart_location();
	// psidpportaldisplayname = _confParser.getPsidpportaldisplayname();
	//
	// }

	public static String getInitParameter(String key) {
		// return parameters.get(key).toString();
		if (getParameters().get(key) != null) {
			return getParameters().get(key).toString();
		}
		return null;
	}

	public static String getCognos_entry() {

		return getInitParameter("Cognos_Entry");
	}

	public static String getDmis_entry() {

		return getInitParameter("DMIS_Entry");
	}

	public static String getPsidp_entry() {

		return getInitParameter("PSIDP_Entry");
	}

	public static String getPsisp_datammessagehost() {
		return getInitParameter("psidpwebDataMMessageHost");
	}

	public static String getPsisp_datammessageport() {
		return getInitParameter("psidpwebDataMMessagePort");
	}

	/**
	 * 2009-05-18 CY
	 */
	public static String getPsidp_projectname() {
		return getInitParameter("PSIDP_Project_LocalName");
	}

	/**
	 * 2009-10-09 CY 设置自动登录的用户
	 */
	public static String getPSIDP_AutoLoginUser() {
		try {
			return getInitParameter("PSIDP_AutoLoginUser");
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 2009-10-09 CY 设置自动登录的用户的密码
	 */
	public static String getPSIDP_AutoLoginPWD() {
		try {
			return getInitParameter("PSIDP_AutoLoginPWD");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 2008-06-04 LS
	 * 
	 * @return
	 */
	public static String getPSIDP_InvTimerHost() {
		return getInitParameter("psidpwebInvTimerHost");
	}

	public static String getPSIDP_InvTimerPort() {
		return getInitParameter("psidpwebInvTimerPort");
	}

	public static String getPSIDPWEBDebug() {
		return getInitParameter("psidpwebdebug");
	}

	/**
	 * 2008-08-29 LS
	 * 
	 * @return
	 */
	public static String getCognosPathDirection() {
		return getInitParameter("cognosPathDirection");
	}

	/**
	 * 2008-08-29 LS
	 * 
	 * @return
	 */
	public static String getCognosWebUrl() {
		return getInitParameter("cognosWebUrl");
	}

	public static String getMessage_model() {

		return "";
	}

	// public static BigDecimal getNextID(String schema, String table,
	// String id_colum_name) {
	// // String sql_str = "select max(\"" + id_colum_name.toUpperCase() + "\")
	// // from \"" + schema +
	// // "\".\"" + table + "\"";
	//
	// /**
	// * 2006-09-11 添加全局序列
	// */
	// String sql_str = "select IDP_WEB.\"全局序列\".nextval from dual";
	// // System.out.println(sql_str);
	//
	// DBService dbservice = null;
	//
	// ArrayList records = null;
	// try {
	// dbservice = DBManager.getDBService();
	// records = dbservice.executeQueryA(sql_str);
	// } catch (Exception ex) {
	// } finally {
	// dbservice.closeConn();
	// }
	// if (records.size() == 0) {
	// return new BigDecimal("1");
	// }
	//
	// ArrayList record = (ArrayList) records.get(0);
	// Object maxObj = record.get(0);
	// if (maxObj == null) {
	// return new BigDecimal("1");
	// }
	// BigDecimal maxValue = (BigDecimal) maxObj;
	//
	// BigDecimal nextValue = maxValue.add(new BigDecimal("1"));
	//
	// // System.out.println("The new nextval is:"+nextValue.intValue());
	//
	// String date = getCurrentDate("ymdhms");
	//
	// CommonTools.psidpwebdebuginfor(9, "idp_web 全局序列" + date + " nextval",
	// nextValue.toString());
	// return nextValue;
	//
	// }

	public static String getISO(String str) {
		try {
			return new String(str.getBytes("gb2312"), "iso-8859-1");
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 对应JavaScript中的escape方法
	 * 
	 * @param src
	 *            要编码的字符串
	 * @return 编码后字符串
	 */
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);

		for (i = 0; i < src.length(); i++) {

			j = src.charAt(i);

			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * 对应JavaScript中的unescape方法
	 * 
	 * @param src
	 *            需要解码的字符串
	 * @return 解码字符串
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src
							.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src
							.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static String getGBK(String str) {

		try {
			return new String(str.getBytes("iso-8859-1"), "gb2312");
		} catch (Exception e) {
			return str;
		}
	}

	public static String getLGBK(String str) {

		try {
			return new String(str.getBytes("utf-8"), "gb2312");
		} catch (Exception e) {
			return str;
		}
	}

	public static String getLISO(String str) {

		try {
			return new String(str.getBytes("gb2312"), "utf-8");
		} catch (Exception e) {
			return str;
		}
	}

	public static String getGBKAdaptByOS(String str) {
		if (os_type.toUpperCase().equals("UNIX")) {
			return str;
		}
		try {
			return new String(str.getBytes("iso-8859-1"), "gb2312");
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 返回 web 路径名 2006-08-15
	 * 
	 * @param serveletContext
	 *            ServletContext
	 * @return String
	 */

	public static String getWebAppName(ServletContext serveletContext) {
		if (webappName == null || webappName.equals("")) {
			String parent_path = "";
			try {
				parent_path = serveletContext.getResource("/").getPath()
						.toString();
				parent_path = parent_path.substring(1, parent_path.length());
				parent_path = parent_path.substring(parent_path.indexOf("/"),
						parent_path.length());
				// parent_path = psidp_entry+""+parent_path;

			} catch (Exception e) {
				e.printStackTrace();
			}
			webappName = parent_path;
		}
		return webappName;

	}

	public static String getWebAppName() {
		// System.out.println("webappName:"+webappName);
		return webappName;
	}

	public static String getSystemName() {
		return "";
	}

	/**
	 * 2007-03-16 LS
	 * 
	 * @param list
	 *            ArrayList
	 * @param isNum
	 *            boolean
	 * @return String
	 */
	public static String getStrFromList(ArrayList list, boolean isNum) {
		String str = "";

		if (isNum) {
			for (int i = 0; i < list.size(); i++) {
				String str1 = (String) list.get(i);
				if (i != list.size() - 1) {
					str = str + str1 + ",";
				} else {
					str = str + str1;
				}
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				String str1 = (String) list.get(i);
				if (i != list.size() - 1) {
					str = str + "'" + str1 + "'" + ",";
				} else {
					str = str + "'" + str1 + "'";
				}
			}
		}

		return str;
	}

	public static int getPosisitonInArrayList(String str, ArrayList temp) {
		int index = -1;
		if (str != null && temp != null) {
			for (int i = 0; i < temp.size(); i++) {
				String teststr = (String) temp.get(i);
				if (str != null && str.equals(teststr)) {
					index = i;
				}
			}
		}
		return index;
	}

	public static boolean isInBlockList(String str, ArrayList list) {

		boolean flag = false;

		for (int i = 0; i < list.size(); i++) {
			String tt = (String) list.get(i);
			if (tt != null && tt.equals(str)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 2007-07-09 LS
	 * 
	 * @param _newlist
	 *            ArrayList
	 * @param _oldlist
	 *            ArrayList
	 * @return ArrayList
	 */
	public static ArrayList getAddList(ArrayList _newlist, ArrayList _oldlist) {
		ArrayList temp = new ArrayList();

		for (int i = 0; i < _newlist.size(); i++) {
			String _str = (String) _newlist.get(i);
			boolean flag = isStrinArray(_str, _oldlist);
			if (!flag) {
				temp.add(_str);
			}
		}
		return temp;
	}

	public static boolean isStrinArray(String str, ArrayList list) {

		boolean flag = false;

		for (int i = 0; i < list.size(); i++) {
			String tt = (String) list.get(i);
			if (tt != null && tt.equals(str)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 2007-07-09 LS
	 * 
	 * @param _newlist
	 *            ArrayList
	 * @param _oldlist
	 *            ArrayList
	 * @return ArrayList
	 */
	public static ArrayList getDelList(ArrayList _newlist, ArrayList _oldlist) {
		ArrayList temp = new ArrayList();

		for (int i = 0; i < _oldlist.size(); i++) {
			String _str = (String) _oldlist.get(i);
			boolean flag = isStrinArray(_str, _oldlist);
			if (flag) {
				temp.add(_str);
			}
		}
		return temp;
	}

	/**
	 * 2007-07-09 LS
	 * 
	 * @param _strs
	 *            String[]
	 * @return ArrayList
	 */
	public static ArrayList getStrsToArrayList(String _str) {
		ArrayList temp = new ArrayList();
		if (_str != null && !_str.equals("") && !_str.equals("null")) {
			String[] strs = _str.split(";");

			for (int i = 0; i < strs.length; i++) {
				String str1 = strs[i];
				temp.add(str1);

			}
		}
		return temp;
	}

	/**
	 * 2007-11-23
	 * 
	 * @param _str
	 * @param seprtflag
	 * @return
	 */
	public static ArrayList getStrsToArrayList(String _str, String seprtflag) {
		ArrayList temp = new ArrayList();
		if (_str != null && !_str.equals("") && !_str.equals("null")) {
			String[] strs = _str.split(seprtflag);

			for (int i = 0; i < strs.length; i++) {
				String str1 = strs[i];
				str1 = str1.trim();
				temp.add(str1);
			}
		}
		return temp;
	}

	/**
	 * 2007-08-09 LS
	 * 
	 * @param sql_str
	 * @return
	 */
	// public static OracleCachedRowSet get_sql_ResultSet(String sql_str) {
	// // System.out.println(sql_str);
	// DBService dbService = null;
	// ResultSet rs_tmp = null;
	// OracleCachedRowSet rs_return = null;;
	// try {
	// rs_return = new OracleCachedRowSet();
	// dbService = DBManager.getDBService();
	// // ///System.out.println(sql_str);
	// rs_tmp = dbService.executeQuery(sql_str);
	// rs_return.populate(rs_tmp);
	// } catch (Exception ex) {
	// } finally {
	// dbService.closeConn();
	// }
	//
	// return rs_return;
	// }
	/**
	 * 2010-12-28 LS
	 * 
	 * @param sql_str
	 * @return
	 */
	public static List get_DB_ResultList_Hibernate(String sql_str) {
		List list = null;
		try {
			list = DBTemplate.getInstance().getResultMapList(sql_str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void update_DB_Hibernate(String sql_str) {

		try {
			DBTemplate.getInstance().executeSql(sql_str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 2011-04-02 16:42 LS
	 * 
	 * @param sql_str
	 * @return
	 */
	// public static boolean get_DB_ResultUpdate(String sql_str) {
	// boolean flag = false;
	// DBService db = null;
	// try {
	// db = DBManager.getDBService();
	// db.setAutoCommit(false);
	// int i = db.executeUpdate(sql_str);
	// if (i == 1) {
	// db.commit();
	// flag = true;
	// } else {
	// db.rollback();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// db.closeConn();
	// }
	//
	// return flag;
	// }
	/**
	 * 2011-12-16 10:07 YXQ 更新多行数据
	 * 
	 * @param sql_str
	 * @return
	 */
	 public static boolean get_DB_ResultUpdateAll(String sql_str) {
	 boolean flag = false;
	 DBService db = null;
	 try {
	 db = DBManager.getDBService();
	 int i = db.executeUpdate(sql_str);
	 if(i != -1)
	 flag = true;
	 } catch (Exception e) {
	 e.printStackTrace();
	 } finally {
	 db.closeConn();
	 }
	
	 return flag;
	 }
	/**
	 * 2007-08-09 LS
	 * 
	 * @param sql_str
	 * @return
	 */
	public static ArrayList get_DB_ResultArrayList(String sql_str) {
		List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
		ArrayList retList = new ArrayList();
		// DBService dbService = null;
		// System.out.println(sql_str);
		try {
			// dbService = DBManager.getDBService();
			// temp = dbService.executeQueryA(sql_str);
			temp = DBTemplate.getInstance().getResultMapList(sql_str);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// finally {
		// dbService.closeConn();
		// }

		for (int i = 0; i < temp.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) temp.get(i);
			Set<String> set = map.keySet();//       
			Iterator<String> it = set.iterator();//     
			ArrayList al = null;
			while (it.hasNext()) {//           
				String s = (String) it.next();// 
				al = new ArrayList();
				al.add(map.get(s));//       
			}
			retList.add(al);
		}
		return retList;
	}

	/**
	 * 2007-08-24 ls
	 * 
	 * @param sql_str
	 * @return
	 */
	public static ArrayList get_DB_ResultArrayListOneDilment(String sql_str) {
		List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();

		// DBService dbService = null;
		// System.out.println(sql_str);
		try {
			// dbService = DBManager.getDBService();
			// temp = dbService.executeQueryA(sql_str);
			temp = DBTemplate.getInstance().getResultMapList(sql_str);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// finally {
		// dbService.closeConn();
		// }

		ArrayList temp2 = new ArrayList();
		for (int i = 0; i < temp.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) temp.get(i);
			Set<String> set = map.keySet();// 
			Iterator<String> it = set.iterator();//
			Object obj2 = new Object();
			while (it.hasNext()) {//           
				String s = (String) it.next();// 
				obj2 = map.get(s);
				break;
			}
			temp2.add(obj2);
		}

		return temp2;

	}

	// public static ArrayList get_DB_ResultArrayListOneDilmentN(String sql_str)
	// {
	// ArrayList temp = new ArrayList();
	//
	// DBService dbService = null;
	// // System.out.println(sql_str);
	// try {
	// dbService = DBManager.getDBService();
	// temp = dbService.executeQueryA(sql_str);
	// } catch (Exception ex) {
	// } finally {
	// dbService.closeConn();
	// }
	//
	// ArrayList temp2 = new ArrayList();
	// for (int i = 0; i < temp.size(); i++) {
	// ArrayList obj = (ArrayList) temp.get(i);
	// Object obj2 = (Object) obj.get(0);
	//
	// if (obj2 != null) {
	// temp2.add(obj2);
	// } else {
	// temp2.add("");
	// }
	// }
	//
	// return temp2;
	//
	// }

	/**
	 * 2007-08-23 ls
	 * 
	 * @param _temp
	 * @return
	 */
	public static String transferArrayLisToStringOneDimens(ArrayList _temp) {

		String str = "";
		if (_temp.size() != 0 && _temp.size() != 1) {
			for (int i = 0; i < _temp.size(); i++) {
				String str1 = (String) _temp.get(i);

				if (i == 0) {
					str = str1;
				} else if (i != _temp.size() - 1) {
					str = str + "," + str1;
				} else if (i == _temp.size() - 1) {
					str = str + "," + str1;
				}

			}
		} else if (_temp.size() == 1) {
			String str1 = (String) _temp.get(0);
			str = str1;
		} else {

			str = "";
		}

		return str;
	}

	public static String transferArrayLisToStringsOneDimens(ArrayList _temp) {

		String str = "";
		if (_temp.size() != 0 && _temp.size() != 1) {
			// YXY change 2010-08-26
			str = _temp.toString();
			str = "'" + str.substring(1, str.length() - 1) + "'";
			str = str.replaceAll(", ", "','");
			// END YXY
			// for (int i = 0; i < _temp.size(); i++) {
			// String str1 = (String) _temp.get(i);
			//
			// if (i == 0) {
			// str = "'" + str1 + "'";
			// } else if (i != _temp.size() - 1) {
			//
			// str = "'" + str + "'" + "," + "'" + str1 + "'";
			//
			// } else if (i == _temp.size() - 1) {
			//
			// str = "'" + str + "'" + "," + "'" + str1 + "'";
			//
			// }
			//
			// }
		} else if (_temp.size() == 1) {
			String str1 = (String) _temp.get(0);
			str = "'" + str1 + "'";
		} else {

			str = "''";
		}

		return str;
	}

	public static String transferArrayLisToStringTwoDimens(ArrayList _temp,
			boolean isvalue) {
		String flag = "";
		if (isvalue) {
			flag = "'";
		}

		String str = "";
		if (_temp.size() != 0 && _temp.size() != 1) {
			// //System.out.println("_temp # is:"+_temp);
			for (int i = 0; i < _temp.size(); i++) {
				ArrayList temp2 = (ArrayList) _temp.get(i);
				String str1 = (String) temp2.get(0);
				// //System.out.println("str1 # is:"+str1);

				if (i == 0) {
					str = str + flag + str1 + flag;
				} else if (i != _temp.size() - 1) {
					str = str + "," + flag + str1 + flag;
				} else if (i == _temp.size() - 1) {
					str = str + "," + flag + str1 + flag;
				}

			}
		} else if (_temp.size() == 1) {
			ArrayList temp2 = (ArrayList) _temp.get(0);
			String str1 = (String) temp2.get(0);
			str = flag + str1 + flag;
		} else {

			str = "";
		}

		return str;
	}

	/**
	 * 2008-02-13 shj
	 * 
	 * @param list
	 * @return
	 */
	public static String transferArrayListToString(ArrayList list) {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			Object obj = (Object) list.get(i);
			if (obj != null) {
				str = str + "'" + obj.toString() + "',";
			}
		}
		str = str.substring(0, str.lastIndexOf(","));
		return str;
	}

	public static String transferArrayLisToString(ArrayList _temp, int d) {
		String str = "";
		if (d == 2) {

			str = transferArrayLisToStringTwoDimens(_temp, true);

		} else if (d == 1) {

			str = transferArrayLisToStringOneDimens(_temp);
		}

		return str;
	}

	/**
	 * 2007-08-24 ls
	 * 
	 * @param twodimensList
	 * @param num
	 * @return
	 */
	public static ArrayList getTwoDimensNumValue(ArrayList twodimensList,
			int num) {
		ArrayList temp = new ArrayList();

		for (int i = 0; i < twodimensList.size(); i++) {

			ArrayList temp2 = (ArrayList) twodimensList.get(i);

			String str = (String) temp2.get(num);
			temp.add(str);
		}
		return temp;
	}

	/**
	 * 2007-09-11 ls
	 * 
	 * @param temp
	 * @return
	 */
	public static boolean isArrayElementAllSame(ArrayList temp) {
		boolean flag = false;

		for (int i = 0; i < temp.size(); i++) {
			if (i != 0 && i != temp.size() - 1) {
				String strcurr = (String) temp.get(i);
				String strnext = (String) temp.get(i + 1);

				if (strcurr != null && strcurr.equals(strnext)) {
					flag = false;

				} else {
					flag = true;
					return flag;
				}
			}
			// else if(i==temp.size()-1){
			// String strcurr = (String)temp.get(i);
			// String strold = (String)temp.get(i-1);
			//
			// if(strcurr!=null&&strcurr.equals(strold)){
			// flag = false;
			// }else{
			// flag = true;
			// return flag;
			// }
			// }
		}
		// System.out.println("flag is:"+flag);
		return flag;
	}

	/**
	 * 
	 * @param _temp
	 * @return
	 */
	public static String transferStrArrayLisToStringOneDimens(ArrayList _temp) {

		String str = "";
		if (_temp.size() != 0 && _temp.size() != 1) {
			for (int i = 0; i < _temp.size(); i++) {
				String str1 = (String) _temp.get(i);

				if (i == 0) {
					str = "'" + str1 + "'";
				} else if (i != _temp.size() - 1) {
					str = str + "," + "'" + str1 + "'";
				} else if (i == _temp.size() - 1) {
					str = str + "," + "'" + str1 + "'";
				}

			}
		} else if (_temp.size() == 1) {
			String str1 = (String) _temp.get(0);
			str = "'" + str1 + "'";
		} else {

			str = "";
		}

		return str;
	}

	/**
	 * 2007-09-20 ls
	 * 
	 * @param _temp
	 * @param d
	 * @param isNum
	 * @return
	 */
	public static String transferArrayLisToString(ArrayList _temp, int d,
			boolean isNum) {
		String str = "";
		if (d == 2) {

			str = transferArrayLisToStringTwoDimens(_temp, true);

		} else if (d == 1) {

			if (isNum) {

				str = transferArrayLisToStringOneDimens(_temp);
			} else {

				str = transferStrArrayLisToStringOneDimens(_temp);
			}

		}

		return str;
	}

	/**
	 * 
	 * @param vctor
	 * @param dimnum
	 * @return
	 */
	public static Vector transferArrayListToVector(ArrayList lists, int dimnum) {
		Vector vvctor = new Vector();

		if (dimnum == 2) {

			for (int i = 0; i < lists.size(); i++) {
				ArrayList list = (ArrayList) lists.get(i);
				Vector _vct = new Vector();
				for (int j = 0; j < list.size(); j++) {
					Object obj = (Object) list.get(j);
					_vct.add(obj);
				}
				vvctor.add(_vct);
			}
		}
		return vvctor;

	}

	private String getString(String str) {
		Date d = new Date();

		d.getDate();

		System.out.println("");
		return str;
	}

	/**
	 * 按照ftl模板读取数据
	 * 
	 * @param inputDir
	 *            目标文件路径，以\结尾
	 * @param inputFile
	 *            目标文件名
	 * @param ftlDir
	 *            ftl模板路径，以\结尾
	 * @param ftlFile
	 *            ftl模板名
	 * @param Encoding
	 *            编码
	 * @return 返回Map
	 */
	public static Map getdataByFtl(String inputDir, String inputFile,
			String ftlDir, String ftlFile, String Encoding) {
		Map alldata = new HashMap();
		try {
			FileParserObject fpo = new FileParserObject();
			fpo.setEncoding(Encoding);
			fpo.setInputDir(inputDir);
			fpo.setInputFile(inputFile);
			fpo.setParserDir(ftlDir);
			fpo.setParserFile(ftlFile);
			FileParser fp = new FileParser();
			fp.root = new HashMap();
			fp.setObj(fpo);
			printLog("开始读取模板内容, 文件路径是:" + fp.getObj().getParserDir()
					+ "   文件名称是:" + fp.getObj().getParserFile());
			ArrayList syntaxList = SyntaxReader.read(
					fp.getObj().getParserDir(), fp.getObj().getParserFile());
			printLog("模板内容" + syntaxList);
			ArrayList wordList = FileReader.read(fp.getObj().getInputDir(), fp
					.getObj().getInputFile(), fp.getObj().getEncoding(),
					new Date(), false);
			printLog("文件内容" + wordList);
			String virtualRecordID = "mainRecord";
			Context cursor = new Context();
			cursor.setSyntaxIndex(0);
			cursor.setWordIndex(0);
			cursor.setSyntaxAll(syntaxList);
			cursor.setWordAll(wordList);
			cursor.putRecordContext(virtualRecordID, fp.root);
			cursor.setRecordID(virtualRecordID);
			KD.IDP.etl.fileparser.match.Matcher.deal(cursor, null);

			// CommonTools.printLog(fp.getRoot().get("data"));
			alldata = fp.getRoot();
		} catch (Exception e) {
			e.printStackTrace();
			alldata = new HashMap();
		}
		return alldata;
	}

	/**
	 * 
	 * @param root
	 *            键为模板中的输出项，值为对应的数据
	 * @param outputDir
	 *            写的文件路径，以\结尾
	 * @param outputFile
	 *            写的文件名
	 * @param ftlDir
	 *            ftl模板路径，以\结尾
	 * @param ftlFile
	 *            ftl模板文件名
	 * @param Encoding
	 *            编码
	 */
	public static String writeFileByFtl(Map root, String outputDir,
			String outputFile, String ftlDir, String ftlFile, String Encoding) {
		FileWriter out = null;
		try {
			// ByteArrayOutputStream b = new ByteArrayOutputStream();
			// Writer out = new OutputStreamWriter(b);
			out = new FileWriter(outputDir + outputFile);
			Configuration freemarker_cfg = null;
			freemarker_cfg = new Configuration();
			freemarker_cfg.setNumberFormat("0.######");
			freemarker_cfg.setDirectoryForTemplateLoading(new File(ftlDir));
			freemarker.template.Template t = freemarker_cfg
					.getTemplate(ftlFile);
			t.process(root, out);
			// CommonTools.printLog(b);
		} catch (Exception e) {
			e.printStackTrace();
			printLog("写文件失败！");
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			return "false";
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return "true";
	}

	/**
	 * 2008-01-04 LS 21:19 : 2维数组转
	 * 
	 * @param twotierlist
	 * @param idistflag
	 * @param odistflag
	 * @return
	 */
	public static String getStrFromTwoTierArrayList(ArrayList twotierlist,
			String idistflag, String odistflag) {
		String str = "";
		for (int i = 0; i < twotierlist.size(); i++) {
			ArrayList _list = (ArrayList) twotierlist.get(i);
			String[] _strary = CallSuport.getStrFromArrayList(_list);
			String _str = CallSuport.getStringFromGroup(_strary, idistflag);

			str = str + _str + odistflag;
			// /System.out.println(_str);
		}
		return str;
	}

	/**
	 * 2008-01-04 LS
	 * 
	 * @param twotierlist
	 * @param idistflag
	 * @return
	 */
	public static ArrayList getOneTierArrayListFromTwoTierArrayListStr(
			ArrayList twotierlist, String idistflag) {
		ArrayList temp = new ArrayList();
		for (int i = 0; i < twotierlist.size(); i++) {
			ArrayList _list = (ArrayList) twotierlist.get(i);
			String[] _strary = CallSuport.getStrFromArrayList(_list);
			String _str = CallSuport.getStringFromGroup(_strary, idistflag);

			temp.add(_str);
		}
		return temp;
	}

	/**
	 * 2008-01-05 LS
	 * 
	 * @param list
	 * @param iFlag
	 * @param oFlag
	 * @return
	 */
	public static String getTwoHArrayListToString(ArrayList list, String iFlag,
			String oFlag) {
		ArrayList oneTierListFT = CommonTools
				.getOneTierArrayListFromTwoTierArrayListStr(list, iFlag);

		String[] _strary = CallSuport.getStrFromArrayList(oneTierListFT);

		String realstr2 = CommonTools.getRealStr(oFlag);

		String exportdatastr = CallSuport.getStringFromGroup(_strary, realstr2);
		// /System.out.println("### exportdatastr is:"+exportdatastr);
		return exportdatastr;
	}

	public static String getPsidpchartlocation() {
		return getInitParameter("PSIDP_Chart_LOCATION");
	}

	// public static void setPsidpchartlocation(String psidpchartlocation) {
	// CommonTools.psidpchartlocation = psidpchartlocation;
	// }

	public static String getPsidpportaldisplayname() {
		return getInitParameter("PSIDP_Portal_DisplayName");
	}

	// public static void setPsidpportaldisplayname(String
	// psidpportaldisplayname) {
	// CommonTools.psidpportaldisplayname = psidpportaldisplayname;
	// }

	public static String getPsidpDownloadFile() {
		return getInitParameter("downloadDir");
	}

	public static String replaceAllStr(String str1, String str2, String str3) {
		String laststr = "";

		String[] ary = str1.split("\\" + str2);

		for (int i = 0; i < ary.length; i++) {
			String _str = ary[i];

			if (i != ary.length - 1) {
				laststr = laststr + _str + str3;
			} else {
				laststr = laststr + _str;
			}
		}

		return laststr;
	}

	/**
	 * 2008-01-06 LS : 用于转义 \r\n
	 * 
	 * @param str
	 * @return
	 */
	public static String getRealStr(String str) {
		String a = "";

		String ad = "\\r\\n";

		if (str.equals(ad)) {
			a = "\r\n";
		} else {
			a = str;

		}
		return a;
	}

	public static void writeTest() {
		String dir = "F:\\temp\\";
		String name = "20080103162000.txt";
		String content = "福建  陕西  北京  河南\r\n福州  西安  北京  郑州\r\n";

		String a = "\\";
		String b = "r";
		String c = "\\";
		String d = "n";
		System.out.println("###  c is:" + c);
		String f = a + b + c + d;

		System.out.println("###  f is:" + f);

		String g = getRealStr(f);
		System.out.println(g);

		String flag11 = "\r\n";

		if (g.equals(flag11)) {
			System.out.println("true");

		} else {
			System.out.println("false");
		}

		String flag = f;

		String str1 = "福建  陕西  北京  河南";
		String str2 = "福州  西安  北京  郑州";

		String str3 = str1 + g + str2 + g;

		System.out.println(str3);

		String realfilepath = dir + name;

		try {
			FilesPack.writeTextFile(realfilepath, str3);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static ArrayList getReverseTwoDilArrayList(ArrayList _twoDilList) {
		ArrayList temp = new ArrayList();

		ArrayList innerlist = (ArrayList) _twoDilList.get(0);

		int innersize = innerlist.size();

		for (int i = 0; i < innersize; i++) {
			ArrayList _list1 = getLLLList(_twoDilList, i);
			temp.add(_list1);
		}
		return temp;
	}

	public static ArrayList getLLLList(ArrayList _twoDilList, int pnum) {
		ArrayList temp = new ArrayList();
		for (int i = 0; i < _twoDilList.size(); i++) {
			ArrayList temp1 = (ArrayList) _twoDilList.get(i);
			Object obj = (Object) temp1.get(pnum);
			temp.add(obj);
		}
		return temp;
	}

	public static ArrayList getTwoToOneDimensionList(ArrayList twoDimensionList) {
		ArrayList list = new ArrayList();

		for (int i = 0; i < twoDimensionList.size(); i++) {
			ArrayList insideList = (ArrayList) twoDimensionList.get(i);
			for (int j = 0; j < insideList.size(); j++) {
				String str = insideList.get(j).toString();
				list.add(str);
			}
		}
		return list;
	}

	/***************************************************************************
	 * The following examples show how date and time patterns are interpreted in
	 * the U.S. locale. The given date and time are 2001-07-04 12:08:56 local
	 * time in the U.S. Pacific Time time zone. <blockquote> <table border=0
	 * cellspacing=3 cellpadding=0 summary="Examples of date and time patterns
	 * interpreted in the U.S. locale">
	 * <tr bgcolor="#ccccff">
	 * <th align=left>Date and Time Pattern
	 * <th align=left>Result
	 * <tr>
	 * <td><code>"yyyy.MM.dd G
	 * 'at' HH:mm:ss z"</code>
	 * <td><code>2001.07.04 AD at 12:08:56 PDT</code>
	 * <tr bgcolor="#eeeeff">
	 * <td><code>"EEE, MMM d, ''yy"</code>
	 * <td><code>Wed,
	 * Jul 4, '01</code>
	 * <tr>
	 * <td><code>"h:mm a"</code>
	 * <td><code>12:08 PM</code>
	 * <tr bgcolor="#eeeeff">
	 * <td><code>"hh 'o''clock' a, zzzz"</code>
	 * <td><code>12
	 * o'clock PM, Pacific Daylight Time</code>
	 * <tr>
	 * <td><code>"K:mm a, z"</code>
	 * <td><code>0:08 PM, PDT</code>
	 * <tr bgcolor="#eeeeff">
	 * <td><code>"yyyyy.MMMMM.dd
	 * GGG hh:mm aaa"</code>
	 * <td><code>02001.July.04 AD 12:08 PM</code>
	 * <tr>
	 * <td><code>"EEE, d MMM yyyy HH:mm:ss Z"</code>
	 * <td><code>Wed, 4 Jul
	 * 2001 12:08:56 -0700</code>
	 * <tr bgcolor="#eeeeff">
	 * <td><code>"yyMMddHHmmssZ"</code>
	 * <td><code>010704120856-0700</code> </table> </blockquote>
	 */
	public static String getCurrentDate(String timeFormat) {
		String currentdate = "";

		// /Date nowss = new Date();

		// SimpleDateFormat ymdformat = new SimpleDateFormat("yyyy-MM-dd");

		// SimpleDateFormat ymformat = new SimpleDateFormat("yyyy-MM");

		Calendar cal = Calendar.getInstance();
		// /cal.add(cal.DATE, -1); --- lastday 2008-01-11 LS
		Date ddate = cal.getTime();

		if (timeFormat.equals("ymd")) {
			SimpleDateFormat ymdformat = new SimpleDateFormat("yyyy-MM-dd");// zjb20110309
			currentdate = ymdformat.format(ddate);
		} else if (timeFormat.equals("ym")) {
			SimpleDateFormat ymformat = new SimpleDateFormat("yyyy-MM");// zjb20110309
			currentdate = ymformat.format(ddate);
		} else if (timeFormat.equals("y")) {
			SimpleDateFormat yformat = new SimpleDateFormat("yyyy");// zjb20110309
			currentdate = yformat.format(ddate);
		} else if (timeFormat.equals("ymdhms")) {
			SimpleDateFormat ymdhmsformat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");// zjb20110309
			currentdate = ymdhmsformat.format(ddate);
		} else if (timeFormat.equals("ymdhm")) {
			SimpleDateFormat ymdhmformat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");// zjb20110309
			currentdate = ymdhmformat.format(ddate);
		} else if (timeFormat.equals("ymdhmsd")) {
			SimpleDateFormat ymdhmsdformat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss Z");// zjb20110309
			currentdate = ymdhmsdformat.format(ddate);
		} else if (timeFormat.equals("yyyymmddhhmmss")) {
			SimpleDateFormat ymdhmsdformat = new SimpleDateFormat(
					"yyyyMMddHHmmss");// zjb20110309
			currentdate = ymdhmsdformat.format(ddate);
		} else if (timeFormat.equals("HH:mm:ss")) {
			SimpleDateFormat ymdhmsdformat = new SimpleDateFormat("HH:mm:ss");// zjb20110309
			currentdate = ymdhmsdformat.format(ddate);
		} else if (timeFormat.equals("yyyyMMdd")) {
			SimpleDateFormat ymdhmsdformat = new SimpleDateFormat("yyyyMMdd");// YXQ20120111
			currentdate = ymdhmsdformat.format(ddate);
		}
		return currentdate;
	}

	public static void setOs_type(String os_type) {
		CommonTools.os_type = os_type;
	}

	public static String getOs_type() {
		return os_type;
	}

	/**
	 * 2008-03-08
	 * 
	 * @param sourcepath
	 * @return
	 */
	public static ArrayList getFileNamesBySource(String sourcepath) {
		ArrayList temp = new ArrayList();

		ArrayList fileLists = FilesPack.getFilePath(sourcepath);

		for (int i = 0; i < fileLists.size(); i++) {
			String allfile = (String) fileLists.get(i);
			String[] filess = allfile.split("/");
			String filename = filess[filess.length - 1];

			temp.add(filename);
		}
		return temp;
	}

	/**
	 * 2007-02-06 LS
	 * 
	 * @param dataList
	 *            ArrayList
	 * @return String
	 */
	public static String getAWDataFromArrayList(ArrayList dataList) {
		String dataDemo = "";
		String dataDemoStr = "";
		// System.out.println("dataList size is:"+dataList.size());

		if (dataList.size() != 0) {

			for (int j = 0; j < dataList.size(); j++) {
				String dataOne = "";
				Object obj = (Object) dataList.get(j);
				String objStr = "";
				if (obj != null) {
					objStr = obj.toString().replace('\r', ' ');
					objStr = objStr.replace('\n', ' ');
					objStr = objStr.replaceAll("[']", "\\\\'");

				} else {
					objStr = "";
				}
				// System.out.println("objStr is:"+objStr);
				if (j != dataList.size() - 1) {
					dataDemo = dataDemo + "'" + objStr + "', ";
				} else {
					dataDemo = dataDemo + "'" + objStr + "'";
				}
				// System.out.println("dataDemo is:" + dataDemo);
			}
			dataDemoStr = "[" + dataDemo + "]";

		} else {
			dataDemoStr = "['" + dataDemo + "']";
		}
		return dataDemoStr;
	}

	/**
	 * 自动获得序列 by SHJ
	 * 
	 * @2008-03-01
	 * @param sequencename
	 * @return
	 */
	public static long getSequenceNextValue(String sequencename) {
		String sql_str = "select " + sequencename + ".nextval from dual";
//		DBService db = DBManager.getDBService();
		long sequence = 0;
		try {
			List<Map<String, Object>> records = DBTemplate.getInstance().getResultMapList(sql_str);
			Map<String, Object> temp = (Map<String, Object>) records.get(0);
			sequence = Long.parseLong((String) temp.get("NEXTVAL"));
		} catch (Exception e) {
			e.printStackTrace();
		} 
//		finally {
//			db.closeConn();
//		}
		return sequence;

	}

	/**
	 * 为数组元素添加相同的字符串 2008-02-27 SHJ
	 * 
	 * @param str
	 * @param list
	 * @return
	 */
	public static ArrayList addStrOnList(String str, ArrayList list, int index) {
		ArrayList result = new ArrayList();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				String a = list.get(i).toString().trim();
				String b = "";
				if (index == 0) {
					b = str + a;
				} else if (index == 1000) {
					b = a + str;
				} else {
					b = a.substring(0, index) + str + a.substring(index);
				}
				result.add(b);
			}
		} else {
			result = list;
		}
		return result;
	}

	/**
	 * 判断一个java字符串是否为数字 2008-06-02 YXY
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return true是数字 false不是数字
	 */
	public static boolean isNumeric(String str) {
		str = str.replace('.', '0');
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 2008-06-20 LS
	 * 
	 * @param level
	 * @param name
	 * @param message
	 */
	public static void psidpwebdebuginfor(int level, String name, String message) {

		String flag = CommonTools.getPSIDPWEBDebug();

		int outrintlever = Integer.parseInt(flag);

		if (level <= outrintlever) {
			String currentdate = CommonTools.getCurrentDate("ymdhms");
			System.out
					.println("# PSIDP-WEB debug #"
							+ currentdate
							+ " level is: "
							+ level
							+ "  \r\n "
							+ name
							+ " is:  \r\n*********************start*********************   \r\n "
							+ message
							+ "  \r\n********************* end ********************* ");
		}

	}

	/**
	 * 
	 * @author LS 2010-6-24 上午11:02:20 TODO
	 * @param level
	 * @param name
	 * @param message
	 */
	public static void psidpwebdebuginfor(int level, String name, int message) {

		String flag = CommonTools.getPSIDPWEBDebug();

		int outrintlever = Integer.parseInt(flag);

		if (level <= outrintlever) {
			String currentdate = CommonTools.getCurrentDate("ymdhms");
			System.out
					.println("# PSIDP-WEB debug #"
							+ currentdate
							+ " level is: "
							+ level
							+ "  \r\n "
							+ name
							+ " is:  \r\n*********************start*********************   \r\n "
							+ message
							+ "  \r\n********************* end ********************* ");
		}

	}

	/**
	 * 判断一个字符串是否为null或为空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Collection<?> col) {
		if (col == null || col.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串str是否在字符串数组array中存在
	 * 
	 * @param str
	 * @param array
	 * @return
	 */
	public static boolean isStringExistInArray(String str,
			ArrayList<String> array) {
		if (isEmpty(str)) {
			return false;
		}
		if (array == null || array.isEmpty()) {
			return false;
		}
		String temp = null;
		for (int i = 0; i < array.size(); i++) {
			temp = array.get(i);
			if (str.equals(temp)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {

		String sourcename = "E:/ttt/";

		ArrayList alllist = CommonTools.getFileNamesBySource(sourcename);

		System.out.println("### alllist :" + alllist);
	}

	public static void setParameters(Hashtable parameters) {
		CommonTools.parameters = parameters;
	}

	public static Hashtable getParameters() {
		return parameters;
	}

	/**
	 * 2011-03-25 LS 取一个表的自增字段(一般为serialno)的下一个值
	 * 
	 * @param schema
	 *            用户
	 * @param tab
	 *            表名
	 * @param colName
	 *            自增列,缺省为: serialno
	 * @param serialNoType
	 *            序列号类型 如：调度临时交易 5306
	 * @return
	 */
	// public static long getNextSerialNo(String schema, String tab,
	// String colName, int serialNoType) {
	// String sql_str = "select max(" + colName + ")+1 from " + schema + "."
	// + tab + " ";
	// ArrayList next = CommonTools.get_DB_ResultArrayListOneDilment(sql_str);
	// if (next.size() != 0 && next.get(0) != null
	// && !next.get(0).equals("null") && !next.get(0).equals("")) {
	// return ((BigDecimal) next.get(0)).longValue();
	// } else {
	// Integer itg = new Integer(serialNoType);
	// return itg.longValue() * 100000000 + 1;
	// }
	// }
	// /**
	// * 处理默认值 若参数为dutydatetime 返回当班日期和时间 格式yyyy-mm-dd hh:mm:ss 其他返回原值 2011-6-24
	// * 14:58:50 zjb
	// *
	// * @param defaultvalue
	// * @param orgID
	// * @return
	// */
	// public static String newdefaultvalue(String defaultvalue, String orgID) {
	// String res = "";
	// if ("dutydatetime".equals(defaultvalue)) {
	// String date = "";
	// String dutyname;
	// ArrayList currentDuty = DDCommon.getCurrentDuty(orgID);
	// if (currentDuty != null && currentDuty.size() != 0) {
	// date = (String) currentDuty.get(5);
	// dutyname = (String) currentDuty.get(25);
	// String dutytimestart = DDCommon.getdutytime(orgID, dutyname);
	// res = date + " " + dutytimestart;
	// }
	// } else {
	// res = defaultvalue;
	// }
	// return res;
	// }
	/**
	 * 取出下一个id(该字段类型可以不是数值型的) 2011-7-15 lgc
	 * 
	 * @param seqName
	 * @return
	 */
	// public static Object getNextID(String seqName) {
	//
	// String sql_str = "select " + seqName + ".nextval from dual";
	//
	// DBService dbservice = null;
	//
	// ArrayList records = null;
	// try {
	// dbservice = DBManager.getDBService();
	// records = dbservice.executeQueryA(sql_str);
	// } catch (Exception ex) {
	// } finally {
	// dbservice.closeConn();
	// }
	// if (records.size() == 0) {
	// return new BigDecimal("1");
	// }
	//
	// ArrayList record = (ArrayList) records.get(0);
	// Object maxObj = record.get(0);
	// if (maxObj == null) {
	// return new BigDecimal("1");
	// }
	// Object nextValue = null;
	// if (maxObj instanceof BigDecimal) {
	// BigDecimal maxValue = (BigDecimal) maxObj;
	// nextValue = maxValue.add(new BigDecimal("1"));
	// } else if (maxObj instanceof Long) {
	// Long maxValue = (Long) maxObj;
	// nextValue = maxValue + 1;
	// }
	//
	// String date = CommonTools.getCurrentDate("ymdhms");
	//
	// CommonTools.psidpwebdebuginfor(0, seqName + date + " nextval",
	// nextValue.toString());
	//
	// return nextValue;
	// }
	/**
	 * 打印日期，默认debug级别 2011-11-01 YXY
	 * 
	 * @param obj
	 *            错误信息，或者exception对象
	 */
	public static void printLog(Object obj) {
		printLog("debug", obj);
	}

	/**
	 * 打印日志 2011-11-01 lm
	 * 
	 * @param level
	 *            分debug,info,warn,error,fatal五种,一般只用前四种
	 * @return void
	 */
	public static void printLog(String level, Object obj) {

		if ("debug".equalsIgnoreCase(level)) {
			logger.debug(obj);
		} else if ("info".equalsIgnoreCase(level)) {
			logger.info(obj);
		} else if ("warn".equalsIgnoreCase(level)) {
			logger.warn(obj);
		} else if ("error".equalsIgnoreCase(level)) {
			logger.error(obj);
		} else if ("fatal".equalsIgnoreCase(level)) {
			logger.fatal(obj);
		} else {
			logger.debug(obj);
		}
	}

	/**
	 * 读取配置文件 2011-11-21 14:47:36
	 * 
	 * @param path
	 * @param configname
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Properties getConfigMethod(String path, String configname) {
		Properties props = new Properties();
		try {
			InputStream is = CommonTools.class.getResourceAsStream(path
					+ configname);
			props.load(is);
			if (is != null) {
				is.close();
			}

			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String Property = props.getProperty(key);
			}

			return props;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 转译${username}这样的url或者是sql
	 * 
	 * @author YXY
	 * @param str
	 *            需要转换的字符
	 * @param key
	 *            关键字
	 * @param value
	 *            替换值
	 * @return 返回替换好的内容
	 */
	public static String escapePattern(String str, String key, String value) {
		String escaped = Pattern.quote("${" + key + "}");
		str = str.replaceAll(escaped, value);
		return str;
	}

	public static String getRandomString(int length) {
		StringBuffer buffer = new StringBuffer("0123456789");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++) {
			sb.append(buffer.charAt(r.nextInt(range)));
		}
		return sb.toString();
	}

	/**
	 * 生成ID
	 * 
	 * @param prefix
	 * @return
	 */
	public static String createId(String prefix) {
		return prefix + "_" + System.currentTimeMillis() + "_"
				+ getRandomString(4);
	}

	public static void setProjectRealPath(String realpath) {
		paramMap.put("realpath", realpath);
	}

	public static String getProjectRealPath() {
		String realpath = paramMap.get("realpath");
		if (realpath == null) {
			realpath = "";
		}
		return realpath;
	}

	public static void setContextPath(String path) {
		paramMap.put("contextpath", path);
	}

	public static String getContextPath() {
		String contextpath = paramMap.get("contextpath");
		if (contextpath == null) {
			contextpath = "";
		}
		return contextpath;
	}

	public static String[] listToArray(List<String> list) {
		String[] arr = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}

	/**
	 * 获得系统日期字符串格式
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getSystemDateStr(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}

}
