package com.event;

import java.util.HashMap;
import java.util.Map;


/**
 * 事件基类
 * @author caoyu
 *
 */
public class EventDispatcher implements IEventDispatcher{

	private Map<String, EventContext> listener = new HashMap<String, EventContext>();

	public void addEventListener(String type, final IEventCallBack call, boolean sync) {
		EventContext ctx = new EventContext(call,sync);
		listener.put(type, ctx);
	}
	

	public void dispatchEvent(BaseEvent event) {
		EventContext ctx = listener.get(event.type);
		if(ctx != null && ctx.getCall() != null){
			event.target = this;
			ctx.setEvent(event);
			execute(ctx);
		}
	}

	
	public boolean hasEventListener(String type) {
		EventContext ctx = listener.get(type);
		if(ctx != null && ctx.getCall() != null){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean hasEventListener(String type, IEventCallBack call) {
		EventContext ctx = listener.get(type);
		if(ctx != null && ctx.getCall() == call){
			return true;
		}else{
			return false;
		}
	}

	
	public void removeEventListener(String type) {
		listener.remove(type);
	}
	

	public void removeEventListener(String type, IEventCallBack call) {
		EventContext ctx = listener.get(type);
		if(ctx != null && ctx.getCall() == call){
			listener.remove(type);
		}
	}

	
	public void removeAllEventListener() {
		listener.clear();
	}
	
	
	private void execute(EventContext ctx){
		if(ctx.isSync()){
			ctx.getCall().callBack(ctx.getEvent());
		}else{
			new Thread(new EventTask(ctx)).start();
		}
	}


	
	
}
