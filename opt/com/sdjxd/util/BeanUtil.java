package com.sdjxd.util;
import java.lang.reflect.Field; 
import java.lang.reflect.Method; 
import java.util.ArrayList; 
import java.util.List; 
 
import org.apache.log4j.LogManager; 
import org.apache.log4j.Logger; 
import org.springframework.util.Assert; 
import org.springframework.util.ReflectionUtils; 
 
/** 
 * ʹ��java������Ʋ������� 
 *  
 * @author Wang Yuxing 
 * @since 2008-1-30 
 */ 
public class BeanUtil { 
        protected static final Logger logger = LogManager.getLogger(BeanUtil.class); 
        static { 
                logger.setAdditivity(false); 
        } 
 
        // ����ʵ���� 
        private BeanUtil() { 
        } 
 
        /** 
         * ѭ������ת�ͣ���ȡ�����������ֶΡ� 
         *  
         * @param object ���� 
         * @param propertyName �������� 
         * @throws NoSuchFieldException û�и��ֶ�ʱ�׳� 
         * @return �ֶζ��� 
         */ 
        public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException { 
                return getDeclaredField(object.getClass(), propertyName); 
        } 
 
        /** 
         * ѭ������ת�ͣ���ȡ�����������ֶΡ� 
         *  
         * @param clazz �� 
         * @param propertyName �������� 
         * @throws NoSuchFieldException û�и��ֶ�ʱ�׳� 
         * @return �ֶζ��� 
         */ 
        public static Field getDeclaredField(Class clazz, String propertyName) throws NoSuchFieldException { 
                Assert.notNull(clazz); 
                Assert.hasText(propertyName); 
                for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) { 
                        try { 
                                return superClass.getDeclaredField(propertyName); 
                        } catch (NoSuchFieldException e) { 
                                // Field���ڵ�ǰ�ඨ�壬��������ת�� 
                        } 
                } 
                throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName); 
        } 
 
        /** 
         * ������ȡ�������ֵ������private��protected���η������ơ� 
         *  
         * @param object ���� 
         * @param propertyName �������� 
         * @throws NoSuchFieldException û�и��ֶ�ʱ�׳� 
         * @return ���Ե�ֵ 
         */ 
        public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException { 
                Assert.notNull(object); 
                Assert.hasText(propertyName); 
 
                Field field = getDeclaredField(object, propertyName); 
 
                boolean accessible = field.isAccessible(); 
                field.setAccessible(true); 
 
                Object result = null; 
                try { 
                        result = field.get(object); 
                } catch (IllegalAccessException e) { 
                        // logger.info("Can't get {}.{} value", object.getClass().getName(), propertyName); 
                        logger.info("Can't get " + object.getClass().getName() + "." + propertyName + " value"); 
                } 
                field.setAccessible(accessible); 
                return result; 
        } 
 
        /** 
         * �������ö������ֵ������private��protected���η������ơ� 
         *  
         * @param object ���� 
         * @param propertyName �������� 
         * @param newValue ����ֵ 
         * @throws NoSuchFieldException û�и��ֶ�ʱ�׳� 
         */ 
        public static void forceSetProperty(Object object, String propertyName, Object newValue) 
                        throws NoSuchFieldException { 
                Assert.notNull(object); 
                Assert.hasText(propertyName); 
 
                Field field = getDeclaredField(object, propertyName); 
                boolean accessible = field.isAccessible(); 
                field.setAccessible(true); 
                try { 
                        field.set(object, newValue); 
                } catch (IllegalAccessException e) { 
                        logger.info("Can't set " + object.getClass().getName() + "." + propertyName + ""); 
                } 
                field.setAccessible(accessible); 
        } 
 
        /** 
         * �������ö�����������private��protected���η������ơ� 
         *  
         * @param object ���� 
         * @param methodName ������ 
         * @param params ���������б� 
         * @throws NoSuchMethodException û�и÷���ʱ�׳� 
         * @return ���÷�����ķ���ֵ 
         */ 
        public static Object invokePrivateMethod(Object object, String methodName, Object... params) 
                        throws NoSuchMethodException { 
                Assert.notNull(object); 
                Assert.hasText(methodName); 
                Class[] types = new Class[params.length]; 
                for (int i = 0; i < params.length; i++) { 
                        types[i] = params[i].getClass(); 
                } 
 
                Class clazz = object.getClass(); 
                Method method = null; 
                for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) { 
                        try { 
                                method = superClass.getDeclaredMethod(methodName, types); 
                                break; 
                        } catch (NoSuchMethodException e) { 
                                // �������ڵ�ǰ�ඨ��,��������ת�� 
                        } 
                } 
 
                if (method == null) 
                        throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName); 
 
                boolean accessible = method.isAccessible(); 
                method.setAccessible(true); 
                Object result = null; 
                try { 
                        result = method.invoke(object, params); 
                } catch (Exception e) { 
                        ReflectionUtils.handleReflectionException(e); 
                } 
                method.setAccessible(accessible); 
                return result; 
        } 
 
        /** 
         * ������ȡ���ֶ��б� 
         *  
         * @param object ���� 
         * @param propertyType �ֶ����� 
         * @return �ֶ��б� 
         */ 
        public static List<Field> getFieldsByType(Object object, Class propertyType) { 
                List<Field> list = new ArrayList<Field>(); 
                Field[] fields = object.getClass().getDeclaredFields(); 
                for (Field field : fields) { 
                        if (field.getType().isAssignableFrom(propertyType)) { 
                                list.add(field); 
                        } 
                } 
                return list; 
        } 
 
        /** 
         * ���������ƻ�����Ե����͡� 
         *  
         * @param clazz ���� 
         * @param propertyName �������� 
         * @return �������� 
         * @throws NoSuchFieldException û�и�Fieldʱ�׳� 
         */ 
        public static Class getPropertyType(Class clazz, String propertyName) throws NoSuchFieldException { 
                return getDeclaredField(clazz, propertyName).getType(); 
        } 
 
        /** 
         * ���field��getter�������ơ� 
         *  
         * @param clazz ���� 
         * @param fieldName �������� 
         * @return �������� 
         * @throws NoSuchFieldException û�и�Fieldʱ�׳� 
         */ 
        public static String getGetterName(Class clazz, String fieldName) throws NoSuchFieldException { 
                Assert.notNull(clazz, "Class required"); 
                Assert.hasText(fieldName, "FieldName required"); 
 
                Class type = getPropertyType(clazz, fieldName); 
                if (type.getSimpleName().toLowerCase().equals("boolean")) { 
                        return "is" + StringUtils.capitalize(fieldName); 
                } else { 
                        return "get" + StringUtils.capitalize(fieldName); 
                } 
        } 
} 
