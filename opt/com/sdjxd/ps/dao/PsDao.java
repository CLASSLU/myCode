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

public class PsDao
{
	/** 
	 * 构造方法
	 * @return void
	 * @author Wubq
	 * @date 2009-09
	 */
	public PsDao()
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

//	———————————[ 任务模板 ]—————————————

	/**
	 * 任务模板，删除模板
	 * @return boolean
	 * @author Wubq
	 */
	public boolean renwumobanDeleteMoban(String[] h_arrMb)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arrMb.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arrMb[i];

			//删除模板
			sql.setLength(0);
			sql.append("delete from [S].PS_TASKPATTERNMSG where sheetid = '").append(oneItem).append("'");
			arrSql.add(sql.toString());

			//删除模板巡视项
			sql.setLength(0);
			sql.append("delete from [S].PS_ITEMPATTERNMSG where XSPATTERNID = '").append(oneItem).append("'");
			arrSql.add(sql.toString());

			//删除模板抄录项
			sql.setLength(0);
			sql.append("delete from [S].PS_RECORDPATTERNMSG where XSPATTERNID = '").append(oneItem).append("'");
			arrSql.add(sql.toString());
		}

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
	 * 任务模板，增加设备
	 * @return boolean
	 * @author Wubq
	 */
	public boolean addRenwumobanShebei(String h_mbId, String[] h_arrSb)
	{
		for (int i=0; i<h_arrSb.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arrSb[i];
			sql.append("insert into [S].PS_ITEMPATTERNMSG ");
			sql.append("(SHEETID, XSPATTERNID, BARCODEEQID, SHOWORDER, ITEMID, TMSXH, SBSXH)");
			sql.append(" values ");
			sql.append("(");
			sql.append("'" + Guid.create() + "', ");
			sql.append("'" + h_mbId + "', ");
			sql.append("'" + oneItem + "', ");
			sql.append("-1, ");
			sql.append("'-1', ");
			sql.append(renwumobanGetTiaomaOrder(h_mbId, oneItem)).append(", ");
			sql.append(MaxID.getMaxID("PS_ITEMPATTERNMSG", "SBSXH"));
			sql.append(")");

			//执行Sql语句
			try
			{
				DbOper.executeNonQuery(sql.toString());
			}
			catch (SQLException e)
			{
				StringBuffer logError = new StringBuffer(256);
				logError.append("巡视标准化作业, 错误为:");
				logError.append(e.getMessage());
				daoLogger.error(logError);
				return false;
			}
		}

		return true;
	}

	/**
	 * 任务模板，得到条码顺序号
	 * @return boolean
	 * @author Wubq
	 */
	public int renwumobanGetTiaomaOrder(String h_mbId, String h_tsgxId)
	{
		StringBuffer sql = new StringBuffer();

		sql.append("select A.TMSXH from PS_V_MBTMINFO A, PS_BARCODEDEVMSG B ");
		sql.append("where A.TMID = B.BARCODEID ");
		sql.append("AND A.XSPATTERNID = '").append(h_mbId).append("' ");
		sql.append("AND B.SHEETID = '").append(h_tsgxId).append("' ");

		try
		{
			RowSet rs = DbOper.executeQuery(sql.toString());
			if (rs.next())
			{
				return rs.getInt("TMSXH");
			}
			else
			{
				return MaxID.getMaxID("PS_ITEMPATTERNMSG", "TMSXH");
			}
		}
		catch (SQLException e)
		{
			daoLogger.error("巡视标准化作业, 错误为:" + e.getMessage() + "\nsql为：" + sql);
			return MaxID.getMaxID("PS_ITEMPATTERNMSG", "TMSXH");
		}
	}

	/**
	 * 验收任务，增加设备
	 * @return boolean
	 * @author ZYG 2009-12-09
	 */
	public boolean addYsTaskShebei(String h_ysTaskId, String[] h_arrYsPatternID)
	{
		List arrSql = null;

		//ZYG 2009-12-11
		for (int i=0; i<h_arrYsPatternID.length; i++)
		{
			arrSql = new ArrayList();
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arrYsPatternID[i];
			//查询当前的模板所在的设备号
			String strSql = "SELECT DISTINCT SBXH FROM PS_YSITEMMSG WHERE YSTASKID='"+ h_ysTaskId + "' AND YSEQID IN (select eqid from ps_yseqpatternmsg where sheetid ='" + oneItem + "')";
			RowSet rs;
			String strSBXH = "";
			try
			{
				rs = DbOper.executeQuery(strSql);
				while (rs.next())
				{
					strSBXH = rs.getString("SBXH");
				}
				rs.close();
			}
			catch (SQLException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if("" == strSBXH)
			{
				sql.append("insert into [S].PS_YSITEMMSG ");
				sql.append("(SHEETID, YSTASKID, YSEQID, YSITEMID, YSITEMNAME, SBXH, SHOWORDER,YSPATTERNID)");
				sql.append(" SELECT　MW_SYS.NEWGUID() AS NEWID, ");
				sql.append("'" + h_ysTaskId + "',");
				sql.append("YSEQID,YSITEMID,YSITEMNAME,");
				sql.append(MaxID.getMaxID("PS_YSITEMMSG", "SBXH")).append(", ");
				sql.append("SHOWORDER,YSPATTERNID  FROM PS_YSEQITEM");
				sql.append(" WHERE  YSPATTERNID= '" + oneItem + "'");
				arrSql.add(sql.toString());
			}
			else
			{
				sql.append("insert into [S].PS_YSITEMMSG ");
				sql.append("(SHEETID, YSTASKID, YSEQID, YSITEMID, YSITEMNAME, SBXH, SHOWORDER,YSPATTERNID)");
				sql.append(" SELECT　MW_SYS.NEWGUID() AS NEWID, ");
				sql.append("'" + h_ysTaskId + "',");
				sql.append("YSEQID,YSITEMID,YSITEMNAME,");
				sql.append("'" + strSBXH + "',");
				sql.append("SHOWORDER,YSPATTERNID  FROM PS_YSEQITEM");
				sql.append(" WHERE  YSPATTERNID= '" + oneItem + "'");
				arrSql.add(sql.toString());
			}

			//添加完一个以后，直接将数据插入到数据库
			//执行Sql语句
			try
			{
				DbOper.executeNonQuery(arrSql);
			}
			catch (SQLException e)
			{
				StringBuffer logError = new StringBuffer(256);
				logError.append("验收任务【增加设备】, 错误为:");
				logError.append(e.getMessage());
				daoLogger.error(logError);
				return false;
			}
		}
		return true;
	}

	/**
	 * 任务模板，删除设备
	 * @return boolean
	 * @author Wubq
	 */
	public boolean RenwumobanDeleteSb(String h_mbId, String[] h_arrSb)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arrSb.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arrSb[i];

			sql.append("delete from [S].PS_ITEMPATTERNMSG ");
			sql.append("where XSPATTERNID = '").append(h_mbId).append("' ");
			sql.append("and BARCODEEQID = '").append(oneItem).append("'");
			arrSql.add(sql.toString());

			sql.setLength(0);
			sql.append("delete from [S].PS_RECORDPATTERNMSG ");
			sql.append("where XSPATTERNID = '").append(h_mbId).append("' ");
			sql.append("and BARCODEEQID = '").append(oneItem).append("'");
			arrSql.add(sql.toString());
		}

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
	 * 验收任务，删除设备
	 * @return boolean
	 * @author ZYG 2009-12-08
	 */
	public boolean ysTaskDeleteSb(String h_taskId, String[] h_arrSb)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arrSb.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arrSb[i];

			sql.append("delete from [S].PS_YSITEMMSG ");
			sql.append("where YSTASKID = '").append(h_taskId).append("' ");
			sql.append("and YSEQID = '").append(oneItem).append("'");
			arrSql.add(sql.toString());
		}

		//执行Sql语句
		try
		{
			DbOper.executeNonQuery(arrSql);
		}
		catch (SQLException e)
		{
			StringBuffer logError = new StringBuffer(256);
			logError.append("验收任务，删除设备, 错误为:");
			logError.append(e.getMessage());
			daoLogger.error(logError);
			return false;
		}

		return true;
	}

	/**
	 * 任务模板，得到顺序号
	 * @return boolean
	 * @author Wubq
	 */
	public List getRenwumobanXSH(String h_mbId, String h_tsgxid)
	{
		List arr = new ArrayList();

		StringBuffer sql = new StringBuffer();
		sql.append("select TMSXH, SBSXH ");
		sql.append("from [S].PS_V_MBTM ");
		sql.append("where XSPATTERNID = '").append(h_mbId).append("'");
		sql.append("and TSGXID = '").append(h_tsgxid).append("'");

		try
		{
			RowSet rs = DbOper.executeQuery(sql.toString());
			if (rs.next())
			{
				arr.add(rs.getInt("TMSXH"));
				arr.add(rs.getInt("SBSXH"));
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e)
		{
			daoLogger.error("巡视标准化作业, 错误为:" + e.getMessage() + "\nsql为：" + sql);
			return null;
		}

		return arr;
	}

	/**
	 * 任务模板，增加项，巡视
	 * @return boolean
	 * @author Wubq
	 */
	public boolean addRenwumobanXiang_xunshi(String h_mbId, String h_tsgxid, String[] h_arr, Object nTMSXH, Object nSBSXH)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arr.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arr[i];

			sql.append("insert into [S].PS_ITEMPATTERNMSG ");
			sql.append("(SHEETID, XSPATTERNID, BARCODEEQID, SHOWORDER, ITEMID, TMSXH, SBSXH)");
			sql.append(" values ");
			sql.append("(");
			sql.append("'" + Guid.create() + "', ");
			sql.append("'" + h_mbId + "', ");
			sql.append("'" + h_tsgxid + "', ");
			sql.append(MaxID.getMaxID("PS_ITEMPATTERNMSG", "SHOWORDER")).append(", ");
			sql.append("'" + oneItem +  "', ");
			sql.append(nTMSXH).append(", ");
			sql.append(nSBSXH).append(")");

			arrSql.add(sql.toString());
		}

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
	 * 任务模板，增加项，验收
	 * @return boolean
	 * @author ZYG 2009-12-08

	public boolean addYsTaskItem(String h_ysTaskID, String h_sbID, String[] h_arr)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arr.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arr[i];
			sql.append("insert into [S].PS_YSITEMMSG ");
			sql.append("(SHEETID, YSTASKID, YSEQID, YSITEMID, YSITEMNAME, SBXH, SHOWORDER,YSPATTERNID)");
			sql.append(" SELECT　MW_SYS.NEWGUID() AS NEWID, ");
			sql.append("'" + h_ysTaskID + "',");
			sql.append("'" + h_sbID + "',");
			sql.append("YSITEMID,YSITEMNAME,");
			sql.append(MaxID.getMaxID("PS_YSITEMMSG", "SBXH")).append(", ");
			sql.append("-1,YSPATTERNID  FROM PS_YSEQITEM");
			sql.append(" WHERE  YSPATTERNID= '" + oneItem + "'");

			arrSql.add(sql.toString());
		}

		//执行Sql语句
		try
		{
			DbOper.executeNonQuery(arrSql);
		}
		catch (SQLException e)
		{
			StringBuffer logError = new StringBuffer(256);
			logError.append("验收任务添加项目, 错误为:");
			logError.append(e.getMessage());
			daoLogger.error(logError);
			return false;
		}

		return true;
	}	 */

	/**
	 *  任务模板，增加项，抄录
	 * @return boolean
	 * @author Wubq
	 */
	public boolean addRenwumobanXiang_chaolu(String h_mbId, String h_tsgxid, String[] h_arr, Object nTMSXH, Object nSBSXH)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arr.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arr[i];

			sql.append("insert into [S].PS_RECORDPATTERNMSG ");
			sql.append("(SHEETID, XSPATTERNID, BARCODEEQID, SHOWORDER, PARAMID, TMSXH, SBSXH)");
			sql.append(" values ");
			sql.append("(");
			sql.append("'" + Guid.create() + "', ");
			sql.append("'" + h_mbId + "', ");
			sql.append("'" + h_tsgxid + "', ");
			sql.append(MaxID.getMaxID("PS_RECORDPATTERNMSG", "SHOWORDER")).append(", ");
			sql.append("'" + oneItem +  "', ");
			sql.append(nTMSXH).append(", ");
			sql.append(nSBSXH).append(")");

			arrSql.add(sql.toString());
		}

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
	 *  任务模板，删除项
	 * @return boolean
	 * @author Wubq
	 */
	public boolean RenwumobanDeleteItem(String h_tableName, String h_field, String[] h_arr)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arr.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arr[i];

			sql.append("delete from [S].").append(h_tableName);
			sql.append(" where ").append(h_field).append(" = '").append(oneItem).append("'");

			arrSql.add(sql.toString());
		}

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
	 * 任务模板，交换条码顺序号
	 * @param h_mbId 模板ID
	 * @param h_srcTiaomaId 源条码ID
	 * @param h_orgTiaomaId 目标条码ID
	 * @return boolean
	 * @author Wubq
	 */
	public boolean renwumobanChangeTiaomaOrder(String h_mbId, String h_srcTiaomaId, String h_orgTiaomaId)
	{
		String condition = "";
		StringBuffer sql = new StringBuffer();

		//得到原始序号
		condition = "XSPATTERNID = '" + h_mbId + "' and TMID = '" + h_srcTiaomaId + "'";
		int srcOrder = this.getFieldValueInt("PS_V_MBTMINFO", "TMSXH", condition);

		condition = "XSPATTERNID = '" + h_mbId + "' and TMID = '" + h_orgTiaomaId + "'";
		int orgOrder = this.getFieldValueInt("PS_V_MBTMINFO", "TMSXH", condition);

		if((srcOrder < 0) || (orgOrder < 0))
		{
			return false;
		}

		//得到修改Sql语句
		List arrSql = new ArrayList();

		//修改源记录
		condition = "(select SHEETID from [S].PS_BARCODEDEVMSG where BARCODEID = '" + h_srcTiaomaId + "')";

		sql.setLength(0);
		sql.append(" update [S].PS_ITEMPATTERNMSG set TMSXH = ").append(orgOrder);
		sql.append(" where XSPATTERNID = '").append(h_mbId).append("' ");
		sql.append(" and BARCODEEQID in ").append(condition);
		arrSql.add(sql.toString());

		sql.setLength(0);
		sql.append(" update [S].PS_RECORDPATTERNMSG set TMSXH = ").append(orgOrder);
		sql.append(" where XSPATTERNID = '").append(h_mbId).append("' ");
		sql.append(" and BARCODEEQID in ").append(condition);
		arrSql.add(sql.toString());

		//修改目标记录
		condition = "(select SHEETID from [S].PS_BARCODEDEVMSG where BARCODEID = '" + h_orgTiaomaId + "')";

		sql.setLength(0);
		sql.append(" update [S].PS_ITEMPATTERNMSG set TMSXH = ").append(srcOrder);
		sql.append(" where XSPATTERNID = '").append(h_mbId).append("' ");
		sql.append(" and BARCODEEQID in ").append(condition);
		arrSql.add(sql.toString());

		sql.setLength(0);
		sql.append(" update [S].PS_RECORDPATTERNMSG set TMSXH = ").append(srcOrder);
		sql.append(" where XSPATTERNID = '").append(h_mbId).append("' ");
		sql.append(" and BARCODEEQID in ").append(condition);
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

		return true;
	}

	/**
	 * 任务模板，交换设备顺序号
	 * @param h_mbId 模板ID
	 * @param h_srcTsgxId 源条码设备关系ID
	 * @param h_orgTsgxId 目标条码设备关系ID
	 * @return boolean
	 * @author Wubq
	 */
	public boolean renwumobanChangeShebeiOrder(String h_mbId, String h_srcTsgxId, String h_orgTsgxId)
	{
		String condition = "";
		StringBuffer sql = new StringBuffer();

		//得到原始序号
		condition = "XSPATTERNID = '" + h_mbId + "' and TSGXID = '" + h_srcTsgxId + "'";
		int srcOrder = this.getFieldValueInt("PS_V_MBSBINFO", "SBSXH", condition);

		condition = "XSPATTERNID = '" + h_mbId + "' and TSGXID = '" + h_orgTsgxId + "'";
		int orgOrder = this.getFieldValueInt("PS_V_MBSBINFO", "SBSXH", condition);

		if((srcOrder < 0) || (orgOrder < 0))
		{
			return false;
		}

		//得到修改Sql语句
		List arrSql = new ArrayList();

		//修改源记录
		sql.setLength(0);
		sql.append(" update [S].PS_ITEMPATTERNMSG set SBSXH = ").append(orgOrder);
		sql.append(" where XSPATTERNID = '").append(h_mbId).append("' ");
		sql.append(" and BARCODEEQID = '").append(h_srcTsgxId).append("' ");
		arrSql.add(sql.toString());

		sql.setLength(0);
		sql.append(" update [S].PS_RECORDPATTERNMSG set SBSXH = ").append(orgOrder);
		sql.append(" where XSPATTERNID = '").append(h_mbId).append("' ");
		sql.append(" and BARCODEEQID = '").append(h_srcTsgxId).append("' ");
		arrSql.add(sql.toString());

		//修改目标记录
		sql.setLength(0);
		sql.append(" update [S].PS_ITEMPATTERNMSG set SBSXH = ").append(srcOrder);
		sql.append(" where XSPATTERNID = '").append(h_mbId).append("' ");
		sql.append(" and BARCODEEQID = '").append(h_orgTsgxId).append("' ");
		arrSql.add(sql.toString());

		sql.setLength(0);
		sql.append(" update [S].PS_RECORDPATTERNMSG set SBSXH = ").append(srcOrder);
		sql.append(" where XSPATTERNID = '").append(h_mbId).append("' ");
		sql.append(" and BARCODEEQID = '").append(h_orgTsgxId).append("' ");
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

		return true;
	}


