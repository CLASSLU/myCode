package com.sdjxd.power.mail.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.sdjxd.pms.development.tools.UserTool;
import com.sdjxd.pms.platform.base.dao.BaseDao;
import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.tool.Guid;
import com.sdjxd.power.mail.bean.MailBean;

public class OperateMailDao extends BaseDao
{
	private static Logger log = Logger.getLogger(OperateMailDao.class);
	
	public static OperateMailDao createInstance()
    {
        return (OperateMailDao) createInstance(OperateMailDao.class);
    }
	
	public boolean setMailReaded(String ownersSheetId) 
	{
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE [S].JXD7_MAIL_YJSYZ SET XYJZT = '").append("").append("' ,YDWD='").append("已读");
		sql.append("' WHERE SHEETID='").append(ownersSheetId).append("'");
		
    	try 
    	{
			DbOper.executeNonQuery(sql.toString());
			result = true;
    	}
    	catch (SQLException e)
    	{
    		log.error("更新邮件信息失败:" + e.getMessage());
		}
    	
    	return result;
	}

	public boolean moveMailsToTrash(String ownersSheetIds, String curUserId) 
	{
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE [S].JXD7_MAIL_YJSYZ SET YJCFWZ = '").append("垃圾箱");
		sql.append("' WHERE SHEETID IN (").append(ownersSheetIds).append(") AND RY='").append(curUserId).append("'");

    	try 
    	{
			DbOper.executeNonQuery(sql.toString());
			result = true;
    	}
    	catch (SQLException e)
    	{
    		log.error("删除邮件失败:" + e.getMessage());
		}
    	
    	return result;
	}

	public String getMailSheetIdByOwnersSheetId(String ownersSheetId) 
	{
    	String mailSheetId = "";
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT YJSHEETID FROM ");
    	sql.append("[S].JXD7_MAIL_YJSYZ WHERE SHEETID='");
    	sql.append(ownersSheetId).append("'");
    	
    	try 
    	{
			rs = DbOper.executeQuery(sql.toString());
			
			while(rs.next())
			{
				mailSheetId = rs.getString("YJSHEETID");
			}
			
			rs = null;
		}
    	catch (SQLException e)
    	{
    		log.error("获取邮件信息失败:" + e.getMessage());
		}
    	
    	return mailSheetId;
	}

	public boolean deleteMailOwnersBySheetId(String ownersSheetId, String curUserId) 
	{
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		
		sql.append("DELETE FROM ").append("[S].JXD7_MAIL_YJSYZ ");
		sql.append("WHERE SHEETID='").append(ownersSheetId).append("' AND RY='").append(curUserId).append("'");

    	try 
    	{
			DbOper.executeNonQuery(sql.toString());
			result = true;
    	}
    	catch (SQLException e)
    	{
    		log.error("删除邮件失败:" + e.getMessage());
		}
    	
    	return result;
	}

	public int getOwnersNumberOfMail(String mailSheetId) 
	{
    	int ownersNumber = -1;
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT COUNT(RY) AS OWNERSNUMBER FROM [S].JXD7_MAIL_YJSYZ WHERE YJSHEETID='");
    	sql.append(mailSheetId);
    	sql.append("'");
    	
    	try 
    	{
			rs = DbOper.executeQuery(sql.toString());
			
			while(rs.next())
			{
				ownersNumber = rs.getInt("OWNERSNUMBER");
			}
			
			rs = null;
		}
    	catch (SQLException e)
    	{
    		log.error("获取邮件信息失败:" + e.getMessage());
		}
    	
    	return ownersNumber;
	}

	public boolean deleteMailBySheetId(String mailSheetId) 
	{
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		
		sql.append("DELETE FROM ").append("[S].JXD7_MAIL_YJXXB ");
		sql.append("WHERE SHEETID='").append(mailSheetId).append("'");

    	try 
    	{
			DbOper.executeNonQuery(sql.toString());
			result = true;
    	}
    	catch (SQLException e)
    	{
    		log.error("删除邮件失败:" + e.getMessage());
		}
    	
    	return result;
	}

	public String getOwnerSheetIdOfMail(String mailSheetId, String mailPosition, String curUserId) 
	{
    	String ownerSheetId = "";
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT SHEETID FROM ");
    	sql.append("[S].JXD7_MAIL_YJSYZ WHERE YJSHEETID='");
    	sql.append(mailSheetId).append("' AND RY='").append(curUserId).append("' AND YJCFWZ='").append(mailPosition).append("'");
    	
    	try 
    	{
			rs = DbOper.executeQuery(sql.toString());
			
			while(rs.next())
			{
				ownerSheetId = rs.getString("SHEETID");
			}
			
			rs = null;
		}
    	catch (SQLException e)
    	{
    		log.error("获取邮件信息失败:" + e.getMessage());
		}
    	
    	return ownerSheetId;
	}

