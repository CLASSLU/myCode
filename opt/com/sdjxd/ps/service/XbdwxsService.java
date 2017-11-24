package com.sdjxd.ps.service;

import com.sdjxd.ps.dao.XbdwxsDao;

public class XbdwxsService 
{
	/**
	 * SG186PMS，同步人员信息，定期调用
	 * @param h_serverName 
	 * @param h_serverId
	 * @return boolean
	 * @author Wubq
	 */
	public static void SyncUserInfo(String h_serverName, String h_serverId)
	{
		System.out.println("测试测试测试测试测试");
	}
	
	/**
	 * SG186PMS，产生运行记录
	 * @param h_arrPara[0] 巡视任务类别，0：巡视抄录；1：验收任务
	 * @param h_arrPara[10,…] 任务ID
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean SG186PMS_yxjs(String[] h_arrPara)
	{
		if (h_arrPara.length < 11)
		{
			return false;
		}
		
		XbdwxsDao dao = new XbdwxsDao();
		String strType = h_arrPara[0];
		if ("0".equals(strType) == true)
		{
			return dao.SG186PMS_yxjs_xs(h_arrPara);
		}
		else if ("1".equals(strType) == true)
		{
			return dao.SG186PMS_yxjs_ys(h_arrPara);
		}
		else
		{
			return false;
		}
	}
}
