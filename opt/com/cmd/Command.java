package com.cmd;

import com.event.EventDispatcher;
import com.event.IEventCallBack;

public class Command extends EventDispatcher implements ICommand {

	private CommandEvent event;

	private IEventCallBack callBack;

	public Command(CommandEvent event) {
		setEvent(event);
	}

	public Command(CommandEvent event, IEventCallBack callBack) {
		setEvent(event);
		setCallBack(callBack);
	}

	public CommandEvent getEvent() {
		return event;
	}

	public void setEvent(CommandEvent event) {
		this.event = event;
	}

	public IEventCallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(IEventCallBack callBack) {
		this.callBack = callBack;
	}

	public Object excute() {
		if (getCallBack() != null) {
			return doAction(event, callBack);
		} else if (getEvent() != null) {
			return doAction(event);
		} else {
			return doAction();
		}
	}

	public Object doAction() {
		return null;
	}

	public Object doAction(CommandEvent event) {
		return null;
	}

	public Object doAction(CommandEvent event, IEventCallBack callBack) {
		return null;
	}

}