	public String getMailPositionByOwnerSheetId(String ownerSheetId) 
	{
    	String mailPosition = "";
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT YJCFWZ FROM ");
    	sql.append("[S].JXD7_MAIL_YJSYZ WHERE SHEETID='");
    	sql.append(ownerSheetId).append("'");
    	
    	try 
    	{
			rs = DbOper.executeQuery(sql.toString());
			
			while(rs.next())
			{
				mailPosition = rs.getString("YJCFWZ");
			}
			
			rs = null;
		}
    	catch (SQLException e)
    	{
    		log.error("获取邮件信息失败:" + e.getMessage());
		}
    	
    	return mailPosition;
	}
	
	public MailBean getMailBySheetId(String mailSheetId) 
	{
    	MailBean mail = new MailBean();
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT SHEETID,ZT,FJ,YXJ,FJR,YJNR,YDHZ FROM ");
    	sql.append("[S].JXD7_MAIL_YJXXB WHERE SHEETID='");
    	sql.append(mailSheetId).append("'");
    	
    	try 
    	{
			rs = DbOper.executeQuery(sql.toString());
			
			while(rs.next())
			{
				mail.setSheetId(rs.getString("SHEETID"));
				mail.setZt(rs.getString("ZT"));
				mail.setFj(rs.getString("FJ"));
				mail.setYxj(rs.getString("YXJ"));
				mail.setFjr(rs.getString("FJR"));
				mail.setYjnr(rs.getString("YJNR"));
				mail.setYdhz(rs.getString("YDHZ"));
			}
			
			rs = null;
		}
    	catch (SQLException e)
    	{
    		log.error("获取邮件信息失败:" + e.getMessage());
		}
    	
    	return mail;
	}

	public Boolean groupHasUsers(String groupId) 
	{
		boolean hasUsers = false;
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT LXRID FROM ");
    	sql.append("[S].JXD7_MAIL_RECEIVER WHERE FZID='");
    	sql.append(groupId).append("'");
    	
    	try 
    	{
			rs = DbOper.executeQuery(sql.toString());
			
			while(rs.next())
			{
				hasUsers = true;
			}
			
			rs = null;
		}
    	catch (SQLException e)
    	{
    		log.error("获取分组信息失败:" + e.getMessage());
		}
    	
    	return hasUsers;
	}

	public boolean deleteGroupById(String groupId) 
	{
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		
		sql.append("DELETE FROM ").append("[S].JXD7_MAIL_GROUP ");
		sql.append("WHERE SHEETID='").append(groupId).append("'");

    	try 
    	{
			DbOper.executeNonQuery(sql.toString());
			result = true;
    	}
    	catch (SQLException e)
    	{
    		log.error("删除分组失败:" + e.getMessage());
		}
    	
    	return result;
	}

	public String getGroupOwner(String groupId) 
	{
    	String owner = "";
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT FZSSR FROM ");
    	sql.append("[S].JXD7_MAIL_GROUP WHERE SHEETID='");
    	sql.append(groupId).append("'");
    	
    	try 
    	{
			rs = DbOper.executeQuery(sql.toString());
			
			while(rs.next())
			{
				owner = rs.getString("FZSSR");
			}
			
			rs = null;
		}
    	catch (SQLException e)
    	{
    		log.error("获取分组信息失败:" + e.getMessage());
		}
    	
    	return owner;
	}

	public String[] getAllUserIdsFromDB() 
	{
		String[] userIdArray = new String[100000];
		int userIdCount = 0;
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT USERID FROM [S].jxd7_xt_user ");
    	
    	try 
    	{
			rs = DbOper.executeQuery(sql.toString());
			
			while(rs.next())
			{
				String userId = rs.getString("USERID");
		    	
				userIdArray[userIdCount] = userId;
				
				userIdCount++;
			}
			
			rs = null;
		}
    	catch (SQLException e)
    	{
    		log.error("获取人员信息失败:" + e.getMessage());
		}
    	
    	String[] userIdArrayTemp = new String[userIdCount];
    	for (int i = 0; i < userIdCount; i++) 
    	{
    		userIdArrayTemp[i] = userIdArray[i];
		}
    	
    	return userIdArrayTemp;
	}

