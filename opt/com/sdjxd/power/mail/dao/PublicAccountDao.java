package com.sdjxd.power.mail.dao;

import java.sql.SQLException;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.sdjxd.pms.platform.base.dao.BaseDao;
import com.sdjxd.pms.platform.data.DbOper;

public class PublicAccountDao extends BaseDao
{
	private static Logger log = Logger.getLogger(PublicAccountDao.class);
	
	public static PublicAccountDao createInstance()
    {
        return (PublicAccountDao) createInstance(PublicAccountDao.class);
    }
	
	public boolean setMailReaded(String ownersSheetId) 
	{
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE [S].JXD7_MAIL_YJSYZ SET XYJZT = '").append("").append("' ,YDWD='").append("�Ѷ�");
		sql.append("' WHERE SHEETID='").append(ownersSheetId).append("'");
		
    	try 
    	{
			DbOper.executeNonQuery(sql.toString());
			result = true;
    	}
    	catch (SQLException e)
    	{
    		log.error("�����ʼ���Ϣʧ��:" + e.getMessage());
		}
    	
    	return result;
	}

	public boolean userIsPublicAccount(String userId) 
	{
		boolean result = false;
    	RowSet rs;
    	StringBuffer sql = new 	StringBuffer();
    	
    	sql.append("SELECT MC FROM ");
    	sql.append("[S].JXD7_MAIL_GGZH WHERE ID='");
    	sql.append(userId).append("'");
    	
    	try 
    	{
			rs = DbOper.executeQuery(sql.toString());
			
			while(rs.next())
			{
				result = true;
			}
			
			rs = null;
		}
    	catch (SQLException e)
    	{
    		log.error("��ȡ�����˺���Ϣʧ��:" + e.getMessage());
		}
    	
    	return result;
	}
}
