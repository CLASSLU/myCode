package kd.idp.index.register.dao;

import java.util.List;

import kd.idp.cms.bean.priv.OrgBean;
import kd.idp.cms.dao.OrgDao;
import kd.idp.common.consts.TableConst;


public class RegisterDao extends OrgDao{

	/**
	 * ��ѯ��֯����������֯������Ϣ
	 * @param nodeId
	 * @return
	 */
	public List<OrgBean> getChildOrgInfo(String nodeId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_ORG+" WHERE �ϼ���֯����ID='"+nodeId+"' order by ����";
		return getOrgList(sql);
	}
}
