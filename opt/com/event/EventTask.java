package com.event;

public class EventTask implements Runnable{

	
	private EventContext _ctx = null;
	
	public EventTask(EventContext ctx) {
		_ctx = ctx;
	}
	
	
	public void run() {
		if(_ctx != null && _ctx.getCall() != null && _ctx.getEvent() != null){
			_ctx.getCall().callBack(_ctx.getEvent());
		}
	}

}
