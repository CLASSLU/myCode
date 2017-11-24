package kd.idp.cms.bean.priv;

import java.util.ArrayList;
import java.util.List;


public class PrivBean {

	/**
	 * 权限组
	 */
	private String privGroup = null;
	
	/**
	 * 权限ID
	 */
	private String privId = null;
	
	/**
	 * 权限名
	 */
	private String privName = null;
	
	/**
	 * 权限类型
	 */
	private String privType = null;
	
	/**
	 * 权限父ID
	 */
	private String privParentId = null;
	
	/**
	 * 权限图片
	 */
	private String privImage = null;
	
	/**
	 * 权限层次
	 */
	private int privLevel = 0;
	
	/**
	 * 权限顺序
	 */
	private int privOrder = 0;
	
	/**
	 * 权限属性
	 */
	private List<PrivAttrBean> attrList = new ArrayList<PrivAttrBean>();

	public List<PrivAttrBean> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<PrivAttrBean> attrList) {
		this.attrList = attrList;
	}

	public String getPrivGroup() {
		return privGroup;
	}

	public void setPrivGroup(String privGroup) {
		this.privGroup = privGroup;
	}

	public String getPrivId() {
		return privId;
	}

	public void setPrivId(String privId) {
		this.privId = privId;
	}

	public String getPrivName() {
		return privName;
	}

	public void setPrivName(String privName) {
		this.privName = privName;
	}

	public String getPrivType() {
		return privType;
	}

	public void setPrivType(String privType) {
		this.privType = privType;
	}

	public String getPrivParentId() {
		return privParentId;
	}

	public void setPrivParentId(String privParentId) {
		this.privParentId = privParentId;
	}

	public String getPrivImage() {
		return privImage;
	}

	public void setPrivImage(String privImage) {
		this.privImage = privImage;
	}

	public int getPrivLevel() {
		return privLevel;
	}

	public void setPrivLevel(int privLevel) {
		this.privLevel = privLevel;
	}

	public int getPrivOrder() {
		return privOrder;
	}

	public void setPrivOrder(int privOrder) {
		this.privOrder = privOrder;
	}
	
	
}
