package kd.idp.index.register.bean;

import kd.idp.cms.bean.priv.UserBean;

public class UserPowerPlantPersonBean extends UserBean {
	
	/**
	 * ���糧��Ա
	 */
	private UserAttrPowerPlantPersonBean attrPowerPlantPerson = null;

	public UserAttrPowerPlantPersonBean getAttrPowerPlantPerson() {
		return attrPowerPlantPerson;
	}

	public void setAttrPowerPlantPerson(
			UserAttrPowerPlantPersonBean attrPowerPlantPerson) {
		this.attrPowerPlantPerson = attrPowerPlantPerson;
	}
	
	

}
