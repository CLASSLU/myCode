package com.event;


public interface IEventDispatcher {

	/**
	 * 添加事件监听
	 * @param type 事件名称
	 * @param call 回调方法
	 * @param sync 是否同步 true 同步 false 异步
	 */
	public void addEventListener(String type, IEventCallBack call, boolean sync);

	/**
	 * 是否含有事件
	 * @param type
	 * @return
	 */
	public boolean hasEventListener(String type);

	/**
	 * 事件是否绑定该方法
	 * @param type
	 * @param call
	 * @return
	 */
	public boolean hasEventListener(String type, IEventCallBack call);

	/**
	 * @param type
	 * @param call
	 */
	public void removeEventListener(String type, IEventCallBack call);

	/**
	 * 删除某个事件监听
	 * @param type
	 */
	public void removeEventListener(String type);

	/**
	 * 删除所有事件监听
	 */
	public void removeAllEventListener();

	/**
	 * 抛出事件
	 * @param event
	 */
	public void dispatchEvent(BaseEvent event);
	
}