	public String getMailSendTime(String mailSheetId, String ry) 
	{
    	String time = "";
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT SJ FROM ");
    	sql.append("[S].JXD7_MAIL_YJSYZ WHERE YJSHEETID='");
    	sql.append(mailSheetId).append("' AND RY='").append(ry).append("'");
    	
    	try 
    	{
			rs = DbOper.executeQuery(sql.toString());
			
			while(rs.next())
			{
				time = rs.getString("SJ");
			}
			
			rs = null;
		}
    	catch (SQLException e)
    	{
    		log.error("获取邮件信息失败:" + e.getMessage());
		}
    	
    	return time;
	}
	/**
	 * 邮件所有者,添加记录
	 * @param guid SHEETID
	 * @param i 状态(未使用)
	 * @param mailSheetId 邮件信息SHEETID
	 * @param userId 人员,所有者ID
	 * @param rybs 人员表示,[收件人|发件人]
	 * @param xyjzt 新邮件状态[新|""]
	 * @param ydwd [已读|未读]
	 * @param yjcfwz 邮件存放位置[收件箱|草稿箱|发件箱|垃圾箱]
	 * @param fszt 发送状态[已发送|......]
	 * @param sj 时间
	 * @return [true|false]
	 */
	public boolean insertNewRecordToMailOwner(String guid, int i, String mailSheetId, String userId, String rybs, String xyjzt, String ydwd, String yjcfwz, String fszt, String sj) 
	{
		boolean success = false;
		
		StringBuffer insertSql = new StringBuffer();
		insertSql.append("INSERT INTO [S].JXD7_MAIL_YJSYZ (SHEETID, DATASTATUSID, YJSHEETID, RY, RYBS, XYJZT, YDWD, YJCFWZ, FSZT, SJ) VALUES(");
		insertSql.append("'").append(guid).append("',");
		insertSql.append(2).append(",");
		insertSql.append("'").append(mailSheetId).append("',");
		insertSql.append("'").append(userId).append("',");
		insertSql.append("'").append(rybs).append("',");
		insertSql.append("'").append(xyjzt).append("',");
		insertSql.append("'").append(ydwd).append("',");
		insertSql.append("'").append(yjcfwz).append("',");
		insertSql.append("'").append(fszt).append("',");
		insertSql.append("'").append(sj).append("')");
			
		try 
		{
			DbOper.executeNonQuery(insertSql.toString());
			
			success = true;
		}
		catch (SQLException e)
		{
			log.error("保存邮件所有者失败:" + e.getMessage());
		}
		
		return success;
	}

	public boolean clearOpener(String yjSheetId) 
	{
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE [S].jxd7_mail_yjxxb SET opener=null,openerid=null ");
		sql.append("WHERE SHEETID='").append(yjSheetId).append("'");

    	try 
    	{
			DbOper.executeNonQuery(sql.toString());
			result = true;
    	}
    	catch (SQLException e)
    	{
    		log.error("更新邮件信息失败:" + e.getMessage());
		}
    	
    	return result;
	}

	public String getMailFjrNameByFjrId(String fjrId) 
	{
    	String fjrName = "";
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT USERNAME FROM ");
    	sql.append("[S].jxd7_xt_user WHERE USERID='");
    	sql.append(fjrId).append("'");
    	
    	try 
    	{
			rs = DbOper.executeQuery(sql.toString());
			
			while(rs.next())
			{
				fjrName = rs.getString("USERNAME");
			}
			
			rs = null;
		}
    	catch (SQLException e)
    	{
    		log.error("获取发件人信息失败:" + e.getMessage());
		}
    	
    	return fjrName;
	}

