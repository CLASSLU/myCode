package kd.idp.cms.bean.system;

/**
 * �ֵ�
 * @author caoyu
 *
 */
public class DictionaryBean {

	
	/**
	 * ����
	 */
	private String type = null;
	
	/**
	 * ����
	 */
	private String name = null;
	
	/**
	 * ��ʶ
	 */
	private String code = null;
	
	/**
	 * ˳��
	 */
	private int order = 0;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	
}
