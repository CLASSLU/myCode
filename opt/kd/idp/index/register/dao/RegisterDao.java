package kd.idp.index.register.dao;

import java.util.List;

import kd.idp.cms.bean.priv.OrgBean;
import kd.idp.cms.dao.OrgDao;
import kd.idp.common.consts.TableConst;


public class RegisterDao extends OrgDao{

	/**
	 * 查询组织机构的子组织机构信息
	 * @param nodeId
	 * @return
	 */
	public List<OrgBean> getChildOrgInfo(String nodeId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_ORG+" WHERE 上级组织机构ID='"+nodeId+"' order by 排序";
		return getOrgList(sql);
	}
}
