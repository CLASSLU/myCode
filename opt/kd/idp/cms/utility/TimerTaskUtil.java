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
	 * 定时任务列表
	 */
	public static List<TimerTaskBean> timerTasks = new ArrayList<TimerTaskBean>();
	
	public static Map<String, TaskThread> taskmap = new HashMap<String, TaskThread>();
	
	
	/**
	 * 初始化 可用定时任务
	 * @return
	 */
	public static void loadTimerTasks(){
		destoryTaskThread();
		initTaskThread();
	}
	
	/**
	 * 重新加载定时任务
	 * @param task
	 */
	public static void reloadTimerTask(TimerTaskBean task){
		if(task != null){
			try {
				if(taskmap.get(task.getTaskId()) != null){
					System.out.println("重新加载定时任务:"+task.getTaskName());
					for(int i = 0;i<timerTasks.size();i++){
						if(timerTasks.get(i).getTaskId().equals(task.getTaskId())){
							timerTasks.remove(i);
							timerTasks.add(task);
							break;
						}
					}
				}else{
					timerTasks.add(task);
					System.out.println("新增定时任务:"+task.getTaskName());
				}
				taskmap.put(task.getTaskId(), new TaskThread(task));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 移除定时任务
	 * @param task
	 */
	public static void removeTimerTask(TimerTaskBean task){
		if(task != null){
			try {
				for(int i = 0;i<timerTasks.size();i++){
					if(timerTasks.get(i).getTaskId().equals(task.getTaskId())){
						timerTasks.remove(i);
						System.out.println("移除定时任务:"+task.getTaskName());
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
	 * 初始化任务线程
	 */
	private static void initTaskThread(){
		List<TimerTaskBean> tasks = ServiceManager.getSystemDao().getTimerTaskList();
		if(tasks != null){
			for(int i=0;i<tasks.size();i++){
				try {
					taskmap.put(tasks.get(i).getTaskId(), new TaskThread(tasks.get(i)));
					timerTasks.add(tasks.get(i));
					System.out.println("加载定时任务:"+tasks.get(i).getTaskName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 销毁任务线程
	 */
	private static void destoryTaskThread(){
		taskmap = new HashMap<String, TaskThread>();
		timerTasks = new ArrayList<TimerTaskBean>();
	}
	
	/**
	 * 执行任务
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
//		System.out.println("执行定时任务:   "+task.getTaskDesc());
//		Object result;
		try {
			method.invoke(taskInstance);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
//		System.out.println("执行成功:   "+result);
	}
	
}
