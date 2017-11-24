package kd.idp.cms.utility;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.ServiceManager;
import kd.idp.cms.bean.system.TimerTaskBean;

public class TimerTaskUtil {

	
	/**
	 * ��ʱ�����б�
	 */
	public static List<TimerTaskBean> timerTasks = new ArrayList<TimerTaskBean>();
	
	public static Map<String, TaskThread> taskmap = new HashMap<String, TaskThread>();
	
	
	/**
	 * ��ʼ�� ���ö�ʱ����
	 * @return
	 */
	public static void loadTimerTasks(){
		destoryTaskThread();
		initTaskThread();
	}
	
	/**
	 * ���¼��ض�ʱ����
	 * @param task
	 */
	public static void reloadTimerTask(TimerTaskBean task){
		if(task != null){
			try {
				if(taskmap.get(task.getTaskId()) != null){
					System.out.println("���¼��ض�ʱ����:"+task.getTaskName());
					for(int i = 0;i<timerTasks.size();i++){
						if(timerTasks.get(i).getTaskId().equals(task.getTaskId())){
							timerTasks.remove(i);
							timerTasks.add(task);
							break;
						}
					}
				}else{
					timerTasks.add(task);
					System.out.println("������ʱ����:"+task.getTaskName());
				}
				taskmap.put(task.getTaskId(), new TaskThread(task));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * �Ƴ���ʱ����
	 * @param task
	 */
	public static void removeTimerTask(TimerTaskBean task){
		if(task != null){
			try {
				for(int i = 0;i<timerTasks.size();i++){
					if(timerTasks.get(i).getTaskId().equals(task.getTaskId())){
						timerTasks.remove(i);
						System.out.println("�Ƴ���ʱ����:"+task.getTaskName());
						taskmap.remove(task.getTaskId());
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��ʼ�������߳�
	 */
	private static void initTaskThread(){
		List<TimerTaskBean> tasks = ServiceManager.getSystemDao().getTimerTaskList();
		if(tasks != null){
			for(int i=0;i<tasks.size();i++){
				try {
					taskmap.put(tasks.get(i).getTaskId(), new TaskThread(tasks.get(i)));
					timerTasks.add(tasks.get(i));
					System.out.println("���ض�ʱ����:"+tasks.get(i).getTaskName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ���������߳�
	 */
	private static void destoryTaskThread(){
		taskmap = new HashMap<String, TaskThread>();
		timerTasks = new ArrayList<TimerTaskBean>();
	}
	
	/**
	 * ִ������
	 * @param taskId
	 * @return
	 */
	public static boolean callTask(String taskId){
		try {
			Thread t = new Thread(taskmap.get(taskId));
			t.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}

@SuppressWarnings("unchecked")
class TaskThread implements Runnable{
	
	private TimerTaskBean task;
	private Class reflectClass;
	private Method method;
	private Object taskInstance;
	
	public TaskThread(TimerTaskBean _task) throws ClassNotFoundException, SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		task = _task;
		this.reflectClass = Class.forName(task.getClassName());
		this.method = this.reflectClass.getMethod(task.getMethodName());
		taskInstance = this.reflectClass.newInstance();
	}

	public void run() {
//		System.out.println("ִ�ж�ʱ����:   "+task.getTaskDesc());
//		Object result;
		try {
			method.invoke(taskInstance);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
//		System.out.println("ִ�гɹ�:   "+result);
	}
	
}
