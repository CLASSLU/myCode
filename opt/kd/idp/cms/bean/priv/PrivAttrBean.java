package kd.idp.cms.bean.priv;

/**
 * 权限属性
 * @author caoyu
 *
 */
public class PrivAttrBean {

	
	/**
	 * 权限属性ID
	 */
	private String privAttrId = null;
	
	/**
	 * 权限ID
	 */
	private String privId = null;
	
	/**
	 * 权限属性类型
	 */
	private String privAttrType = null;
	
	/**
	 * 权限属性名
	 */
	private String privAttrName = null;
	
	/**
	 * 权限属性值
	 */
	private String privAttrValue = null;
	
	/**
	 * 排序
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
