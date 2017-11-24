package com.event;

public class EventContext {

	
	private BaseEvent event = null;
	
	private IEventCallBack call = null;

	private boolean sync = false;

	
	public EventContext() {
		
	}
	
	public EventContext(IEventCallBack _call, boolean _sync) {
		this.call = _call;
		this.sync = _sync;
	}
	
	
	public EventContext(BaseEvent _event,IEventCallBack _call, boolean _sync) {
		this.event = _event;
		this.call = _call;
		this.sync = _sync;
	}

	public IEventCallBack getCall() {
		return call;
	}

	public void setCall(IEventCallBack call) {
		this.call = call;
	}

	public boolean isSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}


	public BaseEvent getEvent() {
		return event;
	}


	public void setEvent(BaseEvent event) {
		this.event = event;
	}

	
}
