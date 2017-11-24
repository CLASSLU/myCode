package com.flex.tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import flex.messaging.io.ArrayCollection;
import flex.messaging.io.ObjectProxy;
import flex.messaging.io.amf.ASObject;

public class BeanTool {

//	private static final Logger log = LoggerFactory.getLogger(BeanTool.class);
	private static Logger log = Logger.getLogger(BeanTool.class);

	@SuppressWarnings("unchecked")
	public static final Object invokeMethod(String classPath,
			String methodName, Object[] args) throws Exception {
		Class clazz = Class.forName(classPath);
		return execute(clazz, methodName, args);
	}

	@SuppressWarnings("unchecked")
	public static final Object execute(Class clazz, String methodName,
			Object[] args) throws Exception {
		Object ret = null;
		if (clazz != null) {
			Method method = getMethod(clazz, methodName, args);
			if (method != null) {
				ret = invokeMethod(clazz,method, args);
			} else {
				StringBuffer m = new StringBuffer(256);
				m.append("调用了不存在的方法:");
				m.append(clazz);
				m.append(".").append(methodName);
				m.append("(");
				if ((args != null) && (args.length > 0)) {
					for (int i = 0; i < args.length; i++) {
						if (args[i] == null)
							m.append("null,");
						else
							m.append(args[i].getClass()).append(",");
					}
					m.setLength(m.length() - 1);
				}
				m.append(")");
				throw new Exception(m.toString());
			}
		} else {
			throw new Exception("调用了不存在的类！");
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static final Method getMethod(Class objClass, String methodName,
			Object[] args) {
		if (objClass == null) {
			log.error("使用了不存在的类！");
			return null;
		}
		Class[] argTypes = getType(args);
		Method method = null;
		if (method == null) {
			try {
				boolean isMatch = false;
				Method[] methodes = objClass.getDeclaredMethods();
				for (int i = 0; i < methodes.length; i++) {
					method = methodes[i];
					if (!method.getName().equals(methodName))
						continue;
					isMatch = true;
					Class[] types = method.getParameterTypes();
					if (types.length != argTypes.length) {
						continue;
					}
					for (int j = 0; j < types.length; j++) {
						if (argTypes[j] == null)
							continue;
						if (types[j].equals(argTypes[j]))
							continue;
						isMatch = false;
						break;
					}
					if (isMatch) {
						break;
					}
				}
				if (!isMatch) {
					method = null;
				}
			} catch (Exception exp1) {
				log.error("调用过程出现错误。错误名称为：" + exp1.getMessage());
			}
		}
		return method;
	}

	@SuppressWarnings("unchecked")
	public static final Object invokeMethod(Class clazz ,Method method, Object[] args) throws Exception {
		if (method == null) {
			throw new Exception("调用了不存在的方法！");
		}
		Object ret;
		try {
			method.setAccessible(true);
			Object instance = clazz.newInstance();
			if(args == null || args.length == 0){
				ret = method.invoke(instance);
			}else{
				ret = method.invoke(instance,args);
			}
		} catch (InvocationTargetException exp) {
			log.error(method.toString());
			throw ((Exception) exp.getTargetException());
		} catch (Exception exp) {
			log.error("调用过程出现错误。错误名称为：" + exp.getMessage());
			throw exp;
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static final Class[] getType(Object[] args) {
		if (args == null) {
			return new Class[0];
		}
		int len = args.length;
		Class[] parameterTypes = new Class[len];
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				if (args[i] == null) {
					parameterTypes[i] = null;
				} else {
					Class classType = args[i].getClass();
					parameterTypes[i] = getType(classType);
				}
			}
		}
		return parameterTypes;
	}

	@SuppressWarnings("unchecked")
	public static final Class getType(Class pClassType) {
		Class classType;
		if (pClassType == null) {
			classType = null;
			return classType;
		} else if (pClassType.equals(Integer.class) ) {
			classType = Integer.TYPE;
			return classType;
		} else if (pClassType.equals(Byte.class)) {
			classType = Byte.TYPE;
			return classType;
		} else if (pClassType.equals(Short.class)) {
			classType = Short.TYPE;
			return classType;
		} else if (pClassType.equals(Float.class)) {
			classType = Float.TYPE;
			return classType;
		} else if (pClassType.equals(Double.class)) {
			classType = Double.TYPE;
			return classType;
		} else if (pClassType.equals(Character.class)) {
			classType = Character.TYPE;
			return classType;
		} else if (pClassType.equals(Long.class)) {
			classType = Long.TYPE;
			return classType;
		} else if (pClassType.equals(Boolean.class)) {
			classType = Boolean.TYPE;
			return classType;
		} else if (pClassType.equals(boolean.class)){
			classType = boolean.class;
			return classType;
		} else if (pClassType.equals(ArrayCollection.class)) {
			classType = List.class;
			return classType;
		}else if(pClassType.equals(ASObject.class) || pClassType.equals(ObjectProxy.class)){
			classType = Map.class;
			return classType;
		} else {
			classType = pClassType;
			return classType;
		}
	}
	public static void main(String[] args) {
		System.out.println(Boolean.TYPE);
		System.out.println(Boolean.class);
		System.out.println(Integer.TYPE);
	}
}
