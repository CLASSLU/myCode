package kd.idp.index.register.bean;

import kd.idp.cms.bean.priv.UserBean;

public class UserElectricCompanyPersonBean extends UserBean {
	
	/**
	 * ������˾��Ա
	 */
	private UserAttrElectricCompanyPersonBean attrElectricCompanyPerson = null;

	public UserAttrElectricCompanyPersonBean getAttrElectricCompanyPerson() {
		return attrElectricCompanyPerson;
	}

	public void setAttrElectricCompanyPerson(
			UserAttrElectricCompanyPersonBean attrElectricCompanyPerson) {
		this.attrElectricCompanyPerson = attrElectricCompanyPerson;
	}
	
	
}