//	———————————[ 任务 ]—————————————

	/**
	 * 任务，删除任务
	 * @return boolean
	 * @author Wubq
	 */
	public boolean renwumobanDeleteRenwu(String[] h_arrRw)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arrRw.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arrRw[i];

			//删除任务
			sql.setLength(0);
			sql.append("delete from [S].PS_TASKMSG where sheetid = '").append(oneItem).append("'");
			arrSql.add(sql.toString());

			//删除任务巡视项
			sql.setLength(0);
			sql.append("delete from [S].PS_TASKITEMMSG where TASKID = '").append(oneItem).append("'");
			arrSql.add(sql.toString());

			//删除任务抄录项
			sql.setLength(0);
			sql.append("delete from [S].PS_TASKRECORDMSG where TASKID = '").append(oneItem).append("'");
			arrSql.add(sql.toString());
		}

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
	 * 任务，新建任务
	 * @return boolean
	 * @author Wubq
	 */
	public String newRenwu(String h_bdzId, String h_mbId)
	{
		List arrSql = null;
		arrSql = new ArrayList();
		String taskId = Guid.create();

		//复制模板模板信息
		StringBuffer sql = new StringBuffer();
		sql.append(" select *");
		sql.append(" from [S].").append("PS_TASKPATTERNMSG");
		sql.append(" where SHEETID = '").append(h_mbId).append("'");
		try
		{
			RowSet rs = DbOper.executeQuery(sql.toString());
			if (rs.next())
			{
				sql.setLength(0);
				sql.append("insert into PS_TASKMSG (SHEETID, SHOWORDER, TASKNAME, PATROLPROPERTYID, ");
				sql.append("STATIONID, TASKSTATUS, NOTICEITEM, PDAID )");
				sql.append("values(");
				sql.append("'").append(taskId).append("', ");
				sql.append(MaxID.getMaxID("PS_TASKPATTERNMSG", "SHOWORDER")).append(", ");
				sql.append("'").append(rs.getString("PATTERNNAME")).append("', ");
				sql.append(rs.getInt("PATROLTASKPROPERTYID")).append(", ");
				sql.append("'").append(h_bdzId).append("', ");
				sql.append(0).append(", ");
				sql.append("'").append(rs.getString("NOTICEITEM")).append("', ");
				sql.append("'").append(rs.getString("PDAID")).append("'");
				sql.append(")");

				arrSql.add(sql.toString());
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

		//巡视项
		sql.setLength(0);
		sql.append("insert into [S].PS_TASKITEMMSG (SHEETID, SHOWORDER, TASKID, BARCODEEQID, ITEMID, ITEMNAME, TMSXH, SBSXH) ");
		sql.append("select MW_SYS.NEWGUID(), ");
		sql.append("SHOWORDER, ");
		sql.append("'").append(taskId).append("' , ");
		sql.append("BARCODEEQID, ");
		sql.append("ITEMID, ");
		sql.append("ITEMNAME, ");
		sql.append("TMSXH, ");
		sql.append("SBSXH ");
		sql.append("from [S].PS_V_MBXSXINFO ");
		sql.append("where XSPATTERNID ='").append(h_mbId).append("'");
		arrSql.add(sql.toString());

		//抄录项
		sql.setLength(0);
		sql.append("insert into [S].PS_TASKRECORDMSG (SHEETID, SHOWORDER, TASKID, BARCODEEQID, PARAMID, PARAMNAME, TMSXH, SBSXH, DW, SX, XX) ");
		sql.append("select MW_SYS.NEWGUID(), ");
		sql.append("SHOWORDER, ");
		sql.append("'").append(taskId).append("' , ");
		sql.append("BARCODEEQID, ");
		sql.append("PARAMID, ");
		sql.append("PARAMNAME, ");
		sql.append("TMSXH, ");
		sql.append("SBSXH, ");
		sql.append("DW, ");
		sql.append("SX, ");
		sql.append("XX ");
		sql.append("from [S].PS_V_MBXCLINFO ");
		sql.append("where XSPATTERNID ='").append(h_mbId).append("'");
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
			return "";
		}

		return taskId;
	}

	/**
	 * 任务，增加设备
	 * @return boolean
	 * @author Wubq
	 */
	public boolean addRenwuShebei(String h_rwId, String[] h_arrSb)
	{
		for (int i=0; i<h_arrSb.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arrSb[i];
			sql.append("insert into [S].PS_TASKITEMMSG ");
			sql.append("(SHEETID, TASKID, BARCODEEQID, SHOWORDER, ITEMID, ITEMNAME, TMSXH, SBSXH)");
			sql.append(" values ");
			sql.append("(");
			sql.append("'" + Guid.create() + "', ");
			sql.append("'" + h_rwId + "', ");
			sql.append("'" + oneItem + "', ");
			sql.append("-1, ");
			sql.append("'-1', ");
			sql.append("'-1', ");
			sql.append(renwuGetTiaomaOrder(h_rwId, oneItem)).append(", ");
			sql.append(MaxID.getMaxID("PS_ITEMPATTERNMSG", "SBSXH"));	//为了和模板统一，取模板的号
			sql.append(")");

			//执行Sql语句
			try
			{
				DbOper.executeNonQuery(sql.toString());
			}
			catch (SQLException e)
			{
				StringBuffer logError = new StringBuffer(256);
				logError.append("巡视标准化作业, 错误为:");
				logError.append(e.getMessage());
				daoLogger.error(logError);
				return false;
			}
		}

		return true;
	}

	/**
	 * 任务，得到条码顺序号
	 * @return boolean
	 * @author Wubq
	 */
	public int renwuGetTiaomaOrder(String h_rwId, String h_tsgxId)
	{
		StringBuffer sql = new StringBuffer();

		sql.append("select A.TMSXH from PS_V_RWTMINFO A, PS_BARCODEDEVMSG B ");
		sql.append("where A.TMID = B.BARCODEID ");
		sql.append("AND A.TASKID = '").append(h_rwId).append("' ");
		sql.append("AND B.SHEETID = '").append(h_tsgxId).append("' ");

		try
		{
			RowSet rs = DbOper.executeQuery(sql.toString());
			if (rs.next())
			{
				return rs.getInt("TMSXH");
			}
			else
			{
				return MaxID.getMaxID("PS_ITEMPATTERNMSG", "TMSXH");	//为了和模板统一，取模板的号
			}
		}
		catch (SQLException e)
		{
			daoLogger.error("巡视标准化作业, 错误为:" + e.getMessage() + "\nsql为：" + sql);
			return MaxID.getMaxID("PS_ITEMPATTERNMSG", "TMSXH");	//为了和模板统一，取模板的号
		}
	}

	/**
	 * 任务，删除设备
	 * @return boolean
	 * @author Wubq
	 */
	public boolean RenwuDeleteSb(String h_rwId, String[] h_arrSb)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arrSb.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arrSb[i];

			sql.append("delete from [S].PS_TASKITEMMSG ");
			sql.append("where TASKID = '").append(h_rwId).append("' ");
			sql.append("and BARCODEEQID = '").append(oneItem).append("'");
			arrSql.add(sql.toString());

			sql.setLength(0);
			sql.append("delete from [S].PS_TASKRECORDMSG ");
			sql.append("where TASKID = '").append(h_rwId).append("' ");
			sql.append("and BARCODEEQID = '").append(oneItem).append("'");
			arrSql.add(sql.toString());
		}

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
	 * 任务，得到顺序号
	 * @return boolean
	 * @author Wubq
	 */
	public List getRenwuXSH(String h_rwId, String h_tsgxid)
	{
		List arr = new ArrayList();

		StringBuffer sql = new StringBuffer();
		sql.append("select TMSXH, SBSXH ");
		sql.append("from [S].PS_TASKITEMMSG ");
		sql.append("where TASKID = '").append(h_rwId).append("'");
		sql.append("and BARCODEEQID = '").append(h_tsgxid).append("'");

		try
		{
			RowSet rs = DbOper.executeQuery(sql.toString());
			if (rs.next())
			{
				arr.add(rs.getInt("TMSXH"));
				arr.add(rs.getInt("SBSXH"));
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e)
		{
			daoLogger.error("巡视标准化作业, 错误为:" + e.getMessage() + "\nsql为：" + sql);
			return null;
		}

		return arr;
	}

	/**
	 * 任务，增加项，巡视
	 * @return boolean
	 * @author Wubq
	 */
	public boolean addRenwuXiang_xunshi(String h_rwId, String h_tsgxid, String[] h_arr, Object nTMSXH, Object nSBSXH)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arr.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arr[i];

			sql.append("insert into [S].PS_TASKITEMMSG ");
			sql.append("(SHEETID, TASKID, BARCODEEQID, SHOWORDER, ITEMID, ITEMNAME, TMSXH, SBSXH)");
			sql.append(" values ");
			sql.append("(");
			sql.append("'" + Guid.create() + "', ");
			sql.append("'" + h_rwId + "', ");
			sql.append("'" + h_tsgxid + "', ");
			sql.append(MaxID.getMaxID("PS_ITEMPATTERNMSG", "SHOWORDER")).append(", ");//为了和模板统一，取模板的号
			sql.append("'" + oneItem +  "', ");
			sql.append("'" + this.getFieldValueStr("PS_DEVXSJBNR", "ITEMNAME", "SHEETID='" + oneItem + "'") +  "', ");
			sql.append(nTMSXH).append(", ");
			sql.append(nSBSXH).append(")");

			arrSql.add(sql.toString());
		}

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
	 *  任务，增加项，抄录
	 * @return boolean
	 * @author Wubq
	 */
	public boolean addRenwuXiang_chaolu(String h_rwId, String h_tsgxid, String[] h_arr, Object nTMSXH, Object nSBSXH)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arr.length; i++)
		{
			StringBuffer sql = new StringBuffer();
			String oneItem = h_arr[i];

			sql.append("insert into [S].PS_TASKRECORDMSG ");
			sql.append("(SHEETID, TASKID, BARCODEEQID, SHOWORDER, PARAMID, PARAMNAME, TMSXH, SBSXH, DW, SX, XX)");
			sql.append(" values ");
			sql.append("(");
			sql.append("'" + Guid.create() + "', ");
			sql.append("'" + h_rwId + "', ");
			sql.append("'" + h_tsgxid + "', ");
			sql.append(MaxID.getMaxID("PS_RECORDPATTERNMSG", "SHOWORDER")).append(", ");//为了和模板统一，取模板的号
			sql.append("'" + oneItem +  "', ");
			sql.append("'" + this.getFieldValueStr("PS_DEVCLJBNR", "PARAMNAME", "SHEETID='" + oneItem + "'") +  "', ");
			sql.append(nTMSXH).append(", ");
			sql.append(nSBSXH).append(", ");
			sql.append("'" + this.getFieldValueStr("PS_DEVCLJBNR", "DW", "SHEETID='" + oneItem + "'") +  "', ");
			sql.append(this.getFieldValueFload("PS_DEVCLJBNR", "SX", "SHEETID='" + oneItem + "'") +  ", ");
			sql.append(this.getFieldValueFload("PS_DEVCLJBNR", "XX", "SHEETID='" + oneItem + "'") +  ")");

			arrSql.add(sql.toString());
		}

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
	 * 任务，交换条码顺序号
	 * @param h_rwId 任务ID
	 * @param h_srcTiaomaId 源条码ID
	 * @param h_orgTiaomaId 目标条码ID
	 * @return boolean
	 * @author Wubq
	 */
	public boolean renwuChangeTiaomaOrder(String h_rwId, String h_srcTiaomaId, String h_orgTiaomaId)
	{
		String condition = "";
		StringBuffer sql = new StringBuffer();

		//得到原始序号
		condition = "TASKID = '" + h_rwId + "' and TMID = '" + h_srcTiaomaId + "'";
		int srcOrder = this.getFieldValueInt("PS_V_RWTMINFO", "TMSXH", condition);

		condition = "TASKID = '" + h_rwId + "' and TMID = '" + h_orgTiaomaId + "'";
		int orgOrder = this.getFieldValueInt("PS_V_RWTMINFO", "TMSXH", condition);

		if((srcOrder < 0) || (orgOrder < 0))
		{
			return false;
		}

		//得到修改Sql语句
		List arrSql = new ArrayList();

		//修改源记录
		condition = "(select SHEETID from [S].PS_BARCODEDEVMSG where BARCODEID = '" + h_srcTiaomaId + "')";

		sql.setLength(0);
		sql.append(" update [S].PS_TASKITEMMSG set TMSXH = ").append(orgOrder);
		sql.append(" where TASKID = '").append(h_rwId).append("' ");
		sql.append(" and BARCODEEQID in ").append(condition);
		arrSql.add(sql.toString());

		sql.setLength(0);
		sql.append(" update [S].PS_TASKRECORDMSG set TMSXH = ").append(orgOrder);
		sql.append(" where TASKID = '").append(h_rwId).append("' ");
		sql.append(" and BARCODEEQID in ").append(condition);
		arrSql.add(sql.toString());

		//修改目标记录
		condition = "(select SHEETID from [S].PS_BARCODEDEVMSG where BARCODEID = '" + h_orgTiaomaId + "')";

		sql.setLength(0);
		sql.append(" update [S].PS_TASKITEMMSG set TMSXH = ").append(srcOrder);
		sql.append(" where TASKID = '").append(h_rwId).append("' ");
		sql.append(" and BARCODEEQID in ").append(condition);
		arrSql.add(sql.toString());

		sql.setLength(0);
		sql.append(" update [S].PS_TASKRECORDMSG set TMSXH = ").append(srcOrder);
		sql.append(" where TASKID = '").append(h_rwId).append("' ");
		sql.append(" and BARCODEEQID in ").append(condition);
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

		return true;
	}

	/**
	 * 任务，交换设备顺序号
	 * @param h_rwId 任务ID
	 * @param h_srcTsgxId 源条码设备关系ID
	 * @param h_orgTsgxId 目标条码设备关系ID
	 * @return boolean
	 * @author Wubq
	 */
	public boolean renwuChangeShebeiOrder(String h_rwId, String h_srcTsgxId, String h_orgTsgxId)
	{
		String condition = "";
		StringBuffer sql = new StringBuffer();

		//得到原始序号
		condition = "TASKID = '" + h_rwId + "' and TSGXID = '" + h_srcTsgxId + "'";
		int srcOrder = this.getFieldValueInt("PS_V_RWSBINFO", "SBSXH", condition);

		condition = "TASKID = '" + h_rwId + "' and TSGXID = '" + h_orgTsgxId + "'";
		int orgOrder = this.getFieldValueInt("PS_V_RWSBINFO", "SBSXH", condition);

		if((srcOrder < 0) || (orgOrder < 0))
		{
			return false;
		}

		//得到修改Sql语句
		List arrSql = new ArrayList();

		//修改源记录
		sql.setLength(0);
		sql.append(" update [S].PS_TASKITEMMSG set SBSXH = ").append(orgOrder);
		sql.append(" where TASKID = '").append(h_rwId).append("' ");
		sql.append(" and BARCODEEQID = '").append(h_srcTsgxId).append("' ");
		arrSql.add(sql.toString());

		sql.setLength(0);
		sql.append(" update [S].PS_TASKRECORDMSG set SBSXH = ").append(orgOrder);
		sql.append(" where TASKID = '").append(h_rwId).append("' ");
		sql.append(" and BARCODEEQID = '").append(h_srcTsgxId).append("' ");
		arrSql.add(sql.toString());

		//修改目标记录
		sql.setLength(0);
		sql.append(" update [S].PS_TASKITEMMSG set SBSXH = ").append(srcOrder);
		sql.append(" where TASKID = '").append(h_rwId).append("' ");
		sql.append(" and BARCODEEQID = '").append(h_orgTsgxId).append("' ");
		arrSql.add(sql.toString());

		sql.setLength(0);
		sql.append(" update [S].PS_TASKRECORDMSG set SBSXH = ").append(srcOrder);
		sql.append(" where TASKID = '").append(h_rwId).append("' ");
		sql.append(" and BARCODEEQID = '").append(h_orgTsgxId).append("' ");
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

		return true;
	}

	/**
	 * 查询信息
	 * @return boolean
	 * @author Wubq
	 * @date 2009-09
	 */
	public String getSbxh(String h_sbID)
	{

		String sql = "select obj_id from XBBZH_SG186_SBXH where sbxh in (select sbxh from jxd_eq_eqinfo t where EQID="
			+ "'" + h_sbID + "')";
		RowSet rs;
		String strPDAMsg[] = new String[1];
		strPDAMsg[0] = "";
		try
		{
			rs = DbOper.executeQuery(sql);
			if (rs.next())
			{
				strPDAMsg[0] = rs.getString("obj_id");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		String strSbxh = strPDAMsg[0].toString();
		return strSbxh;
	}

	/**
	 * 根据验收设备模板ID得到设备ID
	 * @return boolean
	 * @author Wubq
	 * @date 2009-09
	 */
	public String getSbIDbyYsPatternID(String h_ysPatternID)
	{
		String sql = "select EQID from ps_yseqpatternmsg where SHEETID="
			+ "'" + h_ysPatternID + "'";
		RowSet rs;
		String strPDAMsg[] = new String[1];
		try
		{
			rs = DbOper.executeQuery(sql);
			if (rs.next())
			{
				strPDAMsg[0] = rs.getString("EQID");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		String strSbID = strPDAMsg[0].toString();
		return strSbID;
	}

	public  String getStationIDbyYsTaskID(String h_ysTaskID)
	{
		String sql = "select STATIONID from ps_ystaskmsg where SHEETID="
			+ "'" + h_ysTaskID + "'";
		RowSet rs;
		String strPDAMsg[] = new String[1];
		try
		{
			rs = DbOper.executeQuery(sql);
			if (rs.next())
			{
				strPDAMsg[0] = rs.getString("STATIONID");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		String strStationID = strPDAMsg[0].toString();
		return strStationID;
	}
	/**
	 * 保存信息
	 * @return boolean
	 * @author Wubq
	 * @date 2009-09
	 */
	public boolean addYsSbItem(String h_sbId, String[] h_arrYsItem)
	{
		List arrSql = null;
		arrSql = new ArrayList();

		for (int i=0; i<h_arrYsItem.length; i++)
		{
			String strSheetid = Guid.create();

			StringBuffer sql = new StringBuffer();
			String oneItem = h_arrYsItem[i];

			sql.append("insert into [S].ps_yseqitem ");
			//ZYG 2009-12-11
			sql.append("(SHEETID,yspatternid,ysitemid, ysitemname, yseqid, showorder)");
			sql.append(" values ");
			sql.append("('" + strSheetid + "', "+ oneItem + ", '" + h_sbId + "'," + MaxID.getMaxID("ps_yseqitem", "showorder") + ")");

			arrSql.add(sql.toString());
		}

		//执行Sql语句
		try
		{
			DbOper.executeNonQuery(arrSql);
		}
		catch (SQLException e)
		{
			StringBuffer logError = new StringBuffer(256);
			logError.append("验收任务条目添加失败, 错误为:");
			logError.append(e.getMessage());
			daoLogger.error(logError);
			return false;
		}

		return true;
	}


	//得到表的字段值
	private String getFieldValueStr(String h_talbeName, String h_fieldName, String h_condition)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select ").append(h_fieldName);
		sql.append(" from [S].").append(h_talbeName);
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

	//得到表的字段值
	private float getFieldValueFload(String h_talbeName, String h_fieldName, String h_condition)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select ").append(h_fieldName);
		sql.append(" from [S].").append(h_talbeName);
		sql.append(" where ").append(h_condition);

		try
		{
			RowSet rs = DbOper.executeQuery(sql.toString());
			if (rs.next())
			{
				return rs.getFloat(h_fieldName);
			}
			else
			{
				return 0;
			}
		}
		catch (SQLException e)
		{
			daoLogger.error("巡视标准化作业, 错误为:" + e.getMessage() + "\nsql为：" + sql);
			return 0;
		}
	}

	//得到表的字段值
	private int getFieldValueInt(String h_talbeName, String h_fieldName, String h_condition)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select ").append(h_fieldName);
		sql.append(" from [S].").append(h_talbeName);
		sql.append(" where ").append(h_condition);

		try
		{
			RowSet rs = DbOper.executeQuery(sql.toString());
			if (rs.next())
			{
				return rs.getInt(h_fieldName);
			}
			else
			{
				return -1;
			}
		}
		catch (SQLException e)
		{
			daoLogger.error("巡视标准化作业, 错误为:" + e.getMessage() + "\nsql为：" + sql);
			return -1;
		}
	}

	/**
	 * 验收任务，修改顺序
	 * @return boolean
	 * @author ZYG 2009-12-11
	 */
	public boolean ysTaskChangeShebeiOrder(String h_ysTaskId, String h_srcSbId, String h_orgSbId)
	{
		StringBuffer sql = new StringBuffer();
		//查询当前
		String strSql = "SELECT DISTINCT SBXH FROM PS_YSITEMMSG WHERE YSTASKID='"+ h_ysTaskId + "' AND YSEQID='" + h_srcSbId + "'";
		RowSet rs;
		String strSrcSbXH = "";
		try
		{
			rs = DbOper.executeQuery(strSql);
			while (rs.next())
			{
				strSrcSbXH = rs.getString("SBXH");
			}
			rs.close();
		}
		catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//查询当前的模板所在的设备号
		strSql = "SELECT DISTINCT SBXH FROM PS_YSITEMMSG WHERE YSTASKID='"+ h_ysTaskId + "' AND YSEQID='" + h_orgSbId + "'";
		String strOrgSbXH = "";
		try
		{
			rs = DbOper.executeQuery(strSql);
			while (rs.next())
			{
				strOrgSbXH = rs.getString("SBXH");
			}
			rs.close();
		}
		catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String[] arrSqlText = new String[2];
		arrSqlText[0] = "UPDATE PS_YSITEMMSG SET SBXH=" + strSrcSbXH + " WHERE YSTASKID ='" + h_ysTaskId + "' AND YSEQID='" + h_orgSbId + "'";
		arrSqlText[1] = "UPDATE PS_YSITEMMSG SET SBXH=" + strOrgSbXH + " WHERE YSTASKID ='" + h_ysTaskId + "' AND YSEQID='" + h_srcSbId + "'";

		//添加完一个以后，直接将数据插入到数据库
		//执行Sql语句
		try
		{
			DbOper.executeNonQuery(arrSqlText);
		}
		catch (SQLException e)
		{
			StringBuffer logError = new StringBuffer(256);
			logError.append("验收任务【改变顺序】, 错误为:");
			logError.append(e.getMessage());
			daoLogger.error(logError);
			return false;
		}

		return true;
	}

	/**———————————————————————————————
	 pulice方法定义
	 * */
	/**
	 * 修改过期任务
	 * @return boolean
	 * @author Wubq
	 */
	public boolean UpdateTaskMsg(String a, String b)
	{
		String strSql = "SELECT SHEETID FROM PS_TASKMSG  WHERE TASKSTATUS=0 AND ENDTIME <'" + getServerTime()  + "'";

		RowSet rs;
		String strTaskIDs = "";
		try
		{
			rs = DbOper.executeQuery(strSql);
			while (rs.next())
			{
				strTaskIDs += "'" + rs.getString("SHEETID") + "',";
			}
			rs.close();
		}
		catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if(strTaskIDs != "")
		{
			//删除最后一个字符
			strTaskIDs = strTaskIDs.substring(0,strTaskIDs.length()-1);
			List arrSql = null;
			arrSql = new ArrayList();

			strSql = "UPDATE PS_TASKMSG SET TASKSTATUS=4 WHERE SHEETID IN (" + strTaskIDs +")";
			arrSql.add(strSql.toString());
			strSql =" INSERT INTO PS_PATROLMSGUPLOAD(SHEETID,SHOWORDER,TASKID,BARCODEID,EQID,JOBID,SELECTBARFLAG,BARCODE,EQNAME,JOBNAME,PRIEQID,TMSXH,SBSXH)" +
					" SELECT E.SHEETID,  E.SHOWORDER,G.SHEETID," +
				 	" I.BARCODEID,I.EQID, E.ITEMID,0, C.BARCODE,B.EQNAME, F.ITEMNAME,(G.SHEETID||I.BARCODEID||I.EQID) AS prieqid," +
				  	" E.TMSXH, E.SBSXH " +
					" FROM JXD_EQ_EQINFO     B,  PS_BARCODEBASEMSG C," +
					" JXD7_XT_STATION   D, PS_TASKITEMMSG    E, " +
					" PS_DEVXSJBNR      F,  PS_TASKMSG        G, " +
					" PS_PDADEVMSG      H,  Ps_Barcodedevmsg  I" +
					" WHERE  G.PDAID = H.SHEETID AND " +
					" G.SHEETID = E.TASKID " +
					" AND G.STATIONID = D.STATIONID AND" +
					" E.TASKID = G.SHEETID AND E.ITEMID = F.SHEETID AND " +
					" E.BARCODEEQID = I.SHEETID AND I.EQID = B.EQID AND " +
					" I.BARCODEID = C.SHEETID AND G.SHEETID IN(" + strTaskIDs + ")";
			arrSql.add(strSql.toString());

			strSql = "INSERT INTO PS_RECORDMSGUPLOAD(SHEETID,TASKID,BARCODEID,EQID,PARAMID,BARCODE,EQNAME,RECORDNAME,PRIEQID,SX,XX,DW,TMSXH,SBSXH) " +
					" SELECT 	E.SHEETID,G.SHEETID,   " +
					" I.BARCODEID, I.EQID,E.PARAMID,   C.BARCODE, B.EQNAME,   F.PARAMNAME,   " +
					" (G.SHEETID||I.BARCODEID||I.EQID) AS   PRIEQID,E.SX, E.XX, E.DW,E.TMSXH, E.SBSXH" +
					" FROM JXD_EQ_EQINFO           B,   PS_BARCODEBASEMSG       C,   JXD7_XT_STATION         D, " +
					" PS_TASKRECORDMSG        E,   PS_DEVCLJBNR            F,   PS_TASKMSG              G,   PS_PDADEVMSG            H," +
					" PS_BARCODEDEVMSG        I  WHERE G.PDAID = H.SHEETID AND   G.SHEETID = E.TASKID  AND  " +
					" G.STATIONID = D.STATIONID AND   E.TASKID = G.SHEETID AND E.PARAMID = F.SHEETID AND   " +
					" E.BARCODEEQID = I.SHEETID AND I.EQID = B.EQID AND   I.BARCODEID = C.SHEETID    " +
					" AND G.SHEETID IN(" + strTaskIDs + ")";
			arrSql.add(strSql.toString());
			try
			{
				DbOper.executeNonQuery(arrSql);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}

		}

		return true;
	}


	public String getServerTime()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateFormat.format(new Date());
		return time;
	}
}
