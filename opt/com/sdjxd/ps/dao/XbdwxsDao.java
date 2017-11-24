package com.sdjxd.ps.dao;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.sdjxd.pms.platform.base.MaxID;
import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.tool.Guid;


public class XbdwxsDao
{
	/**
	 * 构造方法
	 * @return void
	 * @author Wubq
	 * @date 2009-09
	 */
	public XbdwxsDao()
	{
	}

	/**———————————————————————————————
	 变量定义
	 * */

	/**
	 * daoLogger
	 * */
	private static Logger daoLogger = Logger.getLogger("EditGridDao");

	/**———————————————————————————————
	 pulice方法定义
	 * */
	/**
	 * 同步人员信息
	 * @return boolean
	 * @author Wubq
	 */
	public boolean SyncUserInfo()
	{
		List arrSql = new ArrayList();
		StringBuffer sql = new StringBuffer();
		
		//增
		sql.append("insert into h_xbxs.JXD7_XT_USER (USERID, COMPID, DEPTID, USERCODE, USERNAME, ");
		sql.append("BYNAME, PASSWD, SHOWORDER, ");
		sql.append("DATASTATUSID, STARTTIME, ENDTIME) ");
		sql.append("select T.USERID, '' AS COMPID,  T.DEPTID, T.USERCODE, T.USERNAME, ");
		sql.append("'' AS BYNAME, 'C4CA4238A0B923820DCC509A6F75849B' AS PASSWD, T.SHOWORDER, ");
		sql.append("1 AS DATASTATUSID, '1900-01-01' AS STARTTIME, '3000-12-31' AS ENDTIME ");
		sql.append("from h_xbxs.SG186_user T where USERID not in (select USERID from h_xbxs.jxd7_xt_user)");
		arrSql.add(sql.toString());
		
		//删
		sql.append("delete from h_xbxs.jxd7_xt_user ");
		sql.append("where USERID not in (select USERID from h_xbxs.SG186_user) ");
		arrSql.add(sql.toString());
		
		//执行Sql语句
		try
		{
			DbOper.executeNonQuery(arrSql);
		}
		catch (SQLException e)
		{
			StringBuffer logError = new StringBuffer(256);
			logError.append("巡视标准化作业, 错误为:");
			logError.append(e.getMessage());
			daoLogger.error(logError);
			return false;
		}
		
		while (arrSql.size() > 0)
		{
			arrSql.remove(0);
		}
		
		//改

		//执行Sql语句
		try
		{
			DbOper.executeNonQuery(arrSql);
		}
		catch (SQLException e)
		{
			StringBuffer logError = new StringBuffer(256);
			logError.append("巡视标准化作业, 错误为:");
			logError.append(e.getMessage());
			daoLogger.error(logError);
			return false;
		}

		return true;
	}

