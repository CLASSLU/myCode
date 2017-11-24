package com.sdjxd.common.organise;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.data.ListMap;
import com.sdjxd.pms.platform.organize.User;

/**
 * 
 * @author ����· 2011-6-29
 *
 */
public class OrganiseService {
	public static Logger log = Logger.getLogger(UserRole.class);

	/**
	 * ������֯��λ���ƣ���������ѯ���һ��id
	 * 
	 * @param orgNameStr��֯��λ����
	 * @return
	 */
	public static String getOrgNameToId(String orgNameStr) {
		if (orgNameStr.indexOf("--") == -1 && orgNameStr.indexOf("-") != -1)
		{
			orgNameStr = orgNameStr.replaceAll("-", "--");
		}
		
		String[] str = orgNameStr.split("--");
		if (orgNameStr == null || "".equals(orgNameStr)) {
			return "";
		}
		// ��һ���˵�
		String sql = "SELECT ORGANISEID,ORGANISENAME FROM [S].JXD7_XT_ORGANISE T WHERE T.PREORGANISEID is null AND T.DATASTATUSID = 1 ORDER BY SHOWORDER ASC";
		String sheetid = "";
		RowSet rs;
		try {
			rs = DbOper.executeQuery(sql);
			// ���Ϊ��һ��ֱ�ӷ���
			if (rs.next()) {
				sheetid = rs.getString("ORGANISEID");
				if (str[0].equals(rs.getString("ORGANISENAME")) && str.length==1) {
					return sheetid;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.debug(e.getMessage());
		}
		String sid = "";
		for (int i = 0; i < str.length; i++) {
			//������λ�ͱ������Ʋ�ѯ
			sql = "select ORGANISEID, ORGANISENAME from [S].JXD7_XT_ORGANISE T where t.preorganiseid = '"
					+ sheetid + "' and t.organisename = '" + str[i] + "'";
			RowSet rs2;
			try {
				rs2 = DbOper.executeQuery(sql);
				if (rs2.next()) {
					sheetid = rs2.getString("ORGANISEID");
					sid = sheetid;
				}
				else
				{
					sid = "";
				}
			} catch (SQLException e) {
				e.printStackTrace();
				log.debug(e.getMessage());
			}
		}
		return sid;
	}
	/**
	 * ���벿��ID,�����ϼ���λ��Ϣ
	 * @author Key 2011-07-18
	 * @param deptId
	 * @return
	 */
	public static ListMap getPreorganise(String deptId)
	{
		ListMap lm = new ListMap();
		String sql = "select t1.organiseid,t1.organisename,t1.organiselevel from [S].jxd7_xt_organise t1 where t1.organiseid=" +
				"(select preorganiseid from [S].jxd7_xt_organise t2 where t2.organiseid='"+deptId+"')";
		try{
			List list = DbOper.executeList(sql);
			if(list.size()>0)
				lm = (ListMap) (list.get(0));
		}catch(Exception e){
			e.printStackTrace();
		}
		return lm;
	}
	/**
	 * ���ر�����������λ
	 * �������ݺ�����һ��������(�����������λ);
	 * @author Key 2011-07-18
	 * @param deptId
	 * @return
	 */
	public static ListMap getCurOrganise(String deptId)
	{
		ListMap lm = new ListMap();
		String sql = "select organisename,organiseid from  [S].jxd7_xt_organise o where o.organiselevel like'____'"+
				"and (select organiselevel from [S].jxd7_xt_organise p where p.organiseid='"+deptId+"') like o.organiselevel||'%'";
		try{
			List list = DbOper.executeList(sql);
			if(list.size()>0)
				lm = (ListMap) (list.get(0));
		}catch(Exception e){
			e.printStackTrace();
		}
		return lm;
	}
	
	/**
	 * ��ȡ��֯���� ����ID
	 * 
	 * @author Key 2011-09-16
	 * @param organiseId
	 * @return
	 */
	public static int getOrgTypeId(String organiseId)
	{
		int n = -1;
		if(organiseId == null)
			organiseId = User.getCurrentUser().getDeptId();
		try{
			ResultSet rs = DbOper.executeQuery("SELECT DEPLEVEL_ID FROM [S].JXD7_XT_ORGANISE WHERE ORGANISEID='"+organiseId+"'");
			if(rs.next())
				n = rs.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
		}
		return n;
	}
	
	/**
	 * �Ƚ�������֯���������Ƿ���ͬ
	 * 
	 * @author Key 2011-09-16
	 * @param org1
	 * @param org2
	 * @return
	 */
	public static boolean isSameTypeOrg(String org1, String org2)
	{
		boolean f = false;
		int t1 = getOrgTypeId(org1);
		int t2 = getOrgTypeId(org2);
		if(t1 != -1 && t2 != -1 && t1==t2)
			f = true;
		return f;
	}
}
