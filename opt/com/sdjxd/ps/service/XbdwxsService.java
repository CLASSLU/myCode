package com.sdjxd.ps.service;

import com.sdjxd.ps.dao.XbdwxsDao;

public class XbdwxsService 
{
	/**
	 * SG186PMS��ͬ����Ա��Ϣ�����ڵ���
	 * @param h_serverName 
	 * @param h_serverId
	 * @return boolean
	 * @author Wubq
	 */
	public static void SyncUserInfo(String h_serverName, String h_serverId)
	{
		System.out.println("���Բ��Բ��Բ��Բ���");
	}
	
	/**
	 * SG186PMS���������м�¼
	 * @param h_arrPara[0] Ѳ���������0��Ѳ�ӳ�¼��1����������
	 * @param h_arrPara[10,��] ����ID
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
