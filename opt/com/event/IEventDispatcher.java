package com.event;


public interface IEventDispatcher {

	/**
	 * ����¼�����
	 * @param type �¼�����
	 * @param call �ص�����
	 * @param sync �Ƿ�ͬ�� true ͬ�� false �첽
	 */
	public void addEventListener(String type, IEventCallBack call, boolean sync);

	/**
	 * �Ƿ����¼�
	 * @param type
	 * @return
	 */
	public boolean hasEventListener(String type);

	/**
	 * �¼��Ƿ�󶨸÷���
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
	 * ɾ��ĳ���¼�����
	 * @param type
	 */
	public void removeEventListener(String type);

	/**
	 * ɾ�������¼�����
	 */
	public void removeAllEventListener();

	/**
	 * �׳��¼�
	 * @param event
	 */
	public void dispatchEvent(BaseEvent event);
	
}
