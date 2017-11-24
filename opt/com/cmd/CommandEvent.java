package com.cmd;

import com.event.BaseEvent;



public class CommandEvent extends BaseEvent{
	
	
	private Object cmdParameter = null;


	public Object getCmdParameter() {
		return cmdParameter;
	}


	public void setCmdParameter(Object cmdParameter) {
		this.cmdParameter = cmdParameter;
	}


	public CommandEvent(String _type) {
		super(_type);
	}

}
