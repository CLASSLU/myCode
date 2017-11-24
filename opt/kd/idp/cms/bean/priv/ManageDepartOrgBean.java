package kd.idp.cms.bean.priv;

/**
 * 调度机构部门 详细组织机构信息
 * @author Administrator
 *(组织机构ID,部门名称,部门简称,所属调度机构 )
 */
public class ManageDepartOrgBean extends OrgBean{


	/**
	 * 所属调度机构
	 */
	private String ownerCompanyName = null;
	
	
	public String getOwnerCompanyName() {
		return ownerCompanyName;
	}
	public void setOwnerCompanyName(String ownerCompanyName) {
		this.ownerCompanyName = ownerCompanyName;
	}
	
	
	
}
