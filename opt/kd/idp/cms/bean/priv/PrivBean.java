package kd.idp.cms.bean.priv;

import java.util.ArrayList;
import java.util.List;


public class PrivBean {

	/**
	 * Ȩ����
	 */
	private String privGroup = null;
	
	/**
	 * Ȩ��ID
	 */
	private String privId = null;
	
	/**
	 * Ȩ����
	 */
	private String privName = null;
	
	/**
	 * Ȩ������
	 */
	private String privType = null;
	
	/**
	 * Ȩ�޸�ID
	 */
	private String privParentId = null;
	
	/**
	 * Ȩ��ͼƬ
	 */
	private String privImage = null;
	
	/**
	 * Ȩ�޲��
	 */
	private int privLevel = 0;
	
	/**
	 * Ȩ��˳��
	 */
	private int privOrder = 0;
	
	/**
	 * Ȩ������
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
