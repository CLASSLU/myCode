package com.sdjxd.power.mail.service;

//import java.util.ArrayList;
//import java.util.List;

import com.sdjxd.pms.development.tools.UserTool;
//import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.tool.BeanTool;
//import com.sdjxd.pms.platform.tool.Guid;
import com.sdjxd.power.mail.bean.MailBean;
import com.sdjxd.power.mail.dao.OperateMailDao;

public class OperateMailService
{ 
	
	public static OperateMailDao operateMailDao;

    static
    {
    	operateMailDao = OperateMailDao.createInstance();
    }

    public static boolean setMailReaded(String ownersSheetId)
    {
    	boolean result = false;
    	
    	if(ownersSheetId.equals(""))
    	{
    		result = true;
    	}
    	else
    	{
    		result = operateMailDao.setMailReaded(ownersSheetId);
    	}
    	
    	return result;
    }
    
    public static boolean moveMailsToTrash(String ownersSheetIds)
    {
    	boolean result = false;
    	
    	if(ownersSheetIds.equals(""))
    	{
    		result = true;
    	}
    	else
    	{
    		result = operateMailDao.moveMailsToTrash(ownersSheetIds, UserTool.getCurUserId());
    	}
    	
    	return result;
    }
    
	public static void deleteMails(String[] ownersSheetIdArray) 
	{
		if(ownersSheetIdArray != null)
		{
			for (int i = 0; i < ownersSheetIdArray.length; i++) 
			{
				deleteMail(ownersSheetIdArray[i]);
			}
		}
	}

	private static boolean deleteMail(String ownersSheetId) 
	{
		String mailSheetId = "";
		boolean successDeletedOwner = false;
		boolean successDeletedMail = false;
		int ownersNumber = -1;
		
		if((ownersSheetId != null) && (!"".equals(ownersSheetId)))
		{
			mailSheetId = operateMailDao.getMailSheetIdByOwnersSheetId(ownersSheetId);
			
			if((mailSheetId != null) && (!"".equals(mailSheetId)))
			{
				successDeletedOwner = operateMailDao.deleteMailOwnersBySheetId(ownersSheetId, UserTool.getCurUserId());
				
				if(successDeletedOwner == true)
				{
					//查询邮件所有者个数
					ownersNumber = operateMailDao.getOwnersNumberOfMail(mailSheetId);
					
					//删除邮件内容
					if(ownersNumber == 0)
					{
						successDeletedMail = operateMailDao.deleteMailBySheetId(mailSheetId);
					}
				}
			}
		}
		
		return successDeletedOwner;
	}
	
	public static boolean deleteMailFromForm(String mailSheetId, String mailPosition) 
	{
		String ownerSheetId = "";
		boolean result = false;
		
		ownerSheetId = operateMailDao.getOwnerSheetIdOfMail(mailSheetId, mailPosition, UserTool.getCurUserId());
		
		if((ownerSheetId != null) && (!"".equals(ownerSheetId)))
		{
			if((mailPosition != null) && ("垃圾箱".equals(mailPosition)))
			{
				result = deleteMail(ownerSheetId);
			}
			else
			{
				StringBuffer ownersSheetIds = new StringBuffer();
				ownersSheetIds.append("'").append(ownerSheetId).append("'");
				result = moveMailsToTrash(ownersSheetIds.toString());
			}
		}
		
		return result;
	}
	
	public static String getMailBySheetId(String mailSheetId) 
	{
		MailBean mb = operateMailDao.getMailBySheetId(mailSheetId);
		mb.setFjrName(operateMailDao.getMailFjrNameByFjrId(mb.getFjr()));
		return BeanTool.toJson(mb);
	}
	
	public static String deleteGroup(String groupId)
	{
		String returnValue = "";
		
//		String groupOwner = operateMailDao.getGroupOwner(groupId);
		
		//if("PUBLICGROUP".equals(groupOwner))
		{
			//returnValue = "publicGroup";
		}
		//else
		{
			if(operateMailDao.groupHasUsers(groupId))
			{
				returnValue = "hasUsers";
			}
			else
			{
				if(operateMailDao.deleteGroupById(groupId))
				{
					returnValue = "deleteSuccess";
				}
				else
				{
					returnValue = "deleteFailed";
				}
			}
		}
		
		return returnValue;
	}
	
	public static String getGroupOwner(String groupId)
	{
		return operateMailDao.getGroupOwner(groupId);
	}
	
	public static boolean sendToAllUsers(String mailSheetId)
	{
		boolean returnValue = false;
		
		if((mailSheetId == null) || ("".equals(mailSheetId)))
		{
			returnValue = true;
		}
		else
		{
			String[] userIdArray = operateMailDao.getAllUserIdsFromDB();
			if(userIdArray == null)
			{
				returnValue = true;
			}
			else
			{
				String sj = operateMailDao.getMailSendTime(mailSheetId, "sendToAllUsers");
//				List<String> sqlList = new ArrayList<String>();
				returnValue = operateMailDao.mailAll(userIdArray, mailSheetId, sj);
//				for (int i = 0; i < userIdArray.length; i++) 
//				{
//					//String userId = userIdArray[i];
//					//String guid = Guid.create();
//					
////					sqlList.add(operateMailDao.insertNewRecordToMailOwner(guid, 2, mailSheetId, userId, "收件人", "新", "未读", "收件箱", "已发送", sj));
//					//operateMailDao.mailAll(userIdArray, mailSheetId, sj);
//				}
//				//returnValue = true;
			}
		}
		
		
		return returnValue;
	}
	
	public static boolean clearOpener(String yjSheetId)
	{
		return operateMailDao.clearOpener(yjSheetId);
	}
	
    public static boolean sendReadReceipt(String ownersSheetId)
    {
    	boolean result = false;
    	
    	if(ownersSheetId.equals(""))
    	{
    		result = true;
    	}
    	else
    	{
    		String mailSheetId = operateMailDao.getMailSheetIdByOwnersSheetId(ownersSheetId);
    		MailBean mb = operateMailDao.getMailBySheetId(mailSheetId);
    		mb.setFjrName(operateMailDao.getMailFjrNameByFjrId(mb.getFjr()));
    		String ydhz = mb.getYdhz();
    		if((ydhz != null) && ("true".equals(ydhz)))
    		{
    			operateMailDao.sendReadReceipt(mb);
    			result = true;
    		}
    		else
    		{
    			result = true;
    		}
    	}
    	
    	return result;
    }
}
