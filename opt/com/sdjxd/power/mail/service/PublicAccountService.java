package com.sdjxd.power.mail.service;

import java.sql.SQLException;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.power.mail.dao.PublicAccountDao;

public class PublicAccountService 
{ 
	
	public static PublicAccountDao publicAccountDao;

    static
    {
    	publicAccountDao = PublicAccountDao.createInstance();
    }

    public static boolean userIsPublicAccount(String userId)
    {
    	boolean result = false;
    	
    	if(("".equals(userId)) || (userId == null))
    	{
    		result = true;
    	}
    	else
    	{
    		result = publicAccountDao.userIsPublicAccount(userId);
    	}
    	
    	return result;
    }
  //将草稿箱里的记录设置为发送
    public static int updatemail(String flowobjectid)
    {	
    	int result = 0;
    	String updatesql="update JXD7_MAIL_YJSYZ  set fszt='已发送' where yjsheetid in (select sheetid from jxd7_mail_ownermail where flowobjectid='"+flowobjectid+"') ";
    	try{
    		result=DbOper.executeNonQuery(updatesql);
		}
		catch(SQLException ee){
			ee.printStackTrace();		
			}
		return result;
    	
    	
    }
}
