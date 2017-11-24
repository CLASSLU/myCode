package com.cmd;

import com.event.IEventCallBack;


public interface ICommand {

	
	public abstract Object excute();
	
	
	public Object doAction();
	
	
	public Object doAction(CommandEvent event);
	
	
	public Object doAction(CommandEvent event,IEventCallBack callBack);
	
	
}
