package com.sdjxd.ps.service;

import java.util.List;

import com.sdjxd.ps.dao.PsDao;

public class Ps
{
//	����������������������[ ����ģ�� ]��������������������������

	/** 
	 * ����ģ�壬ɾ��ģ��
	 * @param h_arrMb ģ��ID����
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwumobanDeleteMoban(String[] h_arrMb)
	{
		PsDao dao = new PsDao();
		return dao.renwumobanDeleteMoban(h_arrMb);
	}

	/**
	 * ����ģ�壬�����豸
	 * @param h_mbId ģ��ID
	 * @param h_arrSb �����豸��ϵID����
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean addRenwumobanShebei(String h_mbId, String[] h_arrSb)
	{
		PsDao dao = new PsDao();
		return dao.addRenwumobanShebei(h_mbId, h_arrSb);
	}

	/**
	 * ����ģ�壬ɾ���豸
	 * @param h_mbId ģ��ID
	 * @param h_arr �����豸��ϵID����
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean RenwumobanDeleteSb(String h_mbId, String[] h_arr)
	{
		PsDao dao = new PsDao();
		return dao.RenwumobanDeleteSb(h_mbId, h_arr);
	}

	/**
	 * ����ģ�壬������
	 * @param h_type ���ͣ�0:��¼�1��Ѳ����
	 * @param h_mbId ģ��ID
	 * @param h_tsgxid �����豸��ϵID
	 * @param h_arr ��ID����
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean addRenwumobanXiang(int h_type, String h_mbId, String h_tsgxid, String[] h_arr)
	{
		PsDao dao = new PsDao();

		List arr = dao.getRenwumobanXSH(h_mbId, h_tsgxid);
		if (null == arr)
		{
			return false;
		}

		if (h_type == 0)
		{
			return dao.addRenwumobanXiang_chaolu(h_mbId, h_tsgxid, h_arr, arr.get(0), arr.get(1));
		}
		else if (h_type == 1)
		{
			return dao.addRenwumobanXiang_xunshi(h_mbId, h_tsgxid, h_arr, arr.get(0), arr.get(1));
		}

		return false;
	}

	/**
	 * ����ģ�壬ɾ����
	 * @param h_tableName ���ݿ�����
	 * @param h_field �ֶ�
	 * @param h_arr h_field��Ӧ��ֵ
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean RenwumobanDeleteItem(String h_tableName, String h_field, String[] h_arr)
	{
		PsDao dao = new PsDao();
		return dao.RenwumobanDeleteItem(h_tableName, h_field, h_arr);
	}

	/**
	 * ����ģ�壬��������˳���
	 * @param h_mbId ģ��ID
	 * @param h_srcTiaomaId Դ����ID
	 * @param h_orgTiaomaId Ŀ������ID
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwumobanChangeTiaomaOrder(String h_mbId, String h_srcTiaomaId, String h_orgTiaomaId)
	{
		PsDao dao = new PsDao();
		return dao.renwumobanChangeTiaomaOrder(h_mbId, h_srcTiaomaId, h_orgTiaomaId);
	}

	/**
	 * ����ģ�壬�����豸˳���
	 * @param h_mbId ģ��ID
	 * @param h_srcTsgxId Դ�����豸��ϵID
	 * @param h_orgTsgxId Ŀ�������豸��ϵID
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwumobanChangeShebeiOrder(String h_mbId, String h_srcTsgxId, String h_orgTsgxId)
	{
		PsDao dao = new PsDao();
		return dao.renwumobanChangeShebeiOrder(h_mbId, h_srcTsgxId, h_orgTsgxId);
	}

//	����������������������[ ���� ]��������������������������

	/**
	 * �����½�����
	 * @param h_bdzId ���վID
	 * @param h_mbId ģ��ID
	 * @return boolean
	 * @author Wubq
	 */
	public static String newRenwu(String h_bdzId, String h_mbId)
	{
		PsDao dao = new PsDao();
		return dao.newRenwu(h_bdzId, h_mbId);
	}

