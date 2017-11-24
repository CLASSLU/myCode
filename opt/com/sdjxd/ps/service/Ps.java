package com.sdjxd.ps.service;

import java.util.List;

import com.sdjxd.ps.dao.PsDao;

public class Ps
{
//	———————————[ 任务模板 ]—————————————

	/** 
	 * 任务模板，删除模板
	 * @param h_arrMb 模板ID数组
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwumobanDeleteMoban(String[] h_arrMb)
	{
		PsDao dao = new PsDao();
		return dao.renwumobanDeleteMoban(h_arrMb);
	}

	/**
	 * 任务模板，增加设备
	 * @param h_mbId 模板ID
	 * @param h_arrSb 条码设备关系ID数组
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean addRenwumobanShebei(String h_mbId, String[] h_arrSb)
	{
		PsDao dao = new PsDao();
		return dao.addRenwumobanShebei(h_mbId, h_arrSb);
	}

	/**
	 * 任务模板，删除设备
	 * @param h_mbId 模板ID
	 * @param h_arr 条码设备关系ID数组
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean RenwumobanDeleteSb(String h_mbId, String[] h_arr)
	{
		PsDao dao = new PsDao();
		return dao.RenwumobanDeleteSb(h_mbId, h_arr);
	}

	/**
	 * 任务模板，增加项
	 * @param h_type 类型，0:抄录项，1：巡视项
	 * @param h_mbId 模板ID
	 * @param h_tsgxid 条码设备关系ID
	 * @param h_arr 项ID数组
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
	 * 任务模板，删除项
	 * @param h_tableName 数据库名称
	 * @param h_field 字段
	 * @param h_arr h_field对应的值
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean RenwumobanDeleteItem(String h_tableName, String h_field, String[] h_arr)
	{
		PsDao dao = new PsDao();
		return dao.RenwumobanDeleteItem(h_tableName, h_field, h_arr);
	}

	/**
	 * 任务模板，交换条码顺序号
	 * @param h_mbId 模板ID
	 * @param h_srcTiaomaId 源条码ID
	 * @param h_orgTiaomaId 目标条码ID
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwumobanChangeTiaomaOrder(String h_mbId, String h_srcTiaomaId, String h_orgTiaomaId)
	{
		PsDao dao = new PsDao();
		return dao.renwumobanChangeTiaomaOrder(h_mbId, h_srcTiaomaId, h_orgTiaomaId);
	}

	/**
	 * 任务模板，交换设备顺序号
	 * @param h_mbId 模板ID
	 * @param h_srcTsgxId 源条码设备关系ID
	 * @param h_orgTsgxId 目标条码设备关系ID
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwumobanChangeShebeiOrder(String h_mbId, String h_srcTsgxId, String h_orgTsgxId)
	{
		PsDao dao = new PsDao();
		return dao.renwumobanChangeShebeiOrder(h_mbId, h_srcTsgxId, h_orgTsgxId);
	}

//	———————————[ 任务 ]—————————————

	/**
	 * 任务，新建任务
	 * @param h_bdzId 变电站ID
	 * @param h_mbId 模板ID
	 * @return boolean
	 * @author Wubq
	 */
	public static String newRenwu(String h_bdzId, String h_mbId)
	{
		PsDao dao = new PsDao();
		return dao.newRenwu(h_bdzId, h_mbId);
	}

	/**
	 * 任务，删除任务
	 * @param h_arrRw 任务ID数组
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwumobanDeleteRenwu(String[] h_arrRw)
	{
		PsDao dao = new PsDao();
		return dao.renwumobanDeleteRenwu(h_arrRw);
	}

	/**
	 * 任务，增加设备
	 * @param h_rwId 任务ID
	 * @param h_arrSb 条码设备关系ID数组
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean addRenwuShebei(String h_rwId, String[] h_arrSb)
	{
		PsDao dao = new PsDao();
		return dao.addRenwuShebei(h_rwId, h_arrSb);
	}

	/**
	 * 任务，删除设备
	 * @param h_rwId 模板ID
	 * @param h_arr 条码设备关系ID数组
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean RenwuDeleteSb(String h_rwId, String[] h_arr)
	{
		PsDao dao = new PsDao();
		return dao.RenwuDeleteSb(h_rwId, h_arr);
	}

	/**
	 * 任务，增加项
	 * @param h_type 类型，0:抄录项，1：巡视项
	 * @param h_rwId 模板ID
	 * @param h_tsgxid 条码设备关系ID
	 * @param h_arr 项ID数组
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
	 * 任务，交换条码顺序号
	 * @param h_mbId 任务ID
	 * @param h_srcTiaomaId 源条码ID
	 * @param h_orgTiaomaId 目标条码ID
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwuChangeTiaomaOrder(String h_rwId, String h_srcTiaomaId, String h_orgTiaomaId)
	{
		PsDao dao = new PsDao();
		return dao.renwuChangeTiaomaOrder(h_rwId, h_srcTiaomaId, h_orgTiaomaId);
	}

	/**
	 * 任务，交换设备顺序号
	 * @param h_mbId 任务ID
	 * @param h_srcTsgxId 源条码设备关系ID
	 * @param h_orgTsgxId 目标条码设备关系ID
	 * @return boolean
	 * @author Wubq
	 */
	public static boolean renwuChangeShebeiOrder(String h_rwId, String h_srcTsgxId, String h_orgTsgxId)
	{
		PsDao dao = new PsDao();
		return dao.renwuChangeShebeiOrder(h_rwId, h_srcTsgxId, h_orgTsgxId);
	}

//	———————————[  ]—————————————

	/**
	 * 验收任务，删除设备
	 * @param h_mbId 模板ID
	 * @param h_arr 条码设备关系ID数组
	 * @return boolean
	 * @author ZYG 2009-12-08
	 */
	public static boolean ysTaskDeleteSb(String h_taskId, String[] h_arrSb)
	{
		PsDao dao = new PsDao();
		return dao.ysTaskDeleteSb(h_taskId, h_arrSb);
	}

	/**
	 * 任务模板，增加验收设备
	 * @param h_ysTaskId 验收任务ID
	 * @param h_arrYsPatternID 验收设备模板数组
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
	 * 验收任务，修改顺序
	 * @return boolean
	 * @author ZYG 2009-12-11
	 */
	public static boolean UpdateTaskMsg(String a, String b)
	{
		PsDao dao = new PsDao();
		return dao.UpdateTaskMsg(a, b);
	}

	/**
	 * 验收任务，修改顺序
	 * @return boolean
	 * @author ZYG 2009-12-11
	 */
	public static boolean ysTaskChangeShebeiOrder(String h_ysTaskId, String h_srcSbId, String h_orgSbId)
	{
		PsDao dao = new PsDao();
		return dao.ysTaskChangeShebeiOrder(h_ysTaskId, h_srcSbId, h_orgSbId);
	}

}