	/**
	 * SG186PMS，产生巡视抄录运行记录
	 * @param h_arrPara[0] 巡视任务类别，0：巡视抄录；1：验收任务
	 * @param h_arrPara[10,…] 任务ID
	 * @return boolean
	 * @author Wubq
	 */
	public boolean SG186PMS_yxjs_xs(String[] h_arrPara)
	{
		StringBuffer sql = new StringBuffer();
		List arrSql = new ArrayList();
		
		for (int i=10; i<h_arrPara.length; i++)
		{
			String oneRwId = h_arrPara[i];
			String oneObjId = oneRwId + "-00000";
			
			//判断是否已经添加运行记录
			if (this.isHaseRecord("mw_app.MWT_UD_SBDYX_XSJL", "obj_id = '" + oneObjId + "'") == false)
			{
				continue;
			}
			
			//得到任务信息
			sql.setLength(0);
			sql.append(" select A.*, B.BDZZJ, B.SSDS, B.YXBM ");
			sql.append(" from ").append("[S].ps_taskmsg A, [S].XBBZH_SG186_BDZ B");
			sql.append(" where A.SHEETID = '").append(oneRwId).append("'");
			sql.append(" and A.STATIONID = B.OBJ_ID");
			
			try
			{
				RowSet rs = DbOper.executeQuery(sql.toString());
				if (rs.next())
				{
					//MWT_UD_SBDYX_XSJL(巡视记录)
					sql.setLength(0);
					sql.append("INSERT INTO MW_APP.MWT_UD_SBDYX_XSJL ");
					sql.append("(OBJ_ID, OBJ_DISPIDX, BDZ, DW, KSSJ, JSSJ, XSLX, TQ, WD, XSQK, JL, XSR, OBJ_CAPTION)");
					sql.append(" valuse (");
					sql.append("'" + oneObjId + "', ");							//OBJ_ID
					sql.append("mw_sys.mwq_obj_dispidx.nextval, ");				//OBJ_DISPIDX
					sql.append("'" + rs.getString("STATIONID") + "', ");		//BDZ
					sql.append("'', ");		//DW
					sql.append("'" + rs.getString("PATROLSTARTTIME") + "', ");	//KSSJ
					sql.append("'" + rs.getString("PATROLENDTIME") + "', ");	//JSSJ
					sql.append("'', ");	//XSLX
					sql.append("'" + rs.getString("TIANQI") + "', ");			//TQ
					sql.append("'" + rs.getString("QIWEN") + "', ");			//WD
					sql.append("'PDA巡视', ");		//XSQK
					sql.append("'" + rs.getString("JIELUN") + "', ");			//JL
					sql.append("'" + rs.getString("PATROLUSER0") + " " + rs.getString("PATROLUSER1") + "', ");	//XSR
					sql.append("''");		//OBJ_CAPTION
					sql.append(")");
					arrSql.add(sql.toString());
					
					//MWT_UD_SBDYX_YXRZJL（巡视记录）
					sql.setLength(0);
					sql.append("INSERT INTO MW_APP.MWT_UD_SBDYX_YXRZJL ");
					sql.append("(OBJ_ID, OBJ_DISPIDX, JLRQ, JLNR, URLDZ, SFJS, JLR, DWHBDZ, JJBZJID, XGJLZJ, XGJLPZBS, FSSJ, DWHBDZMC, GZXXGL, SFWC, JLDW, OBJ_CAPTION)");
					sql.append(" valuse (");
					sql.append("'" + oneObjId + "', ");						//OBJ_ID
					sql.append("mw_sys.mwq_obj_dispidx.nextval, ");			//OBJ_DISPIDX
					sql.append("'current_timestamp(6)', ");					//JLRQ
					sql.append("'【PDA巡视】 开始时间：" + rs.getString("PATROLSTARTTIME") + "结束时间：" + rs.getString("PATROLENDTIME") + "结论：" + rs.getString("JIELUN") + "', ");		//JLNR
					sql.append("'MWT_SBDYX_SBXSJL.jsp', ");					//URLDZ
					sql.append("'T', ");	//SFJS
					sql.append("'" + rs.getString("STATIONID") + "', ");	//DWHBDZ
					sql.append("'" + this.getFieldValueStr("mw_app.MWT_UD_SBDYX_JJBJL", "OBJ_ID", "JJDW = '" + rs.getString("YXBM") + "' order by OBJ_DISPIDX desc") + "', ");	//JJBZJID
					sql.append("'" + oneObjId + "', ");						//XGJLZJ
					sql.append("'" + rs.getString("PATROLSTARTTIME") + "', ");	//XGJLZJ
					sql.append("'" + rs.getString("BDZZJ") + "', ");	//DWHBDZMC
					sql.append("'', ");		//GZXXGL
					sql.append("'T', ");		//SFWC
					sql.append("'" + rs.getString("YXBM") + "', ");			//JLDW
					sql.append("''");		//OBJ_CAPTION
					sql.append(")");
					arrSql.add(sql.toString());
				}
			}
			catch (SQLException e)
			{
				daoLogger.error("巡视标准化作业, 错误为:" + e.getMessage() + "\nsql为：" + sql.toString());
				continue;
			}
		}
		
		return true;
	}

	/**
	 * SG186PMS，产生验收任务运行记录
	 * @param h_arrPara[0] 巡视任务类别，0：巡视抄录；1：验收任务
	 * @param h_arrPara[10,…] 任务ID
	 * @return boolean
	 * @author Wubq
	 */
	public boolean SG186PMS_yxjs_ys(String[] h_arrPara)
	{
		return true;
	}
	
	/**
	 * 判断记录是否存在
	 * @param h_tableName 表名称
	 * @param h_condition 条件
	 * @return boolean
	 * @author Wubq
	 */
	public boolean isHaseRecord(String h_tableName, String h_condition)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select 1 from ").append(h_tableName);
		sql.append(" where ").append(h_condition);
		
		try
		{
			RowSet rs = DbOper.executeQuery(sql.toString());
			if (rs.next())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			daoLogger.error("巡视标准化作业, 错误为:" + e.getMessage() + "\nsql为：" + sql.toString());
			return false;
		}
	}
	
	/**
	 * 判断记录是否存在
	 * @param h_tableName 表名称
	 * @param h_fields 字段
	 * @param h_condition 条件
	 * @return RowSet
	 * @author Wubq
	 */
	public RowSet getRowSet(String h_tableName, String h_fields, String h_condition)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select ").append(h_fields);
		sql.append(" from ").append(h_tableName);
		sql.append(" where ").append(h_condition);
		
		try
		{
			RowSet rs = DbOper.executeQuery(sql.toString());
			return rs;
		}
		catch (SQLException e)
		{
			daoLogger.error("巡视标准化作业, 错误为:" + e.getMessage() + "\nsql为：" + sql.toString());
			return null;
		}
	}
	
	//得到表的字段值
	private String getFieldValueStr(String h_talbeName, String h_fieldName, String h_condition)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select ").append(h_fieldName);
		sql.append(" from ").append(h_talbeName);
		sql.append(" where ").append(h_condition);
		
		try
		{
			RowSet rs = DbOper.executeQuery(sql.toString());
			if (rs.next())
			{
				String strValue = rs.getString(h_fieldName);
				if (strValue == null)
				{
					return "";
				}
				
				return strValue;
			}
			else
			{
				return "";
			}
		}
		catch (SQLException e)
		{
			daoLogger.error("巡视标准化作业, 错误为:" + e.getMessage() + "\nsql为：" + sql);
			return "";
		}
	}

}