	/**
	 * ����ɾ������
	 * @param h_arrRw ����ID����
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwumobanDeleteRenwu(String[] h_arrRw)
	{
		PsDao dao = new PsDao();
		return dao.renwumobanDeleteRenwu(h_arrRw);
	}

	/**
	 * ���������豸
	 * @param h_rwId ����ID
	 * @param h_arrSb �����豸��ϵID����
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean addRenwuShebei(String h_rwId, String[] h_arrSb)
	{
		PsDao dao = new PsDao();
		return dao.addRenwuShebei(h_rwId, h_arrSb);
	}

	/**
	 * ����ɾ���豸
	 * @param h_rwId ģ��ID
	 * @param h_arr �����豸��ϵID����
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean RenwuDeleteSb(String h_rwId, String[] h_arr)
	{
		PsDao dao = new PsDao();
		return dao.RenwuDeleteSb(h_rwId, h_arr);
	}

	/**
	 * ����������
	 * @param h_type ���ͣ�0:��¼�1��Ѳ����
	 * @param h_rwId ģ��ID
	 * @param h_tsgxid �����豸��ϵID
	 * @param h_arr ��ID����
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean addRenwuXiang(int h_type, String h_rwId, String h_tsgxid, String[] h_arr)
	{
		PsDao dao = new PsDao();

		List arr = dao.getRenwuXSH(h_rwId, h_tsgxid);
		if (null == arr)
		{
			return false;
		}

		if (h_type == 0)
		{
			return dao.addRenwuXiang_chaolu(h_rwId, h_tsgxid, h_arr, arr.get(0), arr.get(1));
		}
		else if (h_type == 1)
		{
			return dao.addRenwuXiang_xunshi(h_rwId, h_tsgxid, h_arr, arr.get(0), arr.get(1));
		}

		return false;
	}

	/**
	 * ���񣬽�������˳���
	 * @param h_mbId ����ID
	 * @param h_srcTiaomaId Դ����ID
	 * @param h_orgTiaomaId Ŀ������ID
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwuChangeTiaomaOrder(String h_rwId, String h_srcTiaomaId, String h_orgTiaomaId)
	{
		PsDao dao = new PsDao();
		return dao.renwuChangeTiaomaOrder(h_rwId, h_srcTiaomaId, h_orgTiaomaId);
	}

	/**
	 * ���񣬽����豸˳���
	 * @param h_mbId ����ID
	 * @param h_srcTsgxId Դ�����豸��ϵID
	 * @param h_orgTsgxId Ŀ�������豸��ϵID
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwuChangeShebeiOrder(String h_rwId, String h_srcTsgxId, String h_orgTsgxId)
	{
		PsDao dao = new PsDao();
		return dao.renwuChangeShebeiOrder(h_rwId, h_srcTsgxId, h_orgTsgxId);
	}

//	����������������������[  ]��������������������������

	/**
	 * ��������ɾ���豸
	 * @param h_mbId ģ��ID
	 * @param h_arr �����豸��ϵID����
	 * @return boolean
	 * @author ZYG 2009-12-08
	 */
	public static boolean ysTaskDeleteSb(String h_taskId, String[] h_arrSb)
	{
		PsDao dao = new PsDao();
		return dao.ysTaskDeleteSb(h_taskId, h_arrSb);
	}

	/**
	 * ����ģ�壬���������豸
	 * @param h_ysTaskId ��������ID
	 * @param h_arrYsPatternID �����豸ģ������
	 * @return boolean
	 * @author ZYG 2009-12-08
	 */
	public static boolean addYsTaskShebei(String h_ysTaskId, String[] h_arrYsPatternID)
	{
		PsDao dao = new PsDao();
		return dao.addYsTaskShebei(h_ysTaskId, h_arrYsPatternID);
	}

	public static String getSbxh(String h_sbID)
	{
		PsDao dao = new PsDao();
		return dao.getSbxh(h_sbID);
	}

	public static boolean addYsSbItem(String h_sbId, String[] h_arrYsItem)
	{

		PsDao dao = new PsDao();
		return dao.addYsSbItem(h_sbId, h_arrYsItem);
	}

	public static String getSbIDbyYsPatternID(String h_ysPatternID)
	{
		PsDao dao = new PsDao();
		return dao.getSbIDbyYsPatternID(h_ysPatternID);
	}

	public static String getStationIDbyYsTaskID(String h_ysTaskID)
	{
		PsDao dao = new PsDao();
		return dao.getStationIDbyYsTaskID(h_ysTaskID);
	}

	/**
	 * ���������޸�˳��
	 * @return boolean
	 * @author ZYG 2009-12-11
	 */
	public static boolean UpdateTaskMsg(String a, String b)
	{
		PsDao dao = new PsDao();
		return dao.UpdateTaskMsg(a, b);
	}

	/**
	 * ���������޸�˳��
	 * @return boolean
	 * @author ZYG 2009-12-11
	 */
	public static boolean ysTaskChangeShebeiOrder(String h_ysTaskId, String h_srcSbId, String h_orgSbId)
	{
		PsDao dao = new PsDao();
		return dao.ysTaskChangeShebeiOrder(h_ysTaskId, h_srcSbId, h_orgSbId);
	}

}
