package kd.idp.cms.bean.priv;

/**
 * Ȩ������
 * @author caoyu
 *
 */
public class PrivAttrBean {

	
	/**
	 * Ȩ������ID
	 */
	private String privAttrId = null;
	
	/**
	 * Ȩ��ID
	 */
	private String privId = null;
	
	/**
	 * Ȩ����������
	 */
	private String privAttrType = null;
	
	/**
	 * Ȩ��������
	 */
	private String privAttrName = null;
	
	/**
	 * Ȩ������ֵ
	 */
	private String privAttrValue = null;
	
	/**
	 * ����
	 */
	private int attrOrder = 0;

	public String getPrivAttrType() {
		return privAttrType;
	}

	public void setPrivAttrType(String privAttrType) {
		this.privAttrType = privAttrType;
	}

	public String getPrivAttrName() {
		return privAttrName;
	}

	public void setPrivAttrName(String privAttrName) {
		this.privAttrName = privAttrName;
	}

	public String getPrivAttrValue() {
		return privAttrValue;
	}

	public void setPrivAttrValue(String privAttrValue) {
		this.privAttrValue = privAttrValue;
	}

	public String getPrivId() {
		return privId;
	}

	public void setPrivId(String privId) {
		this.privId = privId;
	}

	public String getPrivAttrId() {
		return privAttrId;
	}

	public void setPrivAttrId(String privAttrId) {
		this.privAttrId = privAttrId;
	}

	public int getAttrOrder() {
		return attrOrder;
	}

	public void setAttrOrder(int attrOrder) {
		this.attrOrder = attrOrder;
	}
	
	
}
