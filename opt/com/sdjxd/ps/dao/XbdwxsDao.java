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
	 * ���췽��
	 * @return void
	 * @author Wubq
	 * @date 2009-09
	 */
	public XbdwxsDao()
	{
	}

	/**��������������������������������������������������������������
	 ��������
	 * */

	/**
	 * daoLogger
	 * */
	private static Logger daoLogger = Logger.getLogger("EditGridDao");

	/**��������������������������������������������������������������
	 pulice��������
	 * */
	/**
	 * ͬ����Ա��Ϣ
	 * @return boolean
	 * @author Wubq
	 */
	public boolean SyncUserInfo()
	{
		List arrSql = new ArrayList();
		StringBuffer sql = new StringBuffer();
		
		//��
		sql.append("insert into h_xbxs.JXD7_XT_USER (USERID, COMPID, DEPTID, USERCODE, USERNAME, ");
		sql.append("BYNAME, PASSWD, SHOWORDER, ");
		sql.append("DATASTATUSID, STARTTIME, ENDTIME) ");
		sql.append("select T.USERID, '' AS COMPID,  T.DEPTID, T.USERCODE, T.USERNAME, ");
		sql.append("'' AS BYNAME, 'C4CA4238A0B923820DCC509A6F75849B' AS PASSWD, T.SHOWORDER, ");
		sql.append("1 AS DATASTATUSID, '1900-01-01' AS STARTTIME, '3000-12-31' AS ENDTIME ");
		sql.append("from h_xbxs.SG186_user T where USERID not in (select USERID from h_xbxs.jxd7_xt_user)");
		arrSql.add(sql.toString());
		
		//ɾ
		sql.append("delete from h_xbxs.jxd7_xt_user ");
		sql.append("where USERID not in (select USERID from h_xbxs.SG186_user) ");
		arrSql.add(sql.toString());
		
		//ִ��Sql���
		try
		{
			DbOper.executeNonQuery(arrSql);
		}
		catch (SQLException e)
		{
			StringBuffer logError = new StringBuffer(256);
			logError.append("Ѳ�ӱ�׼����ҵ, ����Ϊ:");
			logError.append(e.getMessage());
			daoLogger.error(logError);
			return false;
		}
		
		while (arrSql.size() > 0)
		{
			arrSql.remove(0);
		}
		
		//��

		//ִ��Sql���
		try
		{
			DbOper.executeNonQuery(arrSql);
		}
		catch (SQLException e)
		{
			StringBuffer logError = new StringBuffer(256);
			logError.append("Ѳ�ӱ�׼����ҵ, ����Ϊ:");
			logError.append(e.getMessage());
			daoLogger.error(logError);
			return false;
		}

		return true;
	}

	/**
	 * SG186PMS������Ѳ�ӳ�¼���м�¼
	 * @param h_arrPara[0] Ѳ���������0��Ѳ�ӳ�¼��1����������
	 * @param h_arrPara[10,��] ����ID
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
			
			//�ж��Ƿ��Ѿ�������м�¼
			if (this.isHaseRecord("mw_app.MWT_UD_SBDYX_XSJL", "obj_id = '" + oneObjId + "'") == false)
			{
				continue;
			}
			
			//�õ�������Ϣ
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
					//MWT_UD_SBDYX_XSJL(Ѳ�Ӽ�¼)
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
					sql.append("'PDAѲ��', ");		//XSQK
					sql.append("'" + rs.getString("JIELUN") + "', ");			//JL
					sql.append("'" + rs.getString("PATROLUSER0") + " " + rs.getString("PATROLUSER1") + "', ");	//XSR
					sql.append("''");		//OBJ_CAPTION
					sql.append(")");
					arrSql.add(sql.toString());
					
					//MWT_UD_SBDYX_YXRZJL��Ѳ�Ӽ�¼��
					sql.setLength(0);
					sql.append("INSERT INTO MW_APP.MWT_UD_SBDYX_YXRZJL ");
					sql.append("(OBJ_ID, OBJ_DISPIDX, JLRQ, JLNR, URLDZ, SFJS, JLR, DWHBDZ, JJBZJID, XGJLZJ, XGJLPZBS, FSSJ, DWHBDZMC, GZXXGL, SFWC, JLDW, OBJ_CAPTION)");
					sql.append(" valuse (");
					sql.append("'" + oneObjId + "', ");						//OBJ_ID
					sql.append("mw_sys.mwq_obj_dispidx.nextval, ");			//OBJ_DISPIDX
					sql.append("'current_timestamp(6)', ");					//JLRQ
					sql.append("'��PDAѲ�ӡ� ��ʼʱ�䣺" + rs.getString("PATROLSTARTTIME") + "����ʱ�䣺" + rs.getString("PATROLENDTIME") + "���ۣ�" + rs.getString("JIELUN") + "', ");		//JLNR
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
				daoLogger.error("Ѳ�ӱ�׼����ҵ, ����Ϊ:" + e.getMessage() + "\nsqlΪ��" + sql.toString());
				continue;
			}
		}
		
		return true;
	}

	/**
	 * SG186PMS�����������������м�¼
	 * @param h_arrPara[0] Ѳ���������0��Ѳ�ӳ�¼��1����������
	 * @param h_arrPara[10,��] ����ID
	 * @return boolean
	 * @author Wubq
	 */
	public boolean SG186PMS_yxjs_ys(String[] h_arrPara)
	{
		return true;
	}
	
	/**
	 * �жϼ�¼�Ƿ����
	 * @param h_tableName ������
	 * @param h_condition ����
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
			daoLogger.error("Ѳ�ӱ�׼����ҵ, ����Ϊ:" + e.getMessage() + "\nsqlΪ��" + sql.toString());
			return false;
		}
	}
	
	/**
	 * �жϼ�¼�Ƿ����
	 * @param h_tableName ������
	 * @param h_fields �ֶ�
	 * @param h_condition ����
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
			daoLogger.error("Ѳ�ӱ�׼����ҵ, ����Ϊ:" + e.getMessage() + "\nsqlΪ��" + sql.toString());
			return null;
		}
	}
	
	//�õ�����ֶ�ֵ
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
			daoLogger.error("Ѳ�ӱ�׼����ҵ, ����Ϊ:" + e.getMessage() + "\nsqlΪ��" + sql);
			return "";
		}
	}

}
