package kd.idp.cms.bean.system;

/**
 * 字典
 * @author caoyu
 *
 */
public class DictionaryBean {

	
	/**
	 * 分类
	 */
	private String type = null;
	
	/**
	 * 名称
	 */
	private String name = null;
	
	/**
	 * 标识
	 */
	private String code = null;
	
	/**
	 * 顺序
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
