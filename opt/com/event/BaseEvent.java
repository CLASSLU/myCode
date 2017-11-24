package com.event;
/**
 * 
 * 
 *
 */
public class BaseEvent {

	protected Object target = null;

	protected String type = null;

	public BaseEvent(String _type) {
		this.type = _type;
	}

	public Object getTarget() {
		return target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