	public boolean sendReadReceipt(MailBean mb) 
	{
		boolean success = false;
		
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day_of_month = now.get(Calendar.DAY_OF_MONTH);
		int hour_of_day = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int second = now.get(Calendar.SECOND);
		
		StringBuffer nowStr = new StringBuffer();
		nowStr.append(year).append("-");
		if(month < 9)
		{
			nowStr.append("0");
		}
		nowStr.append(month+1).append("-");
		if(day_of_month < 10)
		{
			nowStr.append("0");
		}
		nowStr.append(day_of_month);
		nowStr.append(" ");
		if(hour_of_day < 10)
		{
			nowStr.append("0");
		}
		nowStr.append(hour_of_day).append(":");
		if(minute < 10)
		{
			nowStr.append("0");
		}
		nowStr.append(minute).append(":");
		if(second < 10)
		{
			nowStr.append("0");
		}
		nowStr.append(second);
		String nowFormat = nowStr.toString();
		
		String yjSheetId = Guid.create();
		
		StringBuffer insertSql = new StringBuffer();
		insertSql.append("insert into [S].JXD7_MAIL_YJXXB ");
		insertSql.append("(SHEETID, DATASTATUSID, SHOWORDER, CREATEDATE, CREATEUSERID, PATTERNID, ");
		insertSql.append("SJR, ZT, FJ, YXJ, YJNR, FJR, FSZT, YJCFWZ, FJRXM, YDHZ, YJLX) values ");
		insertSql.append("('").append(yjSheetId).append("', ");
		insertSql.append("'1', ");
		insertSql.append("(select MAX(SHOWORDER)+1 from jxd7_mail_yjxxb), ");
		insertSql.append("'").append(nowFormat).append("', ");
		insertSql.append("'").append(UserTool.getCurUserId()).append("', ");
		insertSql.append("'F7F87394-83A7-46DA-8125-CDEA20431DEE', ");
		insertSql.append("'").append(mb.getFjrName()).append("', ");
		insertSql.append("'回执邮件——").append(UserTool.getCurUserName()).append("已经阅读您的邮件《").append(mb.getZt()).append("》',");
		insertSql.append("'', ");
		insertSql.append("'普通', ");
		insertSql.append("'<br/>---------------------------原始邮件内容为：---------------------------：<br/>").append(mb.getYjnr()).append("',");
		insertSql.append("'").append(UserTool.getCurUserId()).append("', ");
		insertSql.append("'', ");
		insertSql.append("'', ");
		insertSql.append("'").append(UserTool.getCurUserName()).append("',");
		insertSql.append("'', ");
		insertSql.append("'已读回执')");
		
		StringBuffer insertOwnerSql = new StringBuffer();
		insertOwnerSql.append("insert into [S].jxd7_mail_yjsyz ");
		insertOwnerSql.append("(SHEETID, DATASTATUSID, SHOWORDER, CREATEDATE, CREATEUSERID, PATTERNID, ");
		insertOwnerSql.append("YJSHEETID, RY, RYBS, XYJZT, YDWD, YJCFWZ, FSZT, SJ) values ");
		insertOwnerSql.append("('").append(Guid.create()).append("', ");
		insertOwnerSql.append("'1', ");
		insertOwnerSql.append("(select MAX(SHOWORDER)+1 from jxd7_mail_yjxxb), ");
		insertOwnerSql.append("'").append(nowFormat).append("', ");
		insertOwnerSql.append("'").append(UserTool.getCurUserId()).append("', ");
		insertOwnerSql.append("'', ");
		insertOwnerSql.append("'").append(yjSheetId).append("', ");
		insertOwnerSql.append("'").append(mb.getFjr()).append("', ");
		insertOwnerSql.append("'收件人', ");
		insertOwnerSql.append("'新', ");
		insertOwnerSql.append("'未读', ");
		insertOwnerSql.append("'收件箱', ");
		insertOwnerSql.append("'已发送', ");
		insertOwnerSql.append("'").append(nowFormat).append("')");
		
		try 
		{
			DbOper.executeNonQuery(insertSql.toString());
			DbOper.executeNonQuery(insertOwnerSql.toString());
			
			success = true;
		}
		catch (SQLException e)
		{
			log.error("保存已读回执失败:" + e.getMessage());
		}
		
		return success;
	}

	public boolean mailAll(String[] userIdArray, String mailSheetId, String sj){
		try{
			List<String> sqlList = new ArrayList<String>();
			String strSql = "";
			for(int i = 0; i < userIdArray.length; i++){
				strSql = "INSERT INTO [S].JXD7_MAIL_YJSYZ (SHEETID, DATASTATUSID, YJSHEETID, RY, RYBS, XYJZT, YDWD, YJCFWZ, FSZT, SJ) VALUES(" +
					"'" + UUID.randomUUID().toString() + "', '2', '" + mailSheetId + "', '" + userIdArray[i] + "', '收件人', '新', '未读', '收件箱', '已发送', '" + sj + "')";
				sqlList.add(strSql);
			}
			DbOper.executeNonQuery(sqlList);
		}catch(Exception err){
			log.error(err.getMessage());
			return false;
		}
		return true;
	}
}
